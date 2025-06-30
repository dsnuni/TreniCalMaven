package it.trenical.client.proxy;

import io.grpc.ManagedChannel;
import it.trenical.grpc.*;

public class TrenoProxy {

    private final ManagedChannel channel;
    private Treno treno;

    public TrenoProxy(ManagedChannel channel) {
        this.channel = channel;
    }

    public static class Builder {
        private final ManagedChannel channel;
        private String trenoID;
        private String tipoTreno;
        private TrattaStandard tratta;
        private int prezzo;
        private int postiPrima;
        private int postiSeconda;
        private int postiTerza;
        private int postiTot;

        public Builder(ManagedChannel channel) {
            this.channel = channel;
        }

        public Builder trenoID(String trenoID) {
            this.trenoID = trenoID;
            return this;
        }

        public Builder tipoTreno(String tipoTreno) {
            this.tipoTreno = tipoTreno;
            return this;
        }

        public Builder tratta(TrattaStandard tratta) {
            this.tratta = tratta;
            return this;
        }

        public Builder prezzo(int prezzo) {
            this.prezzo = prezzo;
            return this;
        }

        public Builder postiPrima(int postiPrima) {
            this.postiPrima = postiPrima;
            return this;
        }

        public Builder postiSeconda(int postiSeconda) {
            this.postiSeconda = postiSeconda;
            return this;
        }

        public Builder postiTerza(int postiTerza) {
            this.postiTerza = postiTerza;
            return this;
        }

        public Builder postiTot(int postiTot) {
            this.postiTot = postiTot;
            return this;
        }

        public TrenoProxy buildAndRegister() {
            TrenoProxy proxy = new TrenoProxy(channel);
            proxy.registerTreno(trenoID, tipoTreno, tratta, prezzo, postiPrima, postiSeconda, postiTerza, postiTot);
            return proxy;
        }
    }

    private void registerTreno(String trenoID, String tipoTreno, TrattaStandard tratta,
                               int prezzo, int postiPrima, int postiSeconda, int postiTerza, int postiTot) {
        try {
            TrenoServiceGrpc.TrenoServiceBlockingStub trenoStub = TrenoServiceGrpc.newBlockingStub(channel);

            this.treno = Treno.newBuilder()
                    .setTrenoID(trenoID)
                    .setTipoTreno(tipoTreno)
                    .setTrattaID(tratta.getCodiceTratta())
                    .setPrezzo(prezzo)
                    .setPostiPrima(postiPrima)
                    .setPostiSeconda(postiSeconda)
                    .setPostiTerza(postiTerza)
                    .setPostiTot(postiTot)
                    .setTempoPercorrenza(tratta.getTempoPercorrenza())
                    .build();

            AddTrenoRequest request = AddTrenoRequest.newBuilder().setTreno(this.treno).build();
            AddTrenoResponse response = trenoStub.addTreno(request);

            if (!response.getSuccess()) {
                this.treno = null;
                throw new RuntimeException("Registrazione treno fallita");
            }

        } catch (Exception e) {
            this.treno = null;
            throw new RuntimeException("Errore durante la registrazione del treno: " + e.getMessage(), e);
        }
    }

    public Treno getTreno() {
        return this.treno;
    }

    public Treno getTreno(String trenoID) {
        try {
            TrenoServiceGrpc.TrenoServiceBlockingStub stub = TrenoServiceGrpc.newBlockingStub(channel);
            GetTrenoRequest request = GetTrenoRequest.newBuilder().setTrenoID(trenoID).build();
            return stub.getTreno(request);
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero del treno: " + e.getMessage(), e);
        }
    }
}
