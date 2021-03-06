package activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chuanqi.yz.R;

import Constance.constance;

public class BannerLinkActivity extends BaseActivity {
    private WebView mWeb;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_shop);
        initview();
    }
    @JavascriptInterface
    private void initview() {
        mWeb = (WebView) findViewById(R.id.wv_shop);
        WebSettings settings = mWeb.getSettings();
        settings.setJavaScriptEnabled(true);// 设置支持js
        WebViewClient wvClient = new WebViewClient();
        mWeb.setWebViewClient(wvClient);
        mWeb.setWebChromeClient(new MyWebChromeClient());
//        mWeb.loadUrl("http://i.qingyiyou.cn/yz/appmsg/shaidan.html?userid=1000817");
        mWeb.loadUrl(getIntent().getStringExtra("link"));
    }
    class MyWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);}

        @Override
        public void onPageFinished(WebView view, String url) {
            stopProgressDialog();
            super.onPageFinished(view, url);
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            startProgressDialog("努力加载中...");
            super.onPageStarted(view, url, favicon);
        }
    }
    class MyWebChromeClient extends WebChromeClient {
        public boolean onJsAlert(WebView view, String url, String message,
                                 JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if(newProgress==100){
                stopProgressDialog();
            }else {
                startProgressDialog("努力加载中...");
            }
        }
    }
}
