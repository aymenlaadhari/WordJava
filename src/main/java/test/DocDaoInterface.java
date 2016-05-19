/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;


import java.util.List;
import javaapplicationtestjar.model.Artikel;
import javaapplicationtestjar.model.Kunden;

/**
 *
 * @author aladhari
 */
public interface DocDaoInterface {
    public List<Kunden> getListKunden();
    public Kunden getKundenByCriteria();
    public List<Artikel> getListArtikel();
    public Artikel getArtikle(String artNummer);
 
    
}
