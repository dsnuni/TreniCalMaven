package it.trenical.client.gui;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import it.trenical.client.pagamento.Pagamento;
import it.trenical.grpc.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

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
       // tabs.addTab("Info", creaTabella("Info", tabs));

        //tabs.addTab("Treni", creaTabella("Treno"));
       // tabs.addTab("Notifiche",new JPanel(new GridLayout(1, 2)));


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
                buttonPanel3.add(buyButton2);
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
                buyButton2.addActionListener(e -> {
                    int row = table.getSelectedRow();

                    if (row != -1) {
                        // Recupera il modello e i dati dalla riga selezionata
                        Object id = table.getValueAt(row, 0);
                        System.out.println("Primo campo della riga selezionata: " + id.toString());
                        acquistaBiglietto(id,channel);
                            }
                    });
                panel.add(scrollPane, BorderLayout.CENTER);
                break;

            case "Biglietto":
                // Layout a divisione orizzontale (cliente | biglietti)
                JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
                splitPane.setResizeWeight(0.3);
                JPanel buttonPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JButton refreshButton = new JButton("Refresh "+tabellaNome);
                JButton modificaBigliettoButton = new JButton("Modifica "+tabellaNome);
                buttonPanel2.add(modificaBigliettoButton);
                buttonPanel2.add(refreshButton);
                // Colori personalizzati
                Color sfondoPrincipale = panel.getBackground();

                Color sfondoPannelli = new Color(255, 255, 255); // grigio chiaro per il pannello principale
                Color sfondoScuro = new Color(255, 255, 255);    // un po' più scuro per i pannelli interni

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
                        new Object[]{"Biglietto_id","Classe","treno_id", "Carrozza", "Posto","priorità", "Prezzo", "Partenza", "Arrivo"}, 0
                );
                caricaDatiDaDB(nuovoModel, channel);
                bigliettiTable.setModel(nuovoModel);
            });

                modificaBigliettoButton.addActionListener(e -> {
                    int row = bigliettiTable.getSelectedRow(); // ✅ tabella corretta
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

                // Assemblo i due pannelli
                splitPane.setLeftComponent(clientePanel);
                splitPane.setRightComponent(bigliettiPanel);

                panel.setLayout(new BorderLayout());
                panel.setBackground(sfondoPannelli); // sfondo più chiaro per contrasto
                panel.setOpaque(true);
                panel.add(splitPane, BorderLayout.CENTER);

                bigliettiPanel.add(buttonPanel2, BorderLayout.NORTH);
                break;

        }
        return panel;
    }
    private static void creaNotifica(JTabbedPane tabs, ManagedChannel channel) {
        if (registrato && cliente != null) {
            JPanel panel = new JPanel(new BorderLayout());
            System.out.println("Notifiche " + registrato);
            JPanel buttonPanel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton ricaricaButton = new JButton("Ricarica");
            buttonPanel3.add(ricaricaButton);
            panel.add(buttonPanel3, BorderLayout.NORTH);
            JPanel notificaPanel = new JPanel(new BorderLayout());
            notificaPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            notificaPanel.setBackground(new Color(255, 255, 255)); // colore azzurrino chiaro

            JLabel titolo = new JLabel("Notifiche Ricevute");
            titolo.setFont(new Font("Arial", Font.BOLD, 16));
            titolo.setHorizontalAlignment(SwingConstants.CENTER);
            notificaPanel.add(titolo, BorderLayout.NORTH);

            // Colonne tabella
            String[] colonne = { "Partenza", "Messaggio" };
            DefaultTableModel notificaModel = new DefaultTableModel(colonne, 0);
            JTable notificaTable = new JTable(notificaModel);
            notificaTable.setRowHeight(40);
            JScrollPane notificaScroll = new JScrollPane(notificaTable);
            notificaPanel.add(notificaScroll, BorderLayout.CENTER);

            ricaricaButton.addActionListener(e -> {
            try {
                notificaModel.setRowCount(0);
                // gRPC: carica le notifiche per cliente
                NotificaServiceGrpc.NotificaServiceBlockingStub notificaStub =
                        NotificaServiceGrpc.newBlockingStub(channel);

                GetNotificaRequest req = GetNotificaRequest.newBuilder()
                        .setCliente(cliente.getCodiceFiscale())
                        .build();

                GetNotificaResponse resp = notificaStub.getNotifica(req);
                for (Notifica n : resp.getNotificheList()) {
                    if (n.getCliente().equals(cliente.getCodiceFiscale())) {

                    if(n.getStato().equals("RIMOSSA")) {
                        String messaggioRim = "Gentile "+n.getCliente()+
                                " ci dispiace infrmorla che il suo treno "+n.getTreno()+" è stato cancellato";
                        notificaModel.addRow(new Object[]{n.getLog(), messaggioRim});

                    } else if(n.getStato().equals("MODIFICATO")) {
                        String messaggioMod = "Gentile "+n.getCliente()+
                                " le comunichiamo che ci sono stati alcuni cambiamenti con la sua prenotazione "+n.getBiglietto()+
                                " per il treno "+n.getTreno()+ " delle ore "+n.getPartenza()+
                                " la invitiamo a consultare la sezione info";
                        notificaModel.addRow(new Object[]{n.getLog(), messaggioMod});

                    } else if(n.getStato().equals("IMMEDIATO")) {
                        String messaggioImm = "Gentile "+n.getCliente()+
                                " le ricordiamo che il suo treno "+n.getTreno()+" delle ore "+n.getPartenza() +
                                " partirà fra meno di un ora";
                        notificaModel.addRow(new Object[]{n.getLog(), messaggioImm});
                    } else if(n.getStato().equals("PROMO")) {
                        notificaModel.addRow( new Object[]{n.getLog(),n.getTreno()});
                    }

                    }
                }
            } catch (Exception e2) {
                System.err.println("Errore nel caricamento notifiche: " + e2.getMessage());
            } });
            panel.add(notificaPanel, BorderLayout.CENTER);
            tabs.addTab("Notifiche", panel);
        }
    }
    private static void caricaDatiDaDB(DefaultTableModel model,  ManagedChannel channel) {

        BigliettoServiceGrpc.BigliettoServiceBlockingStub bigliettoStub = BigliettoServiceGrpc.newBlockingStub(channel);
        GetBigliettiByFiltroRequest request = GetBigliettiByFiltroRequest.newBuilder()
                .setColonna("cliente_id")  // esempio: "dataPartenza", "trenoID", "codiceCliente", ecc.
                .setValore(cliente.getCodiceFiscale())           // il valore da cercare
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
        clientePanel.removeAll(); // pulizia per refresh
        clientePanel.setLayout(new GridLayout(7, 2, 5, 5)); // aumentato da 6 a 7
        clientePanel.setMaximumSize(new Dimension(400, 220));
        //clientePanel.setBackground(new Color(180, 180, 180));
        JButton accessButton = new JButton("Accedi");
        JButton registratoButton = new JButton("Registrati");
        clientePanel.add(new JLabel()); // spazio vuoto per allineamento
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
            panel.add(pannelloCLiente(panel, channel));
            panel.revalidate();
            panel.repaint();
        });
        return clientePanel;
    }
    private static JPanel pannelloAccesso(JTabbedPane tabs, ManagedChannel channel) {
        JPanel clientePanel  = new JPanel();
        clientePanel.removeAll(); // pulizia per refresh
        clientePanel.setLayout(new GridLayout(7, 2, 5, 5)); // aumentato da 6 a 7
        clientePanel.setMaximumSize(new Dimension(400, 220));
        //clientePanel.setBackground(new Color(255, 255, 255));

        clientePanel.add(new JLabel("Codice Fiscale:"));
        JTextField codiceFiscaleField = new JTextField(20);
        clientePanel.add(codiceFiscaleField);

        JButton submitButton = new JButton("Accedi");
        clientePanel.add(new JLabel()); // spazio vuoto per allineamento
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

            clientePanel.removeAll(); // pulizia per refresh
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

            finestraInfo(channel, tabs);
            creaNotifica(tabs, channel);
        });
        return clientePanel;
    }
    private static JPanel pannelloCLiente(JPanel panel, ManagedChannel channel) {
    JPanel clientePanel  = new JPanel();
    clientePanel.removeAll(); // pulizia per refresh
    clientePanel.setLayout(new GridLayout(7, 2, 5, 5)); // aumentato da 6 a 7
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
        JTextField etaField = new JTextField(5);
        clientePanel.add(etaField);

        // ✅ Checkbox Cliente Fedeltà
        clientePanel.add(new JLabel("Cliente fedeltà:"));
        JCheckBox fedeltaBox = new JCheckBox();
        clientePanel.add(fedeltaBox);

        JButton submitButton = new JButton("Submit");
        clientePanel.add(new JLabel()); // spazio vuoto per allineamento
        clientePanel.add(submitButton);
        clientePanel.revalidate();

        submitButton.addActionListener(e -> {
            String cf = codiceFiscaleField.getText();
            String nome = nomeField.getText();
            String cognome = cognomeField.getText();
            String etaStr = etaField.getText();
            boolean isFedelta = fedeltaBox.isSelected();

            System.out.println(cf + " " + nome + " " + cognome + " " + etaStr + " | Fedeltà: " + isFedelta);

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
                        panel.add(panelloClienteRegistrato(channel, panel));
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
    private static JPanel panelloClienteRegistrato(ManagedChannel channel, JPanel clientePanel) {

            clientePanel.removeAll(); // pulizia per refresh
            clientePanel.setLayout(new GridLayout(6, 2, 5, 5));
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

        return clientePanel;
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
                    "ID", "Tipo", "TrattaID", "Prezzo", "Posti Prima", "Posti Seconda", "Posti Terza", "PostiTot", "TempoPercorrenza"
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
                        treno.getTempoPercorrenza()
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

        // Stub per treno
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

        // Pannello centrale
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

        // Pulsanti
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton annullaButton = new JButton("Annulla");

        okButton.addActionListener(e -> {
            String scelta = comboBox.getSelectedItem().toString();
            String priorita = prioritaField.getText().trim();

             // Metodo di autorizzazione

            if(biglietto == null) {
                autorizzaPagamento();
                BigliettoServiceGrpc.BigliettoServiceBlockingStub stub = BigliettoServiceGrpc.newBlockingStub(channel);
                CreaBigliettoRequest requestBiglietto = CreaBigliettoRequest.newBuilder()
                        .addDati(scelta) // classe
                        .addDati(trenoID)
                        .addDati(cliente.getCodiceFiscale())
                        .addDati(priorita)
                        .build();

                CreaBigliettoResponse response = stub.creaBiglietto(requestBiglietto);
                if (response.getSuccess()) {
                    int prezzoFinale = response.getPrezzoFinale();

                try {
                    mostraFinestraPagamento(prezzoFinale);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

                    JOptionPane.showMessageDialog(dialog, "Biglietto creato con successo!");
                } else {
                    JOptionPane.showMessageDialog(dialog, "Errore nella creazione del biglietto.");
                }
                dialog.dispose();

            } else { //usiamo la funzione acquisto per cambiare biglietto
                int prezzoTreno = Integer.parseInt(rowData[3].toString());
                int prezzoVecchio = biglietto.getPrezzo();
                System.out.println(prezzoTreno + "SUCA " + prezzoVecchio);
                int diff=0;
                boolean flg = false;
                if(prezzoTreno < prezzoVecchio) {
                    flg= true;
                    diff=prezzoVecchio - prezzoTreno;
                } else {
                    diff= prezzoTreno - prezzoVecchio;
                }
                mostraCambioBiglietto(scelta,priorita,trenoID,channel,flg,diff);
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
    public static void finestraInfo(ManagedChannel channel, JTabbedPane tabs) {
        JPanel panel = new JPanel(new BorderLayout());
        System.out.println("INfo " + registrato);
        if (cliente != null) {
            // Recupera i biglietti del cliente
            BigliettoServiceGrpc.BigliettoServiceBlockingStub bigliettoStub = BigliettoServiceGrpc.newBlockingStub(channel);
            GetBigliettiByFiltroRequest requestA = GetBigliettiByFiltroRequest.newBuilder()
                    .setColonna("cliente_id")
                    .setValore(cliente.getCodiceFiscale())
                    .build();
            GetBigliettiByFiltroResponse responseA = bigliettoStub.getBigliettiByFiltro(requestA);
            List<Biglietto> biglietti = responseA.getBigliettiList();

            // Crea il pannello principale per le informazioni complete
            JPanel infoPanel = new JPanel(new BorderLayout());
            infoPanel.setBorder(BorderFactory.createTitledBorder("Informazioni Complete Biglietti"));

            // ComboBox per selezionare il biglietto
            JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            selectionPanel.add(new JLabel("Seleziona Biglietto:"));
            JComboBox<String> comboBiglietti = new JComboBox<>();
            comboBiglietti.setPreferredSize(new Dimension(300, 25));
            selectionPanel.add(comboBiglietti);

            // Tabella per mostrare le informazioni complete
            String[] colonne = {"Campo", "Valore"};
            DefaultTableModel infoModel = new DefaultTableModel(colonne, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Tabella in sola lettura
                }
            };
            JTable infoTable = new JTable(infoModel);

            // Personalizzazione della tabella (stesso stile del resto del codice)
            infoTable.setRowHeight(25);
            infoTable.setFont(new Font("Arial", Font.PLAIN, 12));
            infoTable.setGridColor(new Color(200, 200, 200));
            infoTable.setSelectionBackground(new Color(173, 216, 230));
            infoTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
            infoTable.getTableHeader().setBackground(new Color(184, 212, 240));
            infoTable.getTableHeader().setForeground(Color.BLACK);

            // Larghezza delle colonne
            infoTable.getColumnModel().getColumn(0).setPreferredWidth(200);
            infoTable.getColumnModel().getColumn(1).setPreferredWidth(300);

            JScrollPane scrollPan2e = new JScrollPane(infoTable);

            // Popola la ComboBox con i biglietti
            comboBiglietti.removeAllItems();
            if (biglietti.isEmpty()) {
                comboBiglietti.addItem("Nessun biglietto trovato");
                comboBiglietti.setEnabled(false);
            } else {
                for (Biglietto b : biglietti) {
                    String displayText = "Biglietto " + b.getBigliettoID() + " - Treno " + b.getTrenoID();
                    comboBiglietti.addItem(displayText);
                }
                comboBiglietti.setEnabled(true);
            }

            // ActionListener per la selezione del biglietto
            comboBiglietti.addActionListener(e -> {
                int selectedIndex = comboBiglietti.getSelectedIndex();
                if (selectedIndex >= 0 && selectedIndex < biglietti.size()) {
                    Biglietto bigliettoSelezionato = biglietti.get(selectedIndex);

                    // Pulisci la tabella
                    infoModel.setRowCount(0);

                    try {
                        // ========== INFORMAZIONI BIGLIETTO ==========
                        infoModel.addRow(new Object[]{"═══ DATI BIGLIETTO ═══", ""});
                        infoModel.addRow(new Object[]{"ID Biglietto", bigliettoSelezionato.getBigliettoID()});
                        infoModel.addRow(new Object[]{"Classe", bigliettoSelezionato.getClasse()});
                        infoModel.addRow(new Object[]{"Carrozza", bigliettoSelezionato.getCarrozza()});
                        infoModel.addRow(new Object[]{"Posto", bigliettoSelezionato.getPosto()});
                        infoModel.addRow(new Object[]{"Prezzo", "€ " + bigliettoSelezionato.getPrezzo()});
                        infoModel.addRow(new Object[]{"Priorità",
                                bigliettoSelezionato.getPrioritaList().isEmpty() ?
                                        "Nessuna" : bigliettoSelezionato.getPrioritaList().toString()});

                        // ========== RECUPERA E MOSTRA DATI TRENO ==========
                        TrenoServiceGrpc.TrenoServiceBlockingStub trenoStub = TrenoServiceGrpc.newBlockingStub(channel);
                        GetTrenoRequest trenoRequest = GetTrenoRequest.newBuilder()
                                .setTrenoID(bigliettoSelezionato.getTrenoID())
                                .build();
                        it.trenical.grpc.Treno treno = trenoStub.getTreno(trenoRequest);

                        infoModel.addRow(new Object[]{"", ""}); // Riga vuota per separare
                        infoModel.addRow(new Object[]{"═══ DATI TRENO ═══", ""});
                        infoModel.addRow(new Object[]{"ID Treno", treno.getTrenoID()});
                        infoModel.addRow(new Object[]{"Tipo Treno", treno.getTipoTreno()});
                        infoModel.addRow(new Object[]{"Prezzo Base", "€ " + treno.getPrezzo()});
                        infoModel.addRow(new Object[]{"Posti Prima Classe", treno.getPostiPrima()});
                        infoModel.addRow(new Object[]{"Posti Seconda Classe", treno.getPostiSeconda()});
                        infoModel.addRow(new Object[]{"Posti Terza Classe", treno.getPostiTerza()});
                        infoModel.addRow(new Object[]{"Posti Totali", treno.getPostiTot()});
                        infoModel.addRow(new Object[]{"ID Tratta Associata", treno.getTrattaID()});

                        // ========== RECUPERA E MOSTRA DATI TRATTA ==========
                        TrattaServiceGrpc.TrattaServiceBlockingStub trattaStub = TrattaServiceGrpc.newBlockingStub(channel);
                        GetTrattaRequest trattaRequest = GetTrattaRequest.newBuilder()
                                .setCodiceTratta(treno.getTrattaID())
                                .build();
                        it.trenical.grpc.TrattaStandard tratta = trattaStub.getTratta(trattaRequest);

                        infoModel.addRow(new Object[]{"", ""}); // Riga vuota per separare
                        infoModel.addRow(new Object[]{"═══ DATI TRATTA ═══", ""});
                        infoModel.addRow(new Object[]{"ID Tratta", tratta.getCodiceTratta()});
                        infoModel.addRow(new Object[]{"Stazione Partenza", tratta.getStazionePartenza()});
                        infoModel.addRow(new Object[]{"Stazione Arrivo", tratta.getStazioneArrivo()});
                        infoModel.addRow(new Object[]{"Data Partenza", tratta.getDataPartenza()});
                        infoModel.addRow(new Object[]{"Data Arrivo", tratta.getDataArrivo()});
                        infoModel.addRow(new Object[]{"Distanza", tratta.getDistanza() + " km"});
                        infoModel.addRow(new Object[]{"Durata Media", tratta.getTempoPercorrenza() + " min"});

                        System.out.println("Informazioni complete caricate per biglietto: " + bigliettoSelezionato.getBigliettoID());

                    } catch (Exception ex) {
                        System.err.println("Errore nel recupero delle informazioni: " + ex.getMessage());
                        infoModel.addRow(new Object[]{"ERRORE", "Impossibile recuperare tutte le informazioni"});
                        infoModel.addRow(new Object[]{"Dettagli errore", ex.getMessage()});
                    }
                }
            });

            // Seleziona automaticamente il primo biglietto se disponibile
            if (comboBiglietti.getItemCount() > 0 && !comboBiglietti.getItemAt(0).equals("Nessun biglietto trovato")) {
                comboBiglietti.setSelectedIndex(0);
            }

            // Assembla il pannello principale
            infoPanel.add(selectionPanel, BorderLayout.NORTH);
            infoPanel.add(scrollPan2e, BorderLayout.CENTER);
            panel = infoPanel;

        } else {
            // Se il cliente non è loggato, mostra messaggio
            JPanel noClientPanel = new JPanel(new GridBagLayout());
            JLabel noClientLabel = new JLabel("Effettua il login per visualizzare le informazioni dei tuoi biglietti");
            noClientLabel.setFont(new Font("Arial", Font.BOLD, 14));
            noClientLabel.setForeground(Color.GRAY);
            noClientPanel.add(noClientLabel);
            panel = noClientPanel;
        }
        tabs.addTab("Info", panel);
    }
    public static void mostraCambioBiglietto(String scelta, String priorita,String trenoID, ManagedChannel channel,boolean flag, int diff) {
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
                    .addDati(scelta) // classe
                    .addDati(trenoID)
                    .addDati(cliente.getCodiceFiscale())
                    .addDati(priorita)
                    .build();

            CreaBigliettoResponse response = stub.creaBiglietto(requestBiglietto);
            //mostraFinestraPagamento(0);
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
    public static void mostraFinestraPagamento(double importo) {
        JDialog dialog = new JDialog((JFrame) null, "Pagamento", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Etichette
        JLabel labelTipo = new JLabel("Tipo: pagamento elettronico");
        JLabel labelProcesso = new JLabel("Processiamo l'operazione...");
        JLabel labelBanca = new JLabel("Contattiamo la banca...");
        JLabel labelImporto = new JLabel("Stai per pagare l'importo di " + importo + " euro.");
        JLabel labelConferma = new JLabel("Confermi l'acquisto?");

        // Allineamento centrale
        labelTipo.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelProcesso.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelBanca.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelImporto.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelConferma.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Bottoni
        JButton annullaButton = new JButton("Annulla");
        JButton confermaButton = new JButton("Conferma");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(annullaButton);
        buttonPanel.add(confermaButton);

        // Azione "Annulla"
        annullaButton.addActionListener(e -> dialog.dispose());

        // Azione "Conferma"
        confermaButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(dialog, "Acquisto effettuato con successo!");
            dialog.dispose();
        });

        // Aggiunta componenti al pannello
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
    }
    private static void autorizzaPagamento() {
        JDialog dialog = new JDialog((JFrame) null , "Autorizzazione Pagamento", true);
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
                int codiceCarta = Integer.parseInt(campoCarta.getText());
                int cvv = Integer.parseInt(campoCVV.getText());
                int scadenza = Integer.parseInt(campoScadenza.getText());
                String circuito = campoCircuito.getText();
                String titolare = campoTitolare.getText();

                boolean validi = Pagamento.inserimentoDati(codiceCarta, cvv, scadenza, circuito, titolare);
                if (validi) {
                    JOptionPane.showMessageDialog(dialog, "Dati validi. Procedo al pagamento.");
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Dati non validi. Riprova.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Inserisci solo numeri nei campi numerici.");
            }
        });

        annulla.addActionListener(e -> dialog.dispose());

        dialog.add(annulla);
        dialog.add(conferma);

        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        //System.setProperty("sun.java2d.uiScale", "3.0");
        SwingUtilities.invokeLater(() -> {
            new ClientDashboardSwing().setVisible(true);
        });
    }
}
