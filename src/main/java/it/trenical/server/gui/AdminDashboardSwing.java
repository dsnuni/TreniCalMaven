package it.trenical.server.gui;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.*;
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

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(refreshButton);

        addButton.addActionListener(e -> apriFinestraAdd(tabellaNome));

        removeButton.addActionListener(e -> {
            int row = table.getSelectedRow();
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

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void caricaDatiDaDB(String tabella, DefaultTableModel model) {
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

    private void rimuoviDaDB(String tabella, Object id) {
        String sql = "DELETE FROM " + tabella + " WHERE rowid = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errore nella rimozione dalla tabella " + tabella + ": " + e.getMessage());
        }
    }

    private void apriFinestraAdd(String tabella) {
        JFrame frame = new JFrame("Add new entry to " + tabella);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(0, 2));
        JTextField[] fields = new JTextField[5];

        for (int i = 0; i < fields.length; i++) {
            inputPanel.add(new JLabel("Campo " + (i + 1)));
            fields[i] = new JTextField();
            inputPanel.add(fields[i]);
        }

        JButton submit = new JButton("Submit");
        submit.addActionListener(e -> {
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement("INSERT INTO " + tabella + " VALUES (?, ?, ?, ?, ?);")) {
                for (int i = 0; i < 5; i++) {
                    pstmt.setString(i + 1, fields[i].getText());
                }
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Inserimento riuscito");
                frame.dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Errore: " + ex.getMessage());
            }
        });

        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(submit, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AdminDashboardSwing().setVisible(true);
        });
    }
}
