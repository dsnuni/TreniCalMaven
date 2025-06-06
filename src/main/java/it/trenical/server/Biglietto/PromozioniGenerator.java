package it.trenical.server.Biglietto;

import it.trenical.server.Treno.TrenoConcr;
import it.trenical.server.Treno.TrenoImplDB;
import it.trenical.server.Tratta.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class PromozioniGenerator {

    private static PromozioniGenerator instance;
    private final Random random;

    // Costruttore privato (Singleton)
    private PromozioniGenerator() {
        this.random = new Random();
    }

    // Accesso all'unica istanza
    public static PromozioniGenerator getInstance() {
        if (instance == null) {
            instance = new PromozioniGenerator();
        }
        return instance;
    }

    public int calcolaPrezzo(int prezzoBase, String codiceCliente, TrattaPrototype tratta) {
        ArrayList<String> tratteInSconto = tratteInSconto();
        double prezzoFinale = prezzoBase;

        if (codiceCliente != null && codiceCliente.startsWith("TRNCL")) {
            prezzoFinale *= 0.8;
        }

        if (random.nextDouble() < 0.05) {
            prezzoFinale *= 0.95;
        }

//        if (tratteInSconto.contains(tratta.getCodiceTratta())) {
//            prezzoFinale -= 5;
//        }

        return Math.max((int) Math.round(prezzoFinale), 0);
    }

    private ArrayList tratteInSconto() {
        ArrayList<String> tratteSconto = new ArrayList();
        TrenoImplDB db = new TrenoImplDB();
        for (int i = 0; i < 10; i++) {
            int numero = random.nextInt(db.contaTreni());
            TrenoConcr tr = db.getTrenoDallaRiga(numero);
            tratteSconto.add(tr.getTratta().getCodiceTratta());
        }
        return tratteSconto;
    }

}
