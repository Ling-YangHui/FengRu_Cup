package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientThread extends Thread {
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private boolean exit = false;

	public ClientThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			String flag = bufferedReader.readLine();
			if (flag == null || (!flag.equals("st") && !flag.equals("cn"))) {
				throw new IOException();
			}
			final String targetID;
			final Device targetDevice;
			while (true) {
				String findID = bufferedReader.readLine();
				if (findID == null) {
					throw new IOException();
				}
				System.out.print("try find \"" + findID + "\": ");
				Device findDevice = DeviceList.findDevice(findID);
				if (findDevice == null) {
					System.out.println("not found");
					bufferedWriter.write("nf\n");
					bufferedWriter.flush();
				} else if (findDevice.isOccupied()) {
					System.out.println("occupied");
					bufferedWriter.write("oc\n");
					bufferedWriter.flush();
				} else {
					System.out.println("found");
					bufferedWriter.write("fd\n");
					bufferedWriter.flush();
					if (flag.equals("cn")) {
						findDevice.setOccupied(true);
						targetDevice = findDevice;
						targetID = findID;
						break;
					}
				}
			}

			// warning
			Thread dtcThread = new Thread() {
				@Override
				public void run() {
					try {
						String message;
						BufferedReader dReader = targetDevice.getBufferedReader();
						synchronized (dReader) {
							while (!exit) {
								message = dReader.readLine();
								if (message == null) {
									throw new IOException();
								}
								System.out.println("device to client  \"" + targetID + "\": " + message);
								bufferedWriter.write(message + '\n');
								bufferedWriter.flush();
							}
						}
					} catch (IOException e) {
						exit = true;
						targetDevice.setOccupied(false);
						System.out.println("\"" + targetID + "\" connect broken(dtc)");
					}
				}
			};
			Thread ctdThread = new Thread() {
				@Override
				public void run() {
					try {
						String message;
						BufferedWriter dWriter = targetDevice.getBufferedWriter();
						while (!exit) {
							message = bufferedReader.readLine();
							if (message == null) {
								throw new IOException();
							}
							System.out.println("client to device \"" + targetID + "\": " + message);
							dWriter.write(message + "\r\n");
							dWriter.flush();
						}
					} catch (IOException e) {
						exit = true;
						targetDevice.setOccupied(false);
						try {
							socket.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						System.out.println("\"" + targetID + "\" connect broken(ctd)");
					}
				}
			};
			dtcThread.start();
			ctdThread.start();
//			while (true) {
//				try {
//					socket.sendUrgentData(0xFF);
//					sleep(5000);
//				} catch (IOException e) {
//					System.out.println("client log out");
//					dtcThread.interrupt();
//					ctdThread.interrupt();
//					break;
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//					break;
//				}
//			}

		} catch (IOException e) {
			System.out.println("client init fail");
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
