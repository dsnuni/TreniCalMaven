package it.trenical.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import it.trenical.server.Cliente.ClienteServiceImpl;
import it.trenical.server.Treno.TrenoServiceImpl;
import it.trenical.server.Biglietto.BigliettoServiceImpl;
import java.io.IOException;

public class TreniCalServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(50051)
                .addService(new ClienteServiceImpl())
                .addService(new TrenoServiceImpl())
                .addService(new BigliettoServiceImpl())
                .build();

        server.start();
        System.out.println("Server gRPC avviato sulla porta 50051");
        server.awaitTermination();
    }
}
