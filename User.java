import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class User {
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;

	public BufferedReader getBufferedReader() {
		return bufferedReader;
	}

	public BufferedWriter getBufferedWriter() {
		return bufferedWriter;
	}

	private User(Socket socket) {
		this.socket = socket;
	}

	public static void newUser(final Socket socket) {
		new Thread() {
			@Override
			public void run() {
				new User(socket).init();
			}
		}.start();
	}

	private void init() {
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			String targetID;
			Device targetDevice;
			while (true) {
				targetID = bufferedReader.readLine();
				if (targetID == null) {
					throw new IOException();
				}
				System.out.print("try find " + targetID + ": ");
				targetDevice = Device.MapFind(targetID);
				if (targetDevice == null) {
					System.out.println("not found");
					bufferedWriter.write("id not found\n");
					bufferedWriter.flush();
				} else if (targetDevice.isActive()) {
					System.out.println("occupied");
					bufferedWriter.write("device occupied\n");
					bufferedWriter.flush();
				} else {
					System.out.println("found");
					bufferedWriter.write("success\n");
					bufferedWriter.flush();
					break;
				}
			}
			new Bond(this, targetDevice).start();
		} catch (IOException e) {
			System.out.println("user init fail");
		}
	}
}
