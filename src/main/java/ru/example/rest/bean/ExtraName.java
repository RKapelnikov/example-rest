package ru.example.rest.bean;

public enum ExtraName {

	LOGIN("login"), PASSWORD("password");

	private String name;

	private ExtraName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
