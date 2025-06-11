package it.trenical.server.Treno;


import it.trenical.server.Tratta.*;

import java.util.Arrays;
import java.util.Objects;


public abstract class Treno  {
    private static final String[] tipiTreni = {"FrecciaArgento","FrecciaRossa","FrecciaBianca","Regionale"};
    private String trenoID;
    private String tipoTreno;
    private TrattaPrototype tratta;
    private int prezzo;
    private int postiPrima;
    private int postiSeconda;
    private int postiTerza;
    private int postiTot;
    private int tempoPercorrenza;

    public Treno(String trenoID, String tipoTrenoI, TrattaPrototype tratta,
                 int prezzo, int postiPrima, int postiSeconda,int postiTerza, int postiTot) {
        this.trenoID = trenoID;
        this.tipoTreno = this.setTipoTreno(tipoTrenoI);
        this.tratta = tratta;
        this.prezzo = prezzo;
        this.postiPrima = postiPrima;
        this.postiSeconda = postiSeconda;
        this.postiTerza = postiTerza;
        this.postiTot = postiTot;
        this.tempoPercorrenza = this.setTempoPercorrenza();

    }
    private int setTempoPercorrenza() {
        System.out.println("ciao"+tratta);
        int distanza = tratta.getDistanza();
        System.out.println("distanza"+distanza);
        switch(tipoTreno){
            case "FrecciaArgento" :
                return (distanza/300);
            case "FrecciaRossa" :
                 return (distanza/250);
            case "FrecciaBianca" :
                return (distanza/200);
            case "Regionale" :
                return (distanza/150);
        }
        return 007;
    }

    private String setTipoTreno(String tipoTrenoI) {
        if( Arrays.asList(tipiTreni).contains(tipoTrenoI)) {
            return tipoTrenoI;
        }
        return null;
    }
    public Treno() {

    }

    // Metodi abstract da implementare nelle sottoclassi
    public abstract Treno getTreno();
    public abstract void setTreno();
    public abstract boolean remove();

    // Getters
    public String getTrenoID() {
        return trenoID;
    }

    public String getTipoTreno() {
        return tipoTreno;
    }

   public TrattaPrototype getTratta() {
        return tratta;
   }

    public int getPrezzo() {
        return prezzo;
    }
    public int getPostiPrima() {
        return postiPrima;
    }
    public int getPostiSeconda() {
        return postiSeconda;
    }
    public int getPostiTerza() {
        return postiTerza;
    }

    public int getPostiTot() {
        return postiTot;
    }

    public int getTempoPercorrenza() {
        return tempoPercorrenza;
    }

    @Override
    public String toString() {
        return "Treno{" +
                "  trenoID='" + trenoID + '\'' +
                ", tipoTreno='" + tipoTreno + '\'' +
                ", tratta=" + tratta +
                ", prezzo=" + prezzo +
                ", postiPrima=" + postiPrima +
                ", postiSeconda=" + postiSeconda +
                ", postiTerza=" + postiTerza +
                ", postiTot=" + postiTot +
                ", tempoPercorrenza=" + tempoPercorrenza +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Treno treno = (Treno) o;
        return prezzo == treno.prezzo && postiPrima == treno.postiPrima && postiSeconda == treno.postiSeconda && postiTerza == treno.postiTerza && postiTot == treno.postiTot && tempoPercorrenza == treno.tempoPercorrenza && Objects.equals(trenoID, treno.trenoID) && Objects.equals(tipoTreno, treno.tipoTreno) && Objects.equals(tratta, treno.tratta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trenoID, tipoTreno, tratta, prezzo, postiPrima, postiSeconda, postiTerza, postiTot, tempoPercorrenza);
    }
}
