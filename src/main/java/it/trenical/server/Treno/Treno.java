package it.trenical.server.Treno;


import it.trenical.server.Tratta.*;


public abstract class Treno  {
    private int trenoID;
    private String tipoTreno;
    private TrattaPrototype tratta;

    public Treno(int trenoID, String tipoTreno, TrattaPrototype tratta) {
        this.trenoID = trenoID;
        this.tipoTreno = tipoTreno;
        this.tratta = tratta;
    }

    public Treno() {

    }

    // Metodi abstract da implementare nelle sottoclassi
    public abstract Treno getTreno();
    public abstract void setTreno();
    public abstract boolean remove();

    // Getters
    public int getTrenoID() {
        return trenoID;
    }

    public String getTipoTreno() {
        return tipoTreno;
    }

   public TrattaPrototype getTratta() {
        return tratta;
   }
}
