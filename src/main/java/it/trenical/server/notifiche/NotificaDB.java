package it.trenical.server.notifiche;

import it.trenical.server.Cliente.Cliente;
import it.trenical.server.Cliente.ClienteImplDB;
import it.trenical.server.Treno.Treno;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificaDB {
    private static final String url = "jdbc:sqlite:db/treniCal.db";
    private static final NotificaDB instance = new NotificaDB();


    private NotificaDB() {
        if (instance != null) {
            throw new RuntimeException("Usa getInstance() per ottenere l'istanza di NotificaDB");
        }
    }

    public static NotificaDB getInstance() {
        return instance;
    }
    public void setNotifica(Notifica nt) {
        String sql = "INSERT OR REPLACE INTO Notifica (cliente, treno, partenza, arrivo, tempo, biglietto, stato, posto, binario, log) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nt.getCliente());
            stmt.setString(2, nt.getTreno());
            stmt.setString(3, nt.getPartenza());
            stmt.setString(4, nt.getArrivo());
            stmt.setInt(5, nt.getTempo());
            stmt.setString(6, nt.getBiglietto());
            stmt.setString(7, nt.getStato());        // NUOVO
            stmt.setString(8, nt.getPosto());        // NUOVO
            stmt.setInt(9, nt.getBinario());         // NUOVO
            stmt.setString(10, nt.getLog());         // NUOVO

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errore inserimento notifica: " + e.getMessage());
        }
    }

    public List<Notifica> getNotifica(String cl) {
        List<Notifica> notifiche = new ArrayList<>();

        String sql = "SELECT * FROM Notifica WHERE cliente = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cl);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Notifica notifica = new Notifica(
                        rs.getString("cliente"),
                        rs.getString("treno"),
                        rs.getString("partenza"),
                        rs.getString("arrivo"),
                        rs.getInt("tempo"),
                        rs.getString("biglietto"),
                        rs.getString("stato"),        // NUOVO
                        rs.getString("posto"),        // NUOVO
                        rs.getInt("binario"),         // NUOVO
                        rs.getString("log")           // NUOVO
                );
                notifiche.add(notifica);
            }

        } catch (SQLException e) {
            System.err.println("Errore lettura notifiche: " + e.getMessage());
        }

        return notifiche;
    }

    public void removeNotifica(Treno tr) {
        String sql = "DELETE FROM Notifica WHERE treno = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tr.getTrenoID());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Errore rimozione notifica per treno: " + e.getMessage());
        }
    }

}
