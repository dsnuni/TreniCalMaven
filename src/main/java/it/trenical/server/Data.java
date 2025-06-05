package it.trenical.server;

public record Data(int ora, int giorno, int mese, int anno) {

    public String toString() {
        String dataString = "";
        dataString += ora + "-" + giorno + "/" + mese + "/" + anno;
        return dataString;
    }

    public boolean equals(Data data) {
        if( ora != ora || giorno != giorno || mese != mese || anno != anno ){
            return false;
        }
        return true;
    }

}//data
