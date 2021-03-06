package com.pine.mvp.presenter;

import com.pine.base.architecture.mvp.presenter.BasePresenter;
import com.pine.base.component.share.bean.ShareBean;
import com.pine.mvp.MvpUrlConstants;
import com.pine.mvp.contract.IMvpWebViewContract;

import java.util.ArrayList;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class MvpWebViewPresenter extends BasePresenter<IMvpWebViewContract.Ui>
        implements IMvpWebViewContract.Presenter {
    private String mH5Url;

    @Override
    public boolean parseIntentDataOnCreate() {
        mH5Url = getStringExtra("url", MvpUrlConstants.H5_DefaultUrl);
        return false;
    }

    @Override
    public void onUiState(int state) {
        if (state == BasePresenter.UI_STATE_ON_CREATE) {
            getUi().loadUrl(mH5Url);
        }
    }

    @Override
    public String getH5Url() {
        return mH5Url;
    }

    @Override
    public ArrayList<ShareBean> getShareBeanList() {
        ArrayList<ShareBean> shareBeanList = new ArrayList<>();
        ShareBean shareBean = new ShareBean(ShareBean.SHARE_TARGET_QQ, ShareBean.SHARE_CONTENT_TYPE_TEXT_URL,
                "Item Detail Title", "Item Detail Text",
                "Item Detail Desc", mH5Url);
        shareBeanList.add(shareBean);
        shareBean = new ShareBean(ShareBean.SHARE_TARGET_WX, ShareBean.SHARE_CONTENT_TYPE_TEXT_URL,
                "Item Detail Title", "Item Detail Text",
                "Item Detail Desc", mH5Url);
        shareBeanList.add(shareBean);
        shareBean = new ShareBean(ShareBean.SHARE_TARGET_WX_FRIEND_CIRCLE, ShareBean.SHARE_CONTENT_TYPE_TEXT_URL,
                "Item Detail Title", "Item Detail Text",
                "Item Detail Desc", mH5Url);
        shareBeanList.add(shareBean);
        shareBean = new ShareBean(ShareBean.SHARE_TARGET_WEI_BO, ShareBean.SHARE_CONTENT_TYPE_TEXT_URL,
                "Item Detail Title", "Item Detail Text",
                "Item Detail Desc", mH5Url);
        shareBeanList.add(shareBean);
        return shareBeanList;
    }
}
