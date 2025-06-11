package it.trenical.server.Tratta;

public interface TrattaImpl {
     TrattaPrototype getTratta(String trattaID);
     void setTratta( TrattaPrototype tratta );
     boolean removeTratta( String trattaID);
}
