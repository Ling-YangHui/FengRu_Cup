package server;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DeviceList {
	private static Map<String, Device> deviceList = new ConcurrentHashMap<>();

	public static void addDevice(Device device) {
		deviceList.put(device.getId(), device);
		show();
	}

	private static void show() {
		System.out.println("----Device list----");
		Collection<Device> devices = deviceList.values();
		Iterator<Device> iterator = devices.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next().toString());
		}
		System.out.println("--------end--------");
	}

	public static Device findDevice(String id) {
		return deviceList.get(id);
	}

	public static void removeDevice(String id) {
		Device device = deviceList.get(id);
		device.recycle();
		deviceList.remove(id);
		show();
	}
}
