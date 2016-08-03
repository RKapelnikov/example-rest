package ru.example.rest;

import javax.persistence.PersistenceException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import ru.example.rest.bean.CreateAgentRequest;
import ru.example.rest.bean.CreateAgentResponse;
import ru.example.rest.bean.CreateAgentResponse.CreateAgentResult;
import ru.example.rest.bean.GetBalanceRequest;
import ru.example.rest.bean.GetBalanceResponse;
import ru.example.rest.bean.GetBalanceResponse.GetBalanceResult;
import ru.example.rest.bean.RequestType;
import ru.example.rest.dao.Account;
import ru.example.rest.utils.HibernateUtils;
import ru.example.rest.utils.JAXBUtils;

@Path(IPath.pathService)
public class Service {

	public static final Logger logger = Logger.getLogger(Service.class);

	@POST
	@Path(IPath.mthdCreateAccount)
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public CreateAgentResponse createAgent(CreateAgentRequest request) {
		logger.debug(JAXBUtils.getXmlStringFromBean(request));

		CreateAgentResponse resp = new CreateAgentResponse();

		String requestType = request.getRequestType();
		if (!isRequestTypeCorrect(RequestType.CREATE_USER, requestType)) {
			logger.error("Invalid request type");
			resp.setResultCode(CreateAgentResult.ERROR.getCode());
			return resp;
		}

		String login = request.getLogin();
		String password = request.getPassword();

		if (login == null || login.isEmpty() || password == null || password.isEmpty()) {
			logger.error("Invalid credentials");
			resp.setResultCode(CreateAgentResult.ERROR.getCode());
			return resp;
		}

		boolean created = false;
		try {
			HibernateUtils.beginTransaction();
			try {
				Account existing = Account.findByLogin(login);
				if (existing != null) {
					resp.setResultCode(CreateAgentResult.USER_EXISTS.getCode());
					logger.debug(String.format("User %s already exists", login));
				} else {
					Account user = new Account();
					user.setLogin(login);
					user.setPassword(password);
					user.save();
					created = true;
				}
			} catch (Exception e) {
				resp.setResultCode(CreateAgentResult.ERROR.getCode());
				logger.error(String.format("Error creating user %s", login), e);
			} finally {
				if (created) {
					try {
						HibernateUtils.commitTransaction();
						logger.info(String.format("User %s created", login));
					} catch (PersistenceException e) {
						HibernateUtils.rollbackTransaction();
						Session session = HibernateUtils.getSessionFactory().openSession();
						Account existing = session.byId(Account.class).load(login);
						session.close();
						if (existing != null) {
							resp.setResultCode(CreateAgentResult.USER_EXISTS.getCode());
							logger.debug(String.format("User %s was created in another transaction", login));
						} else {
							resp.setResultCode(CreateAgentResult.ERROR.getCode());
							logger.error("Error with transaction", e);
						}
					}
				} else {
					HibernateUtils.rollbackTransaction();
				}
			}
		} catch (Exception e) {
			resp.setResultCode(CreateAgentResult.ERROR.getCode());
			logger.error("Error with transaction", e);
		}

		return resp;
	}

	@POST
	@Path(IPath.mthdGetBalance)
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public GetBalanceResponse getBalance(GetBalanceRequest request) {
		logger.debug(JAXBUtils.getXmlStringFromBean(request));

		GetBalanceResponse resp = new GetBalanceResponse();

		String requestType = request.getRequestType();
		if (!isRequestTypeCorrect(RequestType.GET_BALANCE, requestType)) {
			logger.error("Invalid request type");
			resp.setResultCode(CreateAgentResult.ERROR.getCode());
			return resp;
		}

		String login = request.getLogin();
		String password = request.getPassword();

		if (login == null || login.isEmpty() || password == null || password.isEmpty()) {
			logger.error("Invalid credentials");
			resp.setResultCode(CreateAgentResult.ERROR.getCode());
			return resp;
		}

		double balance = 0;

		try {
			HibernateUtils.beginTransaction();
			try {
				Account user = Account.findByLogin(login);
				if (user == null) {
					resp.setResultCode(GetBalanceResult.UNKNOWN_USER.getCode());
					logger.error(String.format("User %s does not exists", login));
				} else if (!user.checkPassword(password)) {
					resp.setResultCode(GetBalanceResult.WRONG_PASSWORD.getCode());
					logger.error(String.format("Wrong password for user %s", login));
				} else {
					balance = user.getBalance();
					resp.setBalance(balance);
				}
			} catch (Exception e) {
				resp.setResultCode(GetBalanceResult.ERROR.getCode());
				logger.error(String.format("Error getting balance of user %s", login), e);
			} finally {
				HibernateUtils.rollbackTransaction();
			}
		} catch (Exception e) {
			resp.setResultCode(CreateAgentResult.ERROR.getCode());
			logger.error("Error with transaction", e);
		}

		return resp;
	}

	@GET
	@Path(IPath.mthdTest)
	@Produces(MediaType.TEXT_PLAIN)
	public String get() {
		return "OK";
	}

	private boolean isRequestTypeCorrect(RequestType expected, String actual) {
		return expected.getType().equals(actual);
	}

}
