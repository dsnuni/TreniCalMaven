package it.trenical.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import it.trenical.grpc.TreniCalProto;
import it.trenical.grpc.ClienteServiceGrpc;
import it.trenical.grpc.ClienteServiceGrpc.ClienteServiceBlockingStub;

public class ClienteClient {
    public static void main(String[] args) {
        // Creazione del canale verso il server GRPC
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        // Stub bloccante generato da TreniCal.proto
        ClienteServiceBlockingStub stub = ClienteServiceGrpc.newBlockingStub(channel);

        // Costruzione di un oggetto Cliente utilizzando i metodi generati
        it.trenical.grpc.Cliente cliente = it.trenical.grpc.Cliente.newBuilder()
                .setCodiceFiscale("ABCDEF01G23H456I")
                .setNome("Mario")
                .setCognome("Rossi")
                .setCodiceCliente("CL123")
                .setEta(30)
                .build();

        it.trenical.grpc.AddClienteRequest request = it.trenical.grpc.AddClienteRequest.newBuilder()
                .setCliente(cliente)
                .build();

        it.trenical.grpc.AddClienteResponse response = stub.addCliente(request);
        System.out.println(cliente.getCodiceCliente());
        System.out.println("Operazione completata, success = " + response.getSuccess());


        channel.shutdown();
        }
    }