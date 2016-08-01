package ru.example.rest.dao;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import ru.example.rest.utils.HibernateUtils;

@Entity
@Table(name = "account", schema = "service")
public class Account {

	private String login;
	private String password;
	private double balance;

	@Id
	@Column(name = "login", nullable = false, length = 32, unique = true)
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@Basic
	@Column(name = "password", nullable = false, length = 32)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Basic
	@Column(name = "balance", nullable = false, precision = 2)
	public Double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String save() throws Exception {
		return (String) HibernateUtils.getCurrentSession().save(this);
	}

	public static Account findByLogin(String login) throws Exception {
		return HibernateUtils.getCurrentSession().byId(Account.class).load(login);
	}

	public boolean checkPassword(String password) {
		return (this.password == null && password == null) || (this.password != null && this.password.equals(password));
	}

}
