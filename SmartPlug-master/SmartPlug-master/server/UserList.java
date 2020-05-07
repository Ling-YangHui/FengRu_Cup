package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public class UserList {
	private static Map<String, User> userList;

	static {
		File file = new File(System.getProperty("user.dir"), "UserList");
		try {
			if (!file.exists()) {
				file.createNewFile();
				userList = new ConcurrentHashMap<>();
			}else {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
				userList = (Map<String, User>) ois.readObject();
				ois.close();
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void show() {
		System.out.println("----User list----");
		Collection<User> users = userList.values();
		Iterator<User> iterator = users.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next().toString());
		}
		System.out.println("--------end--------");
	}

	public static boolean signUpCheck(String email) {
		Set<String> set = userList.keySet();
		return !set.contains(email);
	}

	public static void addUser(User user) {
		userList.put(user.getEmail(), user);
		record();
		show();
	}

	public static boolean logIn(String email, String password) {
		User user = userList.get(email);
		return user != null && user.checkPassword(password);
	}

	public static void upload(String email, Map<String,String> deviceList) {
		User user = userList.get(email);
		user.setDeviceList(deviceList);
	}

	public static Map<String,String> download(String email) {
		User user = userList.get(email);
		return user.getDeviceList();
	}

	public static String getNickname(String email) {
		User user = userList.get(email);
		return user.getNickname();
	}

	public static void record() {
		File file = new File(System.getProperty("user.dir"), "UserList");
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file, false));
			oos.writeObject(userList);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
