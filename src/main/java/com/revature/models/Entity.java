package com.revature.models;

import com.revature.annotations.Column;
import com.revature.annotations.Table;
import com.revature.repositories.Repository;
import com.revature.util.SqlDataType;


@Table(tableName = "entity")
public class Entity extends Repository {
	
	@Column(name = "id", type = SqlDataType.INTEGER)
	private int id;
	@Column(name = "name", type = SqlDataType.VARCHAR)
	private String name;
	@Column(name = "some_num", type = SqlDataType.DOUBLE)
	private double some_num;
	//private double someNum;
	@Column(name = "other_num", type = SqlDataType.INTEGER)
	private int other_num;
	//private int otherNum;
	
	public Entity() {}
	
	public Entity(int id, String name, double someNum, int otherNum) {
		this.id = id;
		this.name = name;
		this.some_num = someNum;
		this.other_num = otherNum;
		//this.someNum = someNum;
		//this.otherNum = otherNum;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getSomeNum() {
		return some_num;
		//return someNum;
	}
	
	public void setSomeNum(double someNum) {
		this.some_num = someNum;
		//this.someNum = someNum;
	}
	
	public int getOtherNum() {
		return other_num;
		//return otherNum;
	}
	
	public void setOtherNum(int otherNum) {
		this.other_num = otherNum;
		//this.otherNum = otherNum;
	}
	
	@Override
	public String toString() {
		return "Entity{" +
				"id=" + id +
				", name='" + name + '\'' +
				", someNum=" + some_num+//someNum +
				", otherNum=" + other_num+//otherNum +
				'}';
	}
}
