import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class Bond extends Thread {
	User user;
	Device device;
	boolean broken = false;

	public Bond(User user, Device device) {
		this.user = user;
		this.device = device;
	}

	@Override
	public void run() {
		// user to device
		new Thread() {

			@Override
			public void run() {
				try {
					String message;
					BufferedReader bufferedReader = user.getBufferedReader();
					BufferedWriter bufferedWriter = device.getBufferedWriter();
					while (!broken) {
						message = bufferedReader.readLine();
						if (message == null) {
							throw new IOException();
						}
						System.out.println("user to device " + device.getId() + ": " + message);
						bufferedWriter.write(message + '\n');
						bufferedWriter.flush();
					}
				} catch (IOException e) {
					broken = true;
					System.out.println(device.getId() + " bridge broken");
				}
			}

		}.start();

		// device to user
		try {
			String message;
			BufferedReader bufferedReader = device.getBufferedReader();
			BufferedWriter bufferedWriter = user.getBufferedWriter();
			while (!broken) {
				message = bufferedReader.readLine();
				if (message == null) {
					throw new IOException();
				}
				System.out.println("device " + device.getId() + " to user: " + message);
				bufferedWriter.write(message + '\n');
				bufferedWriter.flush();
			}
		} catch (IOException e) {
			broken = true;
			System.out.println(device.getId() + " bridge broken");
		}
	}

}
