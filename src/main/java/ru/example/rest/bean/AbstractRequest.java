package ru.example.rest.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractRequest implements Serializable {
	
	@XmlElement(name = "request-type")
	protected String requestType;

	@XmlElementRef(name = "extra")
	protected List<StringExtra> extras;
	
	public AbstractRequest() {
		this.extras = new ArrayList<>();
		this.extras.add(new StringExtra("login"));
		this.extras.add(new StringExtra("password"));
	}

	public void setLogin(String login) {
		StringExtra loginExtra = extras.get(0);
		loginExtra.setValue(login);
	}

	public void setPassword(String password) {
		StringExtra passwordExtra = extras.get(1);
		passwordExtra.setValue(password);
	}

	public String getRequestType() {
		return requestType;
	}

	public String getLogin() {
		StringExtra loginExtra = extras.get(0);
		return loginExtra.getValue();
	}

	public String getPassword() {
		StringExtra passwordExtra = extras.get(1);
		return passwordExtra.getValue();
	}

}
