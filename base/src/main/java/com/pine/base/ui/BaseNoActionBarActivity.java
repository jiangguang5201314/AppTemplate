package com.pine.base.ui;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.OnKeyboardListener;
import com.pine.base.R;

public abstract class BaseNoActionBarActivity extends BaseActivity {
    private ImmersionBar mImmersionBar;

    @Override
    protected final void setContentView() {
        setContentView(R.layout.base_activity_no_actionbar);
    }

    @CallSuper
    @Override
    protected boolean beforeInitOnCreate() {
        ViewStub content_layout = findViewById(R.id.content_layout);
        content_layout.setLayoutResource(getActivityLayoutResId());
        content_layout.inflate();

        initImmersionBar();

        return false;
    }

    @CallSuper
    @Override
    protected void afterInitOnCreate() {

    }

    @Override
    protected void onPause() {
        //如果软键盘已弹出，收回软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }
        super.onDestroy();
    }

    private void initImmersionBar() {
        findViewById(R.id.base_status_bar_view).setBackgroundResource(getStatusBarBgResId());
        mImmersionBar = ImmersionBar.with(this)
                .statusBarDarkFont(true, 1f)
                .statusBarView(R.id.base_status_bar_view)
                .keyboardEnable(true);
        mImmersionBar.init();
    }

    public void setKeyboardListener(OnKeyboardListener listener) {
        mImmersionBar.setOnKeyboardListener(listener);
    }

    protected int getStatusBarBgResId() {
        return R.mipmap.base_iv_status_bar_bg;
    }
}
