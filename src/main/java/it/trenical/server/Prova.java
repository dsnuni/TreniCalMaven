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
                    TrenoImplDB trenoDb2 = new TrenoImplDB();
                    trenoDb2.removeTreno(1);
                    trenoDb2.removeTreno(42);


                    TrattaPrototype tratta2 = generator.genera();

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

