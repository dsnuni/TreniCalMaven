package it.trenical.client.gui;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import it.trenical.grpc.*;

public class testGetAllTreni {

    public static void testGetAllTreni() {
        // Crea il canale gRPC verso il server
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        // Stub del servizio treno
        TrenoServiceGrpc.TrenoServiceBlockingStub stub = TrenoServiceGrpc.newBlockingStub(channel);
    System.out.println("ciao");
        // Richiesta vuota
        GetAllTreniRequest request = GetAllTreniRequest.newBuilder().build();

        try {
            // Invocazione
            GetAllTreniResponse response = stub.getAllTreni(request);

            // Output dei risultati
            System.out.println("Lista dei treni trovati:");
            for (Treno treno : response.getTreniList()) {
                System.out.println("ID: " + treno.getTrenoID() +
                        ", Tipo: " + treno.getTipoTreno() +
                        ", Tratta: " + treno.getTrattaID() +
                        ", Prezzo: " + treno.getPrezzo() +
                        ", Posti 1ª: " + treno.getPostiPrima() +
                        ", Posti 2ª: " + treno.getPostiSeconda() +
                        ", Posti 3ª: " + treno.getPostiTerza() +
                        ", Posti totali: " + treno.getPostiTot());
            }

        } catch (Exception e) {
            System.err.println("Errore durante la richiesta getAllTreni: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Chiusura canale
            channel.shutdownNow();
        }
    }

    public static void main(String[] args) {
        testGetAllTreni();
    }
}
