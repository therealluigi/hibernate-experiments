package com.luigiiannone.hibernateexperiments;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.Test;

public class TestSchemaGeneration {
	
	@Entity(name = "EntityTwo")
	class EntityTwo {

		@Id
		private long id;
		
		@ManyToMany
		@JoinTable(
				name = "EntityOne_EntityTwo",
				joinColumns = @JoinColumn(name = "entityTwo_id"),
				inverseJoinColumns = @JoinColumn(name = "entityOne_id")
				)
		private final Set<EntityOne> entityOnes = new HashSet<EntityOne>();

		public Set<EntityOne> getEntityOnes() {
			return entityOnes;
		}

		public long getId() {
			return id;
		}

		@Override
		public String toString() {
			return "EntityTwo: "+id;
		}
	}
		
	@Entity(name = "EntityOne")
	class EntityOne {

		@Id
		@GeneratedValue
		private long id;
		
		@ManyToMany(mappedBy = "entityOnes")
		private final Set<EntityTwo> entityTwos = new HashSet<EntityTwo>();

		public Set<EntityTwo> getEntityTwos() {
			return entityTwos;
		}

		public long getId() {
			return id;
		}
		
		@Override
		public String toString() {
			return "EntityOne: "+id;
		}
	}

	@Test
	public void testManyToMany() {
		Properties prop = new Properties();
		prop.setProperty("hibernate.connection.url", "jdbc:h2:~/test");
		prop.setProperty("hibernate.connection.username", "sa");
		prop.setProperty("hibernate.connection.password", "");
		prop.setProperty("dialect", "org.hibernate.dialect.H2Dialect");
		prop.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		prop.setProperty("hibernate.show_sql", "true");

		StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.applySettings(prop)
				.build();

		new MetadataSources(registry)
		 .addAnnotatedClass(EntityOne.class)
		 .addAnnotatedClass(EntityTwo.class)
		 .buildMetadata()
		 .buildSessionFactory();
	}
}
