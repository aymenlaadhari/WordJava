/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dastex.javaword.utils;

import com.dastex.javaword.dao.DocDao;
import com.dastex.javaword.dao.DocDaoInterface;
import com.dastex.javaword.dao.model.Artikel;
import com.dastex.javaword.dao.model.Combination;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author aladhari
 */
public class TestDatabase {
    

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws SQLException {
        // TODO code application logic here
        
        //showArtikles();
       // showOneArtikel();
        //getFarben();
        //getPrises();
        showListCombination();
     
    
}
    
    private static void showArtikles()
    {
          DocDaoInterface daoInterface ;
        daoInterface = new DocDao();
        System.out.println("Searching....");
      daoInterface.getListArtikel().stream().forEach((listArtikel) -> {
          System.out.println(listArtikel.getNr());
        
    }); 
    }
    
    private static void showOneArtikel()
    {
      DocDaoInterface daoInterface;
      daoInterface = new DocDao();
        Artikel artikel = daoInterface.getArtikle("1701000");
        System.out.println(artikel.getNr()+"*"+artikel.getBezeichnung()+"*"+artikel.getFarben()+"*"+ artikel.getBisGroesse()+"*"+artikel.getCombinations().toString());
        
    }
    
    private static void showListCombination() throws SQLException
    {
        DocDao dao = new DocDao();
        List<Combination> combinations = dao.getCombinations("1701000");
        for (int i = 0; i < combinations.size(); i++) {
            System.out.println(combinations.get(i).getFarben()+"*"+combinations.get(i).getGroessen()+"*"+combinations.get(i).getMe()+"*"+combinations.get(i).getPmng()+"*"+combinations.get(i).getVk1()+"*"+combinations.get(i).getVpMng()+"*"+combinations.get(i).getWz()); 
       }
        
    }

    
}
