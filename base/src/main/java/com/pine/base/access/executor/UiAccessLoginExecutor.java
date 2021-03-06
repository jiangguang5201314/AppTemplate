package com.pine.base.access.executor;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.pine.base.BaseApplication;
import com.pine.base.access.IUiAccessExecutor;
import com.pine.router.IRouterCallback;
import com.pine.router.RouterCommand;
import com.pine.router.RouterFactory;

/**
 * Created by tanghongfeng on 2018/9/16
 */

public class UiAccessLoginExecutor implements IUiAccessExecutor {
    private int mForbiddenToastResId = -1;

    public UiAccessLoginExecutor(int forbiddenToastResId) {
        mForbiddenToastResId = forbiddenToastResId;
    }

    @Override
    public boolean onExecute(final Activity activity, String args, boolean isResumeUi) {
        boolean canAccess = BaseApplication.isLogin();
        if (!canAccess) {
            if (isResumeUi) {
                if (activity != null && !activity.isFinishing()) {
                    activity.finish();
                }
            } else {
                RouterFactory.getLoginBundleManager().callUiCommand(BaseApplication.mCurResumedActivity,
                        RouterCommand.LOGIN_goLoginActivity, null, new IRouterCallback() {
                            @Override
                            public void onSuccess(Bundle returnBundle) {
                                if (activity != null && !activity.isFinishing()) {
                                    activity.finish();
                                }
                            }

                            @Override
                            public void onFail(String errorInfo) {
                                if (activity != null && !activity.isFinishing()) {
                                    activity.finish();
                                }
                            }
                        });
                if (mForbiddenToastResId > 0) {
                    Toast.makeText(activity, mForbiddenToastResId, Toast.LENGTH_SHORT).show();
                }
            }
        }
        return canAccess;
    }

    @Override
    public boolean onExecute(Fragment fragment, String args, boolean isResumeUi) {
        return true;
    }
}
