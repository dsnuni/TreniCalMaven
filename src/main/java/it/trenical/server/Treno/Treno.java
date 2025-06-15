package it.trenical.server.Treno;


import it.trenical.server.Tratta.*;

import java.util.Arrays;
import java.util.Objects;


public abstract class Treno  {
    private static final String[] tipiTreni = {"FrecciaArgento","FrecciaRossa","FrecciaBianca","Regionale"};
    private String trenoID;
    private String tipoTreno;
    private TrattaStandard tratta;
    private int prezzo;
    private int postiPrima;
    private int postiSeconda;
    private int postiTerza;
    private int postiTot;
    private int tempoPercorrenza;

    public Treno(String trenoID, String tipoTrenoI, TrattaStandard tratta,
                 int prezzo, int postiPrima, int postiSeconda,int postiTerza, int postiTot) {
        System.out.println("tratta passata: " + tratta);
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
        System.out.println("📌 Avvio calcolo tempo percorrenza");

        if (this.tratta == null) {
            System.err.println("❌ ERRORE: Tratta non inizializzata (null)");
            throw new IllegalStateException("Tratta non inizializzata");
        }

        if (this.tipoTreno == null || this.tipoTreno.isEmpty()) {
            System.err.println("❌ ERRORE: tipoTreno non inizializzato");
            throw new IllegalStateException("tipoTreno non inizializzato");
        }

        System.out.println("✅ Tratta passata: " + tratta.getStazionePartenza() + " → " + tratta.getStazioneArrivo()
                + ", distanza: " + tratta.getDistanza() + " km");

        double distanza = tratta.getDistanza();
        double velocita;

        switch (tipoTreno) {
            case "FrecciaArgento":
                velocita = 300;
                break;
            case "FrecciaRossa":
                velocita = 250;
                break;
            case "FrecciaBianca":
                velocita = 200;
                break;
            case "Regionale":
                velocita = 150;
                break;
            default:
                System.err.println("❌ ERRORE: Tipo treno non riconosciuto: " + tipoTreno);
                throw new IllegalArgumentException("Tipo treno non valido: " + tipoTreno);
        }

        double tempo = distanza / velocita;
        int tempoArrotondato = (int) Math.ceil(tempo);

        System.out.println("🛤 TipoTreno: " + tipoTreno + " → Velocità media: " + velocita + " km/h");
        System.out.println("⏱ Tempo stimato (raw): " + tempo + " h → Arrotondato: " + tempoArrotondato + " h");

        return tempoArrotondato;

    }


    private String setTipoTreno(String tipoTrenoI) {
        if( Arrays.asList(tipiTreni).contains(tipoTrenoI)) {
            return tipoTrenoI;
        }
        throw new IllegalArgumentException("Tipo di treno non valido: " + tipoTrenoI);
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

   public TrattaStandard getTratta() {
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
// setters posti


    public void setPostiPrima(int postiPrima) {
        this.postiPrima = postiPrima;
    }

    public void setPostiSeconda(int postiSeconda) {
        this.postiSeconda = postiSeconda;
    }

    public void setPostiTerza(int postiTerza) {
        this.postiTerza = postiTerza;
    }

    public void setPostiTot(int postiTot) {
        this.postiTot = postiTot;
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
