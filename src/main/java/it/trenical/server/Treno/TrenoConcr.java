package it.trenical.server.Treno;

import it.trenical.server.Tratta.TrattaPrototype;
import it.trenical.server.Tratta.TrattaStandard;

public class TrenoConcr  extends Treno{

    public TrenoConcr(String trenoID, String tipoTreno, TrattaStandard tratta, int prezzo, int postiPrima, int postiSeconda, int postiTerza, int postiTot, int binario, String promozione) {
        super(trenoID,tipoTreno,tratta,prezzo,postiPrima,postiSeconda,postiTerza,postiTot, binario, promozione);
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
