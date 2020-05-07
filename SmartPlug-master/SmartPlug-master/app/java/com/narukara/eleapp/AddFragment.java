package com.narukara.eleapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


public class AddFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getActivity().findViewById(R.id.key).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "开发中", Toast.LENGTH_SHORT).show();
            }
        });
        getActivity().findViewById(R.id.wifi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "开发中", Toast.LENGTH_SHORT).show();
            }
        });
        getActivity().findViewById(R.id.web).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connect.connect(7001);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final EditText editText = new EditText(getContext());
                final Context context = getContext();
                builder.setTitle("请输入设备id").setView(editText).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Connect.close();
                    }
                });
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String id = editText.getText().toString();
                        if (id.equals("")) {
                            Toast.makeText(context, "信息不能为空", Toast.LENGTH_SHORT).show();
                            Connect.close();
                            return;
                        }
                        if (Connect.isConnected()) {
                            Connect.write("st");
                            Connect.write(id);
                            String result = Connect.readLine();
                            if (result.equals("nf")) {
                                Toast.makeText(context, "设备不存在或不在线", Toast.LENGTH_SHORT).show();
                            } else if (result.equals("fd") || result.equals("oc")) {
                                User.addDevice(id);
                                Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
                                if (User.isLogin()) {
                                    User.record(getActivity());
                                }
                            }
                        } else {
                            Toast.makeText(context, "连接服务器失败，请稍后再试", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.show();
            }
        });
        super.onActivityCreated(savedInstanceState);
    }
}
