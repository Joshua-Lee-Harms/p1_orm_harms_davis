package com.revature.test;

import com.revature.util.ReflectInfo;

public class testing2 {

    public static void main(String[] args) {

        Entity e = new Entity(2,"name",1.5,22);


//        System.out.println("Number of fields of " + e.getClass().getName() + " : " + ReflectInfo.getFieldLength(e));

        e.addItem(e);


    }

}
