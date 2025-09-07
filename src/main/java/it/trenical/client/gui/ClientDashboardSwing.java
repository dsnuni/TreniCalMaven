package it.trenical.client.gui;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import it.trenical.client.pagamento.Pagamento;
import it.trenical.grpc.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import it.trenical.grpc.Biglietto;

public class ClientDashboardSwing extends JFrame {
    private static boolean registrato=false;
    private  static Cliente cliente=null;
    private static Biglietto biglietto=null;

    public ClientDashboardSwing() {
        setTitle("Terminale Utente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Biglietti", creaTabella("Biglietto",tabs));
        tabs.addTab("Tratte", creaTabella("Tratta", tabs));
        //tabs.addTab("Info", creaTabella("Info", tabs));

        //tabs.addTab("Treni", creaTabella("Treno"));
        //tabs.addTab("Notifiche",new JPanel(new GridLayout(1, 2)));


        add(tabs);
    }

    JPanel creaTabella(String tabellaNome, JTabbedPane tabs) {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        switch (tabellaNome) {

            case "Tratta":
                JPanel buttonPanel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JButton buyButton2 = new JButton("Acquista Treno");
                JButton filtroButton = new JButton("Filtra Tratte");
                buttonPanel3.add(buyButton2);
                buttonPanel3.add(filtroButton);
                panel.add(buttonPanel3, BorderLayout.NORTH);

                TrattaServiceGrpc.TrattaServiceBlockingStub trattaStub = TrattaServiceGrpc.newBlockingStub(channel);
                GetAllTratteRequest request = GetAllTratteRequest.newBuilder().build();
                GetAllTratteResponse response = trattaStub.getAllTratte(request);
                model.setColumnIdentifiers(new String[]{
                        "ID", "stazione_Partenza", "stazione_Arrivo", "data_Partenza", "data_Arrivo", "distanza", "durata_media"
                });
                for (TrattaStandard tratta : response.getTrattaList()) {
                    model.addRow(new Object[]{
                            tratta.getCodiceTratta(),
                            tratta.getStazionePartenza(),
                            tratta.getStazioneArrivo(),
                            tratta.getDataPartenza(),
                            tratta.getDataArrivo(),
                            tratta.getDistanza(),
                            tratta.getTempoPercorrenza()
                    });
                }

                filtroButton.addActionListener(e -> {
                    JDialog filtroDialog = new JDialog((Frame) null, "Filtro Tratte", true);
                    filtroDialog.setSize(500, 350);
                    filtroDialog.setLocationRelativeTo(null);
                    filtroDialog.setLayout(new BorderLayout());

                    JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
                    formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                    JTextField codiceTrattaField = new JTextField();
                    JTextField cittaPartenzaField = new JTextField();
                    JTextField cittaArrivoField = new JTextField();
                    JTextField dataPartenzaField = new JTextField();
                    JTextField dataArrivoField = new JTextField();

                    formPanel.add(new JLabel("Codice Tratta:"));
                    formPanel.add(codiceTrattaField);
                    formPanel.add(new JLabel("Città Partenza:"));
                    formPanel.add(cittaPartenzaField);
                    formPanel.add(new JLabel("Città Arrivo:"));
                    formPanel.add(cittaArrivoField);
                    formPanel.add(new JLabel("Giorno Partenza (DD-MM-YYYY):"));
                    formPanel.add(dataPartenzaField);
                    formPanel.add(new JLabel("Giorno Arrivo (DD-MM-YYYY):"));
                    formPanel.add(dataArrivoField);

                    JPanel buttonFilterPanel = new JPanel(new FlowLayout());
                    JButton applicaFiltroButton = new JButton("Applica Filtro");
                    JButton resetFiltroButton = new JButton("Reset");
                    buttonFilterPanel.add(applicaFiltroButton);
                    buttonFilterPanel.add(resetFiltroButton);

                    filtroDialog.add(formPanel, BorderLayout.CENTER);
                    filtroDialog.add(buttonFilterPanel, BorderLayout.SOUTH);

                    applicaFiltroButton.addActionListener(filterEvent -> {
                        DefaultTableModel filteredModel = new DefaultTableModel();
                        filteredModel.setColumnIdentifiers(new String[]{
                                "ID", "stazione_Partenza", "stazione_Arrivo", "data_Partenza", "data_Arrivo", "distanza", "durata_media"
                        });

                        String codiceTratta = codiceTrattaField.getText().trim();
                        String cittaPartenza = cittaPartenzaField.getText().trim();
                        String cittaArrivo = cittaArrivoField.getText().trim();
                        String dataPartenza = dataPartenzaField.getText().trim();
                        String dataArrivo = dataArrivoField.getText().trim();

                        for (TrattaStandard tratta : response.getTrattaList()) {
                            boolean match = true;

                            if (!codiceTratta.isEmpty() && !tratta.getCodiceTratta().toLowerCase().contains(codiceTratta.toLowerCase())) {
                                match = false;
                            }
                            if (!cittaPartenza.isEmpty() && !tratta.getStazionePartenza().toLowerCase().contains(cittaPartenza.toLowerCase())) {
                                match = false;
                            }
                            if (!cittaArrivo.isEmpty() && !tratta.getStazioneArrivo().toLowerCase().contains(cittaArrivo.toLowerCase())) {
                                match = false;
                            }
                            if (!dataPartenza.isEmpty()) {
                                String giornoPartenza = tratta.getDataPartenza().split(" ")[0];
                                if (!giornoPartenza.equals(dataPartenza)) {
                                    match = false;
                                }
                            }
                            if (!dataArrivo.isEmpty()) {
                                String giornoArrivo = tratta.getDataArrivo().split(" ")[0];
                                if (!giornoArrivo.equals(dataArrivo)) {
                                    match = false;
                                }
                            }

                            if (match) {
                                filteredModel.addRow(new Object[]{
                                        tratta.getCodiceTratta(),
                                        tratta.getStazionePartenza(),
                                        tratta.getStazioneArrivo(),
                                        tratta.getDataPartenza(),
                                        tratta.getDataArrivo(),
                                        tratta.getDistanza(),
                                        tratta.getTempoPercorrenza()
                                });
                            }
                        }

                        table.setModel(filteredModel);
                        filtroDialog.dispose();
                    });

                    resetFiltroButton.addActionListener(resetEvent -> {
                        DefaultTableModel originalModel = new DefaultTableModel();
                        originalModel.setColumnIdentifiers(new String[]{
                                "ID", "stazione_Partenza", "stazione_Arrivo", "data_Partenza", "data_Arrivo", "distanza", "durata_media"
                        });
                        for (TrattaStandard tratta : response.getTrattaList()) {
                            originalModel.addRow(new Object[]{
                                    tratta.getCodiceTratta(),
                                    tratta.getStazionePartenza(),
                                    tratta.getStazioneArrivo(),
                                    tratta.getDataPartenza(),
                                    tratta.getDataArrivo(),
                                    tratta.getDistanza(),
                                    tratta.getTempoPercorrenza()
                            });
                        }
                        table.setModel(originalModel);
                        filtroDialog.dispose();
                    });

                    filtroDialog.setVisible(true);
                });

                buyButton2.addActionListener(e -> {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        Object id = table.getValueAt(row, 0);
                        System.out.println("Primo campo della riga selezionata: " + id.toString());
                        acquistaBiglietto(id, channel);
                    }
                });
                panel.add(scrollPane, BorderLayout.CENTER);
                break;

            case "Biglietto":
                // Layout a divisione cliente e bigltti
                JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
                splitPane.setResizeWeight(0.5);
                splitPane.setDividerLocation(0.5);
                splitPane.setOneTouchExpandable(true);
                JPanel buttonPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JButton refreshButton = new JButton("Refresh "+tabellaNome);
                JButton modificaBigliettoButton = new JButton("Modifica "+tabellaNome);
                buttonPanel2.add(modificaBigliettoButton);
                buttonPanel2.add(refreshButton);

                Color sfondoPrincipale = panel.getBackground();

                Color sfondoPannelli = new Color(255, 255, 255);
                Color sfondoScuro = new Color(255, 255, 255);

                // Pannello Cliente
                JPanel clientePanel = new JPanel();
                clientePanel.setLayout(new GridLayout(5, 2, 5, 5));
                clientePanel.setBorder(BorderFactory.createTitledBorder("Dati Cliente"));
                clientePanel.setBackground(sfondoScuro);
                clientePanel.setOpaque(false);
                clientePanel.add(ingresso(clientePanel,channel,tabs));

                // Pannello Biglietti
                JPanel bigliettiPanel = new JPanel(new BorderLayout());
                bigliettiPanel.setBorder(BorderFactory.createTitledBorder("Biglietti del Cliente"));
                bigliettiPanel.setBackground(sfondoScuro);
                bigliettiPanel.setOpaque(false);

                JTable bigliettiTable = new JTable(new DefaultTableModel(
                        new Object[]{"ID", "Classe", "Treno", "Carrozza", "Posto", "Prezzo", "Partenza", "Arrivo"}, 0
                ));
                JScrollPane bigliettiScroll = new JScrollPane(bigliettiTable);
                bigliettiPanel.add(bigliettiScroll, BorderLayout.CENTER);
                if(registrato) {
                    caricaDatiDaDB(model,channel);
                }
                refreshButton.addActionListener(e -> {
                DefaultTableModel nuovoModel = new DefaultTableModel(
                    //    new Object[]{"Biglietto_id","Classe","treno_id", "Carrozza", "Posto","priorità", "Prezzo", "Partenza", "Arrivo"}, 0
                        new Object[]{"ID", "Classe", "Treno", "Carrozza", "Posto", "Prezzo", "Partenza", "Arrivo"}, 0
                );
                caricaDatiDaDB(nuovoModel, channel);
                bigliettiTable.setModel(nuovoModel);
            });

                modificaBigliettoButton.addActionListener(e -> {
                    int row = bigliettiTable.getSelectedRow();
                    System.out.println("Modifica " + row);

                    if (row == -1) {
                        JOptionPane.showMessageDialog(null, "Seleziona una riga prima di modificare.");
                        return;
                    }
                    String bigliettoID = bigliettiTable.getValueAt(row, 0).toString();
                    System.out.println("Modifica UUU " + bigliettoID);
                    BigliettoServiceGrpc.BigliettoServiceBlockingStub stub = BigliettoServiceGrpc.newBlockingStub(channel);
                    GetBigliettoRequest request2 = GetBigliettoRequest.newBuilder()
                            .setBigliettoID(bigliettoID)
                            .build();

                    Biglietto response2 = stub.getBiglietto(request2);
                    biglietto = response2;
                    System.out.println("Classe: " + biglietto.toString());
                    JPanel panel3 = creaTabella("Tratta", new JTabbedPane());
                    JDialog dialog2 = new JDialog((Frame) null, "Seleziona nuova tratta", true);
                    dialog2.setSize(800, 400);
                    dialog2.setLocationRelativeTo(null);
                    dialog2.setContentPane(panel3);
                    dialog2.setVisible(true);
                });

                splitPane.setLeftComponent(clientePanel);
                splitPane.setRightComponent(bigliettiPanel);

                panel.setLayout(new BorderLayout());
                panel.setBackground(sfondoPannelli);
                panel.setOpaque(true);
                panel.add(splitPane, BorderLayout.CENTER);

                bigliettiPanel.add(buttonPanel2, BorderLayout.NORTH);
                break;

        }
        return panel;
    }
    private static void caricaDatiDaDB(DefaultTableModel model,  ManagedChannel channel) {

        BigliettoServiceGrpc.BigliettoServiceBlockingStub bigliettoStub = BigliettoServiceGrpc.newBlockingStub(channel);
        GetBigliettiByFiltroRequest request = GetBigliettiByFiltroRequest.newBuilder()
                .setColonna("cliente_id")
                .setValore(cliente.getCodiceFiscale())
                .build();
        GetBigliettiByFiltroResponse response = bigliettoStub.getBigliettiByFiltro(request);
        List<Biglietto> biglietti = response.getBigliettiList();

        model.setRowCount(0);

        for (it.trenical.grpc.Biglietto b : biglietti) {
            System.out.println(b);
            model.addRow(new Object[]{
                    b.getBigliettoID(),
                    b.getClasse(),
                    b.getTrenoID(),
                    b.getCarrozza(),
                    b.getPosto(),
                    b.getPrioritaList(),
                    b.getPrezzo()
                    //ottieniData(b,"Partenza"),
                   // ottieniData(b,"Arrivo")
            });
        }


    }
    private static JPanel ingresso(JPanel panel, ManagedChannel channel,JTabbedPane tabs) {
        JPanel clientePanel  = new JPanel();
        clientePanel.removeAll();
        clientePanel.setLayout(new GridLayout(7, 2, 5, 5));
        clientePanel.setMaximumSize(new Dimension(400, 220));
        //clientePanel.setBackground(new Color(180, 180, 180));
        JButton accessButton = new JButton("Accedi");
        JButton registratoButton = new JButton("Registrati");
        clientePanel.add(new JLabel());
        clientePanel.add(registratoButton);
        clientePanel.add(accessButton);
        clientePanel.revalidate();

        accessButton.addActionListener(e -> {
            registrato = true;
            panel.removeAll();
            panel.add(pannelloAccesso(tabs, channel));
            panel.revalidate();
            panel.repaint();
        });
        registratoButton.addActionListener(e -> {
            panel.removeAll();
            panel.add(pannelloCLiente(panel, channel,tabs));
            panel.revalidate();
            panel.repaint();
        });
        return clientePanel;
    }
    private static JPanel pannelloAccesso(JTabbedPane tabs, ManagedChannel channel) {
        JPanel clientePanel  = new JPanel();
        clientePanel.removeAll();
        clientePanel.setLayout(new GridLayout(7, 2, 5, 5));
        clientePanel.setMaximumSize(new Dimension(400, 220));
        //clientePanel.setBackground(new Color(255, 255, 255));

        clientePanel.add(new JLabel("Codice Fiscale:"));
        JTextField codiceFiscaleField = new JTextField(20);
        clientePanel.add(codiceFiscaleField);

        JButton submitButton = new JButton("Accedi");
        clientePanel.add(new JLabel());
        clientePanel.add(submitButton);
        clientePanel.revalidate();

        submitButton.addActionListener(e -> {
            String cf = codiceFiscaleField.getText();

            ClienteServiceGrpc.ClienteServiceBlockingStub clienteStub = ClienteServiceGrpc.newBlockingStub(channel);
            GetClienteRequest request = GetClienteRequest.newBuilder()
                    .setCodiceFiscale(cf).build();
            it.trenical.grpc.Cliente clienteGrpc = clienteStub.getCliente(request);
            cliente = clienteGrpc;
            System.out.println("Il cliente che ha appena fatto l'accesso "+cliente);

            clientePanel.removeAll();
            clientePanel.setLayout(new GridLayout(6, 2, 5, 5));
            clientePanel.setMaximumSize(new Dimension(400, 220));
           // clientePanel.setBackground(new Color(180, 180, 180));


            clientePanel.add(new JLabel("Codice Fiscale:"));
            clientePanel.add(new JLabel(cliente.getCodiceFiscale()));

            clientePanel.add(new JLabel("Nome:"));
            clientePanel.add(new JLabel(cliente.getNome()));

            clientePanel.add(new JLabel("Cognome:"));
            clientePanel.add(new JLabel(cliente.getCognome()));

            clientePanel.add(new JLabel("Codice Cliente:"));
            clientePanel.add(new JLabel(cliente.getCodiceCliente()));

            clientePanel.add(new JLabel("Età:"));
            clientePanel.add(new JLabel(String.valueOf(cliente.getEta())));

            clientePanel.add(new JLabel("Email:"));
            clientePanel.add(new JLabel(String.valueOf(cliente.getEmail())));
            finestraInfo(channel, tabs);
            creaNotifica(tabs, channel);
        });
        return clientePanel;
    }
    private static JPanel pannelloCLiente(JPanel panel, ManagedChannel channel,JTabbedPane tabs) {
    JPanel clientePanel  = new JPanel();
    clientePanel.removeAll();
    clientePanel.setLayout(new GridLayout(7, 2, 5, 5));
    clientePanel.setMaximumSize(new Dimension(400, 220));
    //clientePanel.setBackground(new Color(180, 180, 180));

    if (!registrato) {
        clientePanel.add(new JLabel("Codice Fiscale:"));
        JTextField codiceFiscaleField = new JTextField(20);
        clientePanel.add(codiceFiscaleField);

        clientePanel.add(new JLabel("Nome:"));
        JTextField nomeField = new JTextField(20);
        clientePanel.add(nomeField);

        clientePanel.add(new JLabel("Cognome:"));
        JTextField cognomeField = new JTextField(20);
        clientePanel.add(cognomeField);

        clientePanel.add(new JLabel("Età:"));
        JTextField etaField = new JTextField(20);
        clientePanel.add(etaField);

        clientePanel.add(new JLabel("email:"));
        JTextField emailField = new JTextField(20);
        clientePanel.add(emailField);

        clientePanel.add(new JLabel("Cliente fedeltà:"));
        JCheckBox fedeltaBox = new JCheckBox();
        clientePanel.add(fedeltaBox);

        JButton submitButton = new JButton("Submit");
        clientePanel.add(new JLabel());
        clientePanel.add(submitButton);
        clientePanel.revalidate();

        submitButton.addActionListener(e -> {
            String cf = codiceFiscaleField.getText();
            String nome = nomeField.getText();
            String cognome = cognomeField.getText();
            String etaStr = etaField.getText();
            String email = emailField.getText();
            boolean isFedelta = fedeltaBox.isSelected();

            System.out.println(cf + " " + nome + " " + cognome + " " + etaStr +" "+email+" | Fedeltà: " + isFedelta);

            if (cf.isEmpty() || nome.isEmpty() || cognome.isEmpty() || etaStr.isEmpty()) {
                JOptionPane.showMessageDialog(clientePanel, "Completa tutti i campi prima di continuare.");
            } else {
                try {
                    IDGeneratorServiceGrpc.IDGeneratorServiceBlockingStub idStub = IDGeneratorServiceGrpc.newBlockingStub(channel);
                    GetGeneratedIDRequest requestID = GetGeneratedIDRequest.newBuilder()
                            .setFidelizzato(isFedelta)
                            .build();
                    GetGeneratedIDResponse responseID = idStub.getGeneratedID(requestID);

                    ClienteServiceGrpc.ClienteServiceBlockingStub clienteStub = ClienteServiceGrpc.newBlockingStub(channel);
                    it.trenical.grpc.Cliente clienteGrpc = it.trenical.grpc.Cliente.newBuilder()
                            .setCodiceFiscale(cf)
                            .setNome(nome)
                            .setCognome(cognome)
                            .setCodiceCliente(responseID.getCodiceCliente())
                            .setEta(Integer.parseInt(etaStr))
                            .setEmail(email)
                            .build();


                    AddClienteResponse response = clienteStub.addCliente(
                            AddClienteRequest.newBuilder().setCliente(clienteGrpc).build()
                    );

                    if (response.getSuccess()) {
                        registrato = true;
                        cliente = clienteGrpc;
                        System.out.println(cliente);
                        System.out.println(registrato);
                        clientePanel.revalidate();
                        clientePanel.setVisible(false);
                        panel.add(panelloClienteRegistrato(channel, panel,tabs));
                    } else {
                        JOptionPane.showMessageDialog(clientePanel, "Errore: cliente non aggiunto.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(clientePanel, "Errore: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
    }
    return clientePanel;
}
    private static JPanel panelloClienteRegistrato(ManagedChannel channel, JPanel clientePanel2,JTabbedPane tabs) {
        JPanel clientePanel = new JPanel();
        clientePanel.removeAll();
        clientePanel.setLayout(new GridLayout(7, 2, 5, 5));
        clientePanel.setMaximumSize(new Dimension(400, 220));


        clientePanel.add(new JLabel("Codice Fiscale:"));
        clientePanel.add(new JLabel(cliente.getCodiceFiscale()));

        clientePanel.add(new JLabel("Nome:"));
        clientePanel.add(new JLabel(cliente.getNome()));

        clientePanel.add(new JLabel("Cognome:"));
        clientePanel.add(new JLabel(cliente.getCognome()));

        clientePanel.add(new JLabel("Codice Cliente:"));
        clientePanel.add(new JLabel(cliente.getCodiceCliente()));

        clientePanel.add(new JLabel("Età:"));
        clientePanel.add(new JLabel(String.valueOf(cliente.getEta())));

        clientePanel.add(new JLabel("Email:"));
        clientePanel.add(new JLabel(String.valueOf(cliente.getEmail())));
        finestraInfo(channel, tabs);
        creaNotifica(tabs, channel);
        clientePanel2.add(clientePanel);
        return clientePanel2;
    }
    private static JPanel createInfoRow(String label, String value) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.setMaximumSize(new Dimension(400, 30));

        JLabel labelComponent = new JLabel(label);
        labelComponent.setPreferredSize(new Dimension(120, 25));
        labelComponent.setFont(labelComponent.getFont().deriveFont(Font.BOLD));

        JLabel valueComponent = new JLabel(value);
        valueComponent.setPreferredSize(new Dimension(200, 25));

        row.add(labelComponent);
        row.add(valueComponent);

        return row;
    }
    private static void acquistaBiglietto(Object id, ManagedChannel channel) {

        if (!registrato) {
            throw new IllegalStateException("Ti devi registrare per poter acquistare.");
        }
            System.out.println("Utente registrato");


            DefaultTableModel model = new DefaultTableModel();
            JDialog dialog = new JDialog((Frame) null, "Acquisto Biglietto", true);
            dialog.setSize(800, 400);
            dialog.setLocationRelativeTo(null);
            dialog.setLayout(new BorderLayout());

            JPanel centerPanel = new JPanel(new BorderLayout());
            JTable treniTable = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(treniTable);
            centerPanel.add(scrollPane, BorderLayout.CENTER);

            TrenoServiceGrpc.TrenoServiceBlockingStub trenoStub = TrenoServiceGrpc.newBlockingStub(channel);
            GetTreniByTrattaIDRequest requestT = GetTreniByTrattaIDRequest.newBuilder()
                    .setTrattaID(id.toString())
                    .build();
            GetTreniByTrattaIDResponse responseT = trenoStub.getTreniByTrattaID(requestT);

            model.setRowCount(0);
            model.setColumnIdentifiers(new String[]{
                    "ID", "Tipo", "TrattaID", "Prezzo", "Posti Prima", "Posti Seconda", "Posti Terza", "PostiTot", "TempoPercorrenza", "Binario", "Promozioni"
            });
            for (Treno treno : responseT.getTreniList()) {
                System.out.println(treno.toString());
                model.addRow(new Object[]{
                        treno.getTrenoID(),
                        treno.getTipoTreno(),
                        treno.getTrattaID(),
                        treno.getPrezzo(),
                        treno.getPostiPrima(),
                        treno.getPostiSeconda(),
                        treno.getPostiTerza(),
                        treno.getPostiTot(),
                        treno.getTempoPercorrenza(),
                        treno.getBinario(),
                        treno.getPromozione()
                });
            }

            JButton conferma = new JButton("Conferma Selezione");
            conferma.addActionListener(e -> {
                int selectedRow = treniTable.getSelectedRow();
                if (selectedRow != -1) {
                    int colCount = treniTable.getColumnCount();
                    Object[] rowData = new Object[colCount];
                    for (int col = 0; col < colCount; col++) {
                        rowData[col] = model.getValueAt(selectedRow, col);
                    }

                    GetTrenoRequest request = GetTrenoRequest.newBuilder()
                            .setTrenoID(rowData[0].toString())
                            .build();
                    Treno treno = trenoStub.getTreno(request);
                    if (treno.getPostiTot() < 0) {
                        JOptionPane.showMessageDialog(dialog,
                                "Il treno che hai selezionato \nID: " + treno.getTrenoID()
                                        + " non ha piu posti disponibili");
                        dialog.dispose();
                    } else {
                        mostraMenuSceltaPosto(rowData, channel);
                        dialog.dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(dialog, "Seleziona una riga prima di confermare.");
                }
            });
            JPanel bottomPanel = new JPanel();
            bottomPanel.add(conferma);
            dialog.add(centerPanel, BorderLayout.CENTER);
            dialog.add(bottomPanel, BorderLayout.SOUTH);
            dialog.setVisible(true);
    }
    private static void mostraMenuSceltaPosto(Object[] rowData, ManagedChannel channel) {
        String trenoID = rowData[0].toString();
        JDialog dialog = new JDialog((Frame) null, "Seleziona Classe", true);
        dialog.setSize(600, 300);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());


        TrenoServiceGrpc.TrenoServiceBlockingStub trenoStub = TrenoServiceGrpc.newBlockingStub(channel);
        GetTrenoRequest request = GetTrenoRequest.newBuilder().setTrenoID(trenoID).build();
        Treno treno = trenoStub.getTreno(request);

        int postiPrima = treno.getPostiPrima();
        int postiSeconda = treno.getPostiSeconda();
        int postiTerza = treno.getPostiTerza();

        String[] classiDisponibili;
        if (postiPrima > 0 && postiSeconda > 0 && postiTerza > 0)
            classiDisponibili = new String[]{"PrimaClasse", "SecondaClasse", "TerzaClasse"};
        else if (postiPrima > 0 && postiSeconda > 0)
            classiDisponibili = new String[]{"PrimaClasse", "SecondaClasse"};
        else if (postiPrima > 0 && postiTerza > 0)
            classiDisponibili = new String[]{"PrimaClasse", "TerzaClasse"};
        else if (postiSeconda > 0 && postiTerza > 0)
            classiDisponibili = new String[]{"SecondaClasse", "TerzaClasse"};
        else if (postiPrima > 0)
            classiDisponibili = new String[]{"PrimaClasse"};
        else if (postiSeconda > 0)
            classiDisponibili = new String[]{"SecondaClasse"};
        else if (postiTerza > 0)
            classiDisponibili = new String[]{"TerzaClasse"};
        else
            classiDisponibili = new String[0];

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel label = new JLabel("Scegli la classe:");
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComboBox<String> comboBox = new JComboBox<>(classiDisponibili);
        comboBox.setMaximumSize(new Dimension(200, 25));
        comboBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel prioritaLabel = new JLabel("Eventuali priorità/necessità:");
        prioritaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField prioritaField = new JTextField();
        prioritaField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        prioritaField.setAlignmentX(Component.LEFT_ALIGNMENT);

        centerPanel.add(label);
        centerPanel.add(comboBox);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(prioritaLabel);
        centerPanel.add(prioritaField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton annullaButton = new JButton("Annulla");

        okButton.addActionListener(e -> {
            String scelta = comboBox.getSelectedItem().toString();
            String priorita = prioritaField.getText().trim();

            if(biglietto == null) {

               if (!autorizzaPagamento()) {
                   System.out.println("Scegli richiesta non valida");
                  return;
               }

                BigliettoServiceGrpc.BigliettoServiceBlockingStub bigliettoPf =  BigliettoServiceGrpc.newBlockingStub(channel);
                CreazionePrezzoFinaleRequest bigliettoPFRequest = CreazionePrezzoFinaleRequest.newBuilder()
                        .setTrenoID(trenoID)
                        .setClasse(scelta)
                        .build();
                CreazionePrezzoFinaleResponse prezzoFinale = bigliettoPf.creazionePrezzoFinale(bigliettoPFRequest);
                int prezzoFinaleUltimo = prezzoFinale.getPrezzoFinale();

                boolean pagamentoConfermato = mostraFinestraPagamento(prezzoFinaleUltimo, () -> {
                    creaBigliettoDopoConfermaPagamento(scelta, trenoID, priorita, prezzoFinaleUltimo, channel, dialog);
                });

                if (pagamentoConfermato) {
                    dialog.dispose();
                }

            } else {
                String trenoNuovoID = rowData[0].toString();
                BigliettoServiceGrpc.BigliettoServiceBlockingStub bigliettoPf =  BigliettoServiceGrpc.newBlockingStub(channel);
                CreazionePrezzoFinaleRequest bigliettoPFRequest = CreazionePrezzoFinaleRequest.newBuilder()
                        .setTrenoID(trenoNuovoID)
                        .setClasse(scelta)
                        .build();
                CreazionePrezzoFinaleResponse prezzoFinale = bigliettoPf.creazionePrezzoFinale(bigliettoPFRequest);

                int prezzoTreno = prezzoFinale.getPrezzoFinale();
                int prezzoVecchio = biglietto.getPrezzo();


                int diff=0;
                boolean flg = false;
                if(prezzoTreno < prezzoVecchio) {
                    flg= true;
                    diff=prezzoVecchio - prezzoTreno;
                } else {
                    diff= prezzoTreno - prezzoVecchio;
                }
                mostraCambioBiglietto(scelta,priorita,trenoID,prezzoTreno,channel,flg,diff);
                BigliettoServiceGrpc.BigliettoServiceBlockingStub stubREM =
                        BigliettoServiceGrpc.newBlockingStub(channel);

                RemoveBigliettoRequest requestREM = RemoveBigliettoRequest.newBuilder()
                        .setBigliettoID(biglietto.getBigliettoID())
                        .build();

                RemoveBigliettoResponse responseREM = stubREM.removeBiglietto(requestREM);
                biglietto = null;
                System.out.println(biglietto.getBigliettoID()+"Status biglietto");
                if (responseREM.getSuccess()) {
                    System.out.println("Biglietto rimosso con successo.");
                } else {
                    JOptionPane.showMessageDialog(dialog, "Errore nella eliminazione del biglietto.");
                }
                dialog.setVisible(true);
                dialog.dispose();
            }
        });
        annullaButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(annullaButton);
        buttonPanel.add(okButton);
        dialog.add(centerPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
        dialog.dispose();
    }
    private static boolean autorizzaPagamento() {
        final boolean[] autorizzato = {false};

        JDialog dialog = new JDialog((JFrame) null, "Autorizzazione Pagamento", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(6, 2, 10, 5));
        dialog.setLocationRelativeTo(null);

        JTextField campoCarta = new JTextField();
        JTextField campoCVV = new JTextField();
        JTextField campoScadenza = new JTextField();
        JTextField campoCircuito = new JTextField();
        JTextField campoTitolare = new JTextField();

        dialog.add(new JLabel("Codice Carta:"));
        dialog.add(campoCarta);
        dialog.add(new JLabel("CVV:"));
        dialog.add(campoCVV);
        dialog.add(new JLabel("Scadenza (MMYY):"));
        dialog.add(campoScadenza);
        dialog.add(new JLabel("Circuito (Visa/Mastercard):"));
        dialog.add(campoCircuito);
        dialog.add(new JLabel("Titolare:"));
        dialog.add(campoTitolare);

        JButton conferma = new JButton("Conferma");
        JButton annulla = new JButton("Annulla");

        conferma.addActionListener(e -> {
            try {
                String codiceCarta = campoCarta.getText();
                int cvv = Integer.parseInt(campoCVV.getText());
                String scadenza = campoScadenza.getText();
                String circuito = campoCircuito.getText();
                String titolare = campoTitolare.getText();

                boolean validi = Pagamento.inserimentoDati(codiceCarta, cvv, scadenza, circuito, titolare);

                if (validi) {
                    autorizzato[0] = true;
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Dati non validi. Riprova.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Inserisci solo numeri nei campi numerici.");
            }
        });

        annulla.addActionListener(e -> {
            autorizzato[0] = false;
            dialog.dispose();
        });

        dialog.add(annulla);
        dialog.add(conferma);
        dialog.setVisible(true);

        return autorizzato[0];
    }
    private static boolean mostraFinestraPagamento(int importo, Runnable onSuccess) {
        final boolean[] confermato = {false};

        JDialog dialog = new JDialog((JFrame) null, "Pagamento", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel labelTipo = new JLabel("Tipo: pagamento elettronico");
        JLabel labelProcesso = new JLabel("Processiamo l'operazione...");
        JLabel labelBanca = new JLabel("Contattiamo la banca...");
        JLabel labelImporto = new JLabel("Stai per pagare l'importo di " + importo + " euro.");
        JLabel labelConferma = new JLabel("Confermi l'acquisto?");

        labelTipo.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelProcesso.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelBanca.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelImporto.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelConferma.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton annullaButton = new JButton("Annulla");
        JButton confermaButton = new JButton("Conferma");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(annullaButton);
        buttonPanel.add(confermaButton);

        annullaButton.addActionListener(e -> {
            confermato[0] = false;
            dialog.setVisible(true);
            dialog.dispose();
            JOptionPane.showMessageDialog(null, "Pagamento annullato. Nessun biglietto creato.");
        });

        confermaButton.addActionListener(e -> {
            confermato[0] = true;
            dialog.dispose();

            if (onSuccess != null) {
                onSuccess.run();
            }
        });

        panel.add(labelTipo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(labelProcesso);
        panel.add(Box.createVerticalStrut(10));
        panel.add(labelBanca);
        panel.add(Box.createVerticalStrut(10));
        panel.add(labelImporto);
        panel.add(Box.createVerticalStrut(10));
        panel.add(labelConferma);
        panel.add(Box.createVerticalStrut(20));
        panel.add(buttonPanel);

        dialog.setContentPane(panel);
        dialog.setVisible(true);

        return confermato[0];
    }
    private static void creaBigliettoDopoConfermaPagamento(String scelta, String trenoID, String priorita, int prezzo, ManagedChannel channel, JDialog parentDialog) {
        try {
            BigliettoServiceGrpc.BigliettoServiceBlockingStub stub = BigliettoServiceGrpc.newBlockingStub(channel);
            CreaBigliettoRequest requestBiglietto = CreaBigliettoRequest.newBuilder()
                    .addDati(scelta)
                    .addDati(trenoID)
                    .addDati(cliente.getCodiceFiscale())
                    .addDati(priorita)
                    .addDati(String.valueOf(prezzo))

                    .build();

            CreaBigliettoResponse response = stub.creaBiglietto(requestBiglietto);

            if (response.getSuccess()) {
                JOptionPane.showMessageDialog(null,
                        "Pagamento confermato!\nBiglietto creato con successo!\nPrezzo finale: " +
                                response.getPrezzoFinale() + " euro");
            } else {
                JOptionPane.showMessageDialog(null,
                        "Errore nella creazione del biglietto dopo pagamento confermato.\nContattare il supporto.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Errore durante la creazione del biglietto: " + ex.getMessage());
        }
    }
    private static void creaNotifica(JTabbedPane tabs, ManagedChannel channel) {
        if (registrato && cliente != null) {
            JPanel panel = new JPanel(new BorderLayout());
            System.out.println("Notifiche " + registrato);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton ricaricaButton = new JButton("Ricarica");
            JLabel lastUpdateLabel = new JLabel("Ultimo aggiornamento: --");
            lastUpdateLabel.setFont(new Font("Arial", Font.ITALIC, 10));

            buttonPanel.add(lastUpdateLabel);
            panel.add(buttonPanel, BorderLayout.NORTH);

            JPanel notificaPanel = new JPanel(new BorderLayout());
            notificaPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            notificaPanel.setBackground(new Color(255, 255, 255));

            JLabel titolo = new JLabel("Notifiche Ricevute");
            titolo.setFont(new Font("Arial", Font.BOLD, 16));
            titolo.setHorizontalAlignment(SwingConstants.CENTER);
            notificaPanel.add(titolo, BorderLayout.NORTH);

            String[] colonne = { "Orario", "Messaggio" };
            DefaultTableModel notificaModel = new DefaultTableModel(colonne, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            JTable notificaTable = new JTable(notificaModel);

            notificaTable.setRowHeight(25);
            notificaTable.setFont(new Font("Arial", Font.PLAIN, 12));
            notificaTable.setGridColor(new Color(200, 200, 200));
            notificaTable.setSelectionBackground(new Color(173, 216, 230));
            notificaTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
            notificaTable.getTableHeader().setBackground(new Color(184, 212, 240));
            notificaTable.getTableHeader().setForeground(Color.BLACK);

            notificaTable.getColumnModel().getColumn(0).setPreferredWidth(150);
            notificaTable.getColumnModel().getColumn(1).setPreferredWidth(450);

            JScrollPane notificaScroll = new JScrollPane(notificaTable);
            notificaPanel.add(notificaScroll, BorderLayout.CENTER);

            Runnable aggiornaNotifiche = () -> {
                SwingUtilities.invokeLater(() -> {
                    try {
                        System.out.println("Aggiornamento notifiche in corso...");
                        notificaModel.setRowCount(0);

                        NotificaServiceGrpc.NotificaServiceBlockingStub notificaStub =
                                NotificaServiceGrpc.newBlockingStub(channel);

                        GetNotificaRequest req = GetNotificaRequest.newBuilder()
                                .setCliente(cliente.getCodiceFiscale())
                                .build();

                        GetNotificaResponse resp = notificaStub.getNotifica(req);

                        int count = 0;
                        for (Notifica n : resp.getNotificheList()) {
                            if (n.getCliente().equals(cliente.getCodiceFiscale())) {
                                String messaggio = "";

                                switch (n.getStato()) {
                                    case "RIMOSSA":
                                        messaggio = "Gentile " + n.getCliente() +
                                                " ci dispiace informarla che il suo treno " + n.getTreno() + " è stato cancellato";
                                        break;

                                    case "MODIFICATO":
                                        messaggio = "Gentile " + n.getCliente() +
                                                " le comunichiamo che ci sono stati alcuni cambiamenti con la sua prenotazione " + n.getBiglietto() +
                                                " per il treno " + n.getTreno() + " delle ore " + n.getPartenza() +
                                                " la invitiamo a consultare la sezione info";
                                        break;

                                    case "IMMINENTE":
                                        messaggio = "Gentile " + n.getCliente() +
                                                " le ricordiamo che il suo treno " + n.getTreno() + " delle ore " + n.getPartenza() +
                                                " partirà fra meno di un ora";
                                        break;

                                    case "PROMO":
                                        messaggio = n.getTreno();
                                        break;

                                }

                                notificaModel.addRow(new Object[]{n.getLog(), messaggio});
                                count++;
                            }
                        }

                        System.out.println("Caricate " + count + " notifiche");

                        lastUpdateLabel.setText("Ultimo aggiornamento: " +
                                java.time.LocalDateTime.now().format(
                                        java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));

                    } catch (Exception e2) {
                        System.err.println("Errore nel caricamento notifiche: " + e2.getMessage());
                        e2.printStackTrace();
                        notificaModel.addRow(new Object[]{"ERRORE", "Errore durante il caricamento delle notifiche: " + e2.getMessage()});
                    }
                });
            };

            ricaricaButton.addActionListener(e -> {
                System.out.println("Ricarica manuale cliccata");
                aggiornaNotifiche.run();
            });

            System.out.println("Caricamento iniziale notifiche");
            aggiornaNotifiche.run();

            Timer autoRefreshTimer = new Timer(10000, new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("Timer triggered - aggiornamento automatico notifiche");
                    aggiornaNotifiche.run();
                }
            });

            autoRefreshTimer.setRepeats(true);
            autoRefreshTimer.start();
            System.out.println("Timer notifiche avviato");

            panel.addHierarchyListener(e -> {
                if ((e.getChangeFlags() & java.awt.event.HierarchyEvent.SHOWING_CHANGED) != 0) {
                    if (!panel.isShowing()) {
                        System.out.println("Pannello notifiche nascosto - fermo il timer");
                        autoRefreshTimer.stop();
                    } else {
                        System.out.println("Pannello notifiche mostrato - riavvio il timer");
                        if (!autoRefreshTimer.isRunning()) {
                            autoRefreshTimer.start();
                        }
                    }
                }
            });

            panel.add(notificaPanel, BorderLayout.CENTER);
            tabs.addTab("Notifiche", panel);
        }
    }
    public static void finestraInfo(ManagedChannel channel, JTabbedPane tabs) {
        JPanel panel = new JPanel(new BorderLayout());
        System.out.println("INfo " + registrato);

        if (cliente != null) {

            JPanel infoPanel = new JPanel(new BorderLayout());
            infoPanel.setBorder(BorderFactory.createTitledBorder("Informazioni Complete Biglietti"));


            JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            selectionPanel.add(new JLabel("Seleziona Biglietto:"));
            JComboBox<String> comboBiglietti = new JComboBox<>();
            comboBiglietti.setPreferredSize(new Dimension(300, 25));
            selectionPanel.add(comboBiglietti);


            JButton refreshButton = new JButton("🔄 Aggiorna");
           // selectionPanel.add(refreshButton);

            JLabel lastUpdateLabel = new JLabel("Ultimo aggiornamento: --");
            lastUpdateLabel.setFont(new Font("Arial", Font.ITALIC, 10));
            selectionPanel.add(lastUpdateLabel);

            String[] colonne = {"Campo", "Valore"};
            DefaultTableModel infoModel = new DefaultTableModel(colonne, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            JTable infoTable = new JTable(infoModel);

            infoTable.setRowHeight(25);
            infoTable.setFont(new Font("Arial", Font.PLAIN, 12));
            infoTable.setGridColor(new Color(200, 200, 200));
            infoTable.setSelectionBackground(new Color(173, 216, 230));
            infoTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
            infoTable.getTableHeader().setBackground(new Color(184, 212, 240));
            infoTable.getTableHeader().setForeground(Color.BLACK);

            infoTable.getColumnModel().getColumn(0).setPreferredWidth(200);
            infoTable.getColumnModel().getColumn(1).setPreferredWidth(300);

            JScrollPane scrollPane = new JScrollPane(infoTable);

            List<Biglietto> biglietti = new ArrayList<>();

            Runnable aggiornaDati = () -> {
                try {
                    BigliettoServiceGrpc.BigliettoServiceBlockingStub bigliettoStub = BigliettoServiceGrpc.newBlockingStub(channel);
                    GetBigliettiByFiltroRequest requestA = GetBigliettiByFiltroRequest.newBuilder()
                            .setColonna("cliente_id")
                            .setValore(cliente.getCodiceFiscale())
                            .build();
                    GetBigliettiByFiltroResponse responseA = bigliettoStub.getBigliettiByFiltro(requestA);
                    String selectedItem = (String) comboBiglietti.getSelectedItem();

                    biglietti.clear();
                    biglietti.addAll(responseA.getBigliettiList());

                    comboBiglietti.removeAllItems();
                    if (biglietti.isEmpty()) {
                        comboBiglietti.addItem("Nessun biglietto trovato");
                        comboBiglietti.setEnabled(false);
                        infoModel.setRowCount(0);
                    } else {
                        for (Biglietto b : biglietti) {
                            String displayText = "Biglietto " + b.getBigliettoID() + " - Treno " + b.getTrenoID();
                            comboBiglietti.addItem(displayText);
                        }
                        comboBiglietti.setEnabled(true);

                        if (selectedItem != null) {
                            for (int i = 0; i < comboBiglietti.getItemCount(); i++) {
                                if (comboBiglietti.getItemAt(i).equals(selectedItem)) {
                                    comboBiglietti.setSelectedIndex(i);
                                    break;
                                }
                            }
                        } else {
                            comboBiglietti.setSelectedIndex(0);
                        }
                    }

                    lastUpdateLabel.setText("Ultimo aggiornamento: " +
                            java.time.LocalDateTime.now().format(
                                    java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));

                } catch (Exception e) {
                    System.err.println("Errore durante l'aggiornamento: " + e.getMessage());
                    infoModel.setRowCount(0);
                    infoModel.addRow(new Object[]{"ERRORE", "Errore durante l'aggiornamento dei dati"});
                }
            };

            Runnable caricaDettagli = () -> {
                int selectedIndex = comboBiglietti.getSelectedIndex();
                if (selectedIndex >= 0 && selectedIndex < biglietti.size()) {
                    Biglietto bigliettoSelezionato = biglietti.get(selectedIndex);

                    infoModel.setRowCount(0);

                    try {
                        TrenoServiceGrpc.TrenoServiceBlockingStub trenoStub = TrenoServiceGrpc.newBlockingStub(channel);
                        GetTrenoRequest trenoRequest = GetTrenoRequest.newBuilder()
                                .setTrenoID(bigliettoSelezionato.getTrenoID())
                                .build();
                        it.trenical.grpc.Treno treno = trenoStub.getTreno(trenoRequest);

                        infoModel.addRow(new Object[]{"═══ DATI BIGLIETTO ═══", ""});
                        infoModel.addRow(new Object[]{"ID Biglietto", bigliettoSelezionato.getBigliettoID()});
                        infoModel.addRow(new Object[]{"Classe", bigliettoSelezionato.getClasse()});
                        infoModel.addRow(new Object[]{"Carrozza", bigliettoSelezionato.getCarrozza()});
                        infoModel.addRow(new Object[]{"Posto", bigliettoSelezionato.getPosto()});
                        infoModel.addRow(new Object[]{"Binario", treno.getBinario()});
                        infoModel.addRow(new Object[]{"Prezzo", "€ " + bigliettoSelezionato.getPrezzo()});
                        infoModel.addRow(new Object[]{"Priorità",
                                bigliettoSelezionato.getPrioritaList().isEmpty() ?
                                        "Nessuna" : bigliettoSelezionato.getPrioritaList().toString()});


                        infoModel.addRow(new Object[]{"", ""});
                        infoModel.addRow(new Object[]{"═══ DATI TRENO ═══", ""});
                        infoModel.addRow(new Object[]{"ID Treno", treno.getTrenoID()});
                        infoModel.addRow(new Object[]{"Tipo Treno", treno.getTipoTreno()});
                        infoModel.addRow(new Object[]{"Prezzo Base", "€ " + treno.getPrezzo()});
                        infoModel.addRow(new Object[]{"Posti Prima Classe", treno.getPostiPrima()});
                        infoModel.addRow(new Object[]{"Posti Seconda Classe", treno.getPostiSeconda()});
                        infoModel.addRow(new Object[]{"Posti Terza Classe", treno.getPostiTerza()});
                        infoModel.addRow(new Object[]{"Posti Totali", treno.getPostiTot()});
                        infoModel.addRow(new Object[]{"Binario", treno.getBinario()});
                        infoModel.addRow(new Object[]{"ID Tratta Associata", treno.getTrattaID()});

                        TrattaServiceGrpc.TrattaServiceBlockingStub trattaStub = TrattaServiceGrpc.newBlockingStub(channel);
                        GetTrattaRequest trattaRequest = GetTrattaRequest.newBuilder()
                                .setCodiceTratta(treno.getTrattaID())
                                .build();
                        it.trenical.grpc.TrattaStandard tratta = trattaStub.getTratta(trattaRequest);

                        infoModel.addRow(new Object[]{"", ""});
                        infoModel.addRow(new Object[]{"═══ DATI TRATTA ═══", ""});
                        infoModel.addRow(new Object[]{"ID Tratta", tratta.getCodiceTratta()});
                        infoModel.addRow(new Object[]{"Stazione Partenza", tratta.getStazionePartenza()});
                        infoModel.addRow(new Object[]{"Stazione Arrivo", tratta.getStazioneArrivo()});
                        infoModel.addRow(new Object[]{"Data Partenza", tratta.getDataPartenza()});
                        infoModel.addRow(new Object[]{"Data Arrivo", tratta.getDataArrivo()});
                        infoModel.addRow(new Object[]{"Distanza", tratta.getDistanza() + " km"});
                        infoModel.addRow(new Object[]{"Durata Media", tratta.getTempoPercorrenza() + " ore"});

                    } catch (Exception ex) {
                        System.err.println("Errore nel recupero delle informazioni: " + ex.getMessage());
                        infoModel.addRow(new Object[]{"ERRORE", "Impossibile recuperare tutte le informazioni"});
                        infoModel.addRow(new Object[]{"Dettagli errore", ex.getMessage()});
                    }
                }
            };

            comboBiglietti.addActionListener(e -> caricaDettagli.run());

            refreshButton.addActionListener(e -> {
                aggiornaDati.run();
                if (comboBiglietti.getItemCount() > 0 && !comboBiglietti.getItemAt(0).equals("Nessun biglietto trovato")) {
                    caricaDettagli.run();
                }
            });

            aggiornaDati.run();
            if (comboBiglietti.getItemCount() > 0 && !comboBiglietti.getItemAt(0).equals("Nessun biglietto trovato")) {
                comboBiglietti.setSelectedIndex(0);
                caricaDettagli.run();
            }

            Timer autoRefreshTimer = new Timer(5000, e -> {
                aggiornaDati.run();
                if (comboBiglietti.getSelectedIndex() >= 0) {
                    caricaDettagli.run();
                }
            });
            autoRefreshTimer.start();

            JPanel finalPanel = panel;
            panel.addHierarchyListener(e -> {
                if ((e.getChangeFlags() & java.awt.event.HierarchyEvent.SHOWING_CHANGED) != 0) {
                    if (!finalPanel.isShowing()) {
                        autoRefreshTimer.stop();
                    }
                }
            });

            infoPanel.add(selectionPanel, BorderLayout.NORTH);
            infoPanel.add(scrollPane, BorderLayout.CENTER);
            panel = infoPanel;

        } else {
            JPanel noClientPanel = new JPanel(new GridBagLayout());
            JLabel noClientLabel = new JLabel("Effettua il login per visualizzare le informazioni dei tuoi biglietti");
            noClientLabel.setFont(new Font("Arial", Font.BOLD, 14));
            noClientLabel.setForeground(Color.GRAY);
            noClientPanel.add(noClientLabel);
            panel = noClientPanel;
        }
        tabs.addTab("Info", panel);
    }
    public static void mostraCambioBiglietto(String scelta, String priorita,String trenoID, int prezzo,ManagedChannel channel,boolean flag, int diff) {
        JDialog dialog = new JDialog( (JFrame) null, "Modifica Biglietto", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel messaggio = new JLabel("Vuoi cambiare il biglietto?");
        messaggio.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel messaggio2;
        if (flag) {
            messaggio2 = new JLabel("Ti verrà rimborsata la differenza di " + diff + " euro");
        } else {
            messaggio2 = new JLabel("Ti verrà addebitata la differenza di " + diff + " euro");
        }
        messaggio2.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton annulla = new JButton("Annulla");
        JButton conferma = new JButton("Conferma");

        JPanel bottoni = new JPanel(new FlowLayout());
        bottoni.add(annulla);
        bottoni.add(conferma);

        annulla.addActionListener(e -> dialog.dispose());

        conferma.addActionListener(e -> {
            BigliettoServiceGrpc.BigliettoServiceBlockingStub stub = BigliettoServiceGrpc.newBlockingStub(channel);
            CreaBigliettoRequest requestBiglietto = CreaBigliettoRequest.newBuilder()
                    .addDati(scelta)
                    .addDati(trenoID)
                    .addDati(cliente.getCodiceFiscale())
                    .addDati(priorita)
                    .addDati(String.valueOf(prezzo))
                    .build();

            CreaBigliettoResponse response = stub.creaBiglietto(requestBiglietto);

            if (response.getSuccess()) {
                JOptionPane.showMessageDialog(dialog, "Biglietto creato con successo!");
            } else {
                JOptionPane.showMessageDialog(dialog, "Errore nella creazione del biglietto.");
            }

           dialog.dispose();
        });

        panel.add(messaggio);
        panel.add(Box.createVerticalStrut(10));
        panel.add(messaggio2);
        panel.add(Box.createVerticalStrut(20));
        panel.add(bottoni);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }



    public static void main(String[] args) {
        //System.setProperty("sun.java2d.uiScale", "3.0");
        SwingUtilities.invokeLater(() -> {
            new ClientDashboardSwing().setVisible(true);
        });
    }
}
