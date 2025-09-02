package it.trenical.server.Cliente;
import it.trenical.server.notifiche.Observable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteImplDB extends Observable implements ClienteImpl {
    private static final String DB_URL = "jdbc:sqlite:db/treniCal.db";
    private static final ClienteImplDB instance = new ClienteImplDB();
    public static ClienteImplDB getInstance() {
        return instance;
    }

    private ClienteImplDB() {
        if (instance != null) {
            throw new RuntimeException("Usa getInstance() per ottenere l'istanza di TrattaImplDB");
        }
    }


    @Override
    public void setCliente(Cliente cliente) {
        String sql = "INSERT OR REPLACE INTO Cliente (codiceFiscale, nome, cognome, codiceCliente, eta) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getCodiceFiscale());
            stmt.setString(2, cliente.getNome());
            stmt.setString(3, cliente.getCognome());
            stmt.setString(4, cliente.getCodiceCliente());
            stmt.setInt(5, cliente.getEtà());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errore inserimento cliente: " + e.getMessage());
        }
    }

    @Override
    public Cliente getCliente(String codiceFiscale) {
        String sql = "SELECT * FROM Cliente WHERE codiceFiscale = ?";
        Cliente cliente = null;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codiceFiscale);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("nome");
                String cognome = rs.getString("cognome");
                String codiceCliente = rs.getString("codiceCliente");
                int eta = rs.getInt("eta");

                cliente = new ClienteConcr(codiceFiscale, nome, cognome, codiceCliente,eta); // o CSecondaClasse, etc.
                (cliente).setEtà(eta);
            }

        } catch (SQLException e) {
            System.err.println("Errore recupero cliente: " + e.getMessage());
        }

        return cliente;
    }

        @Override
        public boolean removeCliente(String codiceFiscale) {
            String sql = "DELETE FROM Cliente WHERE codiceFiscale = ?";
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, codiceFiscale);
                int righe = stmt.executeUpdate();
                return righe > 0;
            } catch (SQLException e) {
                System.err.println("Errore rimozione treno: " + e.getMessage());
                return false;
            }
        }

    public static void removeAll() {
        String sql = "DELETE FROM Cliente";
        String url1 = "jdbc:sqlite:db/treniCal.db";
        try (Connection conn = DriverManager.getConnection(url1);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        }catch (SQLException e) {
            System.err.println("Errore rimozione treno: " + e.getMessage());
        }
    }

    public List<Cliente> getByFiltro(String colonna, String valore) {
        List<Cliente> clienti = new ArrayList<>();
        String sql = "SELECT * FROM Cliente WHERE " + colonna + " = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, valore);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                clienti.add(new ClienteConcr(
                        rs.getString("codiceFiscale"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("codiceCliente"),
                        rs.getInt("eta")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Errore filtro cliente: " + e.getMessage());
        }
        return clienti;
    }

    public Cliente getClienteByCodiceCLiente(String codiceCliente) {
        Cliente cliente = null;
        String sql = "SELECT * FROM Cliente WHERE codiceCliente = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codiceCliente);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String codiceFiscale = rs.getString("codiceFiscale");
                String nome = rs.getString("nome");
                String cognome = rs.getString("cognome");
                int eta = rs.getInt("eta");

                cliente = new ClienteConcr(codiceFiscale, nome, cognome, codiceCliente, eta);
            }
        } catch (SQLException e) {
            System.err.println("Errore filtro cliente: " + e.getMessage());
        }
        return cliente;
    }

    public static ClienteConcr getClienteByRowIndex(int index) {
        String DB_URL = "jdbc:sqlite:db/treniCal.db"; // aggiorna se il path è diverso
        String sql = "SELECT * FROM Cliente LIMIT 1 OFFSET ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, index);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String codiceFiscale = rs.getString("codiceFiscale");
                String nome = rs.getString("nome");
                String cognome = rs.getString("cognome");
                String codiceCliente = rs.getString("codiceCliente");
                int eta = rs.getInt("eta");

                return new ClienteConcr(codiceFiscale, nome, cognome, codiceCliente, eta);
            } else {
                throw new IllegalArgumentException("Nessun cliente alla riga: " + index);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }



}

