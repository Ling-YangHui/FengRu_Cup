package device;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class Device_1 {

	public static void main(String[] args) {
		try {
			Socket socket = new Socket();
			socket.bind(new InetSocketAddress("127.0.0.1", 7101));
			socket.connect(new InetSocketAddress("127.0.0.1", 7000));
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bufferedWriter.write("1\n");
			bufferedWriter.flush();
			Random random = new Random();
			Integer power;
			while(true) {
				Thread.sleep(3000);
				power = random.nextInt(2000);
				bufferedWriter.write(power.toString()+"\n");
				bufferedWriter.flush();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
