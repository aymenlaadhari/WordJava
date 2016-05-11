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
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

/**
 *
 * @author aladhari
 */
public class MainWindow extends javax.swing.JFrame {

    private final DocDaoInterface daoInterface;
    List<Kunden> kundens;
    List<Artikel> artikels;
    List<Combination> combinations;

    String[] columnNames = {"Nummer",
        "Name",
        "PLZ",
        "Straße",
        "Ort", "Land"};
    Object[] rowData = new Object[6];
    Object[] rowDataArtikel = new Object[5];
    Object[] rowDataCombinaison = new Object[7];
    DefaultTableModel tableModel, tableModelArtikel, tableModelCombinaison;

    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        initComponents();
        tableModel = (DefaultTableModel) jTableKundenl.getModel();
        tableModelArtikel = (DefaultTableModel) jTableArtikel.getModel();
        jTableKundenl.setAutoCreateRowSorter(true);
        jTableArtikel.setAutoCreateRowSorter(true);
        daoInterface = new DocDao();
        kundens = daoInterface.getListKunden();
        artikels = daoInterface.getListArtikel();
        populateListKunden();
        populateListArtikel();
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
//        combinaisons.stream().forEach((cnsmr) -> {
//            populateJtableCombinaison(cnsmr);
//            tableModelCombinaison.addRow(rowDataCombinaison);
//            jTableCombinaison.setModel(tableModelCombinaison);
//        });

 for (int i = 0; i < combinaisons.size(); i++) {
            System.out.println(combinaisons.get(i).getFarben()+"*"+combinaisons.get(i).getPmng()); 
            populateJtableCombinaison(combinaisons.get(i));
            tableModelCombinaison.addRow(rowDataCombinaison);
            jTableCombinaison.setModel(tableModelCombinaison);
       }
    }

    private void populateJtableCombinaison(Combination combination) {
//        rowDataCombinaison[0] = combination.getFarben();
//        rowDataCombinaison[1] = combination.getGroessen();
//        rowDataCombinaison[2] = combination.getMe();
//        rowDataCombinaison[3] = combination.getPmng();
//        rowDataCombinaison[4] = combination.getVk1();
//        rowDataCombinaison[5] = combination.getVpMng();
//        rowDataCombinaison[6] = combination.getWz();
        rowDataCombinaison[0] = "0";
        rowDataCombinaison[1] = "1";
        rowDataCombinaison[2] = "2";
        rowDataCombinaison[3] = "3";
        rowDataCombinaison[4] = "4";
        rowDataCombinaison[5] = "5";
        rowDataCombinaison[6] = "6";

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
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableArtikel = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextArtNummer = new javax.swing.JTextField();
        jTextArtBech = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableCombinaison = new javax.swing.JTable();

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

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

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

        jLabel10.setText("Artikel Liste");

        jLabel11.setText("Kunden Liste");

        jLabel1.setText("Kunden Info");

        jLabel12.setText("Eintritt");

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

        jTextArtBech.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextArtBechKeyReleased(evt);
            }
        });

        jTableCombinaison.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Farben", "Groessen", "ME", "PMNG", "VK1", "VPmng", "WZ"
            }
        ));
        jScrollPane5.setViewportView(jTableCombinaison);

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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(409, 409, 409)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                                        .addGap(46, 46, 46)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(textOrt, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(textPLZ, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(textStrasse, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(textName3, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(textName2, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(textName1, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(textAdresse, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(textLand, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(33, 33, 33)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 521, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(91, 91, 91)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 612, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(364, 364, 364)
                                        .addComponent(jLabel12))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jTextArtNummer, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(101, 101, 101)
                                        .addComponent(jTextArtBech, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)))))
                .addContainerGap(63, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(385, 385, 385)
                .addComponent(jLabel10)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(textLand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextArtBech, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextArtNummer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(332, 332, 332)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        try {
            try (FileOutputStream fileOutputStream = new FileOutputStream("First.docx")) {

                WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
                wordMLPackage.getMainDocumentPart().addStyledParagraphOfText("Title", textName1.getText() + "\n" + textPLZ.getText() + "\n" + textStrasse.getText() + "\n" + textOrt.getText());
                wordMLPackage.getMainDocumentPart().addStyledParagraphOfText("Subtitle", "Das ist Subtitle!");
                wordMLPackage.save(fileOutputStream);
                JOptionPane.showMessageDialog(null, "Successeful created");
            } catch (Docx4JException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (IOException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);
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
        System.out.println(nR + "*" + name + "*" + plz);
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
        String nR = jTableArtikel.getValueAt(jTableArtikel.getSelectedRow(), 0).toString();
        jTextArtNummer.setText(nR);
        combinations = new ArrayList<>();
        DocDao dao = new DocDao();
        try {
            combinations = dao.getCombinations(nR);
        } catch (SQLException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        populateListCombinaison(combinations);
    }//GEN-LAST:event_jTableArtikelMouseClicked

    private void jTableArtikelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableArtikelKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_DOWN) {
            String nR = jTableArtikel.getValueAt(jTableArtikel.getSelectedRow(), 0).toString();
            jTextArtNummer.setText(nR);
//             List<Combination> combinations = daoInterface.getArtikle(nR).getCombinations();
//        for (int i = 0; i < combinations.size(); i++) {
//            System.out.println(combinations.get(i).getFarben()+"*"+combinations.get(i).getPmng()); 
//       }
            //  populateListCombinaison(daoInterface.getArtikle(nR).getCombinations());
        }
    }//GEN-LAST:event_jTableArtikelKeyPressed

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
        java.awt.EventQueue.invokeLater(() -> {
            new MainWindow().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
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
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTable jTableArtikel;
    private javax.swing.JTable jTableCombinaison;
    private javax.swing.JTable jTableKundenl;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextArtBech;
    private javax.swing.JTextField jTextArtNummer;
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
