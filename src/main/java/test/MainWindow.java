/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaapplicationtestjar.dao.DocDao;
import javaapplicationtestjar.dao.DocDaoInterface;
import javaapplicationtestjar.model.Angebot;
import javaapplicationtestjar.model.Artikel;
import javaapplicationtestjar.model.Prises;
import javaapplicationtestjar.model.Kunden;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
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
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.TblWidth;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;

/**
 *
 * @author aladhari
 */
public class MainWindow extends javax.swing.JFrame  {

    private final DocDaoInterface daoInterface;
    List<Kunden> kundens;
    List<Artikel> artikels, selectedArtikels;
    List<Prises> combinations;
    private String eintritt, artBeschreibung, artFarben, preis, artGroessen, artNum;
    private Task task;
    Object[] rowData = new Object[6];
    Object[] rowDataArtikel = new Object[5];
    Object[] rowDataCombinaison = new Object[9];
     Object[] rowArtAngebot = new Object[2];;
    DefaultTableModel tableModel, tableModelArtikel, tableModelCombinaison, tableModelSelectedPrises, tableModelSelectedArtikel;
    WordprocessingMLPackage template;
    DocDao dao;
    Artikel artikel;
    int[] selection, selectedArtikel;
    static ObjectFactory factory = new ObjectFactory();
    private Angebot angebot;

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
        tableModelSelectedPrises = (DefaultTableModel) jTableSelectedPrises.getModel();
        tableModelSelectedArtikel = (DefaultTableModel) jTableSelectedArtikels.getModel();
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        jTableCombinaison.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        jTableCombinaison.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
        jTableCombinaison.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);
        jTableSelectedPrises.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        jTableSelectedPrises.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
        jTableSelectedPrises.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);
        jCheckFarben.setSelected(true);
        jCheckGroessen.setSelected(true);
        tableModelArtikel.setRowCount(0);
        tableModelCombinaison.setRowCount(0);
        tableModelSelectedPrises.setRowCount(0);
        tableModelSelectedArtikel.setRowCount(0);
        jTableKundenl.setAutoCreateRowSorter(true);
        jTableArtikel.setAutoCreateRowSorter(true);
        daoInterface = new DocDao();
        kundens = daoInterface.getListKunden();
        artikels = daoInterface.getListArtikel();
        selectedArtikels = new ArrayList<>();
        populateListKunden();
        populateListArtikel();

    }


    class Task extends SwingWorker<Void, Void> {

        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() throws Docx4JException, FileNotFoundException, IOException, Exception {
            eintritt = jAreaEintritt.getText();
            createSecondTable();
            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            try {
                writeDocxToStream(template, "First.docx");
            } catch (IOException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Docx4JException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
           
            Toolkit.getDefaultToolkit().beep();
             JOptionPane.showMessageDialog(null, "Successeful created");
            try {
              
                Desktop.getDesktop().open(new File("First.docx"));
            } catch (IOException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
            setCursor(null); //turn off the wait cursor
          
        }
    }

    private void createSecondTable() throws Docx4JException, FileNotFoundException, Exception {
         getTemplate("template.docx");
         createFirstTable();
                MainDocumentPart documentPart = template.getMainDocumentPart();
                HashMap<String, String> mappings = new HashMap<>();
                mappings.put("name", textName1.getText().replaceAll("&([^;]+(?!(?:\\w|;)))", "&amp;$1"));
                mappings.put("strasse", textStrasse.getText().replaceAll("&([^;]+(?!(?:\\w|;)))", "&amp;$1"));
                mappings.put("plz", textPLZ.getText());
                mappings.put("ort", textOrt.getText());
                mappings.put("eintritt", eintritt);
                mappings.put("artnum", artNum);
                mappings.put("bezeichung", artBeschreibung);
                mappings.put("farben", artFarben);
                mappings.put("gros", artGroessen);
                VariablePrepare.prepare(template);
                documentPart.variableReplace(mappings);

        }
        
        
        

    


    private WordprocessingMLPackage getTemplate(String name) throws Docx4JException, FileNotFoundException, org.docx4j.openpackaging.exceptions.Docx4JException {
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

    private void writeDocxToStream(WordprocessingMLPackage template, String target) throws IOException, Docx4JException {
        File f = new File(target);
        template.save(f);
    }

    
    
    
    
    // new
    
       private  void createFirstTable() throws FileNotFoundException, Exception {
        // get the first Table
        List<Object> tables = getAllElementFromObject(template.getMainDocumentPart(), Tbl.class);
        Tbl firstTable = (Tbl) tables.get(0);
        List<Object> rows = getAllElementFromObject(firstTable, Tr.class);
        Tr templateRow = (Tr) rows.get(1);
        List<HashMap<String, String>> rowsInTableArtPrise = new ArrayList<>();
         int row = jTableSelectedPrises.getRowCount();
            for (int j = 0; j < row; j++) {
                HashMap<String, String> repl2 = new HashMap<>();
                repl2.put("SJ_FAR", jTableSelectedPrises.getValueAt(j, 0).toString());
                repl2.put("SJ_GR", jTableSelectedPrises.getValueAt(j, 1).toString());
                repl2.put("SJ_AR", jTableSelectedPrises.getValueAt(j, 2).toString());
                repl2.put("SJ_AB", jTableSelectedPrises.getValueAt(j, 3).toString());
                repl2.put("SJ_PR", jTableSelectedPrises.getValueAt(j, 4).toString());
                repl2.put("SJ_WZ", jTableSelectedPrises.getValueAt(j, 5).toString());
                repl2.put("SJ_PM", jTableSelectedPrises.getValueAt(j, 6).toString());
                repl2.put("SJ_ME", jTableSelectedPrises.getValueAt(j, 7).toString());
                repl2.put("SJ_VP", jTableSelectedPrises.getValueAt(j, 8).toString());
                rowsInTableArtPrise.add(repl2);
            }
      int atrikelsInAngebot = jTableSelectedArtikels.getRowCount();
        for (int i = 0; i < 6; i++) {
            replaceSecondTable(rowsInTableArtPrise, templateRow,i);
            addRowToTableFirst(firstTable, templateRow);
             
        }

        firstTable.getContent().remove(templateRow);
    }
     
      private  void replaceSecondTable(List<HashMap<String, String>> textToAdd,
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
            for (HashMap<String, String> replacements : textToAdd) {
                // 2 and 3 are done in this method
                addRowToTable(tempTable, templateRow, replacements);
            }

            // 4. remove the template row
            tempTable.getContent().remove(templateRow);
        }
    }
      
       private  void addRowToTableFirst(Tbl reviewtable, Tr templateRow) {
        Tr workingRow = (Tr) XmlUtils.deepCopy(templateRow);
        reviewtable.getContent().add(workingRow);
    }
    
    
       
       
    private void createListArtikelsAngebot()
    {
          
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

    private void populateListCombinaison(List<Prises> combinaisons) {

        combinaisons.stream().forEach((cnsmr) -> {
            populateJtableCombinaison(cnsmr);
            tableModelCombinaison.addRow(rowDataCombinaison);
            jTableCombinaison.setModel(tableModelCombinaison);
        });

    }

    private void populateJtableCombinaison(Prises combination) {
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
        jTableSelectedPrises = new javax.swing.JTable();
        jButtonClearSelected = new javax.swing.JButton();
        jCheckFarben = new javax.swing.JCheckBox();
        jCheckGroessen = new javax.swing.JCheckBox();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTableSelectedArtikels = new javax.swing.JTable();
        jButtonAddtoAngebot = new javax.swing.JButton();

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

        jButtonAddSelection.setText("Add Preises->");
        jButtonAddSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddSelectionActionPerformed(evt);
            }
        });

        jTableSelectedPrises.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableSelectedPrises.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTableSelectedPrisesKeyPressed(evt);
            }
        });
        jScrollPane6.setViewportView(jTableSelectedPrises);

        jButtonClearSelected.setText("Clear");
        jButtonClearSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearSelectedActionPerformed(evt);
            }
        });

        jCheckFarben.setText("Farben");
        jCheckFarben.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckFarbenActionPerformed(evt);
            }
        });

        jCheckGroessen.setText("Groessen");

        jTableSelectedArtikels.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Artikel Nummer", "Artikel Name2"
            }
        ));
        jScrollPane7.setViewportView(jTableSelectedArtikels);

        jButtonAddtoAngebot.setText("Add to Angebot");
        jButtonAddtoAngebot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddtoAngebotActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckFarben)
                            .addComponent(jCheckGroessen)
                            .addComponent(jButtonAddSelection))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButtonClearSelected)
                        .addGap(203, 203, 203))))
            .addGroup(layout.createSequentialGroup()
                .addGap(177, 177, 177)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                            .addGap(1, 1, 1)
                                            .addComponent(jLabel13))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel16)
                                            .addComponent(jLabel14)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(3, 3, 3)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel18)
                                            .addComponent(jLabel17))))
                                .addGap(39, 39, 39)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jTextPrise, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextGroessen, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextFarben, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(166, 166, 166)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jTextArtNummer, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(51, 51, 51)
                                                .addComponent(jLabel15))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(116, 116, 116)
                                                .addComponent(jTextArtBech, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))))))
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
                                .addGap(51, 51, 51)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(textOrt, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textPLZ)
                                    .addComponent(textStrasse, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textName3, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textName2, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textName1, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textAdresse, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textLand, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(33, 33, 33)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 521, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(191, 191, 191)
                        .addComponent(jLabel1)
                        .addGap(346, 346, 346)
                        .addComponent(jLabel11))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jButtonAddtoAngebot)
                        .addGap(28, 28, 28)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 719, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(361, 361, 361))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel11))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                                        .addComponent(jLabel14)
                                        .addGap(34, 34, 34))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jTextArtBech, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel15)
                                            .addComponent(jTextArtNummer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel13))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jTextFarben, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextGroessen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel17))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextPrise, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel18))
                                .addGap(0, 8, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addGap(18, 18, 18)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(25, 25, 25)
                                        .addComponent(jCheckGroessen)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jCheckFarben)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButtonAddSelection)
                                        .addGap(20, 20, 20))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(32, 32, 32)))
                                .addComponent(jButtonClearSelected)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addGap(89, 89, 89))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonAddtoAngebot)
                        .addGap(177, 177, 177))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        // TODO add your handling code here:
      
       task = new Task();
           task.execute();

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
        artBeschreibung = artikel.getText();
        jTextArtNummer.setText(artNum);
        jTextAreaArtText.setText(artikel.getText());
        jTextFarben.setText(artikel.getFarben());
        jTextGroessen.setText(artikel.getGroessen());
        tableModelSelectedPrises.setRowCount(0);
        try {
            combinations = dao.getListCombProc(textAdresse.getText(), artNum);
        } catch (SQLException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!combinations.isEmpty()) {
            tableModelCombinaison.setRowCount(0);
            populateListCombinaison(combinations);

        } else {
            JOptionPane.showMessageDialog(null, "No Record found");

        }

    }//GEN-LAST:event_jTableArtikelMouseClicked

    private void jTableArtikelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableArtikelKeyPressed
        // TODO add your handling code here:
