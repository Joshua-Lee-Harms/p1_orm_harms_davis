package com.revature.models;

import com.revature.annotations.PrimaryKey;
import com.revature.repositories.Repository;


public class Entity2 extends Repository {

//    @PrimaryKey(primaryKey = "id")
    private int id;
    private int num;
    private String name;
    private double num2;

    public Entity2(int id, int num, String name, double num2) {
        this.id = id;
        this.num = num;
        this.name = name;
        this.num2 = num2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getNum2() {
        return num2;
    }

    public void setNum2(double num2) {
        this.num2 = num2;
    }
}
