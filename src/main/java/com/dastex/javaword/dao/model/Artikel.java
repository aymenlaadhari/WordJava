/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dastex.javaword.dao.model;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author aladhari
 */
public class Artikel implements Serializable {

    private static final long serialVersionUID = 1L;
    private String art;
    private String artikelkurzbezeichnung;
    private String atId;
    private String auslieferjahrWoche;
    private String ausweichAtNr;
    private String bearbeitungsNr;
    private String bestellNr;
    private String bezeichnung;
    private String bisGroesse;
    private String datenblatt;
    private String durchgehendlieferbar;
    private String eKPreis;
    private String eKPreis2;
    private String eKPreis3;
    private String eKPreis4;
    private String eKPreis5;
    private String ean;
    private String eigeneStueckliste;
    private String eingabeNr;
    private String etiketten;
    private String etikettenID;
    private String etikettenNr;

    private String farben;
    private String firma;

    private String firmaID;
    private String firmakurzzeichen;
    private String getrenntePreisstaffel;
    private String gewicht;
    private String groessen;


    private String groessenstaffel;


    private String groessenstaffelID;
    private String groessenstaffelNr;
    private String hauptwarengruppe;
    private String id;
    private Integer idNew;


    private String kommision;
    private String kurzzeichen;
    private String laenderkennung;
    private String lieferant;
    private String lieferantID;
    private String lieferantenNr;


    private String liefertermin1;

    private String liefertermin2;

    private String liefertermin3;

    private String liefertermin4;


    private String liefertermin5;

    private String liefertermin6;

    private String liefertermin7;

    private String liefertermin8;
    private String lieferzeit;
    private List<Combination> combinations ;
    private String matchcode;


    private String mengeneinheit;

    private String mengeneinheitsstaffel;
    private String mengenstaffel;
    private String mengenstaffelID;
    private String mengenstaffelNr;
    private String modeID;
    private String modeNr;
    private String modetext;
    private String mwStID;
    private String nachbearbeiten;
    private String nettoEKPreis;
    private String nr;
    private String produktionsart;
    private String qualitaet;
    private String qualitaetID;
    private String qualitaetNr;

    private String qualitaetskontrolle;
    private String rabattEK;
    private String raumvolumen;
    private String saison;
    private String saisonID;
    private String saisonNr;
    private String sperren;
    private String verpackungseinheit;
    private String verpackungstext;
    private String vonGroesse;
    private String vorabNr;
    private String warengruppe;
    private String warengruppeID;
    private String warengruppeNr;
    private String zollNr;
    

    public Artikel() {
    }

