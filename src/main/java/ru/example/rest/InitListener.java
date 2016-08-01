package ru.example.rest;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import ru.example.rest.dao.Account;
import ru.example.rest.utils.HibernateUtils;

@WebListener
public class InitListener implements ServletContextListener {

	public static final Logger logger = Logger.getLogger(InitListener.class);

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		Configuration configuration = new Configuration().configure();
		configuration.addAnnotatedClass(Account.class);
		StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties()).build();
		SessionFactory sessionFactory = configuration.configure().buildSessionFactory(serviceRegistry);
		HibernateUtils.setSessionFactory(sessionFactory);
		logger.debug("Session factory inited");
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		HibernateUtils.getSessionFactory().close();
		logger.debug("Session factory closed");
	}

}
