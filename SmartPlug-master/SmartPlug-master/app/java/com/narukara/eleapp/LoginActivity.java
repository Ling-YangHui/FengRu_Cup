package com.narukara.eleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_login);
    }

    public void login(View view) {
        if (Connect.isConnected()) {
            String email = ((EditText) findViewById(R.id.editText)).getText().toString();
            String password = ((EditText) findViewById(R.id.editText2)).getText().toString();
            if (email.equals("") || password.equals("")) {
                Toast.makeText(this, "信息不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            Connect.write("li");
            Connect.write(email);
            Connect.write(Connect.md5(password));
            String s = Connect.readLine();
            if (s == null) {
                Toast.makeText(this, "连接服务器失败，请稍后再试", Toast.LENGTH_SHORT).show();
            } else if (s.equals("ok")) {
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                String nickname = Connect.readLine();
                User.login(email, nickname, password,this);
                finish();
            } else if (s.equals("wr")) {
                Toast.makeText(this, "登录失败：邮箱或密码错误", Toast.LENGTH_SHORT).show();
                Connect.connect(7002);
            }
        } else {
            Toast.makeText(this, "连接服务器失败，请稍后再试！", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onDestroy() {
        Connect.close();
        super.onDestroy();
    }
}
