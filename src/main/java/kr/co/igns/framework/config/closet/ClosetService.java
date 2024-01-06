package kr.co.igns.framework.config.closet;

public class ClosetService {
	String name;
	
	public ClosetService(String name) {
		this.name = name;
	}
	
	public String getClosetName() {
		return "closet Name is [" + name + "]";
	}
}
