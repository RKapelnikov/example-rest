package ru.example.rest.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractRequest implements Serializable {

	@XmlElement(name = "request-type")
	protected String requestType;

	@XmlElement(name = "extra")
	private List<Extra> properties;

	public AbstractRequest() {
		properties = new ArrayList<>();
	}

	public void setLogin(String login) {
		properties.add(new Extra(ExtraName.LOGIN.getName(), login));
	}

	public void setPassword(String password) {
		properties.add(new Extra(ExtraName.PASSWORD.getName(), password));
	}

	public String getRequestType() {
		return requestType;
	}

	public String getLogin() {
		return getValue(ExtraName.LOGIN.getName());
	}

	public String getPassword() {
		return getValue(ExtraName.PASSWORD.getName());
	}
	
	private String getValue(@NotNull String name) {
		Optional<Extra> optional = properties.stream().filter((Extra extra) -> name.equalsIgnoreCase(extra.key)).findFirst();
		return optional.isPresent() ? optional.get().value : null;
	}

}
