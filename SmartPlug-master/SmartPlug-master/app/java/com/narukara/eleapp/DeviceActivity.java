package com.narukara.eleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.Map;

public class DeviceActivity extends AppCompatActivity {
    private Thread powerThread;
    private static int num;
    private String key;

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
        setContentView(R.layout.activity_device);
        Map<String, String> map = User.getDevicelist();
        Iterator<String> iterator = map.keySet().iterator();
        String dkey = null;
        for (; num >= 0; num--) {
            dkey = iterator.next();
        }
        key = dkey;
        ((TextView) findViewById(R.id.device_act_name)).setText(map.get(key));
        ((TextView) findViewById(R.id.device_act_id)).setText(key);
        powerThread = new getPower((TextView) findViewById(R.id.textView7));
        powerThread.start();
    }

    public void editname(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText editText = new EditText(this);
        final Activity context = this;
        builder.setTitle("请输入设备名").setView(editText).setNegativeButton("取消", null);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editText.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(context, "设备名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                User.getDevicelist().put(key, name);
                ((TextView) context.findViewById(R.id.device_act_name)).setText(name);
            }
        });
        builder.show();
    }

    public void ops(View view) {
        switch (view.getId()) {
            case R.id.switch1:
                Connect.write("PW P SW");
        }
    }

    public static void setNum(int num) {
        DeviceActivity.num = num;
    }

    class getPower extends Thread {
        private TextView textView;

        public getPower(TextView textView) {
            this.textView = textView;
        }

        @Override
        public void run() {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                return;
            }
            if (Connect.isConnected()) {
                Connect.write("cn");
                Connect.write(key);
                String s = Connect.readLine();
                if (s == null) {
                    textView.setText("连接失败");
                    return;
                } else if (s.equals("nf")) {
                    textView.setText("设备离线");
                    return;
                } else if (s.equals("oc")) {
                    textView.setText("设备被占用");
                    return;
                } else if (s.equals("fd")) {
                    while (!isInterrupted()) {
                        s = Connect.readLine();
                        if (s == null) {
                            break;
                        }
                        textView.setText(s);
                    }
                }
            } else {
                textView.setText("连接失败");
                return;
            }
        }
    }

    @Override
    protected void onDestroy() {
        Connect.close();
        powerThread.interrupt();
        super.onDestroy();
    }
}
