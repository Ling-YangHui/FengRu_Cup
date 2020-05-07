package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) {

		// client
		new Thread() {
			@Override
			public void run() {
				try {
					@SuppressWarnings("resource")
					ServerSocket serverSocket7001 = new ServerSocket(7001);
					Socket socket7001;
					while (true) {
						socket7001 = serverSocket7001.accept();
						System.out.println("new client log in");
						new ClientThread(socket7001).start();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();

		// user
		new Thread() {
			@Override
			public void run() {
				try {
					@SuppressWarnings("resource")
					ServerSocket serverSocket7002 = new ServerSocket(7002);
					Socket socket7002;
					while (true) {
						socket7002 = serverSocket7002.accept();
						System.out.println("new user log in");
						new UserThread(socket7002).start();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();

		// device
		try {
			@SuppressWarnings("resource")
			ServerSocket serverSocket7000 = new ServerSocket(7000);
			Socket socket7000;
			while (true) {
				socket7000 = serverSocket7000.accept();
				System.out.println("new device log in");
				new DeviceThread(socket7000).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
