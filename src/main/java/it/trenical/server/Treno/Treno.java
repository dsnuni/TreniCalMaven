package it.trenical.server.Treno;


import it.trenical.server.Tratta.*;

import java.util.Objects;


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

    @Override
    public String toString() {
        return "Treno{" +
                "tipoTreno='" + tipoTreno + '\'' +
                ", trenoID=" + trenoID +
                ", tratta=" + tratta +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Treno treno = (Treno) o;
        return trenoID == treno.trenoID && Objects.equals(tipoTreno, treno.tipoTreno) && Objects.equals(tratta, treno.tratta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trenoID, tipoTreno, tratta);
    }
}
