package com.luigiiannone.hibernateexperiments;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;

import java.util.Properties;

import org.hamcrest.Matchers;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.Before;
import org.junit.Test;

public class TestMergeBack {

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

		sessionFactory = new MetadataSources(registry).addAnnotatedClass(EntityOne.class)
				.addAnnotatedClass(EntityTwo.class).buildMetadata().buildSessionFactory();
	}

	@Test
	public void test() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		EntityOne entityOne = new EntityOne();
		EntityTwo entityTwo = new EntityTwo();
		entityTwo.getEntityOnes().add(entityOne);
		entityOne.getEntityTwos().add(entityTwo);
		session.save(entityTwo);
		session.save(entityOne);
		session.getTransaction().commit();
		session.close();
	
		
		
		
		session = sessionFactory.openSession();
		session.beginTransaction();
		entityOne = session.get(EntityOne.class, entityOne.getId());
		entityTwo = session.get(EntityTwo.class, entityTwo.getId());
		assertThat(entityOne.getEntityTwos(), contains(hasProperty("id", Matchers.is(entityTwo.getId()))));
		assertThat(entityTwo.getEntityOnes(), contains(hasProperty("id", Matchers.is(entityOne.getId()))));
		session.getTransaction().commit();
		session.close();
	}
}
