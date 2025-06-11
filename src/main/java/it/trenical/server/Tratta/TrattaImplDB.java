package it.trenical.server.Tratta;

import it.trenical.server.Cliente.Cliente;
import it.trenical.server.Cliente.ClienteConcr;
import it.trenical.server.Treno.TrenoImplDB;

import java.sql.*;

public class TrattaImplDB implements TrattaImpl {
    private final String url = "jdbc:sqlite:db/treniCal.db";
    private static final TrattaImplDB instance = new TrattaImplDB();
    public static TrattaImplDB getInstance() {
        return instance;
    }

    private TrattaImplDB() {
        if (instance != null) {
            throw new RuntimeException("Usa getInstance() per ottenere l'istanza di TrattaImplDB");
        }
    }
    @Override
    public TrattaPrototype getTratta(String trattaID) {
        String sql = "SELECT * FROM Cliente WHERE  trattaID = ?";
        TrattaPrototype tratta = null;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, trattaID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String stazione_partenza = rs.getString("stazione_partenza");
                String stazione_arrivo = rs.getString("stazione_arrivo");
                String data_partenza = rs.getString("data_partenza");
                String data_arrivo = rs.getString("data_arrivo");
                int distanza = rs.getInt("distanza");
                int durata_viaggio = rs.getInt("durata_viaggio");


               tratta = new TrattaStandard(trattaID,stazione_partenza,stazione_arrivo,data_partenza,data_arrivo,distanza,durata_viaggio);
            }

        } catch (SQLException e) {
            System.err.println("Errore recupero cliente: " + e.getMessage());
        }

        return tratta;
    }

    @Override
    public void setTratta(TrattaPrototype tratta) {
        String sql = "INSERT OR REPLACE INTO Tratta (trattaID, stazione_partenza,stazione_arrivo,data_partenza,data_arrivo,distanza,durata_viaggio) " +
                "VALUES (?, ?, ?, ?, ?,?,?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tratta.getCodiceTratta());
            stmt.setString(2, tratta.getStazionePartenza());
            stmt.setString(3, tratta.getStazioneArrivo());
            stmt.setString(4, tratta.getDataPartenza());
            stmt.setString(5, tratta.getDataArrivo());
            stmt.setInt(6, tratta.getDistanza());
            stmt.setInt(7, tratta.getTempoPercorrenza());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Errore inserimento cliente: " + e.getMessage());
        }
    }

    public int countTratte() {
        String sql = "SELECT COUNT(*) FROM Tratta";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Errore conteggio tratte: " + e.getMessage());
        }
        return 0;
    }
    public TrattaPrototype getTrattaByIndex(int index) {
        String sql = "SELECT * FROM Tratta ORDER BY trattaID LIMIT 1 OFFSET ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, index);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new TrattaStandard(
                        rs.getString("trattaID"),
                        rs.getString("stazione_partenza"),
                        rs.getString("stazione_arrivo"),
                        rs.getString("data_partenza"),
                        rs.getString("data_arrivo"),
                        rs.getInt("distanza"),
                        rs.getInt("durata_viaggio")
                );
            }

        } catch (SQLException e) {
            System.err.println("Errore durante il recupero della tratta per indice: " + e.getMessage());
        }
        return null;
    }


    @Override
    public boolean removeTratta(String trattaID) {
        String sql = "DELETE FROM Tratta WHERE trattaID = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, trattaID);
            int righe = stmt.executeUpdate();
            return righe > 0;
        } catch (SQLException e) {
            System.err.println("Errore rimozione treno: " + e.getMessage());
            return false;
        }
    }
}
