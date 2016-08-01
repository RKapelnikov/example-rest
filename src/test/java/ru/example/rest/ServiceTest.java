package ru.example.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.util.Collections;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import ru.example.rest.bean.CreateAgentRequest;
import ru.example.rest.bean.CreateAgentResponse;
import ru.example.rest.bean.CreateAgentResponse.CreateAgentResult;
import ru.example.rest.bean.GetBalanceRequest;
import ru.example.rest.bean.GetBalanceResponse;
import ru.example.rest.bean.GetBalanceResponse.GetBalanceResult;
import ru.example.rest.utils.JAXBUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServiceTest {

	public static final Logger logger = Logger.getLogger(ServiceTest.class);

	private static final String propertiesFileName = "testing.properties";
	private static final String propertyUrls = "urls";

	private static final String servletUrl = "/rest";

	private static Client client;
	private static WebTarget[] targets;

	private static Random random;

	private static final String OK = "OK";

	@BeforeClass
	public static void setUp() throws Exception {
		Properties properties = new Properties();
		InputStream is = ClassLoader.getSystemResourceAsStream(propertiesFileName);
		if (is == null) {
			throw new RuntimeException("Missing testing properties file");
		}
		properties.load(is);
		String urls = properties.getProperty(propertyUrls);
		if (urls == null || urls.isEmpty()) {
			throw new RuntimeException("Missing 'urls' property in testing properties file");
		}
		String[] urlsSplitted = urls.split(",");
		targets = new WebTarget[urlsSplitted.length];
		client = ClientBuilder.newClient();
		IntStream.rangeClosed(0, urlsSplitted.length - 1)
				.forEach((int i) -> targets[i] = client.target(urlsSplitted[i] + servletUrl));
		random = new Random();
	}

	/**
	 * Тестовый GET-запрос.
	 */
	@Test
	public void test1Test() {
		Response response = getRandomTarget().path(IPath.pathTest).request().get();
		checkNotNullResponse(response);
		checkResponseStatus(200, response.getStatus());
		String entity = response.readEntity(String.class);
		checkNotNullEntity(entity);
		assertEquals("Unexpected test response:", OK, entity);
	}

	/**
	 * Создание аккаунта. Тест будет пройден только в том случае, если данного
	 * аккаунта не существовало ранееs.
	 */
	@Test
	public void test2CreateAgent() {
		CreateAgentRequest request = new CreateAgentRequest();
		request.setLogin("testUser1");
		request.setPassword("testPwd");
		Response response = getRandomTarget().path(IPath.pathCreateAccount).request().post(Entity.xml(request));
		checkNotNullResponse(response);
		checkResponseStatus(200, response.getStatus());
		CreateAgentResponse entity = response.readEntity(CreateAgentResponse.class);
		checkNotNullEntity(entity);
		logger.debug(JAXBUtils.getXmlStringFromBean(entity));
		checkResultCode(CreateAgentResult.OK.getCode(), entity.getResultCode());
	}

	/**
	 * Запрос на создание аккаунта, который уже существует.
	 */
	@Test
	public void test3CreateAgentExisting() {
		CreateAgentRequest request = new CreateAgentRequest();
		request.setLogin("testUser1");
		request.setPassword("testPwd");
		Response response = getRandomTarget().path(IPath.pathCreateAccount).request().post(Entity.xml(request));
		checkNotNullResponse(response);
		checkResponseStatus(200, response.getStatus());
		CreateAgentResponse entity = response.readEntity(CreateAgentResponse.class);
		checkNotNullEntity(entity);
		logger.debug(JAXBUtils.getXmlStringFromBean(entity));
		checkResultCode(CreateAgentResult.USER_EXISTS.getCode(), entity.getResultCode());
	}

	/**
	 * Запрос баланса.
	 */
	@Test
	public void test4GetBalance() {
		GetBalanceRequest request = new GetBalanceRequest();
		request.setLogin("testUser1");
		request.setPassword("testPwd");
		Response response = getRandomTarget().path(IPath.pathGetBalance).request().post(Entity.xml(request));
		checkNotNullResponse(response);
		checkResponseStatus(200, response.getStatus());
		GetBalanceResponse entity = response.readEntity(GetBalanceResponse.class);
		checkNotNullEntity(entity);
		logger.debug(JAXBUtils.getXmlStringFromBean(entity));
		checkResultCode(GetBalanceResult.OK.getCode(), entity.getResultCode());
	}

	/**
	 * Запрос баланса несуществующего пользователя.
	 */
	@Test
	public void test5GetBalanceUnknownUser() {
		GetBalanceRequest request = new GetBalanceRequest();
		request.setLogin("testUser2");
		request.setPassword("testPwd");
		Response response = getRandomTarget().path(IPath.pathGetBalance).request().post(Entity.xml(request));
		checkNotNullResponse(response);
		checkResponseStatus(200, response.getStatus());
		GetBalanceResponse entity = response.readEntity(GetBalanceResponse.class);
		checkNotNullEntity(entity);
		logger.debug(JAXBUtils.getXmlStringFromBean(entity));
		checkResultCode(GetBalanceResult.UNKNOWN_USER.getCode(), entity.getResultCode());
	}

	/**
	 * Запрос баланса пользователя с указанием неверного пароля.
	 */
	@Test
	public void test6GetBalanceWrongPassword() {
		GetBalanceRequest request = new GetBalanceRequest();
		request.setLogin("testUser1");
		request.setPassword("testPwd1");
		Response response = getRandomTarget().path(IPath.pathGetBalance).request().post(Entity.xml(request));
		checkNotNullResponse(response);
		checkResponseStatus(200, response.getStatus());
		GetBalanceResponse entity = response.readEntity(GetBalanceResponse.class);
		checkNotNullEntity(entity);
		logger.debug(JAXBUtils.getXmlStringFromBean(entity));
		checkResultCode(GetBalanceResult.WRONG_PASSWORD.getCode(), entity.getResultCode());
	}

	/**
	 * Многопоточный тест. 20 потоков одновременно отправляют запросы баланса
	 * (10 с корректным логином/паролем, 10 - с некорректным) на случайный
	 * сервис из набора, указанного в testing.properties
	 */
	@Test
	public void test7MultipleClientsMultipleWebapps() {
		ExecutorService executorService = Executors.newFixedThreadPool(20);
		try {
			executorService.invokeAll(Stream
					.concat(Collections.nCopies(10, callableGetBalance(OK)).stream(),
							Collections.nCopies(10, callableGetBalanceUnknownUser(OK)).stream())
					.collect(Collectors.toList()));
		} catch (InterruptedException e) {
			logger.error("Executor service was interrupted", e);
		} finally {
			executorService.shutdownNow();
		}
	}

	private WebTarget getRandomTarget() {
		int index = random.nextInt(targets.length);
		WebTarget target = targets[index];
		logger.debug(String.format("Thread %s target %s", Thread.currentThread().getName(), target.getUri()));
		return target;
	}

	private Callable<String> callableGetBalance(String result) {
		return () -> {
			IntStream.rangeClosed(1, 10).forEach((int i) -> {
				logger.debug(
						String.format("Thread %s (get balance) iteration %d", Thread.currentThread().getName(), i));
				test4GetBalance();
				logger.debug(String.format("Thread %s (get balance) OK", Thread.currentThread().getName()));
			});
			return result;
		};
	}

	private Callable<String> callableGetBalanceUnknownUser(String result) {
		return () -> {
			IntStream.rangeClosed(1, 10).forEach((int i) -> {
				logger.debug(String.format("Thread %s (get balance unknown user) iteration %d",
						Thread.currentThread().getName(), i));
				test5GetBalanceUnknownUser();
				logger.debug(
						String.format("Thread %s (get balance unknown user) OK", Thread.currentThread().getName()));
			});
			return result;
		};
	}

	private void checkNotNullResponse(Object response) {
		assertNotNull("Response is null", response);
	}

	private void checkNotNullEntity(Object entity) {
		assertNotNull("Response without entity was returned", entity);
	}

	private void checkResponseStatus(int expected, int actual) {
		if (actual == 404) {
			logger.error(String.format("Thread %s receives error 404, please check all running webapps",
					Thread.currentThread().getName()));
		}
		assertEquals("Unexpected response status:", expected, actual);
	}

	private void checkResultCode(int expected, int actual) {
		assertEquals("Unexpected result code:", expected, actual);
	}

	@AfterClass
	public static void tearDown() throws Exception {
		client.close();
	}

}
