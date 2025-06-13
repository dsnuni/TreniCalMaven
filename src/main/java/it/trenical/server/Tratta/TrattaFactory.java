package it.trenical.server.Tratta;


public class TrattaFactory {

    private static TrattaImpl impl = TrattaImplDB.getInstance();

    public TrattaFactory(TrattaImpl impl) {
        this.impl = impl;
    }

    public static TrattaPrototype getTrattaByID(String trenoID) {
        String trtInt = trenoID;

        return impl.getTratta(trtInt);
    }
}
