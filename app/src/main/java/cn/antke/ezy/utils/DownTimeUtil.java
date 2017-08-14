package cn.antke.ezy.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.Locale;

import cn.antke.ezy.R;

/**
 * Created by zww on 2017/7/4.
 * 获取验证码倒计时
 */

public class DownTimeUtil {
    private Context context;
    public CountDownTimer countDownTimer;

    public DownTimeUtil(Context context) {
        this.context = context;
    }

    public void initCountDownTime(TextView textView) {
        countDownTimer = new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                textView.setEnabled(false);
                textView.setText(String.format(Locale.CHINA, context.getString(R.string.login_count_down_text), millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                textView.setEnabled(true);
                textView.setText(context.getString(R.string.register_get_verify_code));
                cancel();
            }
        };
    }
}
