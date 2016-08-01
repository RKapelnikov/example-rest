package ru.example.rest.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractResponse {

	@XmlElement(name = "result-code")
	private int resultCode;

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	
	public int getResultCode() {
		return resultCode;
	}
	
}
