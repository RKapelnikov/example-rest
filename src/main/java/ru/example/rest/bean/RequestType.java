package ru.example.rest.bean;

public enum RequestType {

	CREATE_USER("CREATE-AGT"), GET_BALANCE("GET-BALANCE");
	
	private String type;
	
	private RequestType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
}
