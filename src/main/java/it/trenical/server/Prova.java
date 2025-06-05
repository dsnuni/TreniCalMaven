package it.trenical.server;

import it.trenical.server.Cliente.*;
import it.trenical.server.Tratta.*;
import it.trenical.server.Treno.*;
import it.trenical.server.Biglietto.*;

import java.sql.*;
import java.util.List;

public class Prova {
    private static final String DB_URL = "jdbc:sqlite:db/treniCal.db";

    public static void main(String[] args) {
        // Ensure the SQLite driver is registered if present on the classpath
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver SQLite mancante: " + e.getMessage());
        }
        initDb();

        ClienteImplDB clienteDb = new ClienteImplDB();
        ClienteConcr mario = new ClienteConcr("MRARSS80A01H501U", "Mario", "Rossi", "CLI1");
        mario.setEtà(40);
        clienteDb.setCliente(mario);

        ClienteConcr luisa = new ClienteConcr("LSALSA85B02H501U", "Luisa", "Bianchi", "CLI2");
        luisa.setEtà(30);
        clienteDb.setCliente(luisa);

        // Recupero di esempio dal database
        Cliente marioDb = clienteDb.getCliente(mario.getCodiceFiscale());
        System.out.println("Cliente recuperato: " + marioDb);

        Generator generator = new GeneratorConcr(new TrattaStandard());
        TrenoImplDB trenoDb = new TrenoImplDB();

        TrattaPrototype tratta1 = generator.genera();
        Treno treno1 = new TrenoConcr(1, "Freccia", tratta1);
        trenoDb.setTreno(treno1);

        TrattaPrototype tratta2 = generator.genera();
        Treno treno2 = new TrenoConcr(2, "Regionale", tratta2);
        trenoDb.setTreno(treno2);

        // Recupero di un treno appena inserito
        Treno trenoLetto = trenoDb.getTreno(1);
        System.out.println("Treno recuperato: " + trenoLetto);

        BigliettoDB bigliettoDb = new BigliettoDB();
        BPrimaClasse b1 = new BPrimaClasse(bigliettoDb);
        b1.setBigliettoID("B1");
        b1.setTitolareBiglietto(mario);
        b1.setTrenoBiglietto(treno1);
        b1.setCarrozza("1");
        b1.setPosto("10A");
        // Priorità come lista immutabile per semplicità
        b1.setPriorità(List.of("PRIORITY"));
        b1.setPrezzo(50);
        b1.setBiglietto(b1);

        BSecondaClasse b2 = new BSecondaClasse(bigliettoDb);
        b2.setBigliettoID("B2");
        b2.setTitolareBiglietto(luisa);
        b2.setTrenoBiglietto(treno2);
        b2.setCarrozza("2");
        b2.setPosto("20B");
        b2.setPriorità(List.of("BAGAGLIO"));
        b2.setPrezzo(30);
        b2.setBiglietto(b2);

        Biglietto retrieved = bigliettoDb.getBiglietto("B1");
        System.out.println("Biglietto recuperato: " + retrieved);

        bigliettoDb.removeBiglietto("B2");
        trenoDb.removeTreno(2);

        // Verifiche successive alla rimozione
        System.out.println("Treno 2 esiste ancora? " + trenoDb.getTreno(2));
        System.out.println("Biglietto B2 esiste ancora? " + bigliettoDb.getBiglietto("B2"));
    }

    private static void initDb() {
        new java.io.File("db").mkdirs();
        try (Connection conn = DriverManager.getConnection(DB_URL); Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS Cliente (codiceFiscale TEXT PRIMARY KEY, nome TEXT, cognome TEXT, codiceCliente TEXT, eta INTEGER)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS Treno (trenoID INTEGER PRIMARY KEY, tipoTreno TEXT, trattaID TEXT, stazione_partenza TEXT, stazione_arrivo TEXT, durata_viaggio INTEGER, distanza INTEGER, data_partenza TEXT, data_arrivo TEXT)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS Biglietto (id TEXT PRIMARY KEY, classe TEXT, treno_id INTEGER, carrozza TEXT, posto TEXT, cliente_id TEXT, priorita TEXT, prezzo INTEGER)");
        } catch (SQLException e) {
            System.err.println("Errore inizializzazione DB: " + e.getMessage());
        }
    }
}
