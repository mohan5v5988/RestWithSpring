package org.mohan.app.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Payments {
	@Id
	@GeneratedValue
	private int id;
	@OneToOne
	private Customer customer;
	private Double amount;
	private Date date;
	
	public Payments() { }
	
	public Payments(Customer customer, Double amount, Date date) {
		super();
		this.customer = customer;
		this.amount = amount;
		this.date = date;
	}
	@Override
	public String toString() {
		return "Payments [id=" + id + ", customer=" + customer.getNid() + ", amount=" + amount + ", date=" + date + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
		dbAmount();
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	// to store the negative in db.
	public Double dbAmount() {
		return this.amount*-1;
	}
	// convert from negative to p.
	public void changeToPAmount() {
		Double a = this.amount;
		this.amount = Math.abs(a);
	}
}
