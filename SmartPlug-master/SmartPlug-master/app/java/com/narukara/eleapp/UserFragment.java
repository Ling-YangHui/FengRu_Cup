package com.narukara.eleapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class UserFragment extends Fragment {
    private boolean set = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getActivity().findViewById(R.id.user_head).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (User.isLogin()) {
                    startActivity(new Intent(getContext(), UserActivity.class));
                } else {
                    Toast.makeText(getContext(), "尚未登录", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getActivity().findViewById(R.id.device_manage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (User.isLogin()) {
                    Connect.connect(7002);
                    startActivity(new Intent(getContext(), DeviceManageActivity.class));
                } else {
                    Toast.makeText(getContext(), "尚未登录", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getActivity().findViewById(R.id.setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SettingActivity.class));
            }
        });
        getActivity().findViewById(R.id.about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AboutActivity.class));
            }
        });
        if (User.isLogin()) {
            getActivity().findViewById(R.id.log_in).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.sign_up).setVisibility(View.INVISIBLE);
            ((TextView) getActivity().findViewById(R.id.or)).setText(User.getNickname());
        } else {
            getActivity().findViewById(R.id.log_in).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Connect.connect(7002);
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
            });
            getActivity().findViewById(R.id.sign_up).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Connect.connect(7002);
                    startActivity(new Intent(getContext(), SignUpActivity.class));
                }
            });
            set = true;
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        if (User.isLogin()) {
            getActivity().findViewById(R.id.log_in).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.sign_up).setVisibility(View.INVISIBLE);
            ((TextView) getActivity().findViewById(R.id.or)).setText(User.getNickname());
        } else {
            getActivity().findViewById(R.id.log_in).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.sign_up).setVisibility(View.VISIBLE);
            ((TextView) getActivity().findViewById(R.id.or)).setText(R.string.or);
            if (!set) {
                getActivity().findViewById(R.id.log_in).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Connect.connect(7002);
                        startActivity(new Intent(getContext(), LoginActivity.class));
                    }
                });
                getActivity().findViewById(R.id.sign_up).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Connect.connect(7002);
                        startActivity(new Intent(getContext(), SignUpActivity.class));
                    }
                });
            }
            set = true;
        }
        super.onResume();
    }
}
