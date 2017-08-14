package cn.antke.ezy.launcher.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import java.util.Timer;
import java.util.TimerTask;

import cn.antke.ezy.R;
import cn.antke.ezy.base.BaseActivity;
import cn.antke.ezy.db.CityDBManager;
import cn.antke.ezy.main.controller.MainActivity;

/**
 * Created by liuzhichao on 2017/6/27.
 * 空白启动页，判断进入登录页还是主页
 */
public class SplashActivity extends BaseActivity {
    private static final long LAUNCH_MIN_TIME = 2000L;
    private static final int MSG_CITY_INIT_FINISH = 1;
    private long mLaunchTime;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CITY_INIT_FINISH:
                    gotoActivity();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);
        mLaunchTime = SystemClock.elapsedRealtime();
        initCityDB();
    }

    private void initCityDB() {
        new Thread(() -> {
            CityDBManager.introducedCityDB(this);
            handler.sendEmptyMessageDelayed(MSG_CITY_INIT_FINISH, 100);
        }).start();
    }

    private void gotoActivity() {
        long elapsed = SystemClock.elapsedRealtime() - mLaunchTime;
        if (elapsed >= LAUNCH_MIN_TIME) {
            performGotoActivity();
            finish();
        } else {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (SplashActivity.this.isFinishing()) {
                        return;
                    }
                    cancel();
                    performGotoActivity();
                    finish();
                }
            }, LAUNCH_MIN_TIME - elapsed);
        }

    }

    private void performGotoActivity() {
//        if (UserCenter.isLogin(this)) {
        startActivity(new Intent(this, MainActivity.class));
//            NewActivityActivity.startNewActivityActivity(this, "", "http://onau582bt.bkt.clouddn.com/6505a939-75b0-4c84-b4fd-01ab58d040f2");
//        } else {
//            startActivity(new Intent(this, LoginActivity.class));
//        }
    }

}
