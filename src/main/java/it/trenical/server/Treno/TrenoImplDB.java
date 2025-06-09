package it.trenical.server.Treno;

import it.trenical.grpc.Tratta;
import it.trenical.server.Tratta.*;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;


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
            stmt.setString(3, tratta.getCodiceTratta());
            stmt.setString(4, tratta.getStazionePartenza());
            stmt.setString(5, tratta.getStazioneArrivo());
            stmt.setInt(6, tratta.getTempoPercorrenza());
            stmt.setInt(7, tratta.getDistanza());
            stmt.setString(8, tratta.getDataPartenza());
            stmt.setString(9, tratta.getDataArrivo());

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
    public static void removeAll() {
        String sql = "DELETE FROM Treno";
        String url1 = "jdbc:sqlite:db/treniCal.db";
        try (Connection conn = DriverManager.getConnection(url1);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.executeUpdate();
    }catch (SQLException e) {
            System.err.println("Errore rimozione treno: " + e.getMessage());
            }
        }

    public int contaTreni() {
        String sql = "SELECT COUNT(*) FROM Treno";
        String DB_URL = "jdbc:sqlite:db/treniCal.db";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Errore conteggio treni: " + e.getMessage());
        }

        return 0;
    }

    public TrenoConcr getTrenoDallaRiga(int riga) {
        String sql = "SELECT * FROM Treno LIMIT 1 OFFSET ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, riga); // indice parte da 0
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                TrattaPrototype tratta = new TrattaStandard(
                        rs.getString("trattaID"),
                        rs.getString("stazione_partenza"),
                        rs.getString("stazione_arrivo"),
                        rs.getString("data_partenza"),
                        rs.getString("data_arrivo"),
                        rs.getInt("distanza"),
                        rs.getInt("durata_viaggio")
                );

                return new TrenoConcr(
                        rs.getInt("trenoID"),
                        rs.getString("tipoTreno"),
                        tratta
                );
            }

        } catch (SQLException e) {
            System.err.println("Errore recupero treno alla riga " + riga + ": " + e.getMessage());
        }

        return null;
    }

    public List<Treno> getByFiltro(String colonna, String valore) {
        List<Treno> treni = new ArrayList<>();
        String sql = "SELECT * FROM Treno WHERE " + colonna + " = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, valore);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TrattaPrototype tratta = new TrattaStandard(
                        rs.getString("trattaID"),
                        rs.getString("stazione_partenza"),
                        rs.getString("stazione_arrivo"),
                        rs.getString("data_partenza"),
                        rs.getString("data_arrivo"),
                        rs.getInt("distanza"),
                        rs.getInt("durata_viaggio")
                );

                treni.add(new TrenoConcr(
                        rs.getInt("trenoID"),
                        rs.getString("tipoTreno"),
                        tratta
                ));
            }
        } catch (SQLException e) {
            System.err.println("Errore filtro treno: " + e.getMessage());
        }
        return treni;
    }





}
