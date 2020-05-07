package local;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class LocalTest {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try {
			Scanner scanner = new Scanner(System.in);
			//49.235.143.220
			//127.0.0.1
			Socket s = new Socket("49.235.143.220", scanner.nextInt());
			scanner.nextLine();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

			new Thread() {
				@Override
				public void run() {
					try {
						while (true) {
							System.out.println(">" + bufferedReader.readLine());
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.start();

			while (true) {
				bufferedWriter.write(scanner.nextLine() + "\n");
				bufferedWriter.flush();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
