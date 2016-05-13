/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dastex.javaword;

import com.dastex.javaword.dao.DocDao;
import com.dastex.javaword.dao.DocDaoInterface;
import com.dastex.javaword.dao.model.Artikel;
import com.dastex.javaword.dao.model.Combination;
import com.dastex.javaword.dao.model.Kunden;


import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import org.docx4j.XmlUtils;
import org.docx4j.model.datastorage.migration.VariablePrepare;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;

/**
 *
 * @author aladhari
 */
public class MainWindow extends javax.swing.JFrame {

    private final DocDaoInterface daoInterface;
    List<Kunden> kundens;
    List<Artikel> artikels;

    String[] columnNames = {"Nummer",
        "Name",
        "PLZ",
        "Straße",
        "Ort", "Land"};
    private String templatePath, docOutputPath;
    private String eintritt, artBeschreibung, artFarben, artGroessen, preis, artNum;
    Object[] rowData = new Object[6];
    Object[] rowDataArtikel = new Object[5];
    Object[] rowDataCombinaison = new Object[9];
    DefaultTableModel tableModel, tableModelArtikel, tableModelCombinaison, tableModelSelected;
    WordprocessingMLPackage template;
    DocDao dao;
    Artikel artikel;
    int[] selection;

    /**
     * Creates new form MainWindow
     *
     * @throws org.docx4j.openpackaging.exceptions.Docx4JException
     * @throws java.io.FileNotFoundException
     */
    public MainWindow() throws Docx4JException, FileNotFoundException {
        initComponents();
        dao = new DocDao();
        tableModel = (DefaultTableModel) jTableKundenl.getModel();
        tableModelArtikel = (DefaultTableModel) jTableArtikel.getModel();
        tableModelCombinaison = (DefaultTableModel) jTableCombinaison.getModel();
        tableModelSelected = (DefaultTableModel) jTableSelectedArtikel.getModel();
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        jTableCombinaison.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        jTableCombinaison.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
        jTableCombinaison.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);
        jTableSelectedArtikel.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        jTableSelectedArtikel.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
        jTableSelectedArtikel.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);
        tableModel.setRowCount(0);
        tableModelArtikel.setRowCount(0);
        tableModelCombinaison.setRowCount(0);
        tableModelSelected.setRowCount(0);
        jTableKundenl.setAutoCreateRowSorter(true);
        jTableArtikel.setAutoCreateRowSorter(true);
        daoInterface = new DocDao();
        kundens = daoInterface.getListKunden();
        artikels = daoInterface.getListArtikel();
        populateListKunden();
        populateListArtikel();

    }

    private WordprocessingMLPackage getTemplate(String name) throws Docx4JException, FileNotFoundException {
        template = WordprocessingMLPackage.load(new FileInputStream(new File(name)));
        return template;
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

    private void replacePlaceholder(WordprocessingMLPackage template, String name, String placeholder) {
        List<Object> texts = getAllElementFromObject(template.getMainDocumentPart(), Text.class);

        for (Object text : texts) {
            Text textElement = (Text) text;
            if (textElement.getValue().equals(placeholder)) {
                textElement.setValue(name);
            }
        }
    }

    private void replaceTable(String[] placeholders, List<HashMap<String, String>> textToAdd,
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

    private Tbl getTemplateTable(List<Object> tables, String templateKey) throws Docx4JException, JAXBException {
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

    private void writeDocxToStream(WordprocessingMLPackage template, String target) throws IOException, Docx4JException {
        File f = new File(target);
        template.save(f);
    }

    private void populateListKunden() {

        kundens.stream().forEach((listKunden) -> {
            populateJtableKunden(listKunden);
            tableModel.addRow(rowData);
            jTableKundenl.setModel(tableModel);
        });

    }

    private void populateJtableKunden(Kunden kunden) {
        rowData[0] = kunden.getNr();
        rowData[1] = kunden.getName1();
        rowData[2] = kunden.getPlz();
        rowData[3] = kunden.getStrasse();
        rowData[4] = kunden.getOrt();
        rowData[5] = kunden.getLand();
    }

    private void populateListArtikel() {
        artikels.stream().forEach((artikel) -> {
            populateJtableArtikel(artikel);
            tableModelArtikel.addRow(rowDataArtikel);
            jTableArtikel.setModel(tableModelArtikel);

        });
    }

    private void populateJtableArtikel(Artikel artikel) {
        rowDataArtikel[0] = artikel.getNr();
        rowDataArtikel[1] = artikel.getBezeichnung();

    }

    private void populateListCombinaison(List<Combination> combinaisons) {

        combinaisons.stream().forEach((cnsmr) -> {
            populateJtableCombinaison(cnsmr);
            tableModelCombinaison.addRow(rowDataCombinaison);
            jTableCombinaison.setModel(tableModelCombinaison);
        });

    }

    private void populateJtableCombinaison(Combination combination) {
        rowDataCombinaison[0] = combination.getFarben();
        rowDataCombinaison[1] = combination.getGroessen();
        rowDataCombinaison[2] = combination.getArt();
        rowDataCombinaison[3] = combination.getAb();
        rowDataCombinaison[4] = combination.getPreis();
        rowDataCombinaison[5] = combination.getWz();
        rowDataCombinaison[6] = combination.getPmng();
        rowDataCombinaison[7] = combination.getMe();
        rowDataCombinaison[8] = combination.getVpMng();

    }

    private void jtableKundenFilter(String crieteria) {
        TableRowSorter<DefaultTableModel> rowSorter = new TableRowSorter<>(tableModel);
        rowSorter.setRowFilter(RowFilter.regexFilter("^" + crieteria));
        jTableKundenl.setRowSorter(rowSorter);
    }

    private void jtableArtikelFilter(String crieteria) {
        TableRowSorter<DefaultTableModel> rowSorter = new TableRowSorter<>(tableModelArtikel);
        rowSorter.setRowFilter(RowFilter.regexFilter("^" + crieteria));
        jTableArtikel.setRowSorter(rowSorter);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        textAdresse6 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        textAdresse = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        textName1 = new javax.swing.JTextField();
        textName2 = new javax.swing.JTextField();
        textName3 = new javax.swing.JTextField();
        textStrasse = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        textPLZ = new javax.swing.JTextField();
        textOrt = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        textLand = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableKundenl = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jAreaEintritt = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableArtikel = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextArtNummer = new javax.swing.JTextField();
        jTextArtBech = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableCombinaison = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextAreaArtText = new javax.swing.JTextArea();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jTextFarben = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTextGroessen = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jTextPrise = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jButtonAddSelection = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTableSelectedArtikel = new javax.swing.JTable();
        jButtonClearSelected = new javax.swing.JButton();

        textAdresse6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textAdresse6ActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Create DOC");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        textAdresse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textAdresseActionPerformed(evt);
            }
        });
        textAdresse.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textAdresseKeyReleased(evt);
            }
        });

        jLabel2.setText("Nummer");

        jLabel3.setText("Name1");

        jLabel4.setText("Name2");

        jLabel5.setText("Name3");

        jLabel6.setText("Straße");

        textName1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textName1ActionPerformed(evt);
            }
        });
        textName1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textName1KeyReleased(evt);
            }
        });

        textName2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textName2ActionPerformed(evt);
            }
        });
        textName2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textName2KeyReleased(evt);
            }
        });

        textName3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textName3ActionPerformed(evt);
            }
        });
        textName3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textName3KeyReleased(evt);
            }
        });

        textStrasse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textStrasseActionPerformed(evt);
            }
        });
        textStrasse.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textStrasseKeyReleased(evt);
            }
        });

        jLabel7.setText("PLZ");

        jLabel8.setText("Ort");

        textPLZ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textPLZActionPerformed(evt);
            }
        });
        textPLZ.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textPLZKeyReleased(evt);
            }
        });

        textOrt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textOrtActionPerformed(evt);
            }
        });
        textOrt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textOrtKeyReleased(evt);
            }
        });

        jLabel9.setText("Land");

        textLand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textLandActionPerformed(evt);
            }
        });
        textLand.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textLandKeyReleased(evt);
            }
        });

        jTableKundenl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Nummer", "Name", "PLZ", "Straße", "Ort", "Land"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTableKundenl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableKundenlMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableKundenl);

        jAreaEintritt.setColumns(20);
        jAreaEintritt.setRows(5);
        jScrollPane2.setViewportView(jAreaEintritt);

        jTableArtikel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Nummer", "Beschreibung"
            }
        ));
        jTableArtikel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableArtikelMouseClicked(evt);
            }
        });
        jTableArtikel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTableArtikelKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(jTableArtikel);

        jLabel11.setText("Kunden Liste");

        jLabel1.setText("Kunden Info");

        jTextArtNummer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextArtNummerActionPerformed(evt);
            }
        });
        jTextArtNummer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextArtNummerKeyReleased(evt);
            }
        });

        jTextArtBech.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextArtBechActionPerformed(evt);
            }
        });
        jTextArtBech.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextArtBechKeyReleased(evt);
            }
        });

        jTableCombinaison.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Farben", "Groessen", "Art", "Ab", "Preis", "WZ", "P_Mng", "ME", "VP_Mng"
            }
        ));
        jTableCombinaison.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableCombinaisonMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(jTableCombinaison);

        jLabel13.setText("ArtNumm");

        jTextAreaArtText.setColumns(20);
        jTextAreaArtText.setRows(5);
        jScrollPane4.setViewportView(jTextAreaArtText);

        jLabel14.setText("ArtText");

        jLabel15.setText("ArtBeschr");

        jTextFarben.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFarbenActionPerformed(evt);
            }
        });

        jLabel16.setText("Farben");

        jLabel17.setText("Groessen");

        jLabel18.setText("Prise");

        jButtonAddSelection.setText("Add Artikel->");
        jButtonAddSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddSelectionActionPerformed(evt);
            }
        });

        jTableSelectedArtikel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Farben", "Groessen", "Art", "Ab", "Preis", "WZ", "P_mng", "ME", "VP_Mng"
            }
        ));
        jTableSelectedArtikel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTableSelectedArtikelKeyPressed(evt);
            }
        });
        jScrollPane6.setViewportView(jTableSelectedArtikel);

        jButtonClearSelected.setText("Clear");
        jButtonClearSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearSelectedActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(189, 189, 189)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addGap(342, 342, 342))
            .addGroup(layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel13)))
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jTextPrise, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTextGroessen, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTextFarben, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextArtNummer, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(51, 51, 51)
                                .addComponent(jLabel15)
                                .addGap(18, 18, 18)
                                .addComponent(jTextArtBech, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel6)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(51, 51, 51)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(textOrt, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textPLZ, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textStrasse, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textName3, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textName2, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textName1, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textAdresse, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textLand, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(33, 33, 33)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 521, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(101, 101, 101)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(32, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButtonClearSelected)
                        .addGap(207, 207, 207))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButtonAddSelection)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(jButton1)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textAdresse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(textName1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(textName2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(textName3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(textStrasse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(textPLZ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(textOrt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(textLand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextArtBech, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addGap(7, 7, 7)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(179, 179, 179))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextArtNummer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFarben, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextGroessen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextPrise, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(85, 85, 85)
                                .addComponent(jButtonAddSelection))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addComponent(jButtonClearSelected)
                .addGap(68, 68, 68)
                .addComponent(jButton1)
                .addGap(974, 974, 974))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        // TODO add your handling code here:
        eintritt = jAreaEintritt.getText();
        
//        try {
//            try (FileOutputStream fileOutputStream = new FileOutputStream("First.docx")) {
//
//                WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
//                wordMLPackage.getMainDocumentPart().addStyledParagraphOfText("Title", textName1.getText() + "+++" + textPLZ.getText() + "+++" + textStrasse.getText() + "+++" + textOrt.getText());
//                wordMLPackage.getMainDocumentPart().addStyledParagraphOfText("Title", eintritt + "+++" + artBeschreibung + "+++" + artFarben + "+++" + artGroessen + "+++" + neString);
//                wordMLPackage.getMainDocumentPart().addStyledParagraphOfText("Subtitle", "Das ist Subtitle!");
//                wordMLPackage.save(fileOutputStream);
//                JOptionPane.showMessageDialog(null, "Successeful created");
//            } catch (Docx4JException ex) {
//                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        } catch (IOException | HeadlessException e) {
//            JOptionPane.showMessageDialog(null, e);
//        }
        try {

            getTemplate("template.docx");
            MainDocumentPart documentPart = template.getMainDocumentPart();
            HashMap<String, String> mappings = new HashMap<>();
            mappings.put("name", textName1.getText());
            mappings.put("strasse", textStrasse.getText());
            mappings.put("plz", textStrasse.getText());
            mappings.put("ort", textStrasse.getText());
            mappings.put("eintritt", eintritt);
            mappings.put("artnum", artNum);
            mappings.put("bezeichung", artBeschreibung);
            mappings.put("farben", artFarben);
            mappings.put("gros", artGroessen);
            VariablePrepare.prepare(template);
            documentPart.variableReplace(mappings);
        } catch (JAXBException | Docx4JException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        //replacePlaceholder(template, "OK", "geehrter");
        template.getMainDocumentPart().addStyledParagraphOfText("Nummerierung1", eintritt + "+++" + artBeschreibung + "+++" + artFarben + "+++" + artGroessen);
        List<HashMap<String, String>> rows = new ArrayList<>();
        int row = jTableSelectedArtikel.getRowCount();
        for (int j = 0; j < row; j++) {
             HashMap<String, String> repl2 = new HashMap<>();
               
            repl2.put("SJ_FAR", jTableSelectedArtikel.getValueAt(j, 0).toString());
            repl2.put("SJ_GR", jTableSelectedArtikel.getValueAt(j, 1).toString());
            repl2.put("SJ_AR", jTableSelectedArtikel.getValueAt(j, 2).toString());
            repl2.put("SJ_AB", jTableSelectedArtikel.getValueAt(j, 3).toString());
            repl2.put("SJ_PR", jTableSelectedArtikel.getValueAt(j, 4).toString());
            repl2.put("SJ_WZ", jTableSelectedArtikel.getValueAt(j, 5).toString());
            repl2.put("SJ_PM", jTableSelectedArtikel.getValueAt(j, 6).toString());
            repl2.put("SJ_ME", jTableSelectedArtikel.getValueAt(j, 7).toString());
            repl2.put("SJ_VP", jTableSelectedArtikel.getValueAt(j, 8).toString());
            rows.add(repl2);
            
        }

        try {
            replaceTable(new String[]{"SJ_FAR", "SJ_GR", "SJ_AR", "SJ_AB", "SJ_PR", "SJ_WZ", "SJ_PM", "SJ_ME", "SJ_VP"}, rows, template);
        } catch (Docx4JException | JAXBException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            writeDocxToStream(template, "First.docx");
            JOptionPane.showMessageDialog(null, "Successeful created");
        } catch (IOException | Docx4JException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void textAdresseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textAdresseActionPerformed
        // TODO add your handling code here:


    }//GEN-LAST:event_textAdresseActionPerformed

    private void textName1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textName1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textName1ActionPerformed

    private void textName2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textName2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textName2ActionPerformed

    private void textName3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textName3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textName3ActionPerformed

    private void textStrasseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textStrasseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textStrasseActionPerformed

    private void textPLZActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textPLZActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textPLZActionPerformed

    private void textAdresse6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textAdresse6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textAdresse6ActionPerformed

    private void textOrtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textOrtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textOrtActionPerformed

    private void textLandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textLandActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textLandActionPerformed

    private void jTableKundenlMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableKundenlMouseClicked
        // TODO add your handling code here:
        String nR = jTableKundenl.getValueAt(jTableKundenl.getSelectedRow(), 0).toString();
        String name = jTableKundenl.getValueAt(jTableKundenl.getSelectedRow(), 1).toString();
        String plz = jTableKundenl.getValueAt(jTableKundenl.getSelectedRow(), 2).toString();
        String strasse = jTableKundenl.getValueAt(jTableKundenl.getSelectedRow(), 3).toString();
        String ort = jTableKundenl.getValueAt(jTableKundenl.getSelectedRow(), 4).toString();
        String land = jTableKundenl.getValueAt(jTableKundenl.getSelectedRow(), 5).toString();
        // print first column value from selected row
        textAdresse.setText(nR);
        textName1.setText(name);
        textPLZ.setText(plz);
        textStrasse.setText(strasse);
        textOrt.setText(ort);
        textLand.setText(land);
    }//GEN-LAST:event_jTableKundenlMouseClicked

    private void textAdresseKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textAdresseKeyReleased
        // TODO add your handling code here:
        jtableKundenFilter(textAdresse.getText().toLowerCase());
    }//GEN-LAST:event_textAdresseKeyReleased

    private void textName1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textName1KeyReleased
        // TODO add your handling code here:
        jtableKundenFilter(textName1.getText().toLowerCase());
    }//GEN-LAST:event_textName1KeyReleased

    private void textName2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textName2KeyReleased
        // TODO add your handling code here:
        jtableKundenFilter(textName2.getText().toLowerCase());
    }//GEN-LAST:event_textName2KeyReleased

    private void textName3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textName3KeyReleased
        // TODO add your handling code here:
        jtableKundenFilter(textName3.getText().toLowerCase());
    }//GEN-LAST:event_textName3KeyReleased

    private void textStrasseKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textStrasseKeyReleased
        // TODO add your handling code here:
        jtableKundenFilter(textStrasse.getText().toLowerCase());
    }//GEN-LAST:event_textStrasseKeyReleased

    private void textPLZKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textPLZKeyReleased
        // TODO add your handling code here:
        jtableKundenFilter(textPLZ.getText().toLowerCase());
    }//GEN-LAST:event_textPLZKeyReleased

    private void textOrtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textOrtKeyReleased
        // TODO add your handling code here:
        jtableKundenFilter(textOrt.getText().toLowerCase());
    }//GEN-LAST:event_textOrtKeyReleased

    private void textLandKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textLandKeyReleased
        // TODO add your handling code here:
        jtableKundenFilter(textLand.getText().toLowerCase());
    }//GEN-LAST:event_textLandKeyReleased

    private void jTextArtNummerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextArtNummerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextArtNummerActionPerformed

    private void jTextArtNummerKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArtNummerKeyReleased
        // TODO add your handling code here:
        jtableArtikelFilter(jTextArtNummer.getText());
    }//GEN-LAST:event_jTextArtNummerKeyReleased

    private void jTextArtBechKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArtBechKeyReleased
        // TODO add your handling code here:
        jtableArtikelFilter(jTextArtBech.getText());
    }//GEN-LAST:event_jTextArtBechKeyReleased

    private void jTableArtikelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableArtikelMouseClicked
        // TODO add your handling code here:

        artNum = jTableArtikel.getValueAt(jTableArtikel.getSelectedRow(), 0).toString();
        artikel = daoInterface.getArtikle(artNum);
        //artBeschreibung = jTableArtikel.getValueAt(jTableArtikel.getSelectedRow(), 1).toString();
        artBeschreibung = artikel.getText();
        jTextArtNummer.setText(artNum);
        jTextAreaArtText.setText(artikel.getText());
        jTextFarben.setText(artikel.getFarben());
        jTextGroessen.setText(artikel.getGroessen());
        List<Combination> combinations = null;
        try {
            combinations = dao.getListCombProc(textAdresse.getText(), artNum);
        } catch (SQLException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (combinations.isEmpty()) {

        } else {
            tableModelCombinaison.setRowCount(0);
            populateListCombinaison(combinations);

        }

    }//GEN-LAST:event_jTableArtikelMouseClicked

    private void jTableArtikelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableArtikelKeyPressed
        // TODO add your handling code here:
