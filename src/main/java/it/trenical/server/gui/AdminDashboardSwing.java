package it.trenical.server.gui;
import it.trenical.server.Biglietto.*;
import it.trenical.server.Cliente.*;
import it.trenical.server.Tratta.TrattaPrototype;
import it.trenical.server.Tratta.TrattaStandard;
import it.trenical.server.Treno.*;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;


public class AdminDashboardSwing extends JFrame {

    private static final String DB_URL = "jdbc:sqlite:db/treniCal.db";

    public AdminDashboardSwing() {
        setTitle("Terminale Amministratore");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Clienti", creaTabella("Cliente"));
        tabs.addTab("Treni", creaTabella("Treno"));
        tabs.addTab("Biglietti", creaTabella("Biglietto"));


        add(tabs);
    }

    private JPanel creaTabella(String tabellaNome) {
        JPanel panel = new JPanel(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        caricaDatiDaDB(tabellaNome, model);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add "+tabellaNome);
        JButton removeButton = new JButton("Remove "+tabellaNome);
        JButton refreshButton = new JButton("Refresh "+tabellaNome);
        JButton filtri = new JButton("Filtri "+tabellaNome);

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(filtri);

        switch(tabellaNome) {
            case "Cliente":
                addButton.addActionListener(e -> apriFinestraAdd(tabellaNome,5));
                break;
            case "Treno":
                addButton.addActionListener(e -> apriFinestraAdd(tabellaNome,9));
                break;
            case "Biglietto":
                addButton.addActionListener(e -> apriFinestraAdd(tabellaNome,8));
                break;

                }

        removeButton.addActionListener(e -> {int row = table.getSelectedRow();
            if (row != -1) {
                Object id = table.getValueAt(row, 0);
                rimuoviDaDB(tabellaNome, id);
                ((DefaultTableModel) table.getModel()).removeRow(row);
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
                    String[] opzioniC = {"nome", "cognome", "eta"};
                    filtra(tabellaNome, opzioniC,model);
                    break;
                case "Treno":
                    String[] opzioniT = {"tipoTreno","trattaID","stazione_partenza","stazione_arrivo","durata_viaggio","distanza","data_partenza","data_arrivo"};
                    filtra(tabellaNome, opzioniT,model);
                    break;
                case "Biglietto":
                    String[] opzioniB = {"classe","treno_id","carrozza","posto","cliente_id","prezzo"};
                    filtra(tabellaNome, opzioniB,model);
                    break;
            }
        });
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }


    private void filtra(String tabella, String[] opzioni, DefaultTableModel model) {
        JComboBox<String> comboBox = new JComboBox<>(opzioni);

        JPanel pannello = new JPanel();
        pannello.setLayout(new BoxLayout(pannello, BoxLayout.Y_AXIS));
        pannello.setPreferredSize(new Dimension(400, 120));

        JPanel rigaCombo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rigaCombo.add(new JLabel("Scegli un'opzione:"));
        rigaCombo.add(comboBox);

        JPanel rigaFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField field = new JTextField(15);
        JLabel filtroLabel = new JLabel("Filtro:");
        rigaFiltro.add(filtroLabel);
        rigaFiltro.add(field);
        pannello.add(rigaCombo);
        pannello.add(rigaFiltro);

        int result = JOptionPane.showConfirmDialog(
                null, pannello, "Menu a tendina",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        System.out.println(result);
        System.out.println(JOptionPane.OK_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String colonna = (String) comboBox.getSelectedItem();
            System.out.println(colonna);
            String valore = field.getText();
            System.out.println(valore);

            //DefaultTableModel model = (DefaultTableModel) table.getModel();
           // model.setRowCount(0);

            caricaDatiDaDB2(tabella,model,colonna,valore);
//            switch (tabella) {
//                case "Cliente":
//                    DefaultTableModel nuovoModel = new DefaultTableModel();
//                    nuovoModel.setRowCount(0);
//                    ClienteImplDB clientedb = new ClienteImplDB();
//                    for (Cliente c : clientedb.getByFiltro(colonna, valore)) {
//                        nuovoModel.addRow(new Object[]{
//                                c.getCodiceFiscale(),
//                                c.getNome(),
//                                c.getCognome(),
//                                c.getCodiceCliente(),
//                                c.getEtà()
//                        });
//                        System.out.println(c.getEtà());
//
//                    }
//                break;
//                case "Treno":
//                    TrenoImplDB trenodb = new TrenoImplDB();
//                    for (Treno t : trenodb.getByFiltro(colonna, valore)) {
//                        TrattaPrototype tr = t.getTratta();
//                        model.addRow(new Object[]{
//                                t.getTrenoID(),
//                                t.getTipoTreno(),
//                                tr.getCodiceTratta(),
//                                tr.getStazionePartenza(),
//                                tr.getStazioneArrivo(),
//                                tr.getDistanza(),
//                                tr.getTempoPercorrenza(),
//                                tr.getDataPartenza(),
//                                tr.getDataArrivo()
//                        });
//                    }
//                    break;
//                case "Biglietto":
//                    BigliettoDB bigliettodb = new BigliettoDB();
//                    for (Biglietto b : bigliettodb.getByFiltro(colonna, valore)) {
//                        model.addRow(new Object[]{
//                                b.getBigliettoID(),
//                                b.getTitolareBiglietto().getCodiceFiscale(),
//                                b.getTrenoBiglietto().getTrenoID(),
//                                b.getCarrozza(),
//                                b.getPosto(),
//                                b.getClass().getSimpleName(),
//                                String.join(",", b.getPriorità()),
//                                b.getPrezzo()
//                        });
//                    }
//                    break;
//
//            }

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
    private void caricaDatiDaDB2(String tabella, DefaultTableModel model, String colonna, String valore) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + tabella + " WHERE " + colonna + " = ?")) {

            stmt.setString(1, valore);
            try (ResultSet rs = stmt.executeQuery()) {

                ResultSetMetaData metaData = rs.getMetaData();
                int colCount = metaData.getColumnCount();

                // Pulisce il modello esistente
                model.setRowCount(0);
                model.setColumnCount(0);

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
            }

        } catch (SQLException e) {
            System.err.println("Errore filtrando la tabella " + tabella + ": " + e.getMessage());
        }
    }

    private void rimuoviDaDB(String tabella, Object id) {
        System.out.println(id.toString());
        switch (tabella) {
            case "Cliente":
                ClienteImpl clientedb = new ClienteImplDB();
                clientedb.removeCliente(id.toString());
                break;
            case "Treno":
                TrenoImpl trenodb = new TrenoImplDB();
                trenodb.removeTreno(Integer.parseInt(id.toString()));
                break;
            case "Biglietto":
                BigliettoImpl bigliettodb = new BigliettoDB();
                bigliettodb.removeBiglietto(id.toString());
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
        assembla(inputPanel,fields,campi); //switch

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
                ClienteImpl clientedb = new ClienteImplDB();
                clientedb.setCliente(creaCliente(tabellaNome, fields));
                break;
            case "Treno":
                TrenoImpl trenodb = new TrenoImplDB();
                trenodb.setTreno(creaTreno(tabellaNome, fields));
                break;
            case "Biglietto":
                BigliettoImpl bigliettodb = new BigliettoDB();
                bigliettodb.setBiglietto(creaBiglietto(tabellaNome,fields,bigliettodb));
                break;

        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AdminDashboardSwing().setVisible(true);
        });
    }

