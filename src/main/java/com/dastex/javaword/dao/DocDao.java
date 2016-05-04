/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dastex.javaword.dao;


import com.dastex.javaword.dao.model.Kunden;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author aladhari
 */
public class DocDao implements DocDaoInterface{
    
    
    String dburlProdukt = "jdbc:sqlanywhere:uid=dba;pwd=sql;eng=DBSRV5;database=Produkt5;links=tcpip(host = 10.152.1.203)";
    Kunden kunden;
   

    @Override
    public List<Kunden> getListKunden() {
        Kunden kunden;
     // uid - user id
        // pwd - password
        // eng - Sybase database server name
        // database - sybase database name
        // host - database host machine ip
          
          List<Kunden> kundens = new ArrayList<>();
        
        try 
            ( 
               // Connect to Sybase Database
                Connection conProdukt = DriverManager.getConnection(dburlProdukt); 
              
                Statement statementPro = conProdukt.createStatement(); ResultSet rs = statementPro.executeQuery("SELECT * FROM \"DBA\".\"Kunde\"");) 
        {
            
            while (rs.next()) {
                kunden = new Kunden();
                kunden.setName1(rs.getString("Name_1"));
                kunden.setName2(rs.getString("Name_2"));
                kunden.setName3(rs.getString("Name_3"));
                kunden.setNr(rs.getNString("Nr"));
                kunden.setOrt(rs.getString("Ort"));
                kunden.setPlz(rs.getString("PLZ"));
                kunden.setStrasse(rs.getString("Strasse"));
                kundens.add(kunden);   
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(DocDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return kundens;
    }

    @Override
    public Kunden getKundenByCriteria() {
        
      
        try
            
            ( 
               // Connect to Sybase Database
                Connection conProdukt = DriverManager.getConnection(dburlProdukt); 
              
                Statement statementPro = conProdukt.createStatement();
                
                
                ResultSet rs = statementPro.executeQuery("SELECT * FROM Kunde where Kunde.Nr = 100000");
                
                
                ) 
            
           
            
        {
            kunden = new Kunden();
                kunden.setName1(rs.getString("Name_1"));
                kunden.setName2(rs.getString("Name_2"));
                kunden.setName3(rs.getString("Name_3"));
                kunden.setNr(rs.getNString("Nr"));
                kunden.setOrt(rs.getString("Ort"));
                kunden.setPlz(rs.getString("PLZ"));
                kunden.setStrasse(rs.getString("Strasse"));
                
                
            
        } catch (Exception e) {
        }
     
        return kunden;
    }
    
    
    
   
    
}
