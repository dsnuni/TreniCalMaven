package it.trenical.server.Biglietto;



import it.trenical.server.Cliente.*;
import it.trenical.server.Tratta.TrattaStandard;
import it.trenical.server.Treno.*;
import it.trenical.server.notifiche.Observable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static it.trenical.server.Cliente.ClienteFactory.getClienteByCodiceFiscale;
import static it.trenical.server.Treno.TrenoFactory.getTrenoByID;

public class BigliettoDB extends Observable implements BigliettoImpl {
    private final String DB_URL = "jdbc:sqlite:db/treniCal.db";
    private static final BigliettoDB instance = new BigliettoDB();
    public static BigliettoDB getInstance() {
        return instance;
    }

    private BigliettoDB() {
        if (instance != null) {
            throw new RuntimeException("Usa getInstance() per ottenere l'istanza di TrattaImplDB");
        }
    }



    @Override
    public void setBiglietto(Biglietto biglietto) {
        String sql = "INSERT OR REPLACE INTO Biglietto " +
                "(id, classe, treno_id, carrozza, posto, cliente_id, priorita, prezzo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, biglietto.getBigliettoID());
            stmt.setString(2, biglietto.getClass().getSimpleName().replace("B", "")); // es: "PrimaClasse"
            stmt.setString(3, biglietto.getTrenoBiglietto().getTrenoID());
            stmt.setString(4, biglietto.getCarrozza());
            stmt.setString(5, biglietto.getPosto());
            stmt.setString(6, biglietto.getTitolareBiglietto().getCodiceFiscale());
            stmt.setString(7, String.join(",", biglietto.getPriorità()));
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
                String classe = rs.getString("classe");
                String trenoID = rs.getString("treno_id");
                String carrozza = rs.getString("carrozza");
                String posto = rs.getString("posto");
                String clienteID = rs.getString("cliente_id");
                String prioritaCSV = rs.getString("priorita");
                int prezzo = rs.getInt("prezzo");

                ClienteConcr cliente = (ClienteConcr) getClienteByCodiceFiscale(clienteID);
                Treno treno = getTrenoByID(trenoID);
                List<String> priorita = List.of(prioritaCSV.split(","));

                return switch (classe) {
                    case "PrimaClasse" -> new BPrimaClasse.Builder()
                            .bigliettoID(bigliettoID)
                            .titolareBiglietto(cliente)
                            .trenoBiglietto(treno)
                            .carrozza(carrozza)
                            .posto(posto)
                            .priorità(priorita)
                            .prezzo(prezzo)
                            .implementazione(this)
                            .build();

                    case "SecondaClasse" -> new BSecondaClasse.Builder()
                            .bigliettoID(bigliettoID)
                            .titolareBiglietto(cliente)
                            .trenoBiglietto(treno)
                            .carrozza(carrozza)
                            .posto(posto)
                            .priorità(priorita)
                            .prezzo(prezzo)
                            .implementazione(this)
                            .build();

                    case "TerzaClasse" -> new BTerzaClasse.Builder()
                            .bigliettoID(bigliettoID)
                            .titolareBiglietto(cliente)
                            .trenoBiglietto(treno)
                            .carrozza(carrozza)
                            .posto(posto)
                            .priorità(priorita)
                            .prezzo(prezzo)
                            .implementazione(this)
                            .build();

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

    public static void removeAll() {
        String sql = "DELETE FROM Biglietto";
        String url1 = "jdbc:sqlite:db/treniCal.db";
        try (Connection conn = DriverManager.getConnection(url1);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        }catch (SQLException e) {
            System.err.println("Errore rimozione treno: " + e.getMessage());
        }
    }
    public List<Biglietto> getByFiltro(String colonna, String valore) {
        List<Biglietto> biglietti = new ArrayList<>();
        String sql = "SELECT * FROM Biglietto WHERE " + colonna + " = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, valore);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Cliente cliente = getClienteByCodiceFiscale(rs.getString("cliente_id"));
                Treno treno = getTrenoByID(rs.getString("treno_id"));
                switch (rs.getString("classe")) {
                    case "PrimaClasse":
                             Biglietto b = new BPrimaClasse.Builder()
                            .bigliettoID(rs.getString("id"))
                            //.classe(rs.getString("classe"))
                            .trenoBiglietto(treno)
                            .carrozza(rs.getString("carrozza"))
                            .posto(rs.getString("posto"))
                            .titolareBiglietto(cliente)
                            .priorità(List.of(rs.getString("priorita").split(",")))
                            .prezzo(rs.getInt("prezzo"))
                            .implementazione(this)
                            .build();
                    biglietti.add(b);
                    break;
                    case "SecondaClasse":
                        Biglietto b2 = new BSecondaClasse.Builder()
                                .bigliettoID(rs.getString("id"))
                                .trenoBiglietto(treno)
                                .carrozza(rs.getString("carrozza"))
                                .posto(rs.getString("posto"))
                                .titolareBiglietto(cliente)
                                .priorità(List.of(rs.getString("priorita").split(",")))
                                .prezzo(rs.getInt("prezzo"))
                                .implementazione(this)
                                .build();
                        biglietti.add(b2);
                        break;

                    case "TerzaClasse":
                        Biglietto b3 = new BTerzaClasse.Builder()
                                .bigliettoID(rs.getString("id"))
                                .trenoBiglietto(treno)
                                .carrozza(rs.getString("carrozza"))
                                .posto(rs.getString("posto"))
                                .titolareBiglietto(cliente)
                                .priorità(List.of(rs.getString("priorita").split(",")))
                                .prezzo(rs.getInt("prezzo"))
                                .implementazione(this)
                                .build();
                        biglietti.add(b3);
                        break;

                }
            }
        } catch (SQLException e) {
            System.err.println("Errore filtro biglietto: " + e.getMessage());
        }
        return biglietti;
    }

