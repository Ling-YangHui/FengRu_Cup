package com.narukara.eleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class SettingActivity extends AppCompatActivity {

//    private int t = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    public void newEdition(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://49.235.143.220/smartplug.html")));
    }

    public void egg(View view) {
//        t++;
//        if (t > 4) {
//            startActivity(new Intent(this, NetTestActivity.class));
//        }
    }
}
