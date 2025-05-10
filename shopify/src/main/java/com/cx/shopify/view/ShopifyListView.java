package com.cx.shopify.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.cx.shopify.R;
import com.cx.shopify.api.Config;
import com.cx.shopify.fragment.H5Fragment;
import com.cx.shopify.listener.OnBackListener;
import com.cx.shopify.listener.OnFloatStatusListener;
import com.cx.shopify.util.DpPxUtils;
import com.cx.shopify.util.ShopifyUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class ShopifyListView extends FrameLayout {

    private Context mContext;
    private OnBackListener onBackListener;

    private WebView mWebView;
    private ProgressBar mProgressBar;

    private ShopifyUtil mShopifyUtil;

    private boolean mIsOnBack;
    public ShopifyListView(Context context) {
        super(context);
        init(context);
    }

    public ShopifyListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ShopifyListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        View view = View.inflate(mContext, R.layout.layout_custom_view, this);
        mWebView = view.findViewById(R.id.mWebView);
        mProgressBar = view.findViewById(R.id.mProgressBar);
        initWebview();

    }

    @SuppressLint("JavascriptInterface")
    private void initWebview() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true); // 支持JS
        settings.setSupportZoom(false); // 支持手势缩放
        settings.setBuiltInZoomControls(false);// 支持手势缩放
        settings.setDisplayZoomControls(false);// 隐藏Zoom缩放按钮
        settings.setUseWideViewPort(true);// 任意比例的缩放
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDefaultFontSize(16);
        settings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }//开启硬件加速
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // https配置
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            isNestedScrollingEnabled();

        mWebView.addJavascriptInterface(this, "Hpage");
        mWebView.setLongClickable(false);
        mWebView.setWebChromeClient(mWebChromeClient);
        mWebView.setWebViewClient(mClient);
        mWebView.setBackgroundColor(Color.TRANSPARENT);
    }

    private WebViewClient mClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            // TODO Auto-generated method stub
            // handler.cancel();// Android默认的处理方式
            handler.proceed();// 接受所有网站的证书
            // handleMessage(Message msg);// 进行其他处理
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (errorCode == -2 || errorCode == -10 || errorCode == -6) {

            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
//            showLoadingDialog(); // 显示加载框
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            hideLoadingDialog(); // 隐藏加载框
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        }
    };

    private void hideLoadingDialog() {
        mProgressBar.setVisibility(View.GONE);
    }

    private void showLoadingDialog() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }


    };

    public void setOnBackListener(OnBackListener onBackListener) {
        this.onBackListener = onBackListener;
    }

    @JavascriptInterface
    public void openPage(String url) {
        ((Activity) mContext).runOnUiThread(() -> {
            mShopifyUtil = ShopifyUtil.getInstance((Activity) mContext)
                    .setOnFloatStatusListener(status -> {
                        mIsOnBack = status==1;
                    })
                    .setOnBackListener(data -> {
                        if (onBackListener!=null){
                            onBackListener.onBack(data);
                        }
                    })
                    .openListUrl(url);
        });
    }

    @JavascriptInterface
    public void toPage(String json) {
        ((Activity) mContext).runOnUiThread(() -> {
            if (onBackListener != null) {
                onBackListener.onBack(json);
            }
        });
    }

    @JavascriptInterface
    public void setVideoInfo(String json) {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mWebView != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        int mobileVideoHeight = jsonObject.getInt("mobileVideoHeight");
                        mobileVideoHeight = DpPxUtils.dip2px(mContext, mobileVideoHeight);
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mobileVideoHeight);
                        mWebView.setLayoutParams(params);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }


    /**
     * 设置列表参数
     *
     * @param location           当前页面地址
     * @param quickcepAppVideoID 视频ID
     * @return
     */
    public ShopifyListView setParams(String location, String quickcepAppVideoID) {
        if (TextUtils.isEmpty(location)) {
            Toast.makeText(getContext(), "请设置location", Toast.LENGTH_SHORT).show();
            return this;
        }
        if (TextUtils.isEmpty(quickcepAppVideoID)) {
            Toast.makeText(getContext(), "请设置quickcepAppVideoID", Toast.LENGTH_SHORT).show();
            return this;
        }
        String url = Config.LIST_URL + "&quickcepAppVideoID=" + quickcepAppVideoID + "&location=" + location;
        mClient.shouldOverrideUrlLoading(mWebView, url);
        return this;
    }

    public ShopifyListView setUrl(String url) {
        mClient.shouldOverrideUrlLoading(mWebView, url);
        return this;
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mShopifyUtil!=null) {
            mShopifyUtil.recycle();
            mShopifyUtil=null;
        }
        super.onDetachedFromWindow();
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (mIsOnBack && event.getKeyCode() == KeyEvent.KEYCODE_BACK){
//            if (mShopifyUtil!=null) {
//                mShopifyUtil.recycle();
//                mShopifyUtil=null;
//            }
//            mIsOnBack = false;
//            return true;
//        }
        return super.dispatchKeyEvent(event);
    }
}
