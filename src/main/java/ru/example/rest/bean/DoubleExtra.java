package ru.example.rest.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "extra")
@XmlAccessorType(XmlAccessType.FIELD)
public class DoubleExtra extends AbstractExtra<Double> {

	@XmlValue
	private double value;

	public DoubleExtra() {
		super();
	}
	
	public DoubleExtra(String name) {
		super(name);
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Double getValue() {
		return value;
	}

}
