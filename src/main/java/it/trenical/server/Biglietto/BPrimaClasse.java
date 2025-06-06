package it.trenical.server.Biglietto;

import it.trenical.server.Cliente.*;
import it.trenical.server.Treno.*;
import java.util.ArrayList;
import java.util.List;

public class BPrimaClasse  extends Biglietto {

    public BPrimaClasse(BigliettoImpl impl) {
        this.implementazione = impl;
    }

    public BPrimaClasse(String bigliettoID, ClienteConcr titolareBiglietto, Treno trenoBiglietto, String carrozza, String posto, List<String> priorità, int prezzo) {
        this.bigliettoID = bigliettoID;
        this.titolareBiglietto = titolareBiglietto;
        this.trenoBiglietto = trenoBiglietto;
        this.carrozza = carrozza;
        this.posto = posto;
        this.priorità=priorità;
        this.prezzo=prezzo;
    }
}
