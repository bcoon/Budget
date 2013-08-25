package com.example.budget;

import java.util.Date;

//a purchase to be added to a category's list of purchases that fall under that category
/**
 * A purchase to be added to a category's list of purchases that fall under that category.
 * Class is currently not used.  Meant for later addition after I take up more
 * rewarding projects
 * 
 * @author Brian
 */
public class Transaction {
	private String title;
	private Date date;
	private double amount;
	private String notes;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
}
