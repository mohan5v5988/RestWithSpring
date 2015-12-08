package org.mohan.app.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Transactions {
	@Id
	@GeneratedValue
	private int tid;
	@OneToOne
	private Customer customer;
	@OneToOne
	private Type type;
	private Date date;
	private Calculation calculation = null;

	public Transactions(){ }
	
	public Transactions(Customer customer, Type type, Date date, Calculation calculation) {
		super();
		this.customer = customer;
		this.type = type;
		this.date = date;
		this.calculation = calculation;
	}

	public Calculation getCalculation() {
		return calculation;
	}

	public void setCalculation(Calculation calculation) {
		this.calculation = calculation;
	}

	public int getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Tranasactions [tid=" + tid + ", customer=" + customer.getNid() + ", type=" + type.getType() + ", date=" + date +
				", c=" + "]" + "\n" + calculation.toString();
	}
}