//        if (evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_DOWN) {

    }//GEN-LAST:event_jTableArtikelKeyPressed

    private void jTableCombinaisonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableCombinaisonMouseClicked
        // TODO add your handling code here:
        selection = jTableCombinaison.getSelectedRows();
        System.out.println(selection);
        artFarben = artikel.getFarben();
        artGroessen = artikel.getGroessen();
      //  preis = jTableCombinaison.getValueAt(jTableCombinaison.getSelectedRow(), 4).toString();
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
            if (jCheckFarben.isSelected()) {
                row[0] = tableModelCombinaison.getValueAt(selection[i], 0);
            } else {
                row[0] = "";
            }

            if (jCheckGroessen.isSelected()) {
                row[1] = tableModelCombinaison.getValueAt(selection[i], 1);

            } else {
                row[1] = "";
            }

            row[2] = tableModelCombinaison.getValueAt(selection[i], 2);
            row[3] = tableModelCombinaison.getValueAt(selection[i], 3);
            row[4] = tableModelCombinaison.getValueAt(selection[i], 4);
            row[5] = tableModelCombinaison.getValueAt(selection[i], 5);
            row[6] = tableModelCombinaison.getValueAt(selection[i], 6);
            row[7] = tableModelCombinaison.getValueAt(selection[i], 7);
            row[8] = tableModelCombinaison.getValueAt(selection[i], 8);
            tableModelSelectedPrises.addRow(row);

        }
    }//GEN-LAST:event_jButtonAddSelectionActionPerformed

    private void jButtonClearSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearSelectedActionPerformed
        // TODO add your handling code here:
        tableModelSelectedPrises.setRowCount(0);
    }//GEN-LAST:event_jButtonClearSelectedActionPerformed

    private void jTextArtBechActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextArtBechActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextArtBechActionPerformed

    private void jTableSelectedPrisesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableSelectedPrisesKeyPressed
        // TODO add your handling code here:

        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            tableModelSelectedPrises.removeRow(jTableSelectedPrises.getSelectedRow());

        }
    }//GEN-LAST:event_jTableSelectedPrisesKeyPressed

    private void jCheckFarbenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckFarbenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckFarbenActionPerformed

    private void jButtonAddtoAngebotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddtoAngebotActionPerformed
        // TODO add your handling code here:
        Artikel artikelAngebot = new Artikel();
          artikelAngebot.setNr(artNum);
          artikelAngebot.setBezeichnung(artikel.getBezeichnung());
          artikelAngebot.setCombinations(combinations);
          selectedArtikels.add(artikelAngebot);
             rowArtAngebot[0] = artikelAngebot.getNr();
             rowArtAngebot[1] = artikelAngebot.getBezeichnung();
            tableModelSelectedArtikel.addRow(rowArtAngebot);
    }//GEN-LAST:event_jButtonAddtoAngebotActionPerformed

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
        //</editor-fold>
        //</editor-fold>
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
    private javax.swing.JButton jButtonAddtoAngebot;
    private javax.swing.JButton jButtonClearSelected;
    private javax.swing.JCheckBox jCheckFarben;
    private javax.swing.JCheckBox jCheckGroessen;
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
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTable jTableArtikel;
    private javax.swing.JTable jTableCombinaison;
    private javax.swing.JTable jTableKundenl;
    private javax.swing.JTable jTableSelectedArtikels;
    private javax.swing.JTable jTableSelectedPrises;
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
