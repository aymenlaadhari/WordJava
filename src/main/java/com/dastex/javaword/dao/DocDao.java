/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dastex.javaword.dao;

import com.dastex.javaword.dao.model.Artikel;
import com.dastex.javaword.dao.model.Artikelzusatztext;
import com.dastex.javaword.dao.model.Kunden;
import java.sql.CallableStatement;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aladhari
 */
public class DocDao implements DocDaoInterface {

    private final String dburlProdukt = "jdbc:sqlanywhere:uid=dba;pwd=sql;eng=DBSRV5;database=Produkt5;links=tcpip(host = 10.152.1.203)";
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

        try (
                // Connect to Sybase Database
                Connection conProdukt = DriverManager.getConnection(dburlProdukt);
                Statement statementPro = conProdukt.createStatement(); ResultSet rs = statementPro.executeQuery("SELECT * FROM \"DBA\".\"Kunde\"");) {

            while (rs.next()) {
                kunden = new Kunden();
                kunden.setName1(rs.getString("Name_1"));
                kunden.setName2(rs.getString("Name_2"));
                kunden.setName3(rs.getString("Name_3"));
                kunden.setNr(rs.getNString("Nr"));
                kunden.setOrt(rs.getString("Ort"));
                kunden.setPlz(rs.getString("PLZ"));
                kunden.setStrasse(rs.getString("Strasse"));
                kunden.setLand(rs.getString("Land"));
                kundens.add(kunden);

            }
        } catch (SQLException ex) {
            Logger.getLogger(DocDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return kundens;
    }

    @Override
    public Kunden getKundenByCriteria() {

        try (
                // Connect to Sybase Database
                Connection conProdukt = DriverManager.getConnection(dburlProdukt);
                Statement statementPro = conProdukt.createStatement();
                ResultSet rs = statementPro.executeQuery("SELECT * FROM Kunde where Kunde.Nr = 100000");) {
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

    @Override
    public Artikel getArtikle() {
        Artikel artikel = null ;
        try (
                // Connect to Sybase Database
                Connection conProdukt = DriverManager.getConnection(dburlProdukt);
                Statement statementPro = conProdukt.createStatement();
                ResultSet rsArtikel = statementPro.executeQuery("SELECT * FROM Artikel, Artikelzusatztext WHERE Artikelzusatztext.At_ID = Artikel.ID AND Artikelzusatztext.Sprache = 'XAD' AND Artikel.Nr = '1701000'");) {
            System.out.println("getting Result");
            if (rsArtikel.next()) {
            artikel = new Artikel();
            artikel.setNr(rsArtikel.getString("Nr"));
//            artikel.setFarben(getFarben());
//            artikel.setBisGroesse(getGroessen());
           }
            else{
                System.out.println("result is empty");
            }

        } catch (SQLException ex) {
            Logger.getLogger(DocDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return artikel;
    }

    @Override
    public List<Artikel> getListArtikel() {
        Artikel artikel;

        List<Artikel> artikels = new ArrayList<>();

        try (
                // Connect to Sybase Database
                Connection conProdukt = DriverManager.getConnection(dburlProdukt);
                Statement statementPro = conProdukt.createStatement(); ResultSet rs = statementPro.executeQuery("select * from Artikel");) {
            System.out.println("getting data............");

            while (rs.next()) {

                artikel = new Artikel();
                artikel.setNr(rs.getString("Nr"));
                artikel.setId(rs.getString("ID"));
                artikels.add(artikel);
            }
            System.out.println("Data sucefully loaded");
        } catch (SQLException ex) {
            Logger.getLogger(DocDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return artikels;
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public String getFarben() throws SQLException {
        Connection conProdukt = DriverManager.getConnection(dburlProdukt);
        Statement statementPro = conProdukt.createStatement();
        System.out.println("getting Farben");
        ResultSet rsFarben = statementPro.executeQuery("SELECT hf_artikel_farben_2(Artikel.ID) FROM Artikel WHERE Artikel.Nr = '1701000'");
        return rsFarben.getString(1);
    }

    public String getGroessen() throws SQLException {
        Connection conProdukt = DriverManager.getConnection(dburlProdukt);
        Statement statementPro = conProdukt.createStatement();
        System.out.println("getting Groessen");
        ResultSet rsGroessen = statementPro.executeQuery("SELECT hf_artikel_groessen_2(Artikel.ID) FROM Artikel WHERE Artikel.Nr = '1701000'");
        return rsGroessen.getString("hf_artikel_groessen_2(Artikel.ID)");
    }
    
    public void getfarbenByStoredProcedure() throws SQLException
    {
        String proc = "{? call hf_artikel_farben_2(?)}";
        Connection conProdukt = DriverManager.getConnection(dburlProdukt);
        CallableStatement cs = conProdukt.prepareCall(proc);
        cs.registerOutParameter(1,Types.VARCHAR);
        cs.setString(2, "12");
        cs.execute();
        System.out.println(cs.getString(1));


    }

}
