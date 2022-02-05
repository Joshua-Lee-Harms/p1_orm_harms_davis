package com.revature.util;

import com.revature.annotations.Column;
import com.revature.annotations.Table;


@Table(tableName = "test_model")
public class TestModel {
	
	@Column(name = "id", type = SqlDataType.INTEGER)
	private int id;
	@Column(name = "name", type = SqlDataType.VARCHAR)
	private String name;
	@Column(name = "some_num", type = SqlDataType.DOUBLE)
	private double some_num;
	@Column(name = "other_num", type = SqlDataType.INTEGER)
	private int other_num;
	
	public TestModel() {}
	
	public TestModel(int id, String name, double someNum, int otherNum) {
		this.id = id;
		this.name = name;
		this.some_num = someNum;
		this.other_num = otherNum;
	}
	public TestModel(Integer id, String name, Double someNum, Integer otherNum) {
		this.id = id;
		this.name = name;
		this.some_num = someNum;
		this.other_num = otherNum;
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
	}
	
	public void setSomeNum(double someNum) {
		this.some_num = someNum;
	}
	
	public int getOtherNum() {
		return other_num;
	}
	
	public void setOtherNum(int otherNum) {
		this.other_num = otherNum;
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
