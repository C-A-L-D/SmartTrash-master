package site.nihaoa.smarttrash.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import site.nihaoa.smarttrash.R;


public class NewsFragment extends Fragment {
    private View rootView;
    private WebView webView;


    public NewsFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.news_layout,container,false);
            webView = rootView.findViewById(R.id.webview);
            loadData();
            webView.loadUrl("http://www.gabriel-gp.com/4dffa05cea54450e980ec5523b1d047f.html");
        }
        return rootView;
    }

    private void loadData(){
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }
}
