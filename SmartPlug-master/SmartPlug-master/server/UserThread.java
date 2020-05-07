package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UserThread extends Thread {
	private Socket socket;

	public UserThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		BufferedReader bufferedReader;
		BufferedWriter bufferedWriter;
		String flag, email, nickname, password;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			flag = bufferedReader.readLine();
			if (flag == null) {
				throw new IOException();
			}
			if (flag.equals("su")) {
				System.out.println("try sign up");
				email = bufferedReader.readLine();
				nickname = bufferedReader.readLine();
				password = bufferedReader.readLine();
				if (password == null) {
					throw new IOException();
				}
				if (UserList.signUpCheck(email)) {
					UserList.addUser(new User(email, nickname, password));
					bufferedWriter.write("ok\n");
					System.out.println("sign up success");
				} else {
					bufferedWriter.write("ae\n");
					System.out.println("sign up fail");
				}
				bufferedWriter.flush();
			} else if (flag.equals("li")) {
				System.out.println("try login");
				email = bufferedReader.readLine();
				password = bufferedReader.readLine();
				if (password == null) {
					throw new IOException();
				}
				if (UserList.logIn(email, password)) {
					bufferedWriter.write("ok\n");
					bufferedWriter.flush();
					bufferedWriter.write(UserList.getNickname(email));
					System.out.println("login success");
				} else {
					bufferedWriter.write("wr\n");
					System.out.println("login fail");
				}
				bufferedWriter.flush();
			} else if (flag.equals("ul")) {
				System.out.println("try upload");
				email = bufferedReader.readLine();
				password = bufferedReader.readLine();
				if (password == null) {
					throw new IOException();
				}
				if (UserList.logIn(email, password)) {
					bufferedWriter.write("ok\n");
					bufferedWriter.flush();
					Map<String, String> deviceList = new HashMap<String, String>();
					while (true) {
						String key = bufferedReader.readLine();
						if (key.equals("endofdevicelist")) {
							break;
						}
						String value = bufferedReader.readLine();
						deviceList.put(key, value);
					}
					UserList.upload(email, deviceList);
					bufferedWriter.write("ok\n");
					System.out.println("upload success");
				} else {
					bufferedWriter.write("wr\n");
					System.out.println("upload fail: wrong account");
				}
				bufferedWriter.flush();
			} else if (flag.equals("dl")) {
				System.out.println("try download");
				email = bufferedReader.readLine();
				password = bufferedReader.readLine();
				if (password == null) {
					throw new IOException();
				}
				if (UserList.logIn(email, password)) {
					bufferedWriter.write("ok\n");
					bufferedWriter.flush();
					Iterator<Map.Entry<String, String>> iterator = UserList.download(email).entrySet().iterator();
					while (iterator.hasNext()) {
						Map.Entry<String, String> entry = iterator.next();
						bufferedWriter.write(entry.getKey() + "\n");
						bufferedWriter.write(entry.getValue() + "\n");
					}
					bufferedWriter.write("endofdevicelist\n");
					bufferedWriter.flush();
					System.out.println("download finish");
				} else {
					bufferedWriter.write("wr\n");
					System.out.println("download fail: worng account");
				}
				bufferedWriter.flush();
			}
		} catch (IOException e) {
			System.out.println("User operation fail");
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
