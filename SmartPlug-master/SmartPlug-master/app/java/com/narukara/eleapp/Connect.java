package com.narukara.eleapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Connect {
    private static Socket socket;
    private static BufferedReader bufferedReader;
    private static BufferedWriter bufferedWriter;
    private static boolean isConnected = false;

    public static void connect(final int port) {
        new Thread() {
            @Override
            public void run() {
                try {
                    socket = new Socket("49.235.143.220", port);
                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    isConnected = true;
                } catch (IOException e) {
                    isConnected = false;
                }
            }
        }.start();
    }

    public static boolean isConnected() {
        return isConnected;
    }

    public static String readLine() {
        if (!isConnected) {
            return null;
        }
        final String[] s = new String[1];
        synchronized (bufferedReader) {
            new Thread() {
                @Override
                public void run() {
                    synchronized (bufferedReader) {
                        try {
                            s[0] = bufferedReader.readLine();
                        } catch (IOException e) {
                            s[0] = null;
                            close();
                        } finally {
                            bufferedReader.notify();
                        }
                    }
                }
            }.start();
            try {
                bufferedReader.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return s[0];
        }
    }

    public static boolean write(final String s) {
        if (!isConnected) {
            return false;
        }
        Thread thread = new Thread() {
            @Override
            public void run() {
                synchronized (bufferedWriter) {
                    try {
                        bufferedWriter.write(s);
                        if (!s.contains("\n")) {
                            bufferedWriter.write("\n");
                        }
                        bufferedWriter.flush();
                    } catch (IOException e) {
                        close();
                    }
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void close() {
        isConnected = false;
        new Thread() {
            @Override
            public void run() {
                try {
                    socket.close();
                } catch (IOException e) {

                }
            }
        }.start();
    }

    public static String md5(String s) {
        try {
            return new BigInteger(1, MessageDigest.getInstance("md5").digest(s.getBytes())).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
