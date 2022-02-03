package com.revature.test;

import com.revature.annotations.Column;
import com.revature.annotations.Table;
import com.revature.repositories.Repository;

@Table(tableName = "entity")
public class Entity extends Repository {

	@Column(name = "id")
	private int id;
	@Column(name = "name")
	private String name;
	@Column(name = "some_num")
	private double someNum;
	@Column(name = "other_num")
	private int otherNum;
	
	public Entity() {
	}
	
	public Entity(int id, String name, double someNum, int otherNum) {
		this.id = id;
		this.name = name;
		this.someNum = someNum;
		this.otherNum = otherNum;
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
		return someNum;
	}
	
	public void setSomeNum(double someNum) {
		this.someNum = someNum;
	}
	
	public int getOtherNum() {
		return otherNum;
	}
	
	public void setOtherNum(int otherNum) {
		this.otherNum = otherNum;
	}
	
	@Override
	public String toString() {
		return "Entity{" +
				"id=" + id +
				", name='" + name + '\'' +
				", someNum=" + someNum +
				", otherNum=" + otherNum +
				'}';
	}
}
