/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dastex.javaword.dao;

import com.dastex.javaword.dao.model.Artikel;
import com.dastex.javaword.dao.model.Combination;
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
    public Artikel getArtikle(String artNummer) {
        Artikel artikel = null;
        try (
                // Connect to Sybase Database
                Connection conProdukt = DriverManager.getConnection(dburlProdukt);
                Statement statementPro = conProdukt.createStatement();
                ResultSet rsArtikel = statementPro.executeQuery("SELECT * FROM Artikel, Artikelzusatztext WHERE Artikelzusatztext.At_ID = Artikel.ID AND Artikelzusatztext.Sprache = 'XAD' AND Artikel.Nr = '" + artNummer + "'");) {

            if (rsArtikel.next()) {
                artikel = new Artikel();
                artikel.setNr(rsArtikel.getString("Nr"));
                artikel.setBezeichnung(rsArtikel.getString("Bezeichnung"));
                artikel.setText(rsArtikel.getString("Text"));
                artikel.setFarben(getFarben(artNummer));
                artikel.setGroessen(getGroessen(artNummer));
                artikel.setCombinations(getCombinations(artNummer));
            } else {
                artikel = new Artikel();
                artikel.setCombinations(new ArrayList<>());

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

            while (rs.next()) {

                artikel = new Artikel();
                artikel.setNr(rs.getString("Nr"));
                artikel.setId(rs.getString("ID"));
                artikel.setBezeichnung(rs.getString("Bezeichnung"));

                artikels.add(artikel);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DocDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return artikels;
    }

    private String getFarben(String artNummer) throws SQLException {
        String proc = "SELECT hf_artikel_farben_2(Artikel.ID) FROM Artikel WHERE Artikel.Nr = '" + artNummer + "'";
        String ret = null;
        Connection conProdukt = DriverManager.getConnection(dburlProdukt);
        Statement s = conProdukt.createStatement();

        ResultSet rs = s.executeQuery(proc);
        while (rs.next()) {
            ret = rs.getString(1);

        }

        return ret;
    }

    private String getGroessen(String artNummer) throws SQLException {
        String ret = null;
        String sql = "SELECT hf_artikel_groessen_2(Artikel.ID) FROM Artikel WHERE Artikel.Nr = '" + artNummer + "'";
        Connection conProdukt = DriverManager.getConnection(dburlProdukt);
        Statement statementPro = conProdukt.createStatement();

        ResultSet rsGroessen = statementPro.executeQuery(sql);
        while (rsGroessen.next()) {

            ret = rsGroessen.getString(1);

        }

        return ret;
    }

    public List<Combination> getCombinations(String artNummer) throws SQLException {
        List<Combination> combinations = new ArrayList<>();
        String sql = "SELECT hf_artikel_farben_2_gleicher_Preis( Preisstaffel.At_ID, Preisstaffel.VK_1, Preisstaffel.Preismenge, 1 ) AS 'Farben', hf_artikel_groessen_2_gleicher_Preis( Preisstaffel.At_ID, Preisstaffel.VK_1, Preisstaffel.Preismenge, 1 ) AS 'Groessen', Preisstaffel.VK_1 AS 'VK1', Preisstaffel.Waehrungszeichen AS 'WZ', Preisstaffel.Preismenge AS 'P_Mng', Preisstaffel.Mengeneinheit AS 'ME', Preisstaffel.Verpackungsmenge AS 'VP_Mng' FROM Preisstaffel, Groessenpreisstaffel, Artikel, Groessenstaffel WHERE Preisstaffel.Groessen_ID = Groessenpreisstaffel.ID AND Preisstaffel.At_ID = Groessenpreisstaffel.At_ID AND Preisstaffel.At_ID = Artikel.ID AND Artikel.Groessenstaffel_ID = Groessenstaffel.ID AND Preisstaffel.Nr = '02' AND Artikel.Nr = '" + artNummer + "' AND Groessenpreisstaffel.Groesse <> '<?>' GROUP BY Farben, Groessen, Preisstaffel.VK_1, Preisstaffel.Waehrungszeichen, Preisstaffel.Preismenge, Preisstaffel.Mengeneinheit, Preisstaffel.Verpackungsmenge ORDER BY 3";
        Connection conProdukt = DriverManager.getConnection(dburlProdukt);
        Statement statementPro = conProdukt.createStatement();

        ResultSet rsPrises = statementPro.executeQuery(sql);

        while (rsPrises.next()) {
            Combination ret = new Combination();

            ret.setFarben(rsPrises.getString("Farben"));
            ret.setGroessen(rsPrises.getString("Groessen"));
            ret.setVk1(rsPrises.getString("VK1"));
            ret.setWz(rsPrises.getString("WZ"));
            ret.setPmng(rsPrises.getString("P_Mng"));
            ret.setMe(rsPrises.getString("ME"));
            ret.setVpMng(rsPrises.getString("VP_Mng"));
            combinations.add(ret);

        }
        return combinations;
    }

    public List<Combination> getListCombProc(String kundNum, String artNum) throws SQLException {
        List<Combination> combinations = new ArrayList<>();
        String procName = "{call hf_artikel_alle_preise(?, ?)}";
        Connection conProdukt = DriverManager.getConnection(dburlProdukt);

        CallableStatement cs = conProdukt.prepareCall(procName);
        cs.setString(1, kundNum);				// (1)
        cs.setString(2, artNum);
        ResultSet rs = cs.executeQuery();
        while (rs.next()) {
            Combination ret = new Combination();

            ret.setFarben(rs.getString("Farben"));
            ret.setGroessen(rs.getString("Groessen"));
            ret.setArt(rs.getString("Art"));
            ret.setAb(rs.getString("ab"));
            ret.setPreis(rs.getString("Preis"));
            ret.setWz(rs.getString("WZ"));
            ret.setPmng(rs.getString("P_Mng"));
            ret.setMe(rs.getString("ME"));
            ret.setVpMng(rs.getString("VP_Mng"));
            combinations.add(ret);

        }
        return combinations;
    }

}
