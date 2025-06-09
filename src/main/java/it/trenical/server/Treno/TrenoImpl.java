package it.trenical.server.Treno;

import java.util.List;

public interface TrenoImpl {

    public Treno getTreno(int TrenoID);
    public void setTreno(Treno tr);
    public boolean removeTreno(int TrenoID);
    public List<Treno> getAllTreno();
}
