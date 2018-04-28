package com.luigiiannone.hibernateexperiments;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class EntityOne {

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
