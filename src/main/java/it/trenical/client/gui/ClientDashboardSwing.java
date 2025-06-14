package it.trenical.client.gui;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import it.trenical.grpc.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import it.trenical.grpc.Biglietto;

public class ClientDashboardSwing extends JFrame {
    private static boolean registrato=false;
    private  static Cliente cliente=null;

    public ClientDashboardSwing() {
        setTitle("Terminale Utente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Tratte", creaTabella("Tratta"));
        tabs.addTab("Treni", creaTabella("Treno"));
        tabs.addTab("Biglietti", creaTabella("Biglietto"));
        tabs.addTab("Io",creaTabella("Cliente"));
        tabs.addTab("Notifiche",creaTabella("Notifica"));


        add(tabs);
    }

    JPanel creaTabella(String tabellaNome) {
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
                        "ID", "stazione_Partenza", "stazione_Arrivo", "data_Partenza", "data_Arrivo", "distanza", "durata_viaggio"
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


            case "Treno":
                // Creazione bottone
//                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//                JButton buyButton = new JButton("Acquista Treno");
//                buttonPanel.add(buyButton);
//                panel.add(buttonPanel, BorderLayout.NORTH); //

                TrenoServiceGrpc.TrenoServiceBlockingStub trenoStub = TrenoServiceGrpc.newBlockingStub(channel);
                GetAllTreniRequest requestT = GetAllTreniRequest.newBuilder().build();
                GetAllTreniResponse responseT = trenoStub.getAllTreni(requestT);


                model.setColumnIdentifiers(new String[]{
                        "ID", "Tipo", "TrattaID", "Prezzo", "Posti Prima", "Posti Seconda", "Posti Terza",  "PostiTot", "TempoPercorrenza"
                });
                for (Treno treno : responseT.getTreniList()) {
                    model.addRow(new Object[]{
                            treno.getTrenoID(),
                            treno.getTipoTreno(),
                            treno.getTrattaID(),
                            treno.getPrezzo(),
                            treno.getPostiPrima(),
                            treno.getPostiSeconda(),
                            treno.getPostiTerza(),
                            treno.getPostiTot(),
                            treno.getTempoPercorrenza()});
                }

                // Azione acquisto
//                buyButton.addActionListener(e -> {
//                    int row = table.getSelectedRow();
//                    if (row != -1 && registrato) {
//                        Object id = table.getValueAt(row, 0);
//                        System.out.println("Treno selezionato ID: " + id);
//                        acquista(id, channel);
//                    } else {
//                        JOptionPane.showMessageDialog(panel, "Seleziona un treno e accertati di essere registrato.");
//                    }
//                });
                panel.add(scrollPane, BorderLayout.CENTER);
                break;

            case "Biglietto":
                // Layout a divisione orizzontale (cliente | biglietti)
                JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
                splitPane.setResizeWeight(0.3);
                JPanel buttonPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JButton refreshButton = new JButton("Refresh "+tabellaNome);
                buttonPanel2.add(refreshButton);
                // Colori personalizzati
                Color sfondoPrincipale = panel.getBackground();
                Color sfondoPannelli = new Color(200, 200, 200); // grigio chiaro per il pannello principale
                Color sfondoScuro = new Color(180, 180, 180);    // un po' più scuro per i pannelli interni

                // Pannello Cliente
                JPanel clientePanel = new JPanel();
                clientePanel.setLayout(new GridLayout(5, 2, 5, 5));
                clientePanel.setBorder(BorderFactory.createTitledBorder("Dati Cliente"));
                clientePanel.setBackground(sfondoScuro);
                clientePanel.setOpaque(true);
                JButton bt = new JButton("Acquista Biglietto");
                clientePanel.add(pannelloCLiente(clientePanel,channel));

                // Pannello Biglietti
                JPanel bigliettiPanel = new JPanel(new BorderLayout());
                bigliettiPanel.setBorder(BorderFactory.createTitledBorder("Biglietti del Cliente"));
                bigliettiPanel.setBackground(sfondoScuro);
                bigliettiPanel.setOpaque(true);

                JTable bigliettiTable = new JTable(new DefaultTableModel(
                        new Object[]{"ID", "Classe", "Carrozza", "Posto", "Prezzo"}, 0
                ));
                JScrollPane bigliettiScroll = new JScrollPane(bigliettiTable);
                bigliettiPanel.add(bigliettiScroll, BorderLayout.CENTER);
                if(registrato) {
//                    DefaultTableModel nuovoModel = new DefaultTableModel();
//                    BigliettoServiceGrpc.BigliettoServiceBlockingStub bigliettoStub = BigliettoServiceGrpc.newBlockingStub(channel);
//                    GetBigliettiByFiltroRequest request = GetBigliettiByFiltroRequest.newBuilder()
//                            .setColonna("codiceFiscale")  // esempio: "dataPartenza", "trenoID", "codiceCliente", ecc.
//                            .setValore(cliente.getCodiceFiscale())           // il valore da cercare
//                            .build();
//                   GetBigliettiByFiltroResponse response = bigliettoStub.getBigliettiByFiltro(request);
//                    List<Biglietto> biglietti = response.getBigliettiList();
//                    aggiungiBigliettiATabella(biglietti,model);
                    caricaDatiDaDB(model,channel);

                }
                refreshButton.addActionListener(e -> {
                    DefaultTableModel nuovoModel = new DefaultTableModel(
                            new Object[]{"ID", "Classe", "Carrozza", "Posto", "Prezzo"}, 0
                    );
                    caricaDatiDaDB(nuovoModel, channel); // carica i dati nel modello corretto
                    bigliettiTable.setModel(nuovoModel); // aggiorna la tabella
                });

                // Assembla i due pannelli
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
                    b.getCarrozza(),
                    b.getPosto(),
                    b.getPrezzo()
            });
        }


    }

