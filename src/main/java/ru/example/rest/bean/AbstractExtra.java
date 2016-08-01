package ru.example.rest.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
public abstract class AbstractExtra<T> implements Serializable {

	@XmlAttribute(name = "name")
	private String name;

	public AbstractExtra() {

	}
	
	public AbstractExtra(String name) {
		this.name = name;
	}
	
	public abstract void setValue(T value);
	
	public abstract T getValue();

}
