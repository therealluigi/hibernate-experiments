package com.luigiiannone.hibernateexperiments;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;


@Entity
public class EntityTwo {


	@Id
	@GeneratedValue
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