    public Artikel(Integer idNew) {
        this.idNew = idNew;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Artikel)) {
            return false;
        }
        Artikel other = (Artikel) object;
        if ((this.idNew == null && other.idNew != null) || (this.idNew != null && !this.idNew.equals(other.idNew))) {
            return false;
        }
        return true;
    }
    public String getArt() {
        return art;
    }
    public String getArtikelkurzbezeichnung() {
        return artikelkurzbezeichnung;
    }
    public String getAtId() {
        return atId;
    }
    public String getAuslieferjahrWoche() {
        return auslieferjahrWoche;
    }
    public String getAusweichAtNr() {
        return ausweichAtNr;
    }
    public String getBearbeitungsNr() {
        return bearbeitungsNr;
    }
    public String getBestellNr() {
        return bestellNr;
    }

    
    public String getBezeichnung() {
        return bezeichnung;
    }
    public String getBisGroesse() {
        return bisGroesse;
    }
    public String getDatenblatt() {
        return datenblatt;
    }
    public String getDurchgehendlieferbar() {
        return durchgehendlieferbar;
    }
    public String getEKPreis() {
        return eKPreis;
    }
    public String getEKPreis2() {
        return eKPreis2;
    }
    public String getEKPreis3() {
        return eKPreis3;
    }
    public String getEKPreis4() {
        return eKPreis4;
    }
    public String getEKPreis5() {
        return eKPreis5;
    }
    public String getEan() {
        return ean;
    }
    public String getEigeneStueckliste() {
        return eigeneStueckliste;
    }
    public String getEingabeNr() {
        return eingabeNr;
    }
    public String getEtiketten() {
        return etiketten;
    }
    public String getEtikettenID() {
        return etikettenID;
    }
    public String getEtikettenNr() {
        return etikettenNr;
    }
    public String getFarben() {
        return farben;
    }

    public List<Combination> getCombinations() {
        return combinations;
    }

    public void setCombinations(List<Combination> combinations) {
        this.combinations = combinations;
    }

    
    
    public String getFirma() {
        return firma;
    }
    public String getFirmaID() {
        return firmaID;
    }
    public String getFirmakurzzeichen() {
        return firmakurzzeichen;
    }
    public String getGetrenntePreisstaffel() {
        return getrenntePreisstaffel;
    }
    public String getGewicht() {
        return gewicht;
    }
    public String getGroessen() {
        return groessen;
    }
    public String getGroessenstaffel() {
        return groessenstaffel;
    }
    public String getGroessenstaffelID() {
        return groessenstaffelID;
    }
    public String getGroessenstaffelNr() {
        return groessenstaffelNr;
    }
    public String getHauptwarengruppe() {
        return hauptwarengruppe;
    }
    public String getId() {
        return id;
    }
    public Integer getIdNew() {
        return idNew;
    }
    public String getKommision() {
        return kommision;
    }
    public String getKurzzeichen() {
        return kurzzeichen;
    }
    public String getLaenderkennung() {
        return laenderkennung;
    }

    public String getLieferant() {
        return lieferant;
    }


    public String getLieferantID() {
        return lieferantID;
    }
    public String getLieferantenNr() {
        return lieferantenNr;
    }
    public String getLiefertermin1() {
        return liefertermin1;
    }
    public String getLiefertermin2() {
        return liefertermin2;
    }
    public String getLiefertermin3() {
        return liefertermin3;
    }
    public String getLiefertermin4() {
        return liefertermin4;
    }
    public String getLiefertermin5() {
        return liefertermin5;
    }
    public String getLiefertermin6() {
        return liefertermin6;
        
    }

    public String getLiefertermin7() {
        return liefertermin7;
    }
    public String getLiefertermin8() {
        return liefertermin8;
    }
    public String getLieferzeit() {
        return lieferzeit;
    }

    public String getMatchcode() {
        return matchcode;
    }
    public String getMengeneinheit() {
        return mengeneinheit;
    }
    public String getMengeneinheitsstaffel() {
        return mengeneinheitsstaffel;
    }
    public String getMengenstaffel() {
        return mengenstaffel;
    }
    public String getMengenstaffelID() {
        return mengenstaffelID;
    }
    public String getMengenstaffelNr() {
        return mengenstaffelNr;
    }
    public String getModeID() {
        return modeID;
    }

    public String getModeNr() {
        return modeNr;
    }


    public String getModetext() {
        return modetext;
    }
    public String getMwStID() {
        return mwStID;
    }
    public String getNachbearbeiten() {
        return nachbearbeiten;
    }
    public String getNettoEKPreis() {
        return nettoEKPreis;
    }
    public String getNr() {
        return nr;
    }
    public String getProduktionsart() {
        return produktionsart;
    }
    public String getQualitaet() {
        return qualitaet;
    }
    public String getQualitaetID() {
        return qualitaetID;
    }
    public String getQualitaetNr() {
        return qualitaetNr;
    }
    public String getQualitaetskontrolle() {
        return qualitaetskontrolle;
    }
    public String getRabattEK() {
        return rabattEK;
    }
    public String getRaumvolumen() {
        return raumvolumen;
    }
    public String getSaison() {
        return saison;
    }
    public String getSaisonID() {
        return saisonID;
    }
    public String getSaisonNr() {
        return saisonNr;
    }
    public String getSperren() {
        return sperren;
    }
    public String getVerpackungseinheit() {
        return verpackungseinheit;
    }
    public String getVerpackungstext() {
        return verpackungstext;
    }
    public String getVonGroesse() {
        return vonGroesse;
    }
    public String getVorabNr() {
        return vorabNr;
    }
    public String getWarengruppe() {
        return warengruppe;
    }
    public String getWarengruppeID() {
        return warengruppeID;
    }
    public String getWarengruppeNr() {
        return warengruppeNr;
    }


    public String getZollNr() {
        return zollNr;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idNew != null ? idNew.hashCode() : 0);
        return hash;
    }
    public void setArt(String art) {
        this.art = art;
    }
    public void setArtikelkurzbezeichnung(String artikelkurzbezeichnung) {
        this.artikelkurzbezeichnung = artikelkurzbezeichnung;
    }
    public void setAtId(String atId) {
        this.atId = atId;
    }


    public void setAuslieferjahrWoche(String auslieferjahrWoche) {
        this.auslieferjahrWoche = auslieferjahrWoche;
    }


    public void setAusweichAtNr(String ausweichAtNr) {
        this.ausweichAtNr = ausweichAtNr;
    }
    public void setBearbeitungsNr(String bearbeitungsNr) {
        this.bearbeitungsNr = bearbeitungsNr;
    }
    public void setBestellNr(String bestellNr) {
        this.bestellNr = bestellNr;
    }
    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }
    public void setBisGroesse(String bisGroesse) {
        this.bisGroesse = bisGroesse;
    }
    public void setDatenblatt(String datenblatt) {
        this.datenblatt = datenblatt;
    }
    public void setDurchgehendlieferbar(String durchgehendlieferbar) {
        this.durchgehendlieferbar = durchgehendlieferbar;
    }

    public void setEKPreis(String eKPreis) {
        this.eKPreis = eKPreis;
    }
    public void setEKPreis2(String eKPreis2) {
        this.eKPreis2 = eKPreis2;
    }
    public void setEKPreis3(String eKPreis3) {
        this.eKPreis3 = eKPreis3;
    }
    public void setEKPreis4(String eKPreis4) {
        this.eKPreis4 = eKPreis4;
    }
    public void setEKPreis5(String eKPreis5) {
        this.eKPreis5 = eKPreis5;
    }


    public void setEan(String ean) {
        this.ean = ean;
    }
    public void setEigeneStueckliste(String eigeneStueckliste) {
        this.eigeneStueckliste = eigeneStueckliste;
    }

    public void setEingabeNr(String eingabeNr) {
        this.eingabeNr = eingabeNr;
    }
    public void setEtiketten(String etiketten) {
        this.etiketten = etiketten;
    }
    public void setEtikettenID(String etikettenID) {
        this.etikettenID = etikettenID;
    }
    public void setEtikettenNr(String etikettenNr) {
        this.etikettenNr = etikettenNr;
    }
    public void setFarben(String farben) {
        this.farben = farben;
    }
    public void setFirma(String firma) {
        this.firma = firma;
    }
    public void setFirmaID(String firmaID) {
        this.firmaID = firmaID;
    }
    public void setFirmakurzzeichen(String firmakurzzeichen) {
        this.firmakurzzeichen = firmakurzzeichen;
    }
    public void setGetrenntePreisstaffel(String getrenntePreisstaffel) {
        this.getrenntePreisstaffel = getrenntePreisstaffel;
    }
    public void setGewicht(String gewicht) {
        this.gewicht = gewicht;
    }
    public void setGroessen(String groessen) {
        this.groessen = groessen;
    }
    public void setGroessenstaffel(String groessenstaffel) {
        this.groessenstaffel = groessenstaffel;
    }
    public void setGroessenstaffelID(String groessenstaffelID) {
        this.groessenstaffelID = groessenstaffelID;
    }
    public void setGroessenstaffelNr(String groessenstaffelNr) {
        this.groessenstaffelNr = groessenstaffelNr;
    }
    public void setHauptwarengruppe(String hauptwarengruppe) {
        this.hauptwarengruppe = hauptwarengruppe;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setIdNew(Integer idNew) {
        this.idNew = idNew;
    }
    public void setKommision(String kommision) {
        this.kommision = kommision;
    }
    public void setKurzzeichen(String kurzzeichen) {
        this.kurzzeichen = kurzzeichen;
    }
    public void setLaenderkennung(String laenderkennung) {
        this.laenderkennung = laenderkennung;
    }
    public void setLieferant(String lieferant) {
        this.lieferant = lieferant;
    }
    public void setLieferantID(String lieferantID) {
        this.lieferantID = lieferantID;
    }
    public void setLieferantenNr(String lieferantenNr) {
        this.lieferantenNr = lieferantenNr;
    }

    public void setLiefertermin1(String liefertermin1) {
        this.liefertermin1 = liefertermin1;
    }


    public void setLiefertermin2(String liefertermin2) {
        this.liefertermin2 = liefertermin2;
    }


    public void setLiefertermin3(String liefertermin3) {
        this.liefertermin3 = liefertermin3;
    }


    public void setLiefertermin4(String liefertermin4) {
        this.liefertermin4 = liefertermin4;
    }


    public void setLiefertermin5(String liefertermin5) {
        this.liefertermin5 = liefertermin5;
    }


    public void setLiefertermin6(String liefertermin6) {
        this.liefertermin6 = liefertermin6;
    }


    public void setLiefertermin7(String liefertermin7) {
        this.liefertermin7 = liefertermin7;
    }


    public void setLiefertermin8(String liefertermin8) {
        this.liefertermin8 = liefertermin8;
    }
    public void setLieferzeit(String lieferzeit) {
        this.lieferzeit = lieferzeit;
    }
  
    public void setMatchcode(String matchcode) {
        this.matchcode = matchcode;
    }
    public void setMengeneinheit(String mengeneinheit) {
        this.mengeneinheit = mengeneinheit;
    }
    public void setMengeneinheitsstaffel(String mengeneinheitsstaffel) {
        this.mengeneinheitsstaffel = mengeneinheitsstaffel;
    }
    public void setMengenstaffel(String mengenstaffel) {
        this.mengenstaffel = mengenstaffel;
    }
    public void setMengenstaffelID(String mengenstaffelID) {
        this.mengenstaffelID = mengenstaffelID;
    }
    public void setMengenstaffelNr(String mengenstaffelNr) {
        this.mengenstaffelNr = mengenstaffelNr;
    }
    public void setModeID(String modeID) {
        this.modeID = modeID;
    }
    public void setModeNr(String modeNr) {
        this.modeNr = modeNr;
    }
    public void setModetext(String modetext) {
        this.modetext = modetext;
    }
    public void setMwStID(String mwStID) {
        this.mwStID = mwStID;
    }
    public void setNachbearbeiten(String nachbearbeiten) {
        this.nachbearbeiten = nachbearbeiten;
    }
    public void setNettoEKPreis(String nettoEKPreis) {
        this.nettoEKPreis = nettoEKPreis;
    }
    public void setNr(String nr) {
        this.nr = nr;
    }
    public void setProduktionsart(String produktionsart) {
        this.produktionsart = produktionsart;
    }
    public void setQualitaet(String qualitaet) {
        this.qualitaet = qualitaet;
    }
    public void setQualitaetID(String qualitaetID) {
        this.qualitaetID = qualitaetID;
    }
    public void setQualitaetNr(String qualitaetNr) {
        this.qualitaetNr = qualitaetNr;
    }

    public void setQualitaetskontrolle(String qualitaetskontrolle) {
        this.qualitaetskontrolle = qualitaetskontrolle;
    }
    public void setRabattEK(String rabattEK) {
        this.rabattEK = rabattEK;
    }
    public void setRaumvolumen(String raumvolumen) {
        this.raumvolumen = raumvolumen;
    }
    public void setSaison(String saison) {
        this.saison = saison;
    }
    public void setSaisonID(String saisonID) {
        this.saisonID = saisonID;
    }
    public void setSaisonNr(String saisonNr) {
        this.saisonNr = saisonNr;
    }
    public void setSperren(String sperren) {
        this.sperren = sperren;
    }
    public void setVerpackungseinheit(String verpackungseinheit) {
        this.verpackungseinheit = verpackungseinheit;
    }
    public void setVerpackungstext(String verpackungstext) {
        this.verpackungstext = verpackungstext;
    }
    public void setVonGroesse(String vonGroesse) {
        this.vonGroesse = vonGroesse;
    }
    public void setVorabNr(String vorabNr) {
        this.vorabNr = vorabNr;
    }
    public void setWarengruppe(String warengruppe) {
        this.warengruppe = warengruppe;
    }
    public void setWarengruppeID(String warengruppeID) {
        this.warengruppeID = warengruppeID;
    }
    public void setWarengruppeNr(String warengruppeNr) {
        this.warengruppeNr = warengruppeNr;
    }
    public void setZollNr(String zollNr) {
        this.zollNr = zollNr;
    }

}
