package it.trenical.server.Biglietto;



import it.trenical.server.Cliente.*;
import it.trenical.server.Treno.*;

import java.sql.*;
import java.util.List;

public class BigliettoDB implements BigliettoImpl {

    private final String DB_URL = "jdbc:sqlite:db/treniCal.db";


    @Override
    public void setBiglietto(Biglietto biglietto) {
        String sql = "INSERT OR REPLACE INTO Biglietto " +
                "(id, classe, treno_id, carrozza, posto, cliente_id, priorita, prezzo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, biglietto.getBigliettoID());
            stmt.setString(2, biglietto.getClass().getSimpleName().replace("B", "")); // es: "PrimaClasse"
            stmt.setInt(3, biglietto.getTrenoBiglietto().getTrenoID());
            stmt.setString(4, biglietto.getCarrozza());
            stmt.setString(5, biglietto.getPosto());
            stmt.setString(6, biglietto.getTitolareBiglietto().getCodiceFiscale());
            stmt.setString(7, String.join(",", biglietto.getPriorità())); // serializza lista in CSV
            stmt.setInt(8, biglietto.getPrezzo());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Errore salvataggio biglietto: " + e.getMessage());
        }
    }

    @Override
    public Biglietto getBiglietto(String bigliettoID) {
        String sql = "SELECT * FROM Biglietto WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, bigliettoID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Ottieni campi
                String classe = rs.getString("classe");
                String trenoID = rs.getString("treno_id");
                String carrozza = rs.getString("carrozza");
                String posto = rs.getString("posto");
                String clienteID = rs.getString("cliente_id");
                String prioritaCSV = rs.getString("priorita");
                int prezzo = rs.getInt("prezzo");

                // Ricostruisci oggetti (richiede servizi o DAO per Cliente e Treno)
                TrenoFactory trenoFactory= null;
                Treno treno =  trenoFactory.getTrenoByID(trenoID);
                ClienteFactory clienteFactory = null;
                ClienteConcr cliente = (ClienteConcr) clienteFactory.getClienteByCodiceFiscale(clienteID);
                List<String> priorita = List.of(prioritaCSV.split(","));

                // Factory classi specifiche
                return switch (classe) {
                    case "PrimaClasse" -> new BPrimaClasse(bigliettoID, cliente, treno, carrozza, posto, priorita, prezzo);
                    case "SecondaClasse" -> new BSecondaClasse(bigliettoID, cliente, treno, carrozza, posto, priorita, prezzo);
                    case "TerzaClasse" -> new BTerzaClasse(bigliettoID, cliente, treno, carrozza, posto, priorita, prezzo);
                    default -> null;
                };
            }

        } catch (SQLException e) {
            System.err.println("Errore lettura biglietto: " + e.getMessage());
        }

        return null;
    }

    @Override
    public boolean removeBiglietto(String bigliettoID) {
        String sql = "DELETE FROM Biglietto WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, bigliettoID);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Errore eliminazione biglietto: " + e.getMessage());
        }

        return false;
    }
}

