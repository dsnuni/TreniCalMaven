package it.trenical.server.Treno;

import java.util.List;

public interface TrenoImpl {

    public Treno getTreno(String TrenoID);
    public void setTreno(Treno tr);
    public boolean removeTreno(String TrenoID);
    public List<Treno> getAllTreno();
    public List<Treno> getTrenoByTrattaID(String TrattaID);
}
