package it.trenical.server.Treno;

public class TrenoFactory {

    private static TrenoImpl impl = new TrenoImplDB();

    public TrenoFactory(TrenoImpl impl) {
        this.impl = impl;
    }

    public static Treno getTrenoByID(String trenoID) {
        int trnInt = Integer.parseInt(trenoID);

        return impl.getTreno(trnInt);
    }
}
