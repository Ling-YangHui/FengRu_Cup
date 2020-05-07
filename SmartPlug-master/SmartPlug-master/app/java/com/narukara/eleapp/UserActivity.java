package com.narukara.eleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //改状态栏颜色
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.grey));
        //隐藏标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_user);
        ((TextView) findViewById(R.id.user_email)).setText(User.getEmail());
        ((TextView) findViewById(R.id.user_name)).setText(User.getNickname());
    }

    public void logout(View view) {
        User.logout(this);
        finish();
    }

    public void changePassword(View view) {
        Toast.makeText(this, "开发中", Toast.LENGTH_SHORT).show();
    }
}
