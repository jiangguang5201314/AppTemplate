package com.pine.base.manager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import com.pine.tool.util.LogUtils;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.ByteArrayOutputStream;

/**
 * Created by tanghongfeng on 2018/10/9
 */

public class ShareManager {
    private final static String TAG = LogUtils.makeLogTag(ShareManager.class);
    private static volatile ShareManager mInstance;
    public Context mContext;
    private IWXAPI mIwxApi;
    private Tencent mTencent;
    private String QQ_FOR_APP_KEY = "";
    private String WX_FOR_APP_KEY = "";
    private String WX_SECRET_KEY = "";
    private int ICON_ID;
    private String APP_NAME = "";
    private String HOST = "";

    private ShareManager() {
    }

    public static ShareManager getInstance() {
        if (mInstance == null) {
            synchronized (ShareManager.class) {
                if (mInstance == null) {
                    mInstance = new ShareManager();
                }
            }
        }
        return mInstance;
    }

    public IWXAPI registerTencentApi() {
        if (mIwxApi == null) {
            mIwxApi = WXAPIFactory.createWXAPI(mContext, WX_FOR_APP_KEY, true);
            mIwxApi.registerApp(WX_FOR_APP_KEY);
        }
        return mIwxApi;
    }

    public void init(String qq_for_app_key, String wx_for_app_key, String wx_secret_key,
                     int icon, String appName, String baseUrl) {
        QQ_FOR_APP_KEY = qq_for_app_key;
        WX_FOR_APP_KEY = wx_for_app_key;
        WX_SECRET_KEY = wx_secret_key;
        ICON_ID = icon;
        APP_NAME = appName;
        HOST = baseUrl;
    }

    private boolean isInit() {
        return QQ_FOR_APP_KEY.length() != 0 && WX_FOR_APP_KEY.length() != 0 &&
                WX_SECRET_KEY.length() != 0 && APP_NAME.length() != 0 && HOST.length() != 0;
    }

    /**
     * 分享微信朋友 or 朋友圈 true为朋友  false为朋友圈
     *
     * @param isTimeline
     * @param url
     */
    public void sendWXRequest(Context context, boolean isTimeline, String url, String title, String description) {
        if (!isInit()) {
            LogUtils.d(TAG, "ShareManager was not init");
            return;
        }
        mContext = context;
        mIwxApi = WXAPIFactory.createWXAPI(context, WX_FOR_APP_KEY, true);
        mIwxApi.registerApp(WX_FOR_APP_KEY);
        if (!mIwxApi.isWXAppInstalled()) {
            Toast.makeText(context, "抱歉，您的手机上未安装微信，无法分享！", Toast.LENGTH_SHORT).show();
            return;
        }

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = webpage;
        msg.title = title;
        msg.description = description;
        Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), ICON_ID);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        thumb.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        msg.thumbData = byteArray;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = isTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        mIwxApi.sendReq(req);
    }

    public void shareToQQ(Context context, String title, String description, String url) {
        if (!isInit()) {
            LogUtils.d(TAG, "ShareManager was not init");
            return;
        }
        mContext = context;
        if (mTencent == null) {
            mTencent = Tencent.createInstance(QQ_FOR_APP_KEY, context);
        }

        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, description);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, APP_NAME);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, HOST + "/images/logo2.png");
        /*params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");*/
        mTencent.shareToQQ((Activity) context, params, new BaseUiListener());
    }

    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
            doComplete(response);
        }

        protected void doComplete(Object values) {
        }

        @Override
        public void onError(UiError e) {
            Toast.makeText(mContext, "分享QQ失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(mContext, "取消分享", Toast.LENGTH_SHORT).show();
        }
    }
}
