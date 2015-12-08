package org.mohan.app.model;

import java.sql.Date;

import javax.persistence.Id;
import javax.persistence.Entity;

@Entity
public class Customer {
	@Id
	private String nid;
	private String name;
	private String address;
	private String email;
	private Date date;
	private long pnumber;
	
	public Customer() { }
	
	public Customer(String nid) {
		this.nid = nid;
	}
	public Customer(String nid,String name,String address,String email,Date date,long pnumber) {
		this.nid = nid;
		this.name = name;
		this.address = address;
		this.email = email;
		this.date = date;
		this.pnumber = pnumber;	
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getNid() {
		return nid;
	}
	public void setNid(String nid) {
		this.nid = nid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public long getPnumber() {
		return pnumber;
	}
	public void setPnumber(long pnumber) {
		this.pnumber = pnumber;
	}
	public String toString() {
		return "NID : "+nid+" Name : "+name+" Address : "+address+
				" Phone number : "+pnumber+" Email : "+email+" Date of Birth : "+date;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		try {
			Customer cus = (Customer) obj;
			if(cus == null) result = false;
			else {
				result = this.nid.equals(cus.getNid()) && this.name.equals(cus.getName());
				result = result && this.address.equals(cus.getAddress()) && this.email.equals(cus.getEmail());
				result = result && this.pnumber == cus.getPnumber() && this.date.toString().equals(cus.getDate().toString());
			}
		} catch(ClassCastException ex){
			result = false;
		}
		return result;
	}

}
