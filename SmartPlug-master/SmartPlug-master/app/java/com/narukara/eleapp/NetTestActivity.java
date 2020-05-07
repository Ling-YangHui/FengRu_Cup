package com.narukara.eleapp;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NetTestActivity extends AppCompatActivity {
    Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_test);
    }

    public void netTest(View view) {
        final TextView textView = findViewById(R.id.textView);
        class NetThread extends Thread {
            @Override
            public void run() {
                try {
                    socket = new Socket("49.235.143.220", 7002);
                    textView.setText("连接成功");

                    class GetThread extends Thread {//输入线程

                        @Override
                        public void run() {
                            StringBuilder stringBuilder = new StringBuilder(textView.getText().toString() + "\n接收信息：\n");
                            String tmp;
                            try {
                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                while (true) {
                                    tmp = bufferedReader.readLine();
                                    if (tmp == null) {
                                        break;
                                    }
                                    textView.setText(stringBuilder.append(">" + tmp + "\n").toString());
                                }
                            } catch (IOException e) {
                                textView.setText("输入流异常");
                            }
                        }
                    }
                    new GetThread().start();

                } catch (IOException e) {
                    textView.setText("连接失败");
                }
            }
        }
        new NetThread().start();
    }

    public void sendMessage(View view) {
        if (socket == null) {
            Toast.makeText(this, "尚未连接", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            final EditText editText = new EditText(this);
            final NetTestActivity netTestActivity = this;
            class SendThread extends Thread {//输出线程

                @Override
                public void run() {
                    try {
                        outputStreamWriter.write(editText.getText().toString() + "\n");
                        outputStreamWriter.flush();
                    } catch (IOException e) {
                        Toast.makeText(netTestActivity, "输出流异常", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("发送消息").setView(editText).setNegativeButton("取消", null);
            builder.setPositiveButton("发送", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new SendThread().start();
                }
            });
            builder.show();
        } catch (IOException e) {
            Toast.makeText(this, "输出流创建异常", Toast.LENGTH_SHORT).show();
        }
    }
}
