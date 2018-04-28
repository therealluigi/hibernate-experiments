package com.luigiiannone.proxying;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
class ProxyTarget {

	@Id
	@GeneratedValue
	private long id;

	@Basic
	@Column(length = 30)
	private String property;

	public String getProperty() {
		return property;
	}

	public void setProperty(String aProperty) {
		this.property = aProperty;
	}

	public long getId() {
		return id;
	}

}
