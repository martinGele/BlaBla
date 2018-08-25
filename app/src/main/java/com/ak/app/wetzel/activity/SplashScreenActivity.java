package com.ak.app.wetzel.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import com.util.Utility;
import java.util.Timer;
import java.util.TimerTask;

import static com.util.Dialogs.showMsgDialog;


public class SplashScreenActivity extends Activity {

    private Timer my_timer;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_im);

            my_timer = new Timer();
            my_timer.schedule(new TimerTask() {
                public void run() {
                    Intent i = new Intent(SplashScreenActivity.this, RotiHomeActivity.class);
                    startActivity(i);
                    finish();
                }
            }, 2000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (my_timer != null) {
            my_timer.cancel();
            my_timer = null;
        }
    }


}
