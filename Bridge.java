import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Bridge {

	public static void main(String[] args) {
		try {
			final ServerSocket serverSocket7000 = new ServerSocket(7000);
			final Socket socket7000 = serverSocket7000.accept();
			serverSocket7000.close();
			final ServerSocket serverSocket7001 = new ServerSocket(7001);
			final Socket socket7001 = serverSocket7001.accept();
			serverSocket7001.close();
			System.out.println("connect success");
			
			class McuToApp extends Thread {
				@Override
				public void run() {
					try {
						BufferedReader br = new BufferedReader(new InputStreamReader(socket7000.getInputStream()));
						OutputStreamWriter osw = new OutputStreamWriter(socket7001.getOutputStream());
						String tmpString;
						while (true) {
							tmpString = br.readLine();
							if (tmpString == null) {
								break;
							}
							System.out.println("MCU to app:" + tmpString);
							osw.write(tmpString + "\n");
							osw.flush();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			new McuToApp().start();
			//AppToMcu
			BufferedReader br = new BufferedReader(new InputStreamReader(socket7001.getInputStream()));
			OutputStreamWriter osw = new OutputStreamWriter(socket7000.getOutputStream());
			String tmpString;
			while (true) {
				tmpString = br.readLine();
				if (tmpString == null) {
					break;
				}
				System.out.println("app to MCU:" + tmpString);
				osw.write(tmpString + "\n");
				osw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
