package it.trenical.server.Treno;

import it.trenical.server.Tratta.*;


import java.sql.*;


public class TrenoImplDB implements TrenoImpl{
    private final String url = "jdbc:sqlite:db/treniCal.db";

    @Override
    public Treno getTreno(int id) {
        String sql = "SELECT * FROM Treno WHERE trenoID = ?";
        Treno t = null;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // ricostruisco TrattaPrototype (es. come TrattaStandard)
                TrattaPrototype tratta = new TrattaStandard(
                        rs.getString("trattaID"),
                        rs.getString("stazione_partenza"),
                        rs.getString("stazione_arrivo"),
                        rs.getString("data_partenza"),
                        rs.getString("data_arrivo"),
                        rs.getInt("distanza"),
                        rs.getInt("durata_viaggio")
                );

                t = new TrenoConcr(rs.getInt("trenoID"), rs.getString("tipoTreno"), tratta);
            }

        } catch (SQLException e) {
            System.err.println("Errore lettura treno: " + e.getMessage());
        }

        return t;
    }

    @Override
    public void setTreno(Treno tr) {
        String sql = "INSERT INTO Treno (trenoID, tipoTreno, trattaID,stazione_partenza, stazione_arrivo, durata_viaggio, distanza, data_partenza, data_arrivo) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            TrattaPrototype tratta = tr.getTratta();

            stmt.setInt(1, tr.getTrenoID());
            stmt.setString(2, tr.getTipoTreno());
            stmt.setString(3, tratta.getStazionePartenza());
            stmt.setString(4, tratta.getStazioneArrivo());
            stmt.setInt(5, tratta.getTempoPercorrenza());
            stmt.setInt(6, tratta.getDistanza());
            stmt.setString(7, tratta.getDataPartenza());
            stmt.setString(8, tratta.getDataArrivo());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Errore salvataggio treno: " + e.getMessage());
        }
    }

    @Override
    public boolean removeTreno(int trenoID) {
        String sql = "DELETE FROM Treno WHERE trenoID = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, trenoID);
            int righe = stmt.executeUpdate();
            return righe > 0; // true se è stato effettivamente rimosso

        } catch (SQLException e) {
            System.err.println("Errore rimozione treno: " + e.getMessage());
            return false;
        }
    }
}
