package cn.antke.ezy.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by zww on 2017/7/20.
 * 判断WebView 是否滑动到底部
 */

public class ScrollWebView extends WebView {

    private OnWebScrollListener onWebScrollListener;

    public ScrollWebView(Context context) {
        super(context);
    }

    public ScrollWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onWebScrollListener != null) {
            onWebScrollListener.onSroll();
        }
    }

    public void setOnWebScrollListener(OnWebScrollListener onWebScrollListener) {
        this.onWebScrollListener = onWebScrollListener;
    }

    public OnWebScrollListener getOnWebScroll() {
        return onWebScrollListener;
    }

    public interface OnWebScrollListener {
        void onSroll();
    }
}
