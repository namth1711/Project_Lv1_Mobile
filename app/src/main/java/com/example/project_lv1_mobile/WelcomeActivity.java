package com.example.project_lv1_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        LinearLayout llWelcomeActivity = findViewById(R.id.llWelcomeActivity);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation_welcome);

        llWelcomeActivity.startAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isConnectedToInternet()) {
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(WelcomeActivity.this, "Vui lòng kết nối internet để sử dụng ứng dụng", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }, 3000);

    }

    // Phương thức kiểm tra kết nối internet
    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(WelcomeActivity.this.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }
}