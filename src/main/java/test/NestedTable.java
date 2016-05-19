/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author aladhari
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javaapplicationtestjar.model.Position;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import org.docx4j.XmlUtils;
import org.docx4j.model.datastorage.migration.VariablePrepare;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.CTTblPrBase.TblStyle;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.TblPr;
import org.docx4j.wml.TblWidth;
import org.docx4j.wml.Tc;
import org.docx4j.wml.TcPr;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;
import org.docx4j.wml.TrPr;

public class NestedTable {
    // Johnson Kooroth
    // The innerTable can be XML or
    // you can create using 
    // Tbl tblCredProg = factory.createTbl();
    // as shown below

    static WordprocessingMLPackage wordMLPackage = null;
    static ObjectFactory factory = new ObjectFactory();
    private static  WordprocessingMLPackage template;
    static String tblXML = "<w:tbl xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" "
            + "xmlns:v=\"urn:schemas-microsoft-com:vml\" "
            + "xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
            + "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\">"
            + "<w:tblPr>"
            + "<w:tblStyle w:val=\"TableGrid\" />"
            + "<w:tblW w:w=\"0\" w:type=\"auto\" />"
            + "<w:tblLook w:val=\"04A0\" />"
            + "</w:tblPr>"
            + "<w:tblGrid>"
            + "<w:gridCol w:w=\"392\" />"
            + "<w:gridCol w:w=\"328\" />"
            + "</w:tblGrid>"
            + "<w:tr w:rsidR=\"005C211D\" w:rsidTr=\"005C211D\">"
            + "<w:tc>"
            + "<w:tcPr>"
            + "<w:tcW w:w=\"392\" w:type=\"dxa\" />"
            + "</w:tcPr>"
            + "<w:p w:rsidR=\"005C211D\" w:rsidRDefault=\"005C211D\" w:rsidP=\"005C211D\">"
            + "<w:r>"
            + "<w:t>1</w:t>"
            + "</w:r>"
            + "</w:p>"
            + "</w:tc>"
            + "<w:tc>"
            + "<w:tcPr>"
            + "<w:tcW w:w=\"328\" w:type=\"dxa\" />"
            + "</w:tcPr>"
            + "<w:p w:rsidR=\"005C211D\" w:rsidRDefault=\"005C211D\" w:rsidP=\"005C211D\">"
            + "<w:r>"
            + "<w:t>2</w:t>"
            + "</w:r>"
            + "</w:p>"
            + "</w:tc>"
            + "</w:tr>"
            + "<w:tr w:rsidR=\"005C211D\" w:rsidTr=\"005C211D\">"
            + "<w:trPr>"
            + "<w:trHeight w:val=\"70\" />"
            + "</w:trPr>"
            + "<w:tc>"
            + "<w:tcPr>"
            + "<w:tcW w:w=\"392\" w:type=\"dxa\" />"
            + "</w:tcPr>"
            + "<w:p w:rsidR=\"005C211D\" w:rsidRDefault=\"005C211D\" w:rsidP=\"005C211D\">"
            + "<w:r>"
            + "<w:t>3</w:t>"
            + "</w:r>"
            + "</w:p>"
            + "</w:tc>"
            + "<w:tc>"
            + "<w:tcPr>"
            + "<w:tcW w:w=\"328\" w:type=\"dxa\" />"
            + "</w:tcPr>"
            + "<w:p w:rsidR=\"005C211D\" w:rsidRDefault=\"005C211D\" w:rsidP=\"005C211D\">"
            + "<w:r>"
            + "<w:t>4</w:t>"
            + "</w:r>"
            + "</w:p>"
            + "</w:tc>"
            + "</w:tr>"
            + "</w:tbl>";

    protected static void addTc(Tr tr, String text) {
        Tc tc = factory.createTc();
        TcPr tcPr = new TcPr();
        TblWidth width = new TblWidth();
        width.setType("dxa");
        width.setW(new BigInteger("3192"));
        tcPr.setTcW(width);
        tc.setTcPr(tcPr);
        tc.getEGBlockLevelElts().add(template.getMainDocumentPart().createParagraphOfText(text));
        tr.getEGContentCellContent().add(tc);
    }

    private static WordprocessingMLPackage getTemplate(String name) throws Docx4JException, FileNotFoundException, org.docx4j.openpackaging.exceptions.Docx4JException {
        template = WordprocessingMLPackage.load(new FileInputStream(new File(name)));
        return template;
    }

    private static Tbl getTemplateTable(List<Object> tables, String templateKey) throws Docx4JException, JAXBException {
        for (Object tbl : tables) {
            List<?> textElements = getAllElementFromObject(tbl, Text.class);
            for (Object text : textElements) {
                Text textElement = (Text) text;
                if (textElement.getValue() != null && textElement.getValue().equals(templateKey)) {
                    return (Tbl) tbl;
                }
            }
        }
        return null;
    }

    private static List<Object> getAllElementFromObject(Object obj, Class<?> toSearch) {
        List<Object> result = new ArrayList<>();
        if (obj instanceof JAXBElement) {
            obj = ((JAXBElement<?>) obj).getValue();
        }

        if (obj.getClass().equals(toSearch)) {
            result.add(obj);
        } else if (obj instanceof ContentAccessor) {
            List<?> children = ((ContentAccessor) obj).getContent();
            children.stream().forEach((child) -> {
                result.addAll(getAllElementFromObject(child, toSearch));
            });
        }
        return result;
    }

