package cn.antke.ezy.person;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.antke.ezy.R;
import cn.antke.ezy.base.BaseFragment;
import cn.antke.ezy.login.utils.UserCenter;

/**
 * Created by zww on 2017/8/9.
 * 个人中心网页
 */

public class PersonFragment1 extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_person1, null);
        WebView webView = (WebView) view.findViewById(R.id.person_web);

        webView.setWebViewClient(new WebViewClient());

        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });

        webView.loadUrl("http://m.ezy.antke.cn/user/applogin.do?user_id=?"+ UserCenter.getUserCode(getActivity()));
        return view;
    }
}
