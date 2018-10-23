package com.pine.mvp.ui.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pine.base.architecture.mvp.ui.fragment.BaseMvpFragment;
import com.pine.mvp.R;
import com.pine.mvp.adapter.MvpShopItemPaginationAdapter;
import com.pine.mvp.contract.IMvpShopTreeListContract;
import com.pine.mvp.presenter.MvpShopTreeListPresenter;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpShopTreeListFragment extends BaseMvpFragment<IMvpShopTreeListContract.Ui, MvpShopTreeListPresenter>
        implements IMvpShopTreeListContract.Ui, SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipe_refresh_layout;
    private RecyclerView recycle_view;

    @Override
    protected MvpShopTreeListPresenter createPresenter() {
        return new MvpShopTreeListPresenter();
    }

    @Override
    protected int getFragmentLayoutResId() {
        return R.layout.mvp_fragment_shop_tree_list;
    }

    @Override
    protected void initViewOnCreateView(View layout) {
        swipe_refresh_layout = layout.findViewById(R.id.swipe_refresh_layout);
        recycle_view = layout.findViewById(R.id.recycle_view);
        swipe_refresh_layout.setOnRefreshListener(this);
        swipe_refresh_layout.setColorSchemeResources(
                R.color.red,
                R.color.yellow,
                R.color.green
        );
        swipe_refresh_layout.setDistanceToTriggerSync(250);
        if (swipe_refresh_layout != null) {
            swipe_refresh_layout.setRefreshing(true);
        }
        swipe_refresh_layout.post(new Runnable() {
            @Override
            public void run() {
                swipe_refresh_layout.setRefreshing(true);
                onRefresh();
            }
        });
        swipe_refresh_layout.setEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycle_view.setLayoutManager(linearLayoutManager);
        recycle_view.setHasFixedSize(true);
        recycle_view.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (MvpShopItemPaginationAdapter.isLastVisibleViewFooter(recyclerView)) {
                    onLoadingMore();
                }
            }
        });
        recycle_view.setAdapter(mPresenter.getRecycleViewAdapter());
    }

    @Override
    public void onRefresh() {
        mPresenter.loadShopTreeListData(true);
    }

    public void onLoadingMore() {
        mPresenter.loadShopTreeListData(false);
    }

    @Override
    public void setSwipeRefreshLayoutRefresh(boolean processing) {
        if (swipe_refresh_layout == null) {
            return;
        }
        if (processing) {
            if (swipe_refresh_layout.isRefreshing()) {
                swipe_refresh_layout.setRefreshing(processing);
            }
        } else {
            swipe_refresh_layout.setRefreshing(processing);
        }
    }
}