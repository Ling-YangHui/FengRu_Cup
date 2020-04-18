import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Connect {

	public static void main(String[] args) {

		new Thread() {
			@Override
			public void run() {
				try {
					@SuppressWarnings("resource")
					ServerSocket serverSocket7001 = new ServerSocket(7001);
					Socket socket7001;
					while (true) {
						socket7001 = serverSocket7001.accept();
						System.out.println("new user log in");
						User.newUser(socket7001);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();

		try {
			@SuppressWarnings("resource")
			ServerSocket serverSocket7000 = new ServerSocket(7000);
			Socket socket7000;
			while (true) {
				socket7000 = serverSocket7000.accept();
				System.out.println("new device log in");
				Device.newDevice(socket7000);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
