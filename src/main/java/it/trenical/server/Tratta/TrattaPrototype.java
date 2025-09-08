package it.trenical.server.Tratta;

import java.util.Objects;

public abstract class TrattaPrototype implements Cloneable {
    String codiceTratta;
    String stazionePartenza;
    String stazioneArrivo;
    String dataPartenza;
    String dataArrivo;
    int distanza;
    int tempoPercorrenza;

    public abstract TrattaPrototype clone();
//getters
    public int getTempoPercorrenza() {
        return tempoPercorrenza;
    }

    public String getStazionePartenza() {
        return stazionePartenza;
    }

    public String getStazioneArrivo() {
        return stazioneArrivo;
    }

    public int getDistanza() {
        return distanza;
    }

    public String getDataPartenza() {
        return dataPartenza;
    }

    public String getDataArrivo() {
        return dataArrivo;
    }

    public String getCodiceTratta() {
        return codiceTratta;
    }
//setters
    public void setDataPartenza(String dataPartenza) {
        this.dataPartenza = dataPartenza;
    }

    public void setDataArrivo(String dataArrivo) {
        this.dataArrivo = dataArrivo;
    }

    public void setCodiceTratta(String codiceTratta) {
        this.codiceTratta = codiceTratta;
    }

    public void setStazioneArrivo(String stazioneArrivo) {
        this.stazioneArrivo = stazioneArrivo;
    }

    public void setStazionePartenza(String stazionePartenza) {
        this.stazionePartenza = stazionePartenza;
    }

    public void setTempoPercorrenza(int tempoPercorrenza) {
        this.tempoPercorrenza = tempoPercorrenza;
    }

    public void setDistanza(int distanza) {
        this.distanza = distanza;
    }

    @Override
    public String toString() {
        return "TrattaPrototype{" +
                "codiceTratta='" + codiceTratta + '\'' +
                ", stazionePartenza='" + stazionePartenza + '\'' +
                ", stazioneArrivo='" + stazioneArrivo + '\'' +
                ", dataPartenza='" + dataPartenza + '\'' +
                ", dataArrivo='" + dataArrivo + '\'' +
                ", distanza=" + distanza +
                ", tempoPercorrenza=" + tempoPercorrenza +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TrattaPrototype that = (TrattaPrototype) o;
        return distanza == that.distanza && tempoPercorrenza == that.tempoPercorrenza && Objects.equals(codiceTratta, that.codiceTratta) && Objects.equals(stazionePartenza, that.stazionePartenza) && Objects.equals(stazioneArrivo, that.stazioneArrivo) && Objects.equals(dataPartenza, that.dataPartenza) && Objects.equals(dataArrivo, that.dataArrivo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceTratta, stazionePartenza, stazioneArrivo, dataPartenza, dataArrivo, distanza, tempoPercorrenza);
    }
}
