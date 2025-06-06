package it.trenical.server.Biglietto;

import java.time.DayOfWeek;
import java.time.LocalDate;
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

    // Metodo principale per calcolare il prezzo scontato
    public double calcolaPrezzo(double prezzoBase, String codiceCliente, String trattaID, LocalDate dataPartenza) {
        double prezzoFinale = prezzoBase;

        // Sconto fedeltà (es. clienti VIP)
        if (codiceCliente != null && codiceCliente.startsWith("VIP")) {
            prezzoFinale *= 0.9; // -10%
        }

        // Sconto random (es. 5% dei clienti ricevono uno sconto casuale)
        if (random.nextDouble() < 0.05) {
            prezzoFinale *= 0.85; // -15%
        }

        // Promozione su tratta specifica e giorno specifico
        if ("TRATTA_MILANO_ROMA".equalsIgnoreCase(trattaID) &&
                dataPartenza != null && dataPartenza.getDayOfWeek() == DayOfWeek.TUESDAY) {
            prezzoFinale -= 5.0;
        }

        // Assicura che il prezzo non sia negativo
        return Math.max(prezzoFinale, 0);
    }
}
