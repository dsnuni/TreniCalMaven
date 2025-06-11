package it.trenical.server.Treno;

import it.trenical.server.Tratta.TrattaPrototype;

public class TrenoConcr  extends Treno{

    public TrenoConcr(String trenoID, String tipoTreno, TrattaPrototype tratta, int prezzo, int postiPrima, int postiSeconda,int postiTerza, int postiTot) {
        super(trenoID,tipoTreno,tratta,prezzo,postiPrima,postiSeconda,postiTerza,postiTot);
    }

    @Override
    public Treno getTreno() {
        return this;
    }

    @Override
    public void setTreno() {

    }

    @Override
    public boolean remove() {
        return false;
    }


}
