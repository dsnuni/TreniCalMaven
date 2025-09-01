package it.trenical.client.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TabellaBigliettiTreno extends JFrame {

    private DefaultTableModel tableModel;
    private JTable tabellaBiglietto;
    private JLabel lblTitolo;

    // Dati del biglietto
    private String idBiglietto = "BGL001";
    private String classe = "PrimaClasse";
    private String trenoId = "TR001";
    private int carrozza = 1;
    private int posto = 12;
    private String priorita = "Posto finestrino";
    private int prezzo = 89;
    private String tipoTreno = "Frecciarossa";
    private int tempoPercorrenza = 180;
    private String stazionePartenza = "Milano Centrale";
    private String stazioneArrivo = "Roma Termini";
    private String dataPartenza = "2025-09-15 08:30";
    private String dataArrivo = "2025-09-15 11:30";
    private int distanza = 574;

    public TabellaBigliettiTreno() {
        setTitle("🎫 TreniCal - Biglietto Digitale");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        initializeComponents();
        setupLayout();
        populateBigliettoData();
        setupEventListeners();
    }

    private void initializeComponents() {
        // Tabella con due colonne: Campo e Valore
        String[] colonne = {"Campo", "Valore"};

        tableModel = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Biglietto non editabile una volta emesso
            }
        };

        tabellaBiglietto = new JTable(tableModel);
        setupTableAppearance();
    }

    private void setupTableAppearance() {
        // Aspetto della tabella tipo "biglietto"
        tabellaBiglietto.setRowHeight(30);
        tabellaBiglietto.setFont(new Font("Courier New", Font.PLAIN, 14)); // Font monospace per aspetto "biglietto"
        tabellaBiglietto.setGridColor(new Color(100, 100, 100));
        tabellaBiglietto.setShowGrid(true);
        tabellaBiglietto.setBackground(new Color(255, 255, 240)); // Colore carta crema

        // Header della tabella
        JTableHeader header = tabellaBiglietto.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(41, 128, 185)); // Blu TreniCal
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        // Renderer personalizzato
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        rightRenderer.setFont(new Font("Courier New", Font.BOLD, 14));

        // Prima colonna (etichette) - grassetto e allineato a sinistra
        tabellaBiglietto.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setFont(new Font("Arial", Font.BOLD, 13));
                setHorizontalAlignment(JLabel.LEFT);
                setBackground(new Color(240, 240, 240)); // Grigio chiaro per le etichette
                return c;
            }
        });

        // Seconda colonna (valori) - normale e allineato a destra
        tabellaBiglietto.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setFont(new Font("Courier New", Font.PLAIN, 13));
                setHorizontalAlignment(JLabel.RIGHT);
                setBackground(new Color(255, 255, 240));

                // Colore speciale per il prezzo
                if (table.getValueAt(row, 0).equals("💰 Prezzo")) {
                    setFont(new Font("Courier New", Font.BOLD, 14));
                    setForeground(new Color(0, 128, 0)); // Verde per il prezzo
                } else {
                    setForeground(Color.BLACK);
                }

                return c;
            }
        });

        // Larghezza delle colonne
        tabellaBiglietto.getColumnModel().getColumn(0).setPreferredWidth(250); // Campo
        tabellaBiglietto.getColumnModel().getColumn(1).setPreferredWidth(300); // Valore
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(248, 248, 255));

        // Pannello principale con bordo decorativo
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        mainPanel.setBackground(new Color(255, 255, 240));

        // Titolo del biglietto
        lblTitolo = new JLabel("🚄 BIGLIETTO DIGITALE TRENICAL");
        lblTitolo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitolo.setForeground(new Color(41, 128, 185));
        lblTitolo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitolo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(41, 128, 185)),
                BorderFactory.createEmptyBorder(10, 0, 15, 0)
        ));

        // Scroll pane per la tabella (senza scroll visibili)
        JScrollPane scrollPane = new JScrollPane(tabellaBiglietto);
        scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Pannello bottoni
        JPanel buttonPanel = createButtonPanel();

        // Footer con info
        JLabel footerLabel = new JLabel("🎫 Conserva questo biglietto per tutta la durata del viaggio");
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerLabel.setForeground(Color.GRAY);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        mainPanel.add(lblTitolo, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
        add(footerLabel, BorderLayout.SOUTH);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(new Color(255, 255, 240));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton btnStampa = new JButton("🖨️ Stampa");
        JButton btnSalva = new JButton("💾 Salva PDF");
        JButton btnNuovo = new JButton("🎫 Nuovo Biglietto");
        JButton btnModifica = new JButton("✏️ Modifica Dati");

        // Stile bottoni
        JButton[] buttons = {btnStampa, btnSalva, btnNuovo, btnModifica};
        for (JButton btn : buttons) {
            btn.setBackground(new Color(52, 152, 219));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            btn.setFont(new Font("Arial", Font.PLAIN, 12));
        }

        panel.add(btnStampa);
        panel.add(btnSalva);
        panel.add(btnNuovo);
        panel.add(btnModifica);

        return panel;
    }

    private void populateBigliettoData() {
        // Pulisce i dati esistenti
        tableModel.setRowCount(0);

        // Aggiunge tutti i campi del biglietto con emoticon per renderlo più carino
        tableModel.addRow(new Object[]{"🎫 ID Biglietto", idBiglietto});
        tableModel.addRow(new Object[]{"🏷️ Classe", formatClasse(classe)});
        tableModel.addRow(new Object[]{"🚂 Treno ID", trenoId});
        tableModel.addRow(new Object[]{"🚃 Carrozza", "N° " + carrozza});
        tableModel.addRow(new Object[]{"💺 Posto", "N° " + posto});
        tableModel.addRow(new Object[]{"⭐ Priorità", priorita.isEmpty() ? "Nessuna" : priorita});
        tableModel.addRow(new Object[]{"💰 Prezzo", "€ " + prezzo + ".00"});

        // Separator visivo
        tableModel.addRow(new Object[]{"━━━━━━━━━━━━━━━━━━━━", "━━━━━━━━━━━━━━━━━━━━"});

        tableModel.addRow(new Object[]{"🚄 Tipo Treno", tipoTreno});
        tableModel.addRow(new Object[]{"⏱️ Durata Viaggio", formatTempo(tempoPercorrenza)});
        tableModel.addRow(new Object[]{"🏁 Partenza", stazionePartenza});
        tableModel.addRow(new Object[]{"🎯 Arrivo", stazioneArrivo});
        tableModel.addRow(new Object[]{"📅 Data Partenza", formatData(dataPartenza)});
        tableModel.addRow(new Object[]{"📅 Data Arrivo", formatData(dataArrivo)});
        tableModel.addRow(new Object[]{"📏 Distanza", distanza + " km"});

        // Update titolo con info viaggio
        lblTitolo.setText("🚄 " + stazionePartenza + " → " + stazioneArrivo);
    }

    private String formatClasse(String classe) {
        switch (classe) {
            case "PrimaClasse": return "🥇 Prima Classe";
            case "SecondaClasse": return "🥈 Seconda Classe";
            case "TerzaClasse": return "🥉 Terza Classe";
            default: return classe;
        }
    }

    private String formatTempo(int minuti) {
        int ore = minuti / 60;
        int min = minuti % 60;
        return ore + "h " + min + "min";
    }

    private String formatData(String dataOra) {
        // Assume formato "2025-09-15 08:30"
        String[] parti = dataOra.split(" ");
        if (parti.length == 2) {
            return parti[0] + " alle " + parti[1];
        }
        return dataOra;
    }

    private void setupEventListeners() {
        // Trova i bottoni e aggiungi i listener
        findButtonsAndSetListeners(this);
    }

    private void findButtonsAndSetListeners(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                String text = button.getText();

                if (text.contains("Stampa")) {
                    button.addActionListener(e -> {
                        JOptionPane.showMessageDialog(this,
                                "Funzione di stampa simulata!\n" +
                                        "Biglietto " + idBiglietto + " pronto per la stampa.",
                                "Stampa", JOptionPane.INFORMATION_MESSAGE);
                    });
                } else if (text.contains("Salva")) {
                    button.addActionListener(e -> {
                        JOptionPane.showMessageDialog(this,
                                "Biglietto salvato come PDF!\n" +
                                        "File: biglietto_" + idBiglietto + ".pdf",
                                "Salvataggio", JOptionPane.INFORMATION_MESSAGE);
                    });
                } else if (text.contains("Nuovo")) {
                    button.addActionListener(e -> generaNuovoBiglietto());
                } else if (text.contains("Modifica")) {
                    button.addActionListener(e -> modificaDatiBiglietto());
                }
            } else if (component instanceof Container) {
                findButtonsAndSetListeners((Container) component);
            }
        }
    }

    private void generaNuovoBiglietto() {
        // Simula la generazione di un nuovo biglietto
        idBiglietto = "BGL" + String.format("%03d", (int)(Math.random() * 999 + 1));
        trenoId = "TR" + String.format("%03d", (int)(Math.random() * 999 + 1));
        carrozza = (int)(Math.random() * 8 + 1);
        posto = (int)(Math.random() * 80 + 1);
        prezzo = (int)(Math.random() * 150 + 25);

        populateBigliettoData();

        JOptionPane.showMessageDialog(this,
                "Nuovo biglietto generato!\nID: " + idBiglietto,
                "Nuovo Biglietto", JOptionPane.INFORMATION_MESSAGE);
    }

    private void modificaDatiBiglietto() {
        JDialog dialog = new JDialog(this, "Modifica Biglietto", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(6, 2, 5, 5));

        JTextField fieldClasse = new JTextField(classe);
        JTextField fieldCarrozza = new JTextField(String.valueOf(carrozza));
        JTextField fieldPosto = new JTextField(String.valueOf(posto));
        JTextField fieldPriorita = new JTextField(priorita);
        JTextField fieldPrezzo = new JTextField(String.valueOf(prezzo));

        dialog.add(new JLabel("Classe:"));
        dialog.add(fieldClasse);
        dialog.add(new JLabel("Carrozza:"));
        dialog.add(fieldCarrozza);
        dialog.add(new JLabel("Posto:"));
        dialog.add(fieldPosto);
        dialog.add(new JLabel("Priorità:"));
        dialog.add(fieldPriorita);
        dialog.add(new JLabel("Prezzo:"));
        dialog.add(fieldPrezzo);

        JButton btnSalva = new JButton("Salva");
        JButton btnAnnulla = new JButton("Annulla");

        btnSalva.addActionListener(e -> {
            classe = fieldClasse.getText();
            carrozza = Integer.parseInt(fieldCarrozza.getText());
            posto = Integer.parseInt(fieldPosto.getText());
            priorita = fieldPriorita.getText();
            prezzo = Integer.parseInt(fieldPrezzo.getText());

            populateBigliettoData();
            dialog.dispose();

            JOptionPane.showMessageDialog(this, "Biglietto modificato con successo!");
        });

        btnAnnulla.addActionListener(e -> dialog.dispose());

        dialog.add(btnSalva);
        dialog.add(btnAnnulla);

        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TabellaBigliettiTreno().setVisible(true);



        });
    }
}