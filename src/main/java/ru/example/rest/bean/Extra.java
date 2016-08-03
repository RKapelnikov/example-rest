package ru.example.rest.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "extra")
public class Extra {
	
	@XmlAttribute(name = "name")
	public String key;
	
	@XmlValue
	public String value;

	public Extra() {
		super();
	}

	public Extra(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
}