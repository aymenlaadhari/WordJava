/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dastex.javaword.utils;

/**
 *
 * @author aladhari
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBElement;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.Part;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.Parts;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPart;
import org.docx4j.wml.Text;

public class ReportFromTemplate {

    static File fileToReplace = new File("C:\\test\\logo1.png");

    private static byte[] fileToBytes(File file) throws FileNotFoundException,
            IOException {
        byte[] bytes = null;
// Our utility method wants that as a byte array 
        if (file.exists() && file.isFile()) {
            java.io.InputStream is = new java.io.FileInputStream(file);
            long length = file.length();
// You cannot create an array using a long type. 
// It needs to be an int type. 
            bytes = new byte[(int) length];
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length
                    && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
// Ensure all the bytes have been read in 
            if (offset < bytes.length) {
// System.out.println("Could not completely read file 
// "+file.getName()); 
            }
            is.close();
        } else {
            bytes = new byte[0];
        }
        return bytes;
    }

    public static void main(String args[]) {
        final String XPATH_TO_SELECT_TEXT_NODES = "//w:t";
        String fileName = "";
        try {
// Populate the Strings that will replace the template text 
            Map map = new HashMap();
            map.put("Project", "BP Mount");
            map.put("date", "21-Mar-2011");

// C:\\test\\template1.docx is the template file 
            WordprocessingMLPackage template = WordprocessingMLPackage
                    .load(new File("C:\\test\\template1.docx"));

            Parts parts = template.getParts();

            HashMap partsMap = parts.getParts();
            PartName partName = null;
            Part part = null;

            Set set = partsMap.keySet();
            for (Iterator iterator = set.iterator(); iterator.hasNext();) {
                PartName name = (PartName) iterator.next();
                if (name.getName().equalsIgnoreCase("/word/media/image1.png")) {
                    part = (Part) partsMap.get(name);
                    partName = name;
                }

            }
            if (part != null && partName != null) {
                part = (Part) partsMap.get(partName);
                BinaryPart binaryPart = (BinaryPart) part;
                binaryPart.setBinaryData(fileToBytes(fileToReplace));
            }

            List texts = template.getMainDocumentPart()
                    .getJAXBNodesViaXPath(XPATH_TO_SELECT_TEXT_NODES, true);
            for (Object obj : texts) {
                Text text = (Text) ((JAXBElement) obj).getValue();
                String textValue = text.getValue();
                for (Object key : map.keySet()) {
                    textValue = textValue.replaceAll("\\$\\{" + key + "\\}",
                            (String) map.get(key));
                }
                text.setValue(textValue);
            }
            /* 
* Add the other contents here 
* 
* 
* 
* 
* 
             */
            template.save(new File("C:\\test\\report11.docx"));
            System.out.println("Done");
        } catch (Exception e) {
// TODO Auto-generated catch block 
            e.printStackTrace();
            System.out.println("Errors");
        }
    }
}
