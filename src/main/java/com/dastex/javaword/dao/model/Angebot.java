/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dastex.javaword.dao.model;

import java.util.List;

/**
 *
 * @author aladhari
 */
public class Angebot {

    private List<Artikel> artikels;
    private String id;
    private String internId;
    public List<Artikel> getArtikels() {
        return artikels;
    }

    public String getId() {
        return id;
    }


    public String getInternId() {
        return internId;
    }


    public void setArtikels(List<Artikel> artikels) {
        this.artikels = artikels;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setInternId(String internId) {
        this.internId = internId;
    }

}
