import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class NetTestS {

	public static void main(String[] args) throws IOException, InterruptedException {
		ServerSocket ss = new ServerSocket(7000);
		final Socket s = ss.accept();
		ss.close();
		System.out.println("Connect success");
		
		
		class PutThread extends Thread{
			@Override
			public void run(){
				Scanner sc = new Scanner(System.in);
				OutputStreamWriter osw;
				try{
					osw = new OutputStreamWriter(s.getOutputStream());
					while (true) {
						osw.write(sc.nextLine() + "\n");
						osw.flush();
					}
				} 
				catch (IOException e){
					e.printStackTrace();
				}
				sc.close();
			}
		}
		PutThread t1 = new PutThread();
		t1.start();
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		while (true) 
		{
			System.out.println(">" + br.readLine());	
		}
	}
}
