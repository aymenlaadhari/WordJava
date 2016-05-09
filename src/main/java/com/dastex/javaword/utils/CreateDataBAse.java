/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dastex.javaword.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;




/**
 *
 * @author aladhari
 */
public class CreateDataBAse {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/produkte?zeroDateTimeBehavior=convertToNull";
    static String dburlProdukt = "jdbc:sqlanywhere:uid=dba;pwd=sql;eng=DBSRV5;database=Produkt5;links=tcpip(host = 10.152.1.203)";
    //  Database credentials
    static final String USER = "root";
    static final String PASS = "aymen";
    Connection conProdukt;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        try (
                // Connect to Sybase Database
                Connection conProdukt = DriverManager.getConnection(dburlProdukt);
                Statement statementPro = conProdukt.createStatement();
                ) {
            getDatabaseMetaData(conProdukt, statementPro);

        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
    }

    private static void getDatabaseMetaData(Connection conn, Statement statementPro) {
        try {

            DatabaseMetaData dbmd = conn.getMetaData();
            statementPro = conn.createStatement();

            String[] types = {"TABLE"};
            ResultSet rs = dbmd.getTables(null, null, "%", types);
            while (rs.next()) {
                //System.out.println(rs.getString("TABLE_NAME"));
                ResultSet rs1 = statementPro.executeQuery("SELECT * FROM " + rs.getString("TABLE_NAME"));
                ResultSetMetaData rsmd = rs1.getMetaData();
                createTable(rs.getString("TABLE_NAME"), rsmd);
                
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    private static void createTable(String tableName, ResultSetMetaData rsmd) {
        Connection conn = null;
        Statement stmt = null;
        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to a selected database...");

            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected database successfully...");

            //STEP 4: Execute a query
            System.out.println("Creating table in given database...");
            stmt = conn.createStatement();
            String sqlCreate = "CREATE TABLE " + tableName + " (id INTEGER not NULL,PRIMARY KEY ( id ))";
            stmt.executeUpdate(sqlCreate);
            createTableColumns(rsmd,tableName, stmt,conn);
            System.out.println("Created table in given database...");
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    conn.close();
                }
            } catch (SQLException se) {
            }// do nothing
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
    }

    private static void createTableColumns(ResultSetMetaData rsmd, String tableName, Statement stmt, Connection conn) throws SQLException {
        int columnCount = rsmd.getColumnCount();

        // The column count starts from 1
        for (int i = 1; i <= columnCount; i++) {
            String name = rsmd.getColumnName(i);
            stmt = conn.createStatement();
            String sqlCreate = "ALTER TABLE "+tableName+" ADD "+name+"  varchar(200) NULL";
            stmt.executeUpdate(sqlCreate);
            System.out.println(name);
            // Do stuff with name
        }
    }

}
