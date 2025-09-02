package it.trenical.server.Tratta;

import it.trenical.server.Treno.Treno;
import it.trenical.server.Treno.TrenoConcr;
import it.trenical.server.notifiche.Observable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrattaImplDB extends Observable implements TrattaImpl {
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
    public TrattaStandard getTratta(String trattaID) {
        String sql = "SELECT * FROM Tratta WHERE  trattaID = ?";
        TrattaStandard tratta = null;

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
        TrattaStandard esistente = getTratta(tratta.getCodiceTratta());
        boolean isUpdate = (esistente != null);

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
            if (isUpdate) {
                notifyObservers("MODIFICATA tratta: " + tratta.getCodiceTratta() +
                        " (" + tratta.getStazionePartenza() + " → " + tratta.getStazioneArrivo() + ")");
            } else {
                notifyObservers("AGGIUNTA tratta: " + tratta.getCodiceTratta() +
                        " (" + tratta.getStazionePartenza() + " → " + tratta.getStazioneArrivo() + ")");
            }
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
    public TrattaStandard getTrattaByIndex(int index) {
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
    /*/
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
    }/*/

    public boolean removeTratta(String trattaID) {
        // Prima recupera i dati per la notifica
        TrattaStandard trattaDaRimuovere = getTratta(trattaID);

        String sql = "DELETE FROM Tratta WHERE trattaID = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, trattaID);
            int righe = stmt.executeUpdate();

            if (righe > 0 && trattaDaRimuovere != null) {

                notifyObservers("RIMOSSA tratta: " + trattaID +
                        " (" + trattaDaRimuovere.getStazionePartenza() +
                        " → " + trattaDaRimuovere.getStazioneArrivo() + ")");
                return true;
            }

            return false;

        } catch (SQLException e) {
            System.err.println("Errore rimozione tratta: " + e.getMessage());
            notifyObservers("ERRORE nella rimozione tratta: " + trattaID);
            return false;
        }
    }

    public String trovaTrattaID(String cittaPartenza, String cittaArrivo) {
        String sql = "SELECT trattaID FROM Tratta WHERE cittaPartenza = ? AND cittaArrivo = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cittaPartenza);
            pstmt.setString(2, cittaArrivo);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("trattaID");
            }
        } catch (SQLException e) {
            System.err.println("Errore nella ricerca della tratta: " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<TrattaStandard> getAllTratte() {
        List<TrattaStandard> tratte = new ArrayList<>();
        String sql = "SELECT * FROM Tratta";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                        String trattaID = rs.getString("trattaID");
                        String stazione_Partenza = rs.getString("stazione_partenza");
                        String stazione_arrivo = rs.getString("stazione_arrivo");
                        String data_partenza = rs.getString("data_partenza");
                        String data_arrivo = rs.getString("data_arrivo");
                        int distanza = rs.getInt("distanza");
                        int durata_viaggio = rs.getInt("durata_viaggio");

                TrattaStandard trt = new TrattaStandard(trattaID,stazione_Partenza,stazione_arrivo,data_partenza,data_arrivo,distanza,durata_viaggio);

                tratte.add(trt);

            }}catch (SQLException e) {
            System.err.println("Errore filtro treno: " + e.getMessage());
        }
        return tratte;
    }

}