//        if (evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_DOWN) {
//            String nR = jTableArtikel.getValueAt(jTableArtikel.getSelectedRow(), 0).toString();
//            jTextArtNummer.setText(nR);
//             List<Combination> combinations = daoInterface.getArtikle(nR).getCombinations();
//        if (!combinations.isEmpty()) {
//            
//            populateListCombinaison(combinations);
//        }else
//            tableModelCombinaison.setRowCount(0);
//        }
    }//GEN-LAST:event_jTableArtikelKeyPressed

    private void jTableCombinaisonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableCombinaisonMouseClicked
        // TODO add your handling code here:
        selection = jTableCombinaison.getSelectedRows();
        System.out.println(selection);
        artFarben = artikel.getFarben();
        artGroessen = artikel.getGroessen();
        preis = jTableCombinaison.getValueAt(jTableCombinaison.getSelectedRow(), 4).toString();
    }//GEN-LAST:event_jTableCombinaisonMouseClicked

    private void jTextFarbenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFarbenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFarbenActionPerformed

    private void jButtonAddSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddSelectionActionPerformed
        // TODO add your handling code here:
        selection = jTableCombinaison.getSelectedRows();
        Object row[];
        row = new Object[9];
        for (int i = 0; i < selection.length; i++) {
            row[0] = tableModelCombinaison.getValueAt(selection[i], 0);
            row[1] = tableModelCombinaison.getValueAt(selection[i], 1);
            row[2] = tableModelCombinaison.getValueAt(selection[i], 2);
            row[3] = tableModelCombinaison.getValueAt(selection[i], 3);
            row[4] = tableModelCombinaison.getValueAt(selection[i], 4);
            row[5] = tableModelCombinaison.getValueAt(selection[i], 5);
            row[6] = tableModelCombinaison.getValueAt(selection[i], 6);
            row[7] = tableModelCombinaison.getValueAt(selection[i], 7);
            row[8] = tableModelCombinaison.getValueAt(selection[i], 8);
            tableModelSelected.addRow(row);

        }
    }//GEN-LAST:event_jButtonAddSelectionActionPerformed

    private void jButtonClearSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearSelectedActionPerformed
        // TODO add your handling code here:
        tableModelSelected.setRowCount(0);
    }//GEN-LAST:event_jButtonClearSelectedActionPerformed

    private void jTextArtBechActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextArtBechActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextArtBechActionPerformed

    private void jTableSelectedArtikelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableSelectedArtikelKeyPressed
        // TODO add your handling code here:

        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            tableModelSelected.removeRow(jTableSelectedArtikel.getSelectedRow());

        }
    }//GEN-LAST:event_jTableSelectedArtikelKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new MainWindow().setVisible(true);
                } catch (Docx4JException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea jAreaEintritt;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonAddSelection;
    private javax.swing.JButton jButtonClearSelected;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable jTableArtikel;
    private javax.swing.JTable jTableCombinaison;
    private javax.swing.JTable jTableKundenl;
    private javax.swing.JTable jTableSelectedArtikel;
    private javax.swing.JTextArea jTextAreaArtText;
    private javax.swing.JTextField jTextArtBech;
    private javax.swing.JTextField jTextArtNummer;
    private javax.swing.JTextField jTextFarben;
    private javax.swing.JTextField jTextGroessen;
    private javax.swing.JTextField jTextPrise;
    private javax.swing.JTextField textAdresse;
    private javax.swing.JTextField textAdresse6;
    private javax.swing.JTextField textLand;
    private javax.swing.JTextField textName1;
    private javax.swing.JTextField textName2;
    private javax.swing.JTextField textName3;
    private javax.swing.JTextField textOrt;
    private javax.swing.JTextField textPLZ;
    private javax.swing.JTextField textStrasse;
    // End of variables declaration//GEN-END:variables
}
