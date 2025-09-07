package it.trenical.server.gui;
import it.trenical.server.Biglietto.*;
import it.trenical.server.Cliente.*;
import it.trenical.server.Tratta.TrattaImpl;
import it.trenical.server.Tratta.TrattaPrototype;
import it.trenical.server.Tratta.TrattaStandard;
import it.trenical.server.Treno.*;
import it.trenical.server.promozione.Promozione;
import it.trenical.server.promozione.PromozioneImpl;
import it.trenical.server.promozione.PromozioneImplDB;
import it.trenical.server.Tratta.*;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class AdminDashboardSwing extends JFrame {

    private static final String DB_URL = "jdbc:sqlite:db/treniCal.db";

    public AdminDashboardSwing() {
        setTitle("Terminale Amministratore");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Treni", creaTabella("Treno"));
        tabs.addTab("Tratte", creaTabella("Tratta"));
        tabs.addTab("Clienti", creaTabella("Cliente"));
        tabs.addTab("Promozioni", creaTabella("Promozione"));
        tabs.addTab("Biglietti", creaTabella("Biglietto"));

        add(tabs);
    }
    private JPanel creaTabella(String tabellaNome) {
        JPanel panel = new JPanel(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        caricaDatiDaDB(tabellaNome, model);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Aggiungi "+tabellaNome);
        JButton removeButton = new JButton("Rimuovi "+tabellaNome);
        JButton refreshButton = new JButton("Aggiorna "+tabellaNome);
        JButton modifyButton = new JButton("Modifica "+tabellaNome);
        JButton filtri = new JButton("Filtra "+tabellaNome);

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(filtri);

        switch(tabellaNome) {
            case "Cliente":
                addButton.addActionListener(e -> apriFinestraAdd(tabellaNome,6));
                break;
            case "Treno":
                addButton.addActionListener(e -> apriFinestraAdd(tabellaNome,11));
                break;
            case "Biglietto":
                addButton.addActionListener(e -> apriFinestraAdd(tabellaNome,8));
                break;
            case "Promozione":
                addButton.addActionListener(e -> apriFinestraAdd(tabellaNome, 8));
                break;
            case "Tratta":
                addButton.addActionListener(e -> apriFinestraAdd(tabellaNome, 7));
                break;
                }

        removeButton.addActionListener(e -> {int row = table.getSelectedRow();
            if (row != -1) {
                Object id = table.getValueAt(row, 0);
                switch (tabellaNome) {
                    case "Treno":
                        BigliettoDB bdb = BigliettoDB.getInstance();
                        rimuoviDaDB(tabellaNome, id);
                        bdb.removeBigliettoByTrenoID(id.toString());
                        break;
                    case "Tratta":
                        String tr = id.toString();
                        System.out.println("Calcolo treno");
                        //aspetta();
                        TrenoImplDB tdb = TrenoImplDB.getInstance();
                        List<Treno> treni = tdb.getTrenoByTrattaID(tr);
                        System.out.println(treni.toString());
                        for(Treno trenoID : treni) {
                            tdb.removeTreno(trenoID.getTrenoID());
                            System.out.println("Rimuovo: "+trenoID.toString());
                        }
                        for(Treno trenoID : treni) {
                            BigliettoDB bdb2 = BigliettoDB.getInstance();
                            bdb2.removeBigliettoByTrenoID(trenoID.getTrenoID());
                        }
                        rimuoviDaDB(tabellaNome, id);
                        break;
                   default:
                       rimuoviDaDB(tabellaNome, id);


                }
                ((DefaultTableModel) table.getModel()).removeRow(row);
            }
        });
        modifyButton.addActionListener(e -> { int row = table.getSelectedRow();
            if (row != -1) {
                int colCount = table.getColumnCount();
                Object[] rowData = new Object[colCount];

                for (int col = 0; col < colCount; col++) {
                    rowData[col] = table.getValueAt(row, col);
                }
            switch(tabellaNome) {
                case "Cliente":
                    ClienteImpl cldb = ClienteImplDB.getInstance();

                    String codiceFiscale = rowData[0].toString();
                    String nome = rowData[1].toString();
                    String cognome = rowData[2].toString();
                    String codiceCliente = rowData[3].toString();
                    int eta = Integer.parseInt(rowData[4].toString());
                    String email = rowData[5].toString();
                    Cliente cliente = new ClienteConcr(codiceFiscale, nome, cognome, codiceCliente, eta,email);
                    cldb.setCliente(cliente);
                    break;
                case "Treno":
                    TrenoImpl trdb = TrenoImplDB.getInstance();
                    String trenoID = rowData[0].toString();
                    String tipoTreno = rowData[1].toString();
                    String trattaID = rowData[2].toString();
                    int prezzo = Integer.parseInt(rowData[3].toString());
                    int postiPrima = Integer.parseInt(rowData[4].toString());
                    int postiSeconda = Integer.parseInt(rowData[5].toString());
                    int postiTerza = Integer.parseInt(rowData[6].toString());
                    int postiTot = Integer.parseInt(rowData[7].toString());
                    int binario = Integer.parseInt(rowData[8].toString());
                    String promozione = rowData[9].toString();
                    System.out.println(trenoID+" "+tipoTreno+" "+trattaID+" "+prezzo+" "+" "+postiPrima+" "+" "+postiSeconda+" "+postiTerza+" "+postiTot);
                    TrattaStandard tratta = TrattaImplDB.getInstance().getTratta(trattaID);
                    Treno tr = new TrenoConcr(trenoID,tipoTreno,tratta,prezzo,postiPrima,postiSeconda,postiTerza,postiTot,binario,promozione);
                    trdb.setTreno(tr);
                    break;
                case "Tratta" :
                    TrattaImpl trtdb = TrattaImplDB.getInstance();
                    String trattaID2 = rowData[0].toString();
                    String stazionePartenza = rowData[1].toString();
                    String stazioneArrivo = rowData[2].toString();
                    String dataPartenza = rowData[3].toString();
                    String dataArrivo = rowData[4].toString();
                    int distanza = Integer.parseInt(rowData[5].toString());
                    int durata = Integer.parseInt(rowData[6].toString());

                    TrattaStandard tratta2 = new TrattaStandard(
                            trattaID2, stazionePartenza, stazioneArrivo,
                            dataPartenza, dataArrivo, distanza, durata
                    );
                    trtdb.setTratta(tratta2);
                    break;
                case "Promozione" :
                    PromozioneImpl prdb = PromozioneImplDB.getInstance();
                    String promozioneID = rowData[0].toString();
                    String trenoID3 = rowData[1].toString();
                    String trattaID3 = rowData[2].toString();
                    String dataPartenza3 = rowData[3].toString();
                    String dataFine = rowData[4].toString();
                    boolean clientiFedelta = Boolean.parseBoolean(rowData[5].toString());
                    int prezzoPartenza = Integer.parseInt(rowData[6].toString());
                    double scontistica = Double.parseDouble(rowData[7].toString());

                    System.out.println("== Dati Promozione Selezionata ==");
                    System.out.println("ID Promozione: " + promozioneID);
                    System.out.println("ID Treno: " + trenoID3);
                    System.out.println("ID Tratta: " + trattaID3);
                    System.out.println("Data Partenza: " + dataPartenza3);
                    System.out.println("Data Fine: " + dataFine);
                    System.out.println("Clienti Fedeltà: " + clientiFedelta);
                    System.out.println("Prezzo Partenza: " + prezzoPartenza);
                    System.out.println("Scontistica: " + scontistica);
                    System.out.println("==================================");

                    // Recupero oggetti da DB (assumendo i metodi già esistenti)
                    Treno treno3 = TrenoImplDB.getInstance().getTreno(trenoID3);
                    TrattaPrototype tratta3 = TrattaImplDB.getInstance().getTratta(trattaID3);
                    if (rowData.length < 8) {
                        throw new IllegalArgumentException("Riga non valida, servono almeno 8 colonne");
                    } else {
                        // Costruzione dell'oggetto promozione con il Builder
                        Promozione pr = new Promozione.PromozioneBuilder()
                                .setPromozioneID(promozioneID)
                                .setTreno(treno3)
                                .setTratta(tratta3)
                                .setDataPartenza(dataPartenza3)
                                .setDataFine(dataFine)
                                .setClientiFedelta(clientiFedelta)
                                .setPrezzoPartenza(prezzoPartenza)
                                .setScontistica(scontistica)
                                .build();
                        prdb.setPromozione(pr);
                    }
                    break;
                case "Biglietto" :
                    String bigliettoID = rowData[0].toString();
                    System.out.println("Biglietto ID: " + bigliettoID);

                    String classe = rowData[1].toString();
                    System.out.println("Classe: " + classe);

                    String cfCliente = rowData[5].toString();
                    System.out.println("Codice Fiscale Cliente: " + cfCliente);

                    String trenoID4 = rowData[2].toString();
                    System.out.println("Treno ID: " + trenoID4);

                    String carrozza = rowData[3].toString();
                    System.out.println("Carrozza: " + carrozza);

                    String posto = rowData[4].toString();
                    System.out.println("Posto: " + posto);

                    String prioritaStr = rowData[6].toString(); // es: "Finestrino,Silenzio"
                    System.out.println("Priorità (stringa): " + prioritaStr);

                    int prezzo4 = Integer.parseInt(rowData[7].toString());
                    System.out.println("Prezzo: " + prezzo4);

                    System.out.println("== Riepilogo Biglietto ==");
                    System.out.println("ID: " + bigliettoID + " | CF Cliente: " + cfCliente + " | Treno: " + trenoID4);
                    System.out.println("Carrozza: " + carrozza + " | Posto: " + posto);
                    System.out.println("Priorità: " + prioritaStr + " | Prezzo: " + prezzo4);




                    Cliente cliente4 = ClienteImplDB.getInstance().getCliente(cfCliente);
                    Treno treno4 = TrenoImplDB.getInstance().getTreno(trenoID4);
                    List<String> priorita = Arrays.asList(prioritaStr.split(","));

                    System.out.println("== Dati Biglietto Selezionato ==");
                    System.out.println("ID Biglietto: " + bigliettoID);
                    System.out.println("Codice Fiscale Cliente: " + cfCliente);
                    System.out.println("ID Treno: " + trenoID4);
                    System.out.println("Carrozza: " + carrozza);
                    System.out.println("Posto: " + posto);
                    System.out.println("Priorità: " + prioritaStr);
                    System.out.println("Prezzo: " + prezzo4);
                    System.out.println("-- Oggetti Recuperati --");
                    System.out.println("Cliente: " + (cliente4 != null ? cliente4.toString() : "NON TROVATO"));
                    System.out.println("Treno: " + (treno4 != null ? treno4.toString() : "NON TROVATO"));

                    BigliettoImpl bdb = BigliettoDB.getInstance();

                    Biglietto b = new BSecondaClasse.Builder()
                            .bigliettoID(bigliettoID)
                            .titolareBiglietto(cliente4)
                            .trenoBiglietto(treno4)
                            .carrozza(carrozza)
                            .posto(posto)
                            .priorità(priorita)
                            .prezzo(prezzo4)
                            .implementazione(BigliettoDB.getInstance())
                            .build();
                    bdb.setBiglietto(b);
                    break;
            }
            }
        });
        refreshButton.addActionListener(e -> {
            DefaultTableModel nuovoModel = new DefaultTableModel();
            caricaDatiDaDB(tabellaNome, nuovoModel);
            table.setModel(nuovoModel);
        });

        filtri.addActionListener(e -> {
            switch (tabellaNome) {
                case "Cliente":
                    String[] opzioniC = {"codiceFiscale","nome", "cognome","codiceCliente","eta","email"};
                    filtra(tabellaNome, opzioniC, table);
                    break;
                case "Treno":
                    String[] opzioniT = {"trenoID", "tipoTreno", "trattaID", "prezzo", "postiPrima", "postiSeconda", "postiTerza", "postiTot", "tempoPercorrenza"};
                    filtra(tabellaNome, opzioniT, table);
                    break;
                case "Biglietto":
                    String[] opzioniB = {"id","classe", "treno_id", "carrozza", "posto", "cliente_id", "prezzo"};
                    filtra(tabellaNome, opzioniB, table);
                    break;
                case "Promozione":
                    String[] opzioniP = {"trenoID", "trattaID", "dataPartenza", "dataFine", "clientiFedelta", "prezzoPartenza", "scontistica"};
                    filtra(tabellaNome, opzioniP, table);
                    break;
                case "Tratta":
                    String[] opzioniTr = {"trattaID", "stazione_partenza", "stazione_arrivo", "data_partenza", "data_arrivo", "distanza", "durata_viaggio"};
                    filtra(tabellaNome, opzioniTr, table);
                    break;
            }
        });

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }
    private void filtra(String tabella, String[] opzioni, JTable table) {
        JComboBox<String> comboBox = new JComboBox<>(opzioni);

        JPanel pannello = new JPanel();
        pannello.setLayout(new BoxLayout(pannello, BoxLayout.Y_AXIS));
        pannello.setPreferredSize(new Dimension(400, 120));

        JPanel rigaCombo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rigaCombo.add(new JLabel("Scegli una colonna:"));
        rigaCombo.add(comboBox);

        JPanel rigaFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField field = new JTextField(15);
        JLabel filtroLabel = new JLabel("Filtro:");
        rigaFiltro.add(filtroLabel);
        rigaFiltro.add(field);

        pannello.add(rigaCombo);
        pannello.add(rigaFiltro);

        int result = JOptionPane.showConfirmDialog(
                null, pannello, "Filtro",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String colonna = (String) comboBox.getSelectedItem();
            String valore = field.getText().trim();

            if (valore.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Inserisci un valore valido per il filtro.");
                return;
            }

            caricaDatiDaDB2(tabella, colonna, valore, table);
        }
    }
    public void caricaDatiDaDB(String tabella, DefaultTableModel model) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + tabella)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int colCount = metaData.getColumnCount();

            for (int i = 1; i <= colCount; i++) {
                model.addColumn(metaData.getColumnName(i));
            }

            while (rs.next()) {
                Object[] rowData = new Object[colCount];
                for (int i = 1; i <= colCount; i++) {
                    rowData[i - 1] = rs.getObject(i);
                }
                model.addRow(rowData);
            }

        } catch (SQLException e) {
            System.err.println("Errore caricamento dati da tabella " + tabella + ": " + e.getMessage());
        }
    }
    private void caricaDatiDaDB2(String tabella, String colonna, String valore, JTable table) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM " + tabella + " WHERE " + colonna + " = ?")) {

            stmt.setString(1, valore);

            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int colCount = metaData.getColumnCount();

                Vector<String> columnNames = new Vector<>();
                for (int i = 1; i <= colCount; i++) {
                    columnNames.add(metaData.getColumnName(i));
                }

                Vector<Vector<Object>> data = new Vector<>();
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    for (int i = 1; i <= colCount; i++) {
                        row.add(rs.getObject(i));
                    }
                    data.add(row);
                }

                DefaultTableModel newModel = new DefaultTableModel(data, columnNames);
                table.setModel(newModel);
                SwingUtilities.invokeLater(() -> {
                    table.revalidate();
                    table.repaint();
                });

            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Errore nel caricamento dati dalla tabella " + tabella + ":\n" + e.getMessage(),
                    "Errore SQL", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void rimuoviDaDB(String tabella, Object id) {

        switch (tabella) {
            case "Cliente":
                ClienteImpl clientedb = ClienteImplDB.getInstance();
                clientedb.removeCliente(id.toString());
                break;
            case "Treno":
                TrenoImpl trenodb = TrenoImplDB.getInstance();
                trenodb.removeTreno(id.toString());
                break;
            case "Biglietto":
                BigliettoImpl bigliettodb = BigliettoDB.getInstance();
                bigliettodb.removeBiglietto(id.toString());
                break;
            case "Tratta" :
                TrattaImpl tratta = TrattaImplDB.getInstance();
                tratta.removeTratta(id.toString());
                break;
            case "Promozione":
                PromozioneImpl promozione = PromozioneImplDB.getInstance();
                promozione.removePromozione(id.toString());
                break;


        }
    }
    private void apriFinestraAdd(String tabella, int campi) {

        JFrame frame = new JFrame("Add new entry to " + tabella);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(0, 2));
        JTextField[] fields = new JTextField[campi];
        assembla(inputPanel,fields, tabella); //switch

        JButton submit = new JButton("Submit");
        submit.addActionListener(e -> {
            eseguiAggiunta(tabella,fields);
            frame.dispose();
        });

        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(submit, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
    private void eseguiAggiunta(String tabellaNome, JTextField[] fields) {
        switch(tabellaNome) {
            case "Cliente":
                ClienteImpl clientedb = ClienteImplDB.getInstance();
                clientedb.setCliente(creaCliente(tabellaNome, fields));
                break;
            case "Treno":
                TrenoImpl trenodb = TrenoImplDB.getInstance();
                trenodb.setTreno(creaTreno(tabellaNome, fields));
                break;
            case "Biglietto":
                BigliettoImpl bigliettodb = BigliettoDB.getInstance();
                bigliettodb.setBiglietto(creaBiglietto(tabellaNome,fields,bigliettodb));
                break;
            case "Promozione" :
                PromozioneImpl promozionedb = PromozioneImplDB.getInstance();
               promozionedb.setPromozione(creaPromozione(tabellaNome,fields));
                break;
            case "Tratta" :
                TrattaImpl trattadb = TrattaImplDB.getInstance();
                trattadb.setTratta(creaTratta(tabellaNome,fields));
                break;

        }
    }
    private JTextField[] assembla(JPanel inputPanel,JTextField[] fields, String tabella) {
        switch(tabella) {
            case "Cliente":
                inputPanel.add(new JLabel("codiceFiscale:"));
                fields[0] = new JTextField();
                inputPanel.add(fields[0]);
                inputPanel.add(new JLabel("nome:"));
                fields[1] = new JTextField();
                inputPanel.add(fields[1]);
                inputPanel.add(new JLabel("cognome:"));
                fields[2] = new JTextField();
                inputPanel.add(fields[2]);
                inputPanel.add(new JLabel("codiceCliente:"));
                fields[3] = new JTextField();
                inputPanel.add(fields[3]);
                inputPanel.add(new JLabel("età:"));
                fields[4] = new JTextField();
                inputPanel.add(fields[4]);
                inputPanel.add(new JLabel("email:"));
                fields[5] = new JTextField();
                inputPanel.add(fields[5]);
                break;
            case "Treno":
                inputPanel.add(new JLabel("trenoID:"));
                fields[0] = new JTextField();
                inputPanel.add(fields[0]);
                inputPanel.add(new JLabel("tipoTreno:"));
                fields[1] = new JTextField();
                inputPanel.add(fields[1]);
                inputPanel.add(new JLabel("TrattaID:"));
                fields[2] = new JTextField();
                inputPanel.add(fields[2]);
                inputPanel.add(new JLabel("prezzo:"));
                fields[3] = new JTextField();
                inputPanel.add(fields[3]);
                inputPanel.add(new JLabel("postiPrima:"));
                fields[4] = new JTextField();
                inputPanel.add(fields[4]);
                inputPanel.add(new JLabel("postiSeconda:"));
                fields[5] = new JTextField();
                inputPanel.add(fields[5]);
                inputPanel.add(new JLabel("postiTerza :"));
                fields[6] = new JTextField();
                inputPanel.add(fields[6]);
                inputPanel.add(new JLabel("prezzoTot:"));
                fields[7] = new JTextField();
                inputPanel.add(fields[7]);
                inputPanel.add(new JLabel(" tempoPercorrenza:"));
                fields[8] = new JTextField();
                inputPanel.add(fields[8]);
                inputPanel.add(new JLabel(" binario :"));
                fields[9] = new JTextField();
                inputPanel.add(fields[9]);
                inputPanel.add(new JLabel("promozione:"));
                fields[10] = new JTextField();
                inputPanel.add(fields[10]);

                break;
            case "Biglietto":
                inputPanel.add(new JLabel("bigliettoID:"));
                fields[0] = new JTextField();
                inputPanel.add(fields[0]);
                inputPanel.add(new JLabel("clienteID:"));
                fields[1] = new JTextField();
                inputPanel.add(fields[1]);
                inputPanel.add(new JLabel("trenoID:"));
                fields[2] = new JTextField();
                inputPanel.add(fields[2]);
                inputPanel.add(new JLabel("carrozza:"));
                fields[3] = new JTextField();
                inputPanel.add(fields[3]);
                inputPanel.add(new JLabel("posto:"));
                fields[4] = new JTextField();
                inputPanel.add(fields[4]);
                inputPanel.add(new JLabel("classe:"));
                fields[5] = new JTextField();
                inputPanel.add(fields[5]);
                inputPanel.add(new JLabel("priorità:"));
                fields[6] = new JTextField();
                inputPanel.add(fields[6]);
                inputPanel.add(new JLabel("prezzo:"));
                fields[7] = new JTextField();
                inputPanel.add(fields[7]);
                break;
            case "Promozione":
                inputPanel.add(new JLabel("promozioneID:"));
                fields[0] = new JTextField();
                inputPanel.add(fields[0]);
                inputPanel.add(new JLabel("trenoID:"));
                fields[1] = new JTextField();
                inputPanel.add(fields[1]);
                inputPanel.add(new JLabel("trattaID:"));
                fields[2] = new JTextField();
                inputPanel.add(fields[2]);
                inputPanel.add(new JLabel("dataPartenza:"));
                fields[3] = new JTextField();
                inputPanel.add(fields[3]);
                inputPanel.add(new JLabel("dataFine:"));
                fields[4] = new JTextField();
                inputPanel.add(fields[4]);
                inputPanel.add(new JLabel("perezzoPartenza:"));
                fields[5] = new JTextField();
                inputPanel.add(fields[5]);
                inputPanel.add(new JLabel("scontistica :"));
                fields[6] = new JTextField();
                inputPanel.add(fields[6]);
                break;
            case "Tratta":
                inputPanel.add(new JLabel("trattaID:"));
                fields[0] = new JTextField();
                inputPanel.add(fields[0]);
                inputPanel.add(new JLabel("stazione_Partenza:"));
                fields[1] = new JTextField();
                inputPanel.add(fields[1]);
                inputPanel.add(new JLabel("stazione_arrivo:"));
                fields[2] = new JTextField();
                inputPanel.add(fields[2]);
                inputPanel.add(new JLabel("data_partenza:"));
                fields[3] = new JTextField();
                inputPanel.add(fields[3]);
                inputPanel.add(new JLabel("data_arrivo:"));
                fields[4] = new JTextField();
                inputPanel.add(fields[4]);
                inputPanel.add(new JLabel("distanza:"));
                fields[5] = new JTextField();
                inputPanel.add(fields[5]);
                inputPanel.add(new JLabel("durata_viaggio :"));
                fields[6] = new JTextField();
                inputPanel.add(fields[6]);
                break;
        }
        return fields;
    }
    private Cliente creaCliente(String tabellaNome, JTextField[] fields) {
        if(tabellaNome.equals("Cliente")) {

                ClienteImpl clientedb = ClienteImplDB.getInstance();
                Cliente cl = new ClienteConcr(
                        fields[0].getText(),
                        fields[1].getText(),
                        fields[2].getText(),
                        fields[3].getText(),
                        Integer.parseInt(fields[4].getText()),
                        fields[5].getText());
                clientedb.setCliente(cl);
                return cl;
             }
        return null;
        }
    private Treno creaTreno(String tabellaNome, JTextField[] fields) {
            TrattaImpl trdb = TrattaImplDB.getInstance();
          if(tabellaNome.equals("Treno")) {

              Treno tr = new TrenoConcr(
                      fields[0].getText(),
                      fields[1].getText(),
                      trdb.getTratta(fields[2].getText()),
                      Integer.parseInt(fields[3].getText()),
                      Integer.parseInt(fields[4].getText()),
                      Integer.parseInt(fields[5].getText()),
                      Integer.parseInt(fields[6].getText()),
                      Integer.parseInt(fields[7].getText()),
                      Integer.parseInt(fields[9].getText()),
                      fields[10].getText()
             );
             return tr;
        }
        return null;
    }
    private Biglietto creaBiglietto(String tabellaNome, JTextField[] fields, BigliettoImpl db) {
        if(tabellaNome.equals("Biglietto")) {
            Biglietto bPrimaClasse = new BPrimaClasse.Builder()
                    .bigliettoID(fields[0].getText())
                    .trenoBiglietto(TrenoFactory.getTrenoByID(fields[2].getText()))
                    .carrozza(fields[3].getText())
                    .posto(fields[4].getText())
                    .titolareBiglietto(ClienteFactory.getClienteByCodiceFiscale(fields[1].getText()))
                    .priorità(List.of(fields[6].getText().split(",")))
                    .prezzo(Integer.parseInt(fields[7].getText()))
                    .implementazione(db)
                    .build();
            return bPrimaClasse;
        }
        return null;
    }
    private TrattaPrototype creaTratta(String tabellaNome, JTextField[] fields) {
        if(tabellaNome.equals("Tratta")) {
            TrattaPrototype tratta = new TrattaStandard(
                    fields[0].getText(),
                    fields[1].getText(),
                    fields[2].getText(),
                    fields[3].getText(),
                    fields[4].getText(),
                    Integer.parseInt(fields[5].getText()),
                    Integer.parseInt(fields[6].getText())
            );
            return tratta;
        }
        return null;
    }
    private Promozione creaPromozione(String tabellaNome, JTextField[] fields) {
        if(tabellaNome.equals("Promozione")) {
            Promozione pr =  new Promozione.PromozioneBuilder()
                    .setPromozioneID(fields[0].getText())
                    .setTreno(TrenoFactory.getTrenoByID(fields[1].getText()))
                    .setTratta(TrattaFactory.getTrattaByID(fields[2].getText()))
                    .setDataPartenza(fields[3].getText())
                    .setDataFine(fields[4].getText())
                    .setPrezzoPartenza(Integer.parseInt(fields[5].getText()))
                    .setScontistica(Integer.parseInt(fields[6].getText()))
                    .build();
            return pr;
        }
        return null;
    }
    public static void main(String[] args) {


        SwingUtilities.invokeLater(() -> {
           System.setProperty("sun.java2d.uiScale", "3.0");
            new AdminDashboardSwing().setVisible(true);

        });
    }
}
