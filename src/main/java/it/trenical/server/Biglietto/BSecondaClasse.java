package it.trenical.server.Biglietto;

import it.trenical.server.Cliente.*;
import it.trenical.server.Treno.*;
import java.util.ArrayList;
import java.util.List;

public class BSecondaClasse extends Biglietto {

    public BSecondaClasse(BigliettoImpl impl) {
        this.implementazione = impl;
    }

    public BSecondaClasse(String bigliettoID, ClienteConcr titolareBiglietto, Treno trenoBiglietto, String carrozza, String posto, List<String> priorità, int prezzo) {
        this.bigliettoID = bigliettoID;
        this.titolareBiglietto = titolareBiglietto;
        this.carrozza = carrozza;
        this.posto = posto;
        this.priorità= new ArrayList<>();
        this.prezzo=prezzo;
    }
}
