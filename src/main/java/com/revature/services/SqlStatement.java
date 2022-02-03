package com.revature.services;

public class SqlStatement {

    String tableName = "entity";
    String[] columns = {"id", "name", "some_num", "other_num"};



    //Create - INSERT
//    INSERT INTO Albums VALUES
//    ( DEFAULT, 'Ziltoid the Omniscient', '12' );

    String insert = "INSERT INTO " + tableName + "VALUES (?,?,?,?)";

    //Read - SELECT

    //Update - UPDATE

    //Delete - DELETE







}
