package ru.example.rest.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class CreateAgentResponse extends AbstractResponse {

	public enum CreateAgentResult {

		OK(0), USER_EXISTS(1), ERROR(2);

		private int code;

		private CreateAgentResult(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}

	}

}
