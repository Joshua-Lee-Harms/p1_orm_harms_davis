package com.revature.test;

import com.revature.annotations.Column;
import com.revature.annotations.Table;


@Table(tableName = "merchandise")
public class Merchandise {

	@Column(name = "item_id")
	private int itemId;
	@Column(name = "item_name")
	private String itemName;
	@Column(name = "sell_price")
	private double sellPrice;
	@Column(name = "dept_name")
	private String department;
	
	public Merchandise(){}
	
	public int getItemId() {
		return itemId;
	}
	
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	
	public String getItemName() {
		return itemName;
	}
	
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public double getSellPrice() {
		return sellPrice;
	}
	
	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}
	
	public String getDepartment() {
		return department;
	}
	
	public void setDepartment(String department) {
		this.department = department;
	}
	
	@Override
	public String toString() {
		return "Entity{" +
				"id=" + itemId +
				", name='" + itemName + '\'' +
				", sellPrice=" + sellPrice +
				", department=" + department +
				'}';
	}
}
