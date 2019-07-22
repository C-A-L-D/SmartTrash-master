package site.nihaoa.smarttrash.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.SearchView;

import site.nihaoa.smarttrash.R;

public class WeatherFragment extends Fragment {
    private View rootView;
    private WebView webView;
    public WeatherFragment() {
        super();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.weather_layout,container,false);
            webView = rootView.findViewById(R.id.weather_webview);
            loadData();
            webView.loadUrl("http://www.weather.com.cn/");
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
