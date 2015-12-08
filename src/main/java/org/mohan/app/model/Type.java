package org.mohan.app.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Type {
	@Id 
	private String type;
	private double rate;
	private boolean active;
	
	public Type() { }
	
	public Type(String type) {
		this.type = type;
	}
	
	public Type(String type,double rate,boolean active) {
		this.type = type;
		this.rate = rate;
		this.active = active;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public double getRate() {
		return rate;
	}
	
	public void setRate(double rate) {
		this.rate = rate;
	}
	
	public String toString() {
		return "Type : "+type+" Rate : "+rate;
	}
}
