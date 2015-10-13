package org.mohan.app.model;

public class Due {
	private String nid;
	private Double amount;
	public String getNid() {
		return nid;
	}
	public void setNid(String nid) {
		this.nid = nid;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String toString() {
		return "NID : "+nid+" Amount : "+amount;
	}
}