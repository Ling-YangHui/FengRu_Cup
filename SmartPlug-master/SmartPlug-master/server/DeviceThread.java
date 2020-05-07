package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class DeviceThread extends Thread {
	private Socket socket;
	private String id;

	public DeviceThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		BufferedReader bufferedReader;
		BufferedWriter bufferedWriter;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			id = bufferedReader.readLine();
			if (id == null) {
				throw new IOException();
			}
			System.out.println("new device id: " + id);
		} catch (IOException e) {
			System.out.println("new device init fail");
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
		DeviceList.addDevice(new Device(id, socket, bufferedReader, bufferedWriter));
		new Thread() {
			@Override
			public void run() {
				try {
					while (true) {
						sleep(950);
						synchronized (bufferedReader) {
							bufferedReader.readLine();
						}
					}
				} catch (InterruptedException | IOException e) {
					return;
				}
			}
		}.start();
		while (true) {
			try {
				socket.sendUrgentData(0xFF);
				sleep(5000);
			} catch (IOException e) {
				System.out.println("device \"" + id + "\" log out");
				DeviceList.removeDevice(id);
				break;
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}

	}
}
