package it.trenical.server;

    import it.trenical.server.Cliente.*;
    import it.trenical.server.Tratta.*;
    import it.trenical.server.Treno.*;
    import it.trenical.server.Biglietto.*;

    import java.io.File;
    import java.sql.*;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Random;

public class Prova {
        private static final String DB_URL = "jdbc:sqlite:db/treniCal.db";




    public static void main(String[] args) {
        Random random = new Random();
            ArrayList<String> tratteSconto = new ArrayList();
            TrenoImplDB db = TrenoImplDB.getInstance();
            for (int i = 0; i < 10; i++) {
                int numero = random.nextInt(db.contaTreni());
                TrenoConcr tr = db.getTrenoDallaRiga(numero);
                tratteSconto.add(tr.getTratta().getCodiceTratta());
            }
            System.out.println(tratteSconto.toString());
        }


        }

