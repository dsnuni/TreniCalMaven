package it.trenical.client.proxy;

import io.grpc.ManagedChannel;
import it.trenical.grpc.*;

public class TrattaProxy {

    private final ManagedChannel channel;
    private TrattaStandard tratta;

    public TrattaProxy(ManagedChannel channel) {
        this.channel = channel;
    }

    public static class Builder {
        private final ManagedChannel channel;
        private String codiceTratta;
        private String stazionePartenza;
        private String stazioneArrivo;
        private String dataPartenza;
        private String dataArrivo;
        private int distanza;
        private int tempoPercorrenza;

        public Builder(ManagedChannel channel) {
            this.channel = channel;
        }

        public Builder codiceTratta(String codiceTratta) {
            this.codiceTratta = codiceTratta;
            return this;
        }

        public Builder stazionePartenza(String stazionePartenza) {
            this.stazionePartenza = stazionePartenza;
            return this;
        }

        public Builder stazioneArrivo(String stazioneArrivo) {
            this.stazioneArrivo = stazioneArrivo;
            return this;
        }

        public Builder dataPartenza(String dataPartenza) {
            this.dataPartenza = dataPartenza;
            return this;
        }

        public Builder dataArrivo(String dataArrivo) {
            this.dataArrivo = dataArrivo;
            return this;
        }

        public Builder distanza(int distanza) {
            this.distanza = distanza;
            return this;
        }

        public Builder tempoPercorrenza(int tempoPercorrenza) {
            this.tempoPercorrenza = tempoPercorrenza;
            return this;
        }

        public TrattaProxy buildAndRegister() {
            TrattaProxy proxy = new TrattaProxy(channel);
            proxy.registerTratta(codiceTratta, stazionePartenza, stazioneArrivo, dataPartenza, dataArrivo, distanza, tempoPercorrenza);
            return proxy;
        }
    }

    private void registerTratta(String codiceTratta, String stazionePartenza, String stazioneArrivo,
                                String dataPartenza, String dataArrivo, int distanza, int tempoPercorrenza) {
        try {
            TrattaServiceGrpc.TrattaServiceBlockingStub stub = TrattaServiceGrpc.newBlockingStub(channel);

            this.tratta = TrattaStandard.newBuilder()
                    .setCodiceTratta(codiceTratta)
                    .setStazionePartenza(stazionePartenza)
                    .setStazioneArrivo(stazioneArrivo)
                    .setDataPartenza(dataPartenza)
                    .setDataArrivo(dataArrivo)
                    .setDistanza(distanza)
                    .setTempoPercorrenza(tempoPercorrenza)
                    .build();

            AddTrattaRequest request = AddTrattaRequest.newBuilder().setTratta(this.tratta).build();
            AddTrattaResponse response = stub.addTratta(request);

            if (!response.getSuccess()) {
                this.tratta = null;
                throw new RuntimeException("Registrazione tratta fallita");
            }

        } catch (Exception e) {
            this.tratta = null;
            throw new RuntimeException("Errore durante la registrazione della tratta: " + e.getMessage(), e);
        }
    }

    public TrattaStandard getTratta() {
        return this.tratta;
    }

    public TrattaStandard getTratta(String codiceTratta) {
        try {
            TrattaServiceGrpc.TrattaServiceBlockingStub stub = TrattaServiceGrpc.newBlockingStub(channel);
            GetTrattaRequest request = GetTrattaRequest.newBuilder()
                    .setCodiceTratta(codiceTratta)
                    .build();
            return stub.getTratta(request);
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero della tratta: " + e.getMessage(), e);
        }
    }
}
