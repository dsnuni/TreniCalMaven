package it.trenical.server.Tratta;

public class TrattaStandard extends TrattaPrototype{
    public TrattaStandard(){}
    public TrattaStandard(String codiceTratta, String stazionePartenza, String stazioneArrivo,String dataPartenza, String dataArrivo, int distanza, int tempoPercorrenza){
        this.codiceTratta = codiceTratta;
        this.stazionePartenza = stazionePartenza;
        this.stazioneArrivo = stazioneArrivo;
        this.dataPartenza = dataPartenza;
        this.dataArrivo = dataArrivo;
        this.distanza = distanza;
        this.tempoPercorrenza = tempoPercorrenza;
    }

    @Override
    public TrattaPrototype clone() {
        return new TrattaStandard(codiceTratta,stazionePartenza,stazioneArrivo,dataPartenza,dataArrivo,distanza,tempoPercorrenza);
    }

}
