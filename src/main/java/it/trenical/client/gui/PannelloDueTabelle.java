package it.trenical.client.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PannelloDueTabelle extends JFrame {

    private DefaultTableModel modelSinistra;
    private DefaultTableModel modelDestra;
    private JTable tabellaSinistra;
    private JTable tabellaDestra;

    public PannelloDueTabelle() {
        setTitle("Pannello con Due Tabelle Affiancate - Prova");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        initializeComponents();
        setupLayout();
        populateData();
        setupEventListeners();
    }

    private void initializeComponents() {
        // Modelli delle tabelle
        modelSinistra = new DefaultTableModel();
        modelDestra = new DefaultTableModel();

        // Tabelle
        tabellaSinistra = new JTable(modelSinistra);
        tabellaDestra = new JTable(modelDestra);

        // Personalizzazioni visive
        setupTableAppearance(tabellaSinistra);
        setupTableAppearance(tabellaDestra);
    }

    private void setupTableAppearance(JTable table) {
        table.setRowHeight(25);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setSelectionBackground(new Color(173, 216, 230)); // azzurro chiaro
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(70, 130, 180)); // blu acciaio
        table.getTableHeader().setForeground(Color.WHITE);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Pannello principale con layout orizzontale
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5); // divisione 50-50
        splitPane.setDividerSize(5);

        // Pannello sinistro
        JPanel panelSinistro = new JPanel(new BorderLayout());
        panelSinistro.setBorder(BorderFactory.createTitledBorder("Tabella Studenti"));
        panelSinistro.setBackground(new Color(240, 248, 255)); // alice blue

        JScrollPane scrollSinistro = new JScrollPane(tabellaSinistra);
        panelSinistro.add(scrollSinistro, BorderLayout.CENTER);

        // Pannello destro
        JPanel panelDestro = new JPanel(new BorderLayout());
        panelDestro.setBorder(BorderFactory.createTitledBorder("Tabella Corsi"));
        panelDestro.setBackground(new Color(255, 248, 240)); // antique white

        JScrollPane scrollDestro = new JScrollPane(tabellaDestra);
        panelDestro.add(scrollDestro, BorderLayout.CENTER);

        // Pannello bottoni
        JPanel panelBottoni = createButtonPanel();

        // Assembla tutto
        splitPane.setLeftComponent(panelSinistro);
        splitPane.setRightComponent(panelDestro);

        add(splitPane, BorderLayout.CENTER);
        add(panelBottoni, BorderLayout.SOUTH);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(new Color(245, 245, 245));

        JButton btnAggiungiStudente = new JButton("+ Studente");
        JButton btnAggiungiCorso = new JButton("+ Corso");
        JButton btnRimuoviStudente = new JButton("- Studente");
        JButton btnRimuoviCorso = new JButton("- Corso");
        JButton btnClearAll = new JButton("Pulisci Tutto");

        // Stile bottoni
        Color buttonColor = new Color(100, 149, 237); // cornflower blue
        btnAggiungiStudente.setBackground(buttonColor);
        btnAggiungiCorso.setBackground(buttonColor);
        btnRimuoviStudente.setBackground(new Color(220, 20, 60)); // crimson
        btnRimuoviCorso.setBackground(new Color(220, 20, 60));
        btnClearAll.setBackground(new Color(255, 165, 0)); // orange

        btnAggiungiStudente.setForeground(Color.WHITE);
        btnAggiungiCorso.setForeground(Color.WHITE);
        btnRimuoviStudente.setForeground(Color.WHITE);
        btnRimuoviCorso.setForeground(Color.WHITE);
        btnClearAll.setForeground(Color.WHITE);

        panel.add(btnAggiungiStudente);
        panel.add(btnAggiungiCorso);
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(btnRimuoviStudente);
        panel.add(btnRimuoviCorso);
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(btnClearAll);

        // Event listeners per i bottoni
        btnAggiungiStudente.addActionListener(e -> aggiungiStudente());
        btnAggiungiCorso.addActionListener(e -> aggiungiCorso());
        btnRimuoviStudente.addActionListener(e -> rimuoviRigaSelezionata(tabellaSinistra, modelSinistra));
        btnRimuoviCorso.addActionListener(e -> rimuoviRigaSelezionata(tabellaDestra, modelDestra));
        btnClearAll.addActionListener(e -> {
            modelSinistra.setRowCount(0);
            modelDestra.setRowCount(0);
        });

        return panel;
    }

    private void populateData() {
        // Popola tabella studenti (sinistra)
        modelSinistra.setColumnIdentifiers(new String[]{"ID", "Nome", "Cognome", "Età", "Corso di Studi"});
        modelSinistra.addRow(new Object[]{1, "Mario", "Rossi", 22, "Informatica"});
        modelSinistra.addRow(new Object[]{2, "Giulia", "Verdi", 21, "Ingegneria"});
        modelSinistra.addRow(new Object[]{3, "Luca", "Bianchi", 23, "Economia"});
        modelSinistra.addRow(new Object[]{4, "Anna", "Neri", 20, "Medicina"});
        modelSinistra.addRow(new Object[]{5, "Paolo", "Blu", 24, "Lettere"});

        // Popola tabella corsi (destra)
        modelDestra.setColumnIdentifiers(new String[]{"Codice", "Nome Corso", "Crediti", "Docente", "Semestre"});
        modelDestra.addRow(new Object[]{"INF001", "Programmazione Java", 9, "Prof. Bianchi", "I"});
        modelDestra.addRow(new Object[]{"MAT002", "Analisi Matematica", 12, "Prof. Rossi", "I"});
        modelDestra.addRow(new Object[]{"FIS003", "Fisica Generale", 9, "Prof. Verdi", "II"});
        modelDestra.addRow(new Object[]{"ING004", "Sistemi Operativi", 6, "Prof. Neri", "II"});
        modelDestra.addRow(new Object[]{"ECO005", "Microeconomia", 8, "Prof. Gialli", "I"});
    }

    private void setupEventListeners() {
        // Listener per selezione riga tabella studenti
        tabellaSinistra.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tabellaSinistra.getSelectedRow();
                if (selectedRow != -1) {
                    Object nome = modelSinistra.getValueAt(selectedRow, 1);
                    Object cognome = modelSinistra.getValueAt(selectedRow, 2);
                    System.out.println("Studente selezionato: " + nome + " " + cognome);
                }
            }
        });

        // Listener per selezione riga tabella corsi
        tabellaDestra.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tabellaDestra.getSelectedRow();
                if (selectedRow != -1) {
                    Object nomeCorso = modelDestra.getValueAt(selectedRow, 1);
                    Object crediti = modelDestra.getValueAt(selectedRow, 2);
                    System.out.println("Corso selezionato: " + nomeCorso + " (" + crediti + " crediti)");
                }
            }
        });
    }

    private void aggiungiStudente() {
        int nextId = modelSinistra.getRowCount() + 1;
        modelSinistra.addRow(new Object[]{
                nextId,
                "Nuovo",
                "Studente",
                20,
                "Da Definire"
        });

        // Seleziona automaticamente la nuova riga
        int newRow = modelSinistra.getRowCount() - 1;
        tabellaSinistra.setRowSelectionInterval(newRow, newRow);
        tabellaSinistra.scrollRectToVisible(tabellaSinistra.getCellRect(newRow, 0, true));
    }

    private void aggiungiCorso() {
        String nextCode = "NEW00" + (modelDestra.getRowCount() + 1);
        modelDestra.addRow(new Object[]{
                nextCode,
                "Nuovo Corso",
                6,
                "Prof. Da Assegnare",
                "I"
        });

        // Seleziona automaticamente la nuova riga
        int newRow = modelDestra.getRowCount() - 1;
        tabellaDestra.setRowSelectionInterval(newRow, newRow);
        tabellaDestra.scrollRectToVisible(tabellaDestra.getCellRect(newRow, 0, true));
    }

    private void rimuoviRigaSelezionata(JTable table, DefaultTableModel model) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            model.removeRow(selectedRow);
            System.out.println("Riga rimossa: " + selectedRow);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Seleziona una riga da rimuovere!",
                    "Nessuna selezione",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    // Metodo per ottenere informazioni sulle selezioni correnti
    public void printSelectionInfo() {
        int studentiSelezionati = tabellaSinistra.getSelectedRowCount();
        int corsiSelezionati = tabellaDestra.getSelectedRowCount();

        System.out.println("=== INFO SELEZIONI ===");
        System.out.println("Studenti selezionati: " + studentiSelezionati);
        System.out.println("Corsi selezionati: " + corsiSelezionati);
        System.out.println("Totale righe studenti: " + modelSinistra.getRowCount());
        System.out.println("Totale righe corsi: " + modelDestra.getRowCount());
    }

    // Main per testare il pannello
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PannelloDueTabelle().setVisible(true);

            PannelloDueTabelle frame = new PannelloDueTabelle();
            frame.setVisible(true);

            // Test stampa info dopo 3 secondi
            Timer timer = new Timer(3000, e -> frame.printSelectionInfo());
            timer.setRepeats(false);
            timer.start();
        });
    }
}