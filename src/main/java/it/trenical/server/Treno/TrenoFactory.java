package it.trenical.server.Treno;

public class TrenoFactory {

    private static TrenoImpl impl = TrenoImplDB.getInstance();

    public TrenoFactory(TrenoImpl impl) {
        this.impl = impl;
    }

    public static Treno getTrenoByID(String trenoID) {
        String trnInt = trenoID;

        return impl.getTreno(trnInt);
    }
}
