package ru.example.rest.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "request")
public class GetBalanceRequest extends AbstractRequest {

	public GetBalanceRequest() {
		super();
		this.requestType = RequestType.GET_BALANCE.getType();
	}
	
}
