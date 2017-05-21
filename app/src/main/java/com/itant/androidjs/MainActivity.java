package com.itant.androidjs;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_java_invoke_js;
    private WebView web_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_java_invoke_js = (Button) findViewById(R.id.btn_java_invoke_js);
        btn_java_invoke_js.setOnClickListener(this);

        web_view = (WebView) findViewById(R.id.web_view);
        web_view.loadUrl("file:///android_asset/web.html");
        web_view.getSettings().setJavaScriptEnabled(true);

        // 支持alert
        web_view.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        web_view.addJavascriptInterface(new WebViewJavaScriptInterface(this), "app");



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_java_invoke_js:
                // Java调用JS
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        web_view.loadUrl("javascript:javaInvokeJs()");
                    }
                });
                break;
        }
    }

    /*
     * JavaScript Interface. Web code can access methods in here
     * (as long as they have the @JavascriptInterface annotation)
     */
    public class WebViewJavaScriptInterface{

        private Context context;

        /*
         * Need a reference to the context in order to sent a post message
         */
        public WebViewJavaScriptInterface(Context context){
            this.context = context;
        }

        /*
         * This method can be called from Android. @JavascriptInterface
         * required after SDK version 17.
         */
        @JavascriptInterface
        public void makeToast(String message, boolean lengthLong){
            Toast.makeText(context, message, (lengthLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT)).show();
        }
    }
}
