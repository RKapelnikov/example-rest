package ru.example.rest.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class GetBalanceResponse extends AbstractResponse {

	@XmlElement(name = "balance")
	protected double balance;
	
	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getBalance() {
		return balance;
	}

	public enum GetBalanceResult {

		OK(0), ERROR(2), UNKNOWN_USER(3), WRONG_PASSWORD(4);

		private int code;

		private GetBalanceResult(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}

	}

}