    public List<Biglietto> getAllBiglietti() {
        List<Biglietto> biglietti = new ArrayList<>();
        String sql = "SELECT * FROM Biglietto";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String bigliettoID = rs.getString("id");
                String classe = rs.getString("classe");
                String trenoID = rs.getString("treno_id");
                String carrozza = rs.getString("carrozza");
                String posto = rs.getString("posto");
                String clienteID = rs.getString("cliente_id");
                String prioritaCSV = rs.getString("priorita");
                int prezzo = rs.getInt("prezzo");

                ClienteConcr cliente = (ClienteConcr) getClienteByCodiceFiscale(clienteID);
                Treno treno = getTrenoByID(trenoID);
                List<String> priorita = List.of(prioritaCSV.split(","));
                Biglietto biglietto = null;

                switch (classe) {
                    case "PrimaClasse" -> biglietto = new BPrimaClasse.Builder()
                            .bigliettoID(bigliettoID)
                            .titolareBiglietto(cliente)
                            .trenoBiglietto(treno)
                            .carrozza(carrozza)
                            .posto(posto)
                            .priorità(priorita)
                            .prezzo(prezzo)
                            .implementazione(this)
                            .build();


                    case "SecondaClasse" ->  biglietto = new BSecondaClasse.Builder()
                            .bigliettoID(bigliettoID)
                            .titolareBiglietto(cliente)
                            .trenoBiglietto(treno)
                            .carrozza(carrozza)
                            .posto(posto)
                            .priorità(priorita)
                            .prezzo(prezzo)
                            .implementazione(this)
                            .build();

                    case "TerzaClasse" -> biglietto = new BTerzaClasse.Builder()
                            .bigliettoID(bigliettoID)
                            .titolareBiglietto(cliente)
                            .trenoBiglietto(treno)
                            .carrozza(carrozza)
                            .posto(posto)
                            .priorità(priorita)
                            .prezzo(prezzo)
                            .implementazione(this)
                            .build();
                }

                biglietti.add(biglietto);

            }}catch (SQLException e) {
            System.err.println("Errore filtro treno: " + e.getMessage());
        }
        return biglietti;
    }

    public List<Biglietto> getBigliettiByTrenoID(String trenoID) {
        List<Biglietto> biglietti = new ArrayList<>();
        String sql = "SELECT * FROM Biglietto WHERE treno_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, trenoID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String bigliettoID = rs.getString("id");
                String classe = rs.getString("classe");
                String carrozza = rs.getString("carrozza");
                String posto = rs.getString("posto");
                String clienteID = rs.getString("cliente_id");
                String prioritaCSV = rs.getString("priorita");
                int prezzo = rs.getInt("prezzo");

                ClienteConcr cliente = (ClienteConcr) getClienteByCodiceFiscale(clienteID);
                Treno treno = getTrenoByID(trenoID);
                List<String> priorita = List.of(prioritaCSV.split(","));
                Biglietto biglietto = null;

                switch (classe) {
                    case "PrimaClasse" -> biglietto = new BPrimaClasse.Builder()
                            .bigliettoID(bigliettoID)
                            .titolareBiglietto(cliente)
                            .trenoBiglietto(treno)
                            .carrozza(carrozza)
                            .posto(posto)
                            .priorità(priorita)
                            .prezzo(prezzo)
                            .implementazione(this)
                            .build();

                    case "SecondaClasse" -> biglietto = new BSecondaClasse.Builder()
                            .bigliettoID(bigliettoID)
                            .titolareBiglietto(cliente)
                            .trenoBiglietto(treno)
                            .carrozza(carrozza)
                            .posto(posto)
                            .priorità(priorita)
                            .prezzo(prezzo)
                            .implementazione(this)
                            .build();

                    case "TerzaClasse" -> biglietto = new BTerzaClasse.Builder()
                            .bigliettoID(bigliettoID)
                            .titolareBiglietto(cliente)
                            .trenoBiglietto(treno)
                            .carrozza(carrozza)
                            .posto(posto)
                            .priorità(priorita)
                            .prezzo(prezzo)
                            .implementazione(this)
                            .build();
                }

                if (biglietto != null) {
                    biglietti.add(biglietto);
                }
            }

        } catch (SQLException e) {
            System.err.println("Errore recupero biglietti per treno: " + e.getMessage());
        }

        return biglietti;
    }

    public void removeBigliettoByTrenoID(String trenoID) {
        String sql = "DELETE FROM Biglietto WHERE treno_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, trenoID);
            int count = stmt.executeUpdate();

            System.out.println("Rimossi " + count + " biglietti per il treno ID: " + trenoID);

        } catch (SQLException e) {
            System.err.println("Errore nella rimozione dei biglietti per treno " + trenoID + ": " + e.getMessage());
        }
    }
}

