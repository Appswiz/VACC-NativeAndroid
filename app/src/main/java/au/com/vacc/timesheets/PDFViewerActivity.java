package au.com.vacc.timesheets;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;


public class PDFViewerActivity extends AppCompatActivity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webView = (WebView) findViewById(R.id.webview);
        Button btn = (Button) findViewById(R.id.btn_apply_now);

        Intent intent = getIntent();
        if(!intent.getBooleanExtra("isCareers", false)) {
            btn.setVisibility(View.GONE);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://autoapprenticeships.com.au/JobSeekers/Applicationform.aspx"));
                startActivity(i);
            }
        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);


        webView.loadUrl(String.format("file:///android_asset/pdfjs/web/viewer.html?file=%s",  "file:///android_asset/" + intent.getStringExtra("pdfFileName")));

    }

    @Override
    public void onBackPressed() {
        webView.stopLoading();
        webView.removeAllViews();
        webView.destroy();
        finish();
    }

}