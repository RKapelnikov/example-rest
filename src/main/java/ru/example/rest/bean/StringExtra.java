package ru.example.rest.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "extra")
@XmlAccessorType(XmlAccessType.FIELD)
public class StringExtra extends AbstractExtra<String> {

	@XmlValue
	private String value;

	public StringExtra() {
		super();
	}

	public StringExtra(String name) {
		super(name);
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
