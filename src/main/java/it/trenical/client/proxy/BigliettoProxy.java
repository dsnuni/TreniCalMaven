package it.trenical.client.proxy;

import io.grpc.ManagedChannel;
import it.trenical.grpc.*;
import java.util.List;

public class BigliettoProxy {

    private final ManagedChannel channel;
    private Biglietto biglietto;

    public BigliettoProxy(ManagedChannel channel) {
        this.channel = channel;
    }

    public static class Builder {
        private final ManagedChannel channel;
        private String classe;
        private String trenoID;
        private String clienteID;
        private String priorita;

        public Builder(ManagedChannel channel) {
            this.channel = channel;
        }

        public Builder classe(String classe) {
            this.classe = classe;
            return this;
        }

        public Builder trenoID(String trenoID) {
            this.trenoID = trenoID;
            return this;
        }

        public Builder clienteID(String clienteID) {
            this.clienteID = clienteID;
            return this;
        }

        public Builder priorita(String priorita) {
            this.priorita = priorita;
            return this;
        }

        public BigliettoProxy buildAndCreate() {
            BigliettoProxy proxy = new BigliettoProxy(channel);
            proxy.creaBiglietto(classe, trenoID, clienteID, priorita);
            return proxy;
        }
    }

    private void creaBiglietto(String classe, String trenoID, String clienteID, String priorita) {
        try {
            BigliettoServiceGrpc.BigliettoServiceBlockingStub stub =
                    BigliettoServiceGrpc.newBlockingStub(channel);

            CreaBigliettoRequest request = CreaBigliettoRequest.newBuilder()
                    .addDati(classe)
                    .addDati(trenoID)
                    .addDati(clienteID)
                    .addDati(priorita)
                    .build();

            CreaBigliettoResponse response = stub.creaBiglietto(request);

            if (!response.getSuccess()) {
                throw new RuntimeException("Creazione biglietto fallita");
            }

        } catch (Exception e) {
            throw new RuntimeException("Errore durante la creazione del biglietto: " + e.getMessage(), e);
        }
    }

    public Biglietto getBiglietto(String bigliettoID) {
        try {
            BigliettoServiceGrpc.BigliettoServiceBlockingStub stub =
                    BigliettoServiceGrpc.newBlockingStub(channel);

            GetBigliettoRequest request = GetBigliettoRequest.newBuilder()
                    .setBigliettoID(bigliettoID)
                    .build();

            return stub.getBiglietto(request);
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero del biglietto: " + e.getMessage(), e);
        }
    }

    public boolean removeBiglietto(String bigliettoID) {
        try {
            BigliettoServiceGrpc.BigliettoServiceBlockingStub stub =
                    BigliettoServiceGrpc.newBlockingStub(channel);

            RemoveBigliettoRequest request = RemoveBigliettoRequest.newBuilder()
                    .setBigliettoID(bigliettoID)
                    .build();

            RemoveBigliettoResponse response = stub.removeBiglietto(request);
            return response.getSuccess();
        } catch (Exception e) {
            throw new RuntimeException("Errore nella rimozione del biglietto: " + e.getMessage(), e);
        }
    }

    public List<Biglietto> getBigliettiByFiltro(String colonna, String valore) {
        try {
            BigliettoServiceGrpc.BigliettoServiceBlockingStub stub =
                    BigliettoServiceGrpc.newBlockingStub(channel);

            GetBigliettiByFiltroRequest request = GetBigliettiByFiltroRequest.newBuilder()
                    .setColonna(colonna)
                    .setValore(valore)
                    .build();

            return stub.getBigliettiByFiltro(request).getBigliettiList();
        } catch (Exception e) {
            throw new RuntimeException("Errore nel filtraggio dei biglietti: " + e.getMessage(), e);
        }
    }
}