    private static void addRowToTable(Tbl reviewtable, Tr templateRow, HashMap<String, String> replacements) {
        Tr workingRow = (Tr) XmlUtils.deepCopy(templateRow);
        List textElements = getAllElementFromObject(workingRow, Text.class);
        for (Object object : textElements) {
            Text text = (Text) object;
            String replacementValue = (String) replacements.get(text.getValue());
            if (replacementValue != null) {
                text.setValue(replacementValue);
            }
        }

        reviewtable.getContent().add(workingRow);
    }

    private static void addRowToTableFirst(Tbl reviewtable, Tr templateRow) {
        Tr workingRow = (Tr) XmlUtils.deepCopy(templateRow);
        reviewtable.getContent().add(workingRow);
    }

    private static void replaceTable(String[] placeholders, List<HashMap<String, String>> textToAdd,
            WordprocessingMLPackage template) throws Docx4JException, JAXBException {
        List<Object> tables = getAllElementFromObject(template.getMainDocumentPart(), Tbl.class);

        // 1. find the table
        Tbl tempTable = getTemplateTable(tables, placeholders[0]);

        List<Object> rows = getAllElementFromObject(tempTable, Tr.class);

        // first row is header, second row is content
        if (rows.size() == 2) {
            // this is our template row
            Tr templateRow = (Tr) rows.get(1);

            for (HashMap<String, String> replacements : textToAdd) {
                // 2 and 3 are done in this method
                addRowToTable(tempTable, templateRow, replacements);
            }

            // 4. remove the template row
            tempTable.getContent().remove(templateRow);
        }
    }
    
    private static void replaceSecondTable(List<HashMap<String, String>> textToAddTable,
            Tr template, int i) throws Docx4JException, JAXBException
    {
          List<Object> tables = getAllElementFromObject(template, Tbl.class);

        // 1. find the table
        Tbl tempTable = (Tbl) tables.get(0);

        List<Object> rows = getAllElementFromObject(tempTable, Tr.class);

        // first row is header, second row is content
        if (rows.size() == 3) {
            // this is our template row
     
                // 2 and 3 are done in this method

            
            Tr templateRow = (Tr) rows.get(2);
            for (HashMap<String, String> replacements : textToAddTable) {
                // 2 and 3 are done in this method
                
                addRowToTable(tempTable, templateRow, replacements);
              
            }

            // 4. remove the template row
            tempTable.getContent().remove(templateRow);
        }
    }

    private static void writeDocxToStream(WordprocessingMLPackage template, String target) throws IOException, Docx4JException {
        File f = new File(target);
        template.save(f);
    }

    private static void createFirstTable() throws FileNotFoundException, Exception {
        // get the first Table
        List<Object> tables = getAllElementFromObject(template.getMainDocumentPart(), Tbl.class);
        Tbl firstTable = (Tbl) tables.get(0);
        List<Object> rows = getAllElementFromObject(firstTable, Tr.class);
        Tr templateRow = (Tr) rows.get(1);
        List<HashMap<String, String>> rowsInTableArtPrise = new ArrayList<>();
          for (int i = 0; i < 4; i++) {
               HashMap<String, String> repl2 = new HashMap<>();
                repl2.put("SJ_FAR", "1");
                repl2.put("SJ_GR", "2");
                repl2.put("SJ_AR", "3");
                repl2.put("SJ_AB", "4");
                repl2.put("SJ_PR", "5");
                repl2.put("SJ_WZ", "6");
                repl2.put("SJ_PM", "7");
                repl2.put("SJ_ME", "8");
                repl2.put("SJ_VP", "9");
                rowsInTableArtPrise.add(repl2);  
        }
       
      
        for (int i = 0; i < 10; i++) {
            replaceSecondTable(rowsInTableArtPrise, templateRow,i);
            addRowToTableFirst(firstTable, templateRow);
             
        }

        firstTable.getContent().remove(templateRow);
    }

    private static void createSecondTable() throws Docx4JException, FileNotFoundException, Exception {

        List<HashMap<String, String>> rows = new ArrayList<>();

        for (int j = 0; j < 2; j++) {
            HashMap<String, String> repl2 = new HashMap<>();
            repl2.put("SJ_FAR", "1");
            repl2.put("SJ_GR", "2");
            repl2.put("SJ_AR", "3");
            repl2.put("SJ_AB", "4");
            repl2.put("SJ_PR", "5");
            repl2.put("SJ_WZ", "6");
            repl2.put("SJ_PM", "7");
            repl2.put("SJ_ME", "8");
            repl2.put("SJ_VP", "9");
            rows.add(repl2);
        }

        replaceTable(new String[]{"SJ_FAR", "SJ_GR", "SJ_AR", "SJ_AB", "SJ_PR", "SJ_WZ", "SJ_PM", "SJ_ME", "SJ_VP"}, rows, template);
    }

    
    
    public static void main(String[] args) throws Exception {

        System.out.println("Creating package..");
        getTemplate("C:\\test\\resultnnn.docx");             
        createFirstTable();
          MainDocumentPart documentPart = template.getMainDocumentPart();
                HashMap<String, String> mappings = new HashMap<>();
                mappings.put("artnum", "101010");
                mappings.put("bezeichung", "ok es geht");
                mappings.put("farben", "rot blue");
                mappings.put("gros", "L, M ,XL");
                VariablePrepare.prepare(template);
                documentPart.variableReplace(mappings);
        writeDocxToStream(template, "C:\\test\\new.docx");
        System.out.println("Done.");

    }

}
