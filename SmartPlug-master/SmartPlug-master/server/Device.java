package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

public class Device {
	private String id;
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private boolean isOccupied = false;

	public Device(String id, Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
		this.id = id;
		this.socket = socket;
		this.bufferedReader = bufferedReader;
		this.bufferedWriter = bufferedWriter;
	}

	public boolean isOccupied() {
		return isOccupied;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return id + " " + socket.toString();
	}

	public BufferedReader getBufferedReader() {
		return bufferedReader;
	}

	public BufferedWriter getBufferedWriter() {
		return bufferedWriter;
	}

	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}

	public void recycle() {
		try {
			bufferedReader.close();
			bufferedWriter.close();
			socket.close();
			isOccupied = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
