package com.luigiiannone.hibernateexperiments;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javassist.util.proxy.ProxyFactory;

public class TestProxying {

	private static Class<?> CLASS;

	static {
		try {
			CLASS = Class.forName("com.luigiiannone.proxying.ProxyTarget");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	private SessionFactory sessionFactory;

	@Before
	public void setupDB() {
		Properties prop = new Properties();
		prop.setProperty("hibernate.connection.url", "jdbc:h2:~/test");
		prop.setProperty("hibernate.connection.username", "sa");
		prop.setProperty("hibernate.connection.password", "");
		prop.setProperty("dialect", "org.hibernate.dialect.H2Dialect");
		prop.setProperty("hibernate.hbm2ddl.auto", "create-drop");

		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(prop).build();

		sessionFactory = new MetadataSources(registry).addAnnotatedClass(CLASS).buildMetadata().buildSessionFactory();
	}

	@Test
	public void testProxyingClassFromDifferentPackage() throws ClassNotFoundException, NoSuchMethodException,
			IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		Session session = sessionFactory.openSession();

		ProxyFactory proxyFactory = new ProxyFactory();
		proxyFactory.setSuperclass(CLASS);
		Object proxy = proxyFactory.create(new Class[] {}, new Object[] {});
		proxy.getClass().getMethod("setProperty", new Class[] { String.class }).invoke(proxy, "BOB");

		session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(CLASS.getName(), proxy);
		session.getTransaction().commit();
		session.flush();
		session.close();
		
		session = sessionFactory.openSession();
		session.beginTransaction();
		Object retrieved = session.get(CLASS.getName(), (Long) proxy.getClass().getMethod("getId").invoke(proxy));
		session.getTransaction().commit();
		session.close();
		
		Method method = retrieved.getClass().getMethod("getProperty");
		method.setAccessible(true);
		Assert.assertEquals("BOB", method.invoke(retrieved));		
	}
}
