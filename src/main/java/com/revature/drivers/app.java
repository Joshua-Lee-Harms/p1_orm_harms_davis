package com.revature.drivers;

import com.revature.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class app {

    public static void main(String[] args) {

        Connection conn = ConnectionFactory.getConnection();

        System.out.println(conn);




    }





}
