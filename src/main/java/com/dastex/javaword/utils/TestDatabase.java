/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dastex.javaword.utils;

import com.dastex.javaword.dao.DocDao;
import com.dastex.javaword.dao.DocDaoInterface;
import com.dastex.javaword.dao.model.Artikel;
import com.dastex.javaword.dao.model.Kunden;
import java.sql.SQLException;

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
        //showOneArtikel();
        getFarben();
     
    
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
        Artikel artikel = daoInterface.getArtikle();
        System.out.println(artikel.getFarben()+artikel.getBisGroesse()+artikel.getBezeichnung());
        
    }
    
    private static void getFarben() throws SQLException
    {
        DocDao dao = new DocDao();
        System.out.println(dao.getFarben()); 
   }
    
}
