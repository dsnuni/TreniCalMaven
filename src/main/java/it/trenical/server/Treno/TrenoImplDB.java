package it.trenical.server.Treno;
import it.trenical.server.Tratta.TrattaImpl;
import it.trenical.server.Tratta.TrattaImplDB;
import it.trenical.server.Tratta.TrattaStandard;
import it.trenical.server.notifiche.Observable;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class TrenoImplDB extends Observable implements TrenoImpl {
    private final String url = "jdbc:sqlite:db/treniCal.db";
    private static final TrenoImplDB instance = new TrenoImplDB();
    public static TrenoImplDB getInstance() {
        return instance;
    }


    private TrattaImpl db = TrattaImplDB.getInstance();

    private TrenoImplDB() {

        if (instance != null) {
            throw new RuntimeException("Usa getInstance() per ottenere l'istanza di TrenoImplDB");
        }
    }


    @Override
    public Treno getTreno(String  trenoID) {
        String sql = "SELECT * FROM Treno WHERE trenoID = ?";
        Treno t = null;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, trenoID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                String tipoTreno = rs.getString("tipoTreno");
                String trattaID = rs.getString("trattaID");
                int prezzo = rs.getInt("prezzo");
                int postiPrima = rs.getInt("postiPrima");
                int postiSeconda = rs.getInt("postiSeconda");
                int postiTerza = rs.getInt("postiTerza");
                int postiTot = rs.getInt("postiTot");
               // int tempoPercorrenza = rs.getInt("tempoPercorrenza");

                TrattaStandard tratta =  db.getTratta(trattaID);

                t = new TrenoConcr( trenoID,tipoTreno,tratta,prezzo,postiPrima,postiSeconda,postiTerza,postiTot);

            }

        } catch (SQLException e) {
            System.err.println("Errore lettura treno: " + e.getMessage());
        }

        return t;
    }

    @Override
    public void setTreno(Treno tr) {
        Treno esistente = getTreno(tr.getTrenoID());
        boolean isUpdate = (esistente != null);

        String sql = "INSERT OR REPLACE INTO Treno (trenoID, tipoTreno, trattaID, prezzo, postiPrima, postiSeconda, postiTerza, postiTot, tempoPercorrenza) " +
                "VALUES (?,?, ?, ?, ?, ?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            TrattaStandard tratta = tr.getTratta();
            stmt.setString(1, tr.getTrenoID());
            stmt.setString(2, tr.getTipoTreno());
            stmt.setString(3, tratta.getCodiceTratta());
            stmt.setInt(4, tr.getPrezzo());
            stmt.setInt(5, tr.getPostiPrima());
            stmt.setInt(6, tr.getPostiSeconda());
            stmt.setInt(7, tr.getPostiTerza());
            stmt.setInt(8, tr.getPostiTot());
            stmt.setInt(9, tr.getTempoPercorrenza());

            stmt.executeUpdate();

            // NOTIFICA GLI OBSERVER
            if (isUpdate) {
                notifyObservers("MODIFICATO treno: " + tr.getTrenoID() +
                        " (" + tr.getTipoTreno() + " su tratta " + tratta.getCodiceTratta() +
                        ", prezzo: €" + tr.getPrezzo() + ")");
            } else {
                notifyObservers("AGGIUNTO treno: " + tr.getTrenoID() +
                        " (" + tr.getTipoTreno() + " su tratta " + tratta.getCodiceTratta() +
                        ", prezzo: €" + tr.getPrezzo() + ", posti: " + tr.getPostiTot() + ")");
            }

        } catch (SQLException e) {
            System.err.println("Errore salvataggio treno: " + e.getMessage());
            notifyObservers("ERRORE nell'inserimento treno: " + tr.getTrenoID() + " - " + e.getMessage());
        }
    }

    @Override
    public boolean removeTreno(String trenoID) {
        Treno trenoDaRimuovere = getTreno(trenoID);

        String sql = "DELETE FROM Treno WHERE trenoID = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, trenoID);
            int righe = stmt.executeUpdate();

            if (righe > 0 && trenoDaRimuovere != null) {
                // NOTIFICA GLI OBSERVER
                notifyObservers("RIMOSSO treno: " + trenoID +
                        " (" + trenoDaRimuovere.getTipoTreno() +
                        " su tratta " + trenoDaRimuovere.getTratta().getCodiceTratta() + ")");
                return true;
            }

            return false;

        } catch (SQLException e) {
            System.err.println("Errore rimozione treno: " + e.getMessage());
            notifyObservers("ERRORE nella rimozione treno: " + trenoID + " - " + e.getMessage());
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
        TrenoConcr t= null;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, riga); // indice parte da 0
            ResultSet rs = stmt.executeQuery();


            if (rs.next()) {

                String trenoID = rs.getString("trenoID");
                String tipoTreno = rs.getString("tipoTreno");
                String trattaID = rs.getString("trattaID");
                int prezzo = rs.getInt("prezzo");
                int postiPrima = rs.getInt("postiPrima");
                int postiSeconda = rs.getInt("postiSeconda");
                int postiTerza = rs.getInt("postiTerza");
                int postiTot = rs.getInt("postiTot");

                TrattaStandard tratta =  db.getTratta(trattaID);

                t = new TrenoConcr( trenoID,tipoTreno,tratta,prezzo,postiPrima,postiSeconda,postiTerza,postiTot);

            }


        } catch (SQLException e) {
            System.err.println("Errore recupero treno alla riga " + riga + ": " + e.getMessage());
        }

        return t;
    }
    @Override
    public List<Treno> getAllTreno() {
        List<Treno> treni = new ArrayList<>();
        String sql = "SELECT * FROM Treno";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String trenoID = rs.getString("trenoID");
                String tipoTreno = rs.getString("tipoTreno");
                String trattaID = rs.getString("trattaID");
                int prezzo = rs.getInt("prezzo");
                int postiPrima = rs.getInt("postiPrima");
                int postiSeconda = rs.getInt("postiSeconda");
                int postiTerza = rs.getInt("postiTerza");
                int postiTot = rs.getInt("postiTot");


                TrattaStandard tratta =  db.getTratta(trattaID);

                Treno t = new TrenoConcr( trenoID,tipoTreno,tratta,prezzo,postiPrima,postiSeconda,postiTerza,postiTot);
                treni.add(t);

            }}catch (SQLException e) {
            System.err.println("Errore filtro treno: " + e.getMessage());
        }
        return treni;
    }


    public List<Treno> getByFiltro(String colonna, String valore) {
        List<Treno> treni = new ArrayList<>();
        String sql = "SELECT * FROM Treno WHERE " + colonna + " = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, valore);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {

                String trenoID = rs.getString("trenoID");
                String tipoTreno = rs.getString("tipoTreno");
                String trattaID = rs.getString("trattaID");
                int prezzo = rs.getInt("prezzo");
                int postiPrima = rs.getInt("postiPrima");
                int postiSeconda = rs.getInt("postiSeconda");
                int postiTerza = rs.getInt("postiTerza");
                int postiTot = rs.getInt("postiTot");

                TrattaStandard tratta =  db.getTratta(trattaID);

                Treno t = new TrenoConcr( trenoID,tipoTreno,tratta,prezzo,postiPrima,postiSeconda,postiTerza,postiTot);
                treni.add(t);

            }
        } catch (SQLException e) {
            System.err.println("Errore filtro treno: " + e.getMessage());
        }
        return treni;
    }

    public List<Treno> getTrenoByTrattaID(String trattaID) {
        List<Treno> treni = new ArrayList<>();
        TrattaImplDB dbtrt = TrattaImplDB.getInstance();
        String sql = "SELECT * FROM Treno WHERE trattaID = ?";
        System.out.println("cerco tratte");
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, trattaID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Treno treno = new TrenoConcr(
                        rs.getString("trenoID"),
                        rs.getString("tipoTreno"),
                        dbtrt.getTratta(trattaID),
                        rs.getInt("prezzo"),
                        rs.getInt("postiPrima"),
                        rs.getInt("postiSeconda"),
                        rs.getInt("postiTerza"),
                        rs.getInt("postiTot"));
                treni.add(treno);
            }

        } catch (SQLException e) {
            System.err.println("Errore nella ricerca dei treni per trattaID: " + e.getMessage());
        }

        return treni;
    }


    public static TrenoConcr getTrenoByRowIndex(int index) {
        String DB_URL = "jdbc:sqlite:db/treniCal.db"; // aggiorna se serve
        String sql = "SELECT * FROM Treno LIMIT 1 OFFSET ?";
        TrattaImpl dbt =TrattaImplDB.getInstance();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, index);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String trenoID = rs.getString("trenoID");
                String tipoTreno = rs.getString("tipoTreno");
                String trattaID = rs.getString("trattaID");
                int prezzo = rs.getInt("prezzo");
                int postiPrima = rs.getInt("postiPrima");
                int postiSeconda = rs.getInt("postiSeconda");
                int postiTerza = rs.getInt("postiTerza");
                int postiTot = rs.getInt("postiTot");
                int tempoPercorrenza = rs.getInt("tempoPercorrenza");

                TrattaStandard tratta = dbt.getTratta(trattaID);
                return new TrenoConcr(trenoID, tipoTreno, tratta, prezzo, postiPrima,
                        postiSeconda, postiTerza, postiTot);
            } else {
                throw new IllegalArgumentException("Nessun treno alla riga: " + index);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> removeTrenoByTrattaID(String trattaID) {
        List<String> treniEliminati = new ArrayList<>();
        String sql = "SELECT trenoID FROM Treno WHERE trattaID = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, trattaID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String trenoID = rs.getString("trenoID");

                // Usa il metodo esistente per eliminare il treno
                boolean successo = removeTreno(trenoID);
                if (successo) {
                    treniEliminati.add(trenoID);
                }
            }

        } catch (SQLException e) {
            System.err.println("Errore durante la rimozione dei treni per trattaID " + trattaID + ": " + e.getMessage());
        }

        return treniEliminati;
    }



}
