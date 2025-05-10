package com.cx.shopify.util;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cx.shopify.R;
import com.cx.shopify.api.Config;
import com.cx.shopify.listener.OnBackListener;
import com.cx.shopify.listener.OnFloatStatusListener;
import com.cx.shopify.view.FloatWebView;
import com.cx.shopify.listener.OnFloatListener;
import com.cx.shopify.xtoast.XToast;
import com.cx.shopify.xtoast.draggable.MovingDraggable;

import org.json.JSONObject;

public class ShopifyUtil {
    private Activity mActivity;
    private XToast mxToast;
    private static ShopifyUtil sInstance;
    private FloatWebView mFloatWebView;
    private View viewc;
    private Application mApplication;

    private ShopifyUtil(Activity activity) {
        this.mActivity = activity;
        init();
    }

    public static ShopifyUtil getInstance(Activity activity) {
        sInstance = new ShopifyUtil(activity);
        return sInstance;
    }

    // 设置成全局的悬浮窗，注意需要先申请悬浮窗权限
    private ShopifyUtil(Application application) {
        this.mApplication = application;
        init();
    }

    public static ShopifyUtil getInstance(Application application) {
        sInstance = new ShopifyUtil(application);
        return sInstance;
    }

    private void init() {
        viewc = LayoutInflater.from(mActivity).inflate(com.cx.shopify.R.layout.shopify_float, null);

        mFloatWebView = viewc.findViewById(com.cx.shopify.R.id.flview);

        mFloatWebView.setOnFloatListener(new OnFloatListener() {
            @Override
            public void openPage() {
                mActivity.runOnUiThread(() -> {
                    DisplayMetrics metrics = mActivity.getResources().getDisplayMetrics();
                    if (mFloatWebView != null) {
                        mFloatWebView.getLayoutParams().width = metrics.widthPixels;
                        mFloatWebView.getLayoutParams().height = metrics.heightPixels;
                    }
                    if (mxToast != null) {
                        mxToast.setWH(metrics.widthPixels, metrics.heightPixels);
                        mxToast.setDraggableNull();
                    }
                    if (onFloatStatusListener!=null)onFloatStatusListener.onStatus(1);
                });

            }

            @Override
            public void setVideoInfo(String json) {
                mActivity.runOnUiThread(() -> {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        int mobileVideoHeight = jsonObject.getInt("mobileVideoHeight");
                        int mobileVideoWidth = jsonObject.getInt("mobileVideoWidth");
                        int pluginLocationMobileHeight = jsonObject.getInt("pluginLocationMobileHeight");
                        String pluginLocationMobile = jsonObject.getString("pluginLocationMobile");
                        int pluginLocationMobileWidth = jsonObject.getInt("pluginLocationMobileWidth");
                        mobileVideoHeight = DpPxUtils.dip2px(mActivity, mobileVideoHeight);
                        mobileVideoWidth = DpPxUtils.dip2px(mActivity, mobileVideoWidth);
                        pluginLocationMobileHeight = DpPxUtils.dip2px(mActivity, pluginLocationMobileHeight);
                        pluginLocationMobileWidth = DpPxUtils.dip2px(mActivity, pluginLocationMobileWidth);
                        ViewGroup.LayoutParams params = viewc.getLayoutParams();
                        params.width = mobileVideoWidth;
                        params.height = mobileVideoHeight;
                        if (mFloatWebView != null) {
                            mFloatWebView.getLayoutParams().width = mobileVideoWidth;
                            mFloatWebView.getLayoutParams().height = mobileVideoHeight;
                        }
                        if (mxToast != null) {

                            mxToast.setWidth(mobileVideoWidth);
                            mxToast.setHeight(mobileVideoHeight);
                            int gravity;
                            if ("left".equals(pluginLocationMobile)) {
                                gravity = Gravity.BOTTOM | Gravity.LEFT;
                            } else {
                                gravity = Gravity.BOTTOM | Gravity.RIGHT;
                            }
                            mxToast.setGravity(gravity)
                                    .setYOffset(pluginLocationMobileHeight)
                                    .setXOffset(pluginLocationMobileWidth);
                            mxToast.setDraggable();
                        }
                        if (onFloatStatusListener!=null)onFloatStatusListener.onStatus(2);
                    } catch (Exception e) {
                    }

                });

            }

            @Override
            public void closeFloat() {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (onFloatStatusListener!=null)onFloatStatusListener.onStatus(3);
                        if (mxToast != null) {
                            mxToast.cancel();
                        }
                    }
                });

            }

            @Override
            public void toPage(String json) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (onBackListener != null) onBackListener.onBack(json);
                    }
                });
            }
        });
        if (mActivity != null) {
            mxToast = new XToast<>(mActivity);
        } else if (mApplication != null) {
            mxToast = new XToast<>(mApplication);
        }
        mxToast.setContentView(viewc)
                .setDraggable(new MovingDraggable())
                .setGravity(Gravity.BOTTOM | Gravity.LEFT);
    }

    public ShopifyUtil setParams(String location) {
        if (TextUtils.isEmpty(location)) {
            Toast.makeText(mActivity, "请设置location", Toast.LENGTH_SHORT).show();
            return this;
        }
        if (mFloatWebView != null) {
            mFloatWebView.getLayoutParams().width = 1;
            mFloatWebView.getLayoutParams().height = 0;
        }
        if (mxToast != null) {
            mxToast.setWH(1, 0);
        }
        mFloatWebView.loadUrl(Config.FloatURL + location);
        return this;
    }

    public ShopifyUtil setUrl(String url) {
        if (mFloatWebView != null) {
            mFloatWebView.getLayoutParams().width = 1;
            mFloatWebView.getLayoutParams().height = 0;
        }
        if (mxToast != null) {
            mxToast.setWH(1, 0);
        }
        mFloatWebView.loadUrl(url);
        return this;
    }

    public void show() {

        mxToast.show();

        if (viewc != null) {
            viewc.getLayoutParams().width = 1;
            viewc.getLayoutParams().height = 1;
        }
        if (mFloatWebView != null) {
            mFloatWebView.getLayoutParams().width = 1;
            mFloatWebView.getLayoutParams().height = 0;
        }
    }

    public void cancel() {
        if (mxToast != null)
            mxToast.cancel();
    }

    public void setVisibility(int visibility) {
        if (mxToast != null)
            mxToast.setVisibility(visibility);
    }

    public void recycle() {
        if (mxToast != null)
            mxToast.recycle();
    }

    private OnBackListener onBackListener;

    public ShopifyUtil setOnBackListener(OnBackListener onBackListener) {
        this.onBackListener = onBackListener;
        return this;
    }

    private OnFloatStatusListener onFloatStatusListener;

    public ShopifyUtil setOnFloatStatusListener(OnFloatStatusListener onFloatStatusListener) {
        this.onFloatStatusListener = onFloatStatusListener;
        return this;
    }

    public ShopifyUtil openListUrl(String url){
        DisplayMetrics metrics = mActivity.getResources().getDisplayMetrics();
        if (mFloatWebView != null) {
            mFloatWebView.getLayoutParams().width = metrics.widthPixels;
            mFloatWebView.getLayoutParams().height = metrics.heightPixels;
            mFloatWebView.loadUrl(url);
        }
        if (mxToast != null) {
            mxToast.setWH(metrics.widthPixels, metrics.heightPixels);
            mxToast.setDraggableNull();
        }

        mxToast.show();
        if (onFloatStatusListener!=null)onFloatStatusListener.onStatus(1);
        return this;
    }
}
