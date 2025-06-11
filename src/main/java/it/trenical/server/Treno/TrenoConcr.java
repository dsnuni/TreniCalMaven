package it.trenical.server.Treno;

import it.trenical.server.Tratta.TrattaPrototype;

public class TrenoConcr  extends Treno{
    private TrenoImpl trenoImpl;

    public TrenoConcr(TrenoImpl trenoImpl){
        super();
        this.trenoImpl = trenoImpl;
    }

    public TrenoConcr(String trenoID, String tipoTreno, TrattaPrototype tratta, int prezzo, int postiPrima, int postiSeconda,int postiTerza, int postiTot) {
        super(trenoID,tipoTreno,tratta,prezzo,postiPrima,postiSeconda,postiTerza,postiTot);
    }

    @Override
    public Treno getTreno() {
        return trenoImpl.getTreno(this.getTrenoID());
    }

    @Override
    public void setTreno() {
        trenoImpl.setTreno(this);
    }

    @Override
    public boolean remove() {
        if(trenoImpl.removeTreno(this.getTrenoID())) {
            return true;
        }
        return false;
    }


}
