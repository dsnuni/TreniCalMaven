package it.trenical.server.Biglietto;

import io.grpc.stub.StreamObserver;
import it.trenical.grpc.Biglietto;

public class BigliettoServiceImpl extends it.trenical.grpc.BigliettoServiceGrpc.BigliettoServiceImplBase {

    @Override
    public void addBiglietto(it.trenical.grpc.AddBigliettoRequest request, StreamObserver<it.trenical.grpc.AddBigliettoResponse> responseObserver) {
        Biglietto biglietto = request.getBiglietto();
        System.out.println("Aggiunto biglietto ID: " + biglietto.getBigliettoID());

        it.trenical.grpc.AddBigliettoResponse response = it.trenical.grpc.AddBigliettoResponse.newBuilder()
                .setSuccess(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getBiglietto(it.trenical.grpc.GetBigliettoRequest request, StreamObserver<Biglietto> responseObserver) {
        String id = request.getBigliettoID();
        System.out.println("Richiesta biglietto ID: " + id);

        Biglietto biglietto = Biglietto.newBuilder()
                .setBigliettoID(id)
                .setClasse("SecondaClasse")
                .setTrenoID(1001)
                .setCarrozza("B")
                .setPosto("12A")
                .setClienteID("TRNCL123")
                .addPriorita("Finestrino")
                .setPrezzo(35)
                .build();

        responseObserver.onNext(biglietto);
        responseObserver.onCompleted();
    }

    @Override
    public void removeBiglietto(it.trenical.grpc.RemoveBigliettoRequest request, StreamObserver<it.trenical.grpc.RemoveBigliettoResponse> responseObserver) {
        String id = request.getBigliettoID();
        System.out.println("Rimozione biglietto ID: " + id);

        it.trenical.grpc.RemoveBigliettoResponse response = it.trenical.grpc.RemoveBigliettoResponse.newBuilder()
                .setSuccess(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}