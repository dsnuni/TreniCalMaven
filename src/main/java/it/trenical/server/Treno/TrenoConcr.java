package it.trenical.server.Treno;

import it.trenical.server.Tratta.TrattaPrototype;

public class TrenoConcr  extends Treno{
    private TrenoImpl trenoImpl;

    public TrenoConcr(TrenoImpl trenoImpl){
        super();
        this.trenoImpl = trenoImpl;
    }

    public TrenoConcr(int trenoID, String tipoTreno, TrattaPrototype tratta) {
        super(trenoID,tipoTreno,tratta);
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
