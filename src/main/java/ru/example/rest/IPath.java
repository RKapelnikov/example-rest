package ru.example.rest;

public interface IPath {

	String pathService = "service";

	String mthdTest = "test";
	String mthdCreateAccount = "createAccount";
	String mthdGetBalance = "getBalance";

	String pathCreateAccount = pathService + "/" + mthdCreateAccount;
	String pathGetBalance = pathService + "/" + mthdGetBalance;
	String pathTest = pathService + "/" + mthdTest;

}
