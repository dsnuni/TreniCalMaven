package it.trenical.server.Biglietto;

import java.util.List;

public interface BigliettoImpl {

    void setBiglietto(Biglietto biglietto);

    Biglietto getBiglietto(String BigliettoID);

    boolean removeBiglietto(String BigliettoID);

    public List<Biglietto> getByFiltro(String colonna, String valore);
}