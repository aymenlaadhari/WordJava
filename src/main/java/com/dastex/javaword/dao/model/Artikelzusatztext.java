/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dastex.javaword.dao.model;

import java.io.Serializable;


/**
 *
 * @author aladhari
 */
public class Artikelzusatztext implements Serializable {

    private static final long serialVersionUID = 1L;
  

    //"idNew"
    private Integer idNew;

    //"ID"
    private String id;

    //"At_ID"
    private String atID;

    //"Sprache"
    private String sprache;

    //"Land_id"
    private String landid;

    //"Text"
    private String text;

    public Artikelzusatztext() {
    }

    public Artikelzusatztext(Integer idNew) {
        this.idNew = idNew;
    }

    public Integer getIdNew() {
        return idNew;
    }

    public void setIdNew(Integer idNew) {
        this.idNew = idNew;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAtID() {
        return atID;
    }

    public void setAtID(String atID) {
        this.atID = atID;
    }

    public String getSprache() {
        return sprache;
    }

    public void setSprache(String sprache) {
        this.sprache = sprache;
    }

    public String getLandid() {
        return landid;
    }

    public void setLandid(String landid) {
        this.landid = landid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idNew != null ? idNew.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Artikelzusatztext)) {
            return false;
        }
        Artikelzusatztext other = (Artikelzusatztext) object;
        if ((this.idNew == null && other.idNew != null) || (this.idNew != null && !this.idNew.equals(other.idNew))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "main.model.Artikelzusatztext[ idNew=" + idNew + " ]";
    }

}
