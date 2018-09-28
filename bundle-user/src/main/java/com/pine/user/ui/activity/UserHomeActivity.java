package com.pine.user.ui.activity;

import com.pine.base.access.UiAccessAnnotation;
import com.pine.base.access.UiAccessType;
import com.pine.base.mvp.ui.activity.BaseMvpNoActionBarActivity;
import com.pine.user.R;
import com.pine.user.contract.IUserHomeContract;
import com.pine.user.presenter.UserHomePresenter;

/**
 * Created by tanghongfeng on 2018/9/13
 */

@UiAccessAnnotation(AccessTypes = {UiAccessType.LOGIN}, LevelValues = {-1})
public class UserHomeActivity extends BaseMvpNoActionBarActivity<IUserHomeContract.Ui, UserHomePresenter>
        implements IUserHomeContract.Ui {
    @Override
    protected int getActivityLayoutResId() {
        return R.layout.user_activity_home;
    }

    @Override
    protected boolean initData() {
        return true;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void afterInit() {

    }

    @Override
    protected UserHomePresenter createPresenter() {
        return new UserHomePresenter();
    }
}
