package ru.example.rest.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "request")
public class CreateAgentRequest extends AbstractRequest {

	public CreateAgentRequest() {
		super();
		this.requestType = RequestType.CREATE_USER.getType();
	}

}
