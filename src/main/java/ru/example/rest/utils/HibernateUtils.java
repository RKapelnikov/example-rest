package ru.example.rest.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateUtils {

	private static SessionFactory sessionFactory;

	public static void setSessionFactory(SessionFactory sessionFactory) {
		HibernateUtils.sessionFactory = sessionFactory;
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void beginTransaction() {
		getCurrentSession().beginTransaction();
	}

	public static void commitTransaction() {
		getCurrentSession().getTransaction().commit();
	}

	public static void rollbackTransaction() {
		getCurrentSession().getTransaction().rollback();
	}

	public static Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
}
