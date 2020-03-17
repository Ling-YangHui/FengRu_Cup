import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class NetTestS {

	public static void main(String[] args) {
		try {
			ServerSocket ss = new ServerSocket(7000);
			final Socket s = ss.accept();
			ss.close();
			System.out.println("Connect success");

			class PutThread extends Thread {
				@Override
				public void run() {
					Scanner sc = new Scanner(System.in);
					try {
						OutputStreamWriter osw = new OutputStreamWriter(s.getOutputStream());
						String tmpString;
						while (true) {
							tmpString = sc.nextLine();
							if(tmpString.equals("end")) {
								break;
							}
							osw.write(tmpString + "\n");
							osw.flush();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}finally{
						sc.close();
					}
				}
			}
			new PutThread().start();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String tmpString;
			while (true) {
				tmpString = br.readLine();
				if(tmpString == null) {
					break;
				}
				System.out.println(">" + tmpString);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
