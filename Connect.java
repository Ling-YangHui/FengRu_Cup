import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Connect {
	private static Map<String, Socket> map = new HashMap<>();

	public static void main(String[] args) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					@SuppressWarnings("resource")
					ServerSocket serverSocket7001 = new ServerSocket(7001);
					Socket socket7001;
					while (true) {
						socket7001 = serverSocket7001.accept();
						System.out.println("user log in");
						new UserLogIn(socket7001).start();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();

		try {
			@SuppressWarnings("resource")
			ServerSocket serverSocket7000 = new ServerSocket(7000);
			Socket socket7000;
			while (true) {
				socket7000 = serverSocket7000.accept();
				System.out.println("device log in");
				new PlugLogIn(socket7000).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void MapShow() {
		System.out.println("--------");
		System.out.println(map);
		System.out.println("--------");
	}
	
	public static void MapAdd(String string, Socket socket) {
		map.put(string, socket);
		MapShow();
	}

	public static Socket MapFind(String string) {
		return map.get(string);
	}

	public static void MapRemove(Socket socket) {
		Collection<Socket> values = map.values();
		values.remove(socket);
		MapShow();
	}

}

class PlugLogIn extends Thread {
	Socket socket;

	public PlugLogIn(Socket socket) {
		super();
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			String id = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
			System.out.println("device id: " + id);
			Connect.MapAdd(id, socket);
		} catch (IOException e) {
			System.out.println("device connect broken");
		}
		super.run();
	}

}

class UserLogIn extends Thread {
	Socket socket;

	public UserLogIn(Socket socket) {
		super();
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			Socket targetSocket;
			while (true) {
				String id = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
				System.out.println("try find " + id);
				targetSocket = Connect.MapFind(id);
				if (targetSocket == null) {
					System.out.println("not found");
				} else {
					System.out.println("found");
					break;
				}
			}
			new Bond(socket, targetSocket).start();
		} catch (IOException e) {
			System.out.println("user connect broken");
		}
		super.run();
	}

}

class Bond extends Thread {
	Socket userSocket;
	Socket deviceSocket;
	boolean broken = false;

	public Bond(Socket userSocket, Socket deviceSocket) {
		super();
		this.userSocket = userSocket;
		this.deviceSocket = deviceSocket;
	}

	@Override
	public void run() {
		// app to MCU
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
					OutputStreamWriter osw = new OutputStreamWriter(deviceSocket.getOutputStream());
					String tmpString;
					while (true) {
						tmpString = br.readLine();
						if (tmpString == null) {
							throw new IOException();
						}
						System.out.println("app to MCU:" + tmpString);
						osw.write(tmpString + "\n");
						osw.flush();
					}
				} catch (IOException e) {
					if (!broken) {
						broken = true;
						System.out.println("Bridge broken");
						Connect.MapRemove(deviceSocket);
					}
				}
			}
		}).start();
		// MCU to app
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(deviceSocket.getInputStream()));
			OutputStreamWriter osw = new OutputStreamWriter(userSocket.getOutputStream());
			String tmpString;
			while (true) {
				tmpString = br.readLine();
				if (tmpString == null) {
					throw new IOException();
				}
				System.out.println("MCU to app: " + tmpString);
				osw.write(tmpString + "\n");
				osw.flush();
			}
		} catch (IOException e) {
			if (!broken) {
				broken = true;
				System.out.println("Bridge broken");
				Connect.MapRemove(deviceSocket);
			}
		}
		super.run();
	}

}
