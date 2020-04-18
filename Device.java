import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Device {
	private static Map<String, Device> map = new ConcurrentHashMap<>();
	private String id;
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private boolean active = false;

	public static void newDevice(final Socket socket) {
		new Thread() {
			@Override
			public void run() {
				new Device(socket).init();
			}
		}.start();
	}

	private Device(Socket socket) {
		this.socket = socket;
	}

	public BufferedReader getBufferedReader() {
		return bufferedReader;
	}

	public BufferedWriter getBufferedWriter() {
		return bufferedWriter;
	}

	public boolean isActive() {
		return active;
	}

	public String getId() {
		return id;
	}

	private void init() {
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			id = bufferedReader.readLine();
			if (id == null) {
				throw new IOException();
			}
			System.out.println("new device id: " + id);
		} catch (IOException e) {
			System.out.println("new device init fail");
			return;
		}
		MapAdd(this);
		final Device thisDevice = this;
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						socket.sendUrgentData(0xFF);
						sleep(1000);
					} catch (IOException e) {
						System.out.println("device " + thisDevice.id + " log out");
						MapRemove(thisDevice);
						break;
					} catch (InterruptedException e) {
						e.printStackTrace();
						break;
					}
				}
			}
		}.start();
	}

	private static void MapShow() {
		System.out.println("----Device list----");
		Set<String> ids = map.keySet();
		Iterator<String> iterator = ids.iterator();
		Collection<Device> devices = map.values();
		Iterator<Device> iterator2 = devices.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next() + " " + iterator2.next().socket.toString());
		}
		System.out.println("--------end--------");
	}

	private static void MapAdd(Device device) {
		map.put(device.id, device);
		MapShow();
	}

	public static Device MapFind(String string) {
		return map.get(string);
	}

	private static void MapRemove(Device device) {
		Collection<Device> values = map.values();
		values.remove(device);
		MapShow();
	}
}
