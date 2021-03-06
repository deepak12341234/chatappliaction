package servlets;

import java.io.Serializable;
import java.util.Map;

public class User implements Serializable {
	private static final long serialVersionUID = -5161680823918839255L;
	
	private String name;
	
	private String status;
	public static Map<String, String> data ;
	public static final String ONLINE="online";
	public static final String OFFLINE="online";
	


	public Map<String, String> getData() {
		if(data!=null) {
		return data;
	}
		else {
		return null;
		}
}
	public void setData(Map<String, String> data) {
		User.data = data;
	}

	public String getName() {
		return name;
	}
	
	public String getStatus() {
		return status;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "User [name=" + name + ", status=" + status + ", data=" + data + "]";
	}
	
}
