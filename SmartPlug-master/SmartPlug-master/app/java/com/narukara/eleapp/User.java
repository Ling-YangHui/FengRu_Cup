package com.narukara.eleapp;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class User {
    private static String email;
    private static String nickname;
    private static String password;
    private static boolean login;
    private static Map<String, String> devicelist;
    private static boolean isloaded;

    static {
        isloaded = false;
    }

    public static void load(Activity activity) {
        if (isloaded) {
            return;
        }
        isloaded = true;
        try {
            File f = new File(activity.getFilesDir(), "User");
            File file = new File(activity.getFilesDir(), "Device");
            if (!f.exists() || !file.exists()) {
                f.createNewFile();
                file.createNewFile();
                devicelist = new HashMap<>();
                login = false;
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                email = br.readLine();
                if (email.equals("null")) {
                    devicelist = new HashMap<>();
                    login = false;
                    return;
                }
                nickname = br.readLine();
                password = br.readLine();
                login = true;
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                devicelist = (Map<String, String>) ois.readObject();
                br.close();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            login = false;
        }
    }

    public static void login(String email, String nickname, String password, Activity activity) {
        User.email = email;
        User.nickname = nickname;
        User.password = password;
        User.login = true;
        record(activity);
    }

    public static void logout(Activity activity) {
        email = null;
        nickname = null;
        password = null;
        login = false;
        devicelist = new HashMap<>();
        record(activity);
    }

    public static boolean isLogin() {
        return login;
    }

    public static String getNickname() {
        return nickname;
    }

    public static String getEmail() {
        return email;
    }

    public static void record(Activity activity) {
        try {
            File f = new File(activity.getFilesDir(), "User");
            File file = new File(activity.getFilesDir(), "Device");
            if (!f.exists() || !file.exists()) {
                f.createNewFile();
                file.createNewFile();
            }
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
            bw.write(email + "\n");
            bw.write(nickname + "\n");
            bw.write(password + "\n");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(devicelist);
            bw.close();
            oos.close();
        } catch (IOException e) {

        }
    }

    public static void addDevice(String id) {
        devicelist.put(id, "智能插座");
    }

    public static Map<String, String> getDevicelist() {
        return devicelist;
    }

    public static void login2() {
        if (Connect.isConnected()) {
            Connect.write(email);
            Connect.write(Connect.md5(password));
        }
    }

    public static void setDevicelist(Map<String, String> devicelist) {
        User.devicelist = devicelist;
    }
}
