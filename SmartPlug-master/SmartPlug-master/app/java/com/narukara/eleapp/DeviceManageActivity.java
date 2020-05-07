package com.narukara.eleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DeviceManageActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_device_manage);
    }

    public void syn(View view) {
        if (Connect.isConnected()) {
            switch (view.getId()) {
                case R.id.upload:
                    Connect.write("ul");
                    User.login2();
                    if (Connect.readLine().equals("ok")) {
                        Iterator<Map.Entry<String, String>> iterator = User.getDevicelist().entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry<String, String> entry = iterator.next();
                            Connect.write(entry.getKey());
                            Connect.write(entry.getValue());
                        }
                        Connect.write("endofdevicelist");
                        if (Connect.readLine().equals("ok")) {
                            Toast.makeText(this, "同步成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        Toast.makeText(this, "账户状态异常", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.download:
                    Connect.write("dl");
                    User.login2();
                    if (Connect.readLine().equals("ok")) {
                        Map<String, String> deviceList = new HashMap<>();
                        while (true) {
                            String key = Connect.readLine();
                            if (key.equals("endofdevicelist")) {
                                break;
                            }
                            String value = Connect.readLine();
                            deviceList.put(key, value);
                        }
                        User.setDevicelist(deviceList);
                        Toast.makeText(this, "同步成功", Toast.LENGTH_SHORT).show();
                        finish();
                        User.record(this);
                    } else {
                        Toast.makeText(this, "账户状态异常", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            Connect.close();
        } else {
            Toast.makeText(this, "连接至服务器失败", Toast.LENGTH_SHORT).show();
        }
    }

    public void del(View view) {
        Toast.makeText(this, "开发中", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        Connect.close();
        super.onDestroy();
    }
}