// rivedi la dimensione del pannello e il refresh
    private static JPanel pannelloCLiente(JPanel panel, ManagedChannel channel) {
        JPanel clientePanel  = new JPanel();
        clientePanel.removeAll(); // pulizia per refresh
        clientePanel.setLayout(new GridLayout(6, 2, 5, 5));
        clientePanel.setMaximumSize(new Dimension(400, 220));
        clientePanel.setBackground(new Color(180, 180, 180));

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

//            clientePanel.add(new JLabel("Codice Cliente:"));
//            JTextField codiceClienteField = new JTextField(20);
//            clientePanel.add(codiceClienteField);

            clientePanel.add(new JLabel("Età:"));
            JTextField etaField = new JTextField(5);
            clientePanel.add(etaField);


            JButton submitButton = new JButton("Submit");
            clientePanel.add(new JLabel()); // spazio vuoto per allineamento
            clientePanel.add(submitButton);
            clientePanel.revalidate();
            //clientePanel.repaint();

            submitButton.addActionListener(e -> {
                String cf = codiceFiscaleField.getText();
                String nome = nomeField.getText();
                String cognome = cognomeField.getText();
                String etaStr = etaField.getText();
                System.out.println(cf+" "+nome+" "+cognome+" "+etaStr);
                if (cf.isEmpty() || nome.isEmpty() || cognome.isEmpty() || etaStr.isEmpty()) {
                    JOptionPane.showMessageDialog(clientePanel, "Completa tutti i campi prima di continuare.");
                } else {
                    try {
                        IDGeneratorServiceGrpc.IDGeneratorServiceBlockingStub idStub = IDGeneratorServiceGrpc.newBlockingStub(channel);
                        GetGeneratedIDRequest requestID = GetGeneratedIDRequest.newBuilder().build();
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
                            // aggiorna stato
                            registrato = true;
                            cliente = clienteGrpc;
                            System.out.println(cliente);
                            System.out.println(registrato);
                            // refresh pannello

                            clientePanel.revalidate();
                            clientePanel.setVisible(false);
                            panel.add(panelloClienteRegistrato( channel));
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
        private static JPanel panelloClienteRegistrato(ManagedChannel channel) {

            JPanel clientePanel = new JPanel();

            clientePanel.removeAll(); // pulizia per refresh
            clientePanel.setLayout(new GridLayout(6, 2, 5, 5));
            clientePanel.setMaximumSize(new Dimension(400, 220));
            clientePanel.setBackground(new Color(180, 180, 180));

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
        private static void acquista(Object id, ManagedChannel channel) {
        IDGeneratorServiceGrpc.IDGeneratorServiceBlockingStub idStub = IDGeneratorServiceGrpc.newBlockingStub(channel);
        GetGeneratedIDRequest requestID = GetGeneratedIDRequest.newBuilder().build();
        GetGeneratedIDResponse responseID = idStub.getGeneratedID(requestID);
            /// DEVI USARE LA CLASSE PAGAMENTO E SIMULARE LA TRANSAZIONE
        it.trenical.grpc.BigliettoServiceGrpc.BigliettoServiceBlockingStub bigliettoStub = it.trenical.grpc.BigliettoServiceGrpc.newBlockingStub(channel);
        it.trenical.grpc.Biglietto biglietto = it.trenical.grpc.Biglietto.newBuilder()

                .setBigliettoID(responseID.getBigliettoID())
                .setClasse("SecondaClasse")
                .setTrenoID("1001")
                .setCarrozza("A")
                .setPosto("12A")
                .setClienteID(responseID.getClienteID())
                .addPriorita("Finestrino")
                .setPrezzo(49)
                .build();


    }
    private static void acquistaBiglietto(Object id, ManagedChannel channel) {
        if (!registrato) {
            throw new IllegalStateException("Ti devi registrare per poter acquistare.");
        }

        System.out.println("Utente registrato");

        // Crea finestra indipendente
        DefaultTableModel model = new DefaultTableModel();
        JDialog dialog = new JDialog((Frame) null, "Acquisto Biglietto", true); // true = modale
        dialog.setSize(800, 400);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());

        // Pannello centrale con tabella treni
        JPanel centerPanel = new JPanel(new BorderLayout());
        JTable treniTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(treniTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Caricamento treni
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


        // Pannello inferiore con bottone
        JButton conferma = new JButton("Conferma Selezione");

        conferma.addActionListener(e -> {
            int selectedRow = treniTable.getSelectedRow();
            if (selectedRow != -1) {
                String trenoID = model.getValueAt(selectedRow, 0).toString(); // recupera la prima colonna (ID)
                JOptionPane.showMessageDialog(dialog, "Hai selezionato il treno ID: " + trenoID);
                dialog.dispose(); // chiudi la finestra se vuoi
            } else {
                JOptionPane.showMessageDialog(dialog, "Seleziona una riga prima di confermare.");
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(conferma);

// Aggiungi tutto al dialog
        dialog.add(centerPanel, BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }


    public static void main(String[] args) {
        //System.setProperty("sun.java2d.uiScale", "3.0");
        SwingUtilities.invokeLater(() -> {
            new ClientDashboardSwing().setVisible(true);
        });
    }
}
