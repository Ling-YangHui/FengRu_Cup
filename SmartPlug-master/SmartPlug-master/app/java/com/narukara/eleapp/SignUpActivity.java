package com.narukara.eleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignUpActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_sign_up);
    }

    public void signUp(View view) {
        if (Connect.isConnected()) {
            Connect.write("su");
            String email = ((EditText) findViewById(R.id.editText3)).getText().toString();
            String nickname = ((EditText) findViewById(R.id.editText4)).getText().toString();
            String password = ((EditText) findViewById(R.id.editText5)).getText().toString();
            if (email.equals("") || nickname.equals("") || password.equals("")) {
                Toast.makeText(this, "信息不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            Connect.write(email);
            Connect.write(nickname);
            Connect.write(Connect.md5(password));
            String s = Connect.readLine();
            if (s == null) {
                Toast.makeText(this, "连接服务器失败，请稍后再试", Toast.LENGTH_SHORT).show();
            } else if (s.equals("ok")) {
                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                User.login(email,nickname,password,this);
                finish();
            } else if (s.equals("ae")) {
                Toast.makeText(this, "此邮箱已被注册！", Toast.LENGTH_SHORT).show();
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
