package it.trenical.server.Tratta;

public abstract class TrattaPrototype {
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
        return stazionePartenza + " → " + stazioneArrivo + " | " + dataPartenza + " → " +
                dataArrivo + " | Durata: " +
                tempoPercorrenza + "h"+ " | Distanza: " +
                distanza;
    }
}
