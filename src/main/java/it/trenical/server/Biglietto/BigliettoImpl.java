package it.trenical.server.Biglietto;

public interface BigliettoImpl {

    void setBiglietto(Biglietto biglietto);

    Biglietto getBiglietto(String BigliettoID);

    boolean removeBiglietto(String BigliettoID);
}