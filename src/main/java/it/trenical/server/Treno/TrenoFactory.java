package it.trenical.server.Treno;

public class TrenoFactory {

    private TrenoImpl impl;

    public TrenoFactory(TrenoImpl impl) {
        this.impl = impl;
    }

    public Treno getTrenoByID(String trenoID) {
        int trnInt = Integer.parseInt(trenoID);

        return impl.getTreno(trnInt);
    }
}
