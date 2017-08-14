package cn.antke.ezy.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by jacktian on 15/9/28.
 */
public class InputMethodUtil {

    public static void showInput(Context context, View view) {
        InputMethodManager im = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(view, 0);

    }

    /**
     *
     * @param context
     *            关闭输入法，需要一个activity
     */
    public static void closeInputMethod(Activity context) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

        } catch (Exception e) {
            Log.d("", "关闭输入法异常");
        }
    }
}