    private JTextField[] assembla(JPanel inputPanel,JTextField[] fields, int campi) {
        switch(campi) {
            case 5:

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
                break;
            case 9:
                inputPanel.add(new JLabel("trenoID:"));
                fields[0] = new JTextField();
                inputPanel.add(fields[0]);
                inputPanel.add(new JLabel("tipoTreno:"));
                fields[1] = new JTextField();
                inputPanel.add(fields[1]);
                inputPanel.add(new JLabel("TrattaID:"));
                fields[2] = new JTextField();
                inputPanel.add(fields[2]);
                inputPanel.add(new JLabel("stazionePartenza:"));
                fields[3] = new JTextField();
                inputPanel.add(fields[3]);
                inputPanel.add(new JLabel("stazioneArrivo:"));
                fields[4] = new JTextField();
                inputPanel.add(fields[4]);
                inputPanel.add(new JLabel("durataViaggio:"));
                fields[5] = new JTextField();
                inputPanel.add(fields[5]);
                inputPanel.add(new JLabel("distanza:"));
                fields[6] = new JTextField();
                inputPanel.add(fields[6]);
                inputPanel.add(new JLabel("dataPartenza:"));
                fields[7] = new JTextField();
                inputPanel.add(fields[7]);
                inputPanel.add(new JLabel("dataArrivo:"));
                fields[8] = new JTextField();
                inputPanel.add(fields[8]);
                break;
            case 8:
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

        }
        return fields;
    }
    private Cliente creaCliente(String tabellaNome, JTextField[] fields) {
        if(tabellaNome.equals("Cliente")) {

                ClienteImpl clientedb = new ClienteImplDB();
                Cliente cl = new ClienteConcr(
                        fields[0].getText(),
                        fields[1].getText(),
                        fields[2].getText(),
                        fields[3].getText(),
                        Integer.parseInt(fields[4].getText()));
                clientedb.setCliente(cl);
                return cl;
             }
        return null;
        }
    private Treno creaTreno(String tabellaNome, JTextField[] fields) {

        if(tabellaNome.equals("Treno")) {
            Treno tr = new TrenoConcr(
                    Integer.parseInt(fields[0].getText()),
                    fields[1].getText(),
                    new TrattaStandard(
                            fields[2].getText(),
                            fields[3].getText(),
                            fields[4].getText(),
                            fields[7].getText(),
                            fields[8].getText(),
                            Integer.parseInt(fields[5].getText()),
                            Integer.parseInt(fields[6].getText())
                    )
            );
            return tr;
        }
        return null;
    }
    private Biglietto creaBiglietto(String tabellaNome, JTextField[] fields, BigliettoImpl db) {
        if(tabellaNome.equals("Biglietto")) {
            BPrimaClasse bPrimaClasse = new BPrimaClasse.Builder()
                    .bigliettoID(fields[0].getText()) // id
                    //.classe(fields[1].getText()) // es. "SecondaClasse", sarà gestito da .getClass().getSimpleName()
                    .trenoBiglietto(TrenoFactory.getTrenoByID(fields[2].getText())) // treno_id
                    .carrozza(fields[3].getText()) // carrozza
                    .posto(fields[4].getText()) // posto
                    .titolareBiglietto(ClienteFactory.getClienteByCodiceFiscale(fields[1].getText())) // cliente_id
                    .priorità(List.of(fields[6].getText().split(","))) // priorità (CSV → List<String>)
                    .prezzo(Integer.parseInt(fields[7].getText())) // prezzo
                    .implementazione(db)
                    .build();
            return bPrimaClasse;
        }
        return null;
    }
}
