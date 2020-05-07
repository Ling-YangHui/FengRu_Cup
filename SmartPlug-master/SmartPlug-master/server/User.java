package server;

import java.io.Serializable;
import java.util.Map;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8042827606648394110L;
	private String email;
	private String nickname;
	private String password;
	private Map<String,String> deviceList;

	public User(String email, String nickname, String password) {
		this.email = email;
		this.nickname = nickname;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String toString() {
		return email + " " + nickname;
	}

	public boolean checkPassword(String string) {
		return password.equals(string);
	}

	public Map<String,String> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(Map<String,String> deviceList) {
		this.deviceList = deviceList;
	}

	public String getNickname() {
		return nickname;
	}

}
