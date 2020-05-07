package com.narukara.eleapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DivFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_div, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        String[] from = {"name", "id", "online"};
        int[] to = {R.id.device_name, R.id.device_id, R.id.is_on_line};
        Map<String, String> deviceList = User.getDevicelist();
        if (!deviceList.isEmpty()) {
            Map<String, Object>[] map = new HashMap[deviceList.size()];
            List<Map<String, Object>> list = new ArrayList<>();
            int i = 0;
            for (Map.Entry<String, String> entry : deviceList.entrySet()) {
                map[i] = new HashMap<>();
                map[i].put("name", entry.getValue());
                map[i].put("id", entry.getKey());
                map[i].put("online", R.drawable.online);
                list.add(map[i]);
                i++;
            }
            ListView listView = getActivity().findViewById(R.id.device_list);
            listView.setAdapter(new SimpleAdapter(getContext(), list, R.layout.device_item, from, to));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Connect.connect(7001);
                    DeviceActivity.setNum(position);
                    startActivity(new Intent(getContext(), DeviceActivity.class));
                }
            });
        }
        super.onActivityCreated(savedInstanceState);
    }
}
