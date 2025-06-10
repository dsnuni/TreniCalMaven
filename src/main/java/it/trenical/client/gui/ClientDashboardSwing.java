package it.trenical.client.gui;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import it.trenical.grpc.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClientDashboardSwing extends JFrame {
    private static boolean registrato=false;
    private  static Cliente cliente=null;

    public ClientDashboardSwing() {
        setTitle("Terminale Utente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Treni", creaTabella("Treni"));
        tabs.addTab("Io",creaTabella("Biglietto"));


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
            case "Treni":
                // Creazione bottone
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JButton buyButton = new JButton("Acquista Treno");
                buttonPanel.add(buyButton);
                panel.add(buttonPanel, BorderLayout.NORTH); // ✅ aggiunto in alto

                TrenoServiceGrpc.TrenoServiceBlockingStub trenoStub = TrenoServiceGrpc.newBlockingStub(channel);
                GetAllTreniRequest requestT = GetAllTreniRequest.newBuilder().build();
                GetAllTreniResponse responseT = trenoStub.getAllTreni(requestT);

                model.setColumnIdentifiers(new String[]{
                        "ID", "Tipo", "Partenza", "Arrivo", "Data Partenza", "Data Arrivo"
                });
                for (Treno treno : responseT.getTreniList()) {
                    model.addRow(new Object[]{
                            treno.getTrenoID(),
                            treno.getTipoTreno(),
                            treno.getTratta().getStazionePartenza(),
                            treno.getTratta().getStazioneArrivo(),
                            treno.getTratta().getDataPartenza(),
                            treno.getTratta().getDataArrivo()
                    });
                }

                // Azione acquisto
                buyButton.addActionListener(e -> {
                    int row = table.getSelectedRow();
                    if (row != -1 && registrato) {
                        Object id = table.getValueAt(row, 0);
                        System.out.println("Treno selezionato ID: " + id);
                        acquista(id, channel);
                    } else {
                        JOptionPane.showMessageDialog(panel, "Seleziona un treno e accertati di essere registrato.");
                    }
                });
                panel.add(scrollPane, BorderLayout.CENTER);
                break;

            case "Biglietto":
                // Layout a divisione orizzontale (cliente | biglietti)
                JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
                splitPane.setResizeWeight(0.3);

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
                clientePanel.add(pannelloCLiente(panel,channel));

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

                // Assembla i due pannelli
                splitPane.setLeftComponent(clientePanel);
                splitPane.setRightComponent(bigliettiPanel);

                panel.setLayout(new BorderLayout());
                panel.setBackground(sfondoPannelli); // sfondo più chiaro per contrasto
                panel.setOpaque(true);
                panel.add(splitPane, BorderLayout.CENTER);


                break;
        }



        return panel;
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
                //String codice = codiceClienteField.getText();
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
                            pannelloCLiente(clientePanel, channel);
                            clientePanel.revalidate();
                            clientePanel.repaint();

                        } else {
                            JOptionPane.showMessageDialog(clientePanel, "Errore: cliente non aggiunto.");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(clientePanel, "Errore: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            });

        } else { //cliente registrato

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
        }
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
                .setTrenoID(1001)
                .setCarrozza("A")
                .setPosto("12A")
                .setClienteID(responseID.getClienteID())
                .addPriorita("Finestrino")
                .setPrezzo(49)
                .build();


    }
    public static void main(String[] args) {
        System.setProperty("sun.java2d.uiScale", "3.0");
        SwingUtilities.invokeLater(() -> {
            new ClientDashboardSwing().setVisible(true);
        });
    }
}
