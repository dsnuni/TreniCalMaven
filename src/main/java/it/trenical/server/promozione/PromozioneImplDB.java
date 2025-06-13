package it.trenical.server.promozione;

import it.trenical.server.Tratta.TrattaImpl;
import it.trenical.server.Tratta.TrattaImplDB;
import it.trenical.server.Tratta.TrattaPrototype;
import it.trenical.server.Treno.Treno;
import it.trenical.server.Treno.TrenoImpl;
import it.trenical.server.Treno.TrenoImplDB;
import it.trenical.server.notifiche.Observable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PromozioneImplDB extends Observable implements PromozioneImpl {

    private static final String DB_URL = "jdbc:sqlite:db/treniCal.db";
    private static final PromozioneImplDB instance = new PromozioneImplDB();
    private TrattaImpl trdb = TrattaImplDB.getInstance();
    private TrenoImpl tdb = TrenoImplDB.getInstance();
    public static PromozioneImplDB getInstance() {
        return instance;
    }

    private PromozioneImplDB() {
        if (instance != null) {
            throw new RuntimeException("Usa getInstance() per ottenere l'istanza di PromozioneImplDB");
        }
    }

    public void setPromozione(Promozione promo) {
        String sql = "INSERT OR REPLACE INTO Promozione (promozioneID, trenoID, trattaID, dataPartenza, dataFine, clientiFedelta, prezzoPartenza, scontistica) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, promo.getPromozioneID());
            stmt.setString(2, promo.getTreno().getTrenoID());
            stmt.setString(3, promo.getTratta().getCodiceTratta());
            stmt.setString(4, promo.getDataPartenza());
            stmt.setString(5, promo.getDataFine());
            stmt.setBoolean(6, promo.isClientiFedelta());
            stmt.setInt(7, promo.getPrezzoPartenza());
            stmt.setDouble(8, promo.getScontistica());

            stmt.executeUpdate();
            notifyObservers("Aggiunta promozione per treno " + promo.getTreno().getTrenoID());

        } catch (SQLException e) {
            System.err.println("Errore inserimento promozione: " + e.getMessage());
        }
    }

    public boolean removePromozione(String trenoID) {
        String sql = "DELETE FROM Promozione WHERE trenoID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, trenoID);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Errore rimozione promozione: " + e.getMessage());
            return false;
        }
    }
    public Promozione getPromozione(String promozioneID) {
        String sql = "SELECT * FROM Promozione WHERE trenoID = ?";
        Promozione pr = null;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {

                String trenoID = rs.getString("trenoID");
                String trattaID = rs.getString("trattaID");
                String dataPartenza = rs.getString("dataPartenza");
                String dataFine = rs.getString("dataFine");
                boolean clientiFedelta = rs.getBoolean("clientiFedelta");
                int prezzoPartenza = rs.getInt("prezzoPartenza");
                double scontistica = rs.getDouble("scontistica");

                TrattaPrototype trt = trdb.getTratta(trattaID);
                Treno trn = tdb.getTreno(trenoID);

                 pr = new Promozione.PromozioneBuilder()
                    .setPromozioneID(promozioneID)
                    .setTreno(trn)
                    .setTratta(trt)
                    .setDataPartenza(dataPartenza)
                    .setDataFine(dataFine)
                    .setClientiFedelta( clientiFedelta)
                    .setPrezzoPartenza(prezzoPartenza)
                    .setScontistica(scontistica)
                    .build();
                return pr;
            }
        }catch (SQLException e) {
            System.err.println("Errore lettura promozioni: " + e.getMessage());
        }
        return pr;
    }

    // Esempio di recupero
    public List<Promozione> getAllPromozioni() {
        List<Promozione> list = new ArrayList<>();
        String sql = "SELECT * FROM Promozione";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String promozioneID = rs.getString("promozioneID");
                String trenoID = rs.getString("trenoID");
                String trattaID = rs.getString("trattaID");
                String dataPartenza = rs.getString("dataPartenza");
                String dataFine = rs.getString("dataFine");
                boolean clientiFedelta = rs.getBoolean("clientiFedelta");
                int prezzoPartenza = rs.getInt("prezzoPartenza");
                double scontistica = rs.getDouble("scontistica");

                TrattaPrototype trt = trdb.getTratta(trattaID);
                Treno trn = tdb.getTreno(trenoID);

               Promozione pr = new Promozione.PromozioneBuilder()
                        .setPromozioneID(promozioneID)
                        .setTreno(trn)
                        .setTratta(trt)
                        .setDataPartenza(dataPartenza)
                        .setDataFine(dataFine)
                        .setClientiFedelta( clientiFedelta)
                        .setPrezzoPartenza(prezzoPartenza)
                        .setScontistica(scontistica)
                        .build();
            }

        } catch (SQLException e) {
            System.err.println("Errore lettura promozioni: " + e.getMessage());
        }

        return list;
    }
    public List<Promozione> cercaPromozioniContenenti(String chiave) {
        List<Promozione> risultati = new ArrayList<>();
        TrenoImpl dbtrn = TrenoImplDB.getInstance();
        TrattaImpl dbtrt = TrattaImplDB.getInstance();
        String sql = "SELECT * FROM Promozione";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String promozioneID = rs.getString("promozioneID");
                String dataPartenza = rs.getString("dataPartenza");
                String dataFine = rs.getString("dataFine");
                String trattaID = rs.getString("trattaID");
                String trenoID = rs.getString("trenoID");
                Boolean clientiFedelta = rs.getBoolean("clientiFedelta");
                int prezzoPartenza = rs.getInt("prezzoPartenza");
                int scontistica = rs.getInt("scontistica");

                // Controllo se uno dei campi contiene la chiave (case-sensitive)
                if ((promozioneID != null && promozioneID.contains(chiave)) ||
                        (dataPartenza != null && dataPartenza.contains(chiave)) ||
                        (dataFine != null && dataFine.contains(chiave)) ||
                        (trattaID != null && trattaID.contains(chiave)) ||
                        (trenoID != null && trenoID.contains(chiave))) {

                    Promozione promo = new Promozione.PromozioneBuilder()
                            .setPromozioneID(promozioneID)
                            .setTreno(dbtrn.getTreno(trenoID))
                            .setTratta(dbtrt.getTratta(trattaID))
                            .setDataPartenza(dataPartenza)
                            .setDataFine(dataFine)
                            .setClientiFedelta(clientiFedelta)
                            .setPrezzoPartenza(prezzoPartenza)
                            .setScontistica(scontistica)
                            .build();

                    risultati.add(promo);
                }
            }

        } catch (SQLException e) {
            System.err.println("Errore durante la ricerca delle promozioni: " + e.getMessage());
        }

        return risultati;
    }

}
