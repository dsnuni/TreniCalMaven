package it.trenical.server.Tratta;

import it.trenical.server.Treno.Treno;

import java.util.List;

public interface TrattaImpl {
     TrattaStandard getTratta(String trattaID);
     void setTratta( TrattaPrototype tratta );
     boolean removeTratta( String trattaID);
      List<TrattaStandard> getAllTratte();
      List<TrattaPrototype> getTratteByFiltro(String colonna, String valore);
}
