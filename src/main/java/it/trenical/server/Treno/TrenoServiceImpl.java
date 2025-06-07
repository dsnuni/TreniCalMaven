package it.trenical.server.Treno;

import io.grpc.stub.StreamObserver;
import it.trenical.grpc.Treno;


public class TrenoServiceImpl extends it.trenical.grpc.TrenoServiceGrpc.TrenoServiceImplBase {

    @Override
    public void addTreno(it.trenical.grpc.AddTrenoRequest request, StreamObserver<it.trenical.grpc.AddTrenoResponse> responseObserver) {
        Treno treno = request.getTreno();
        System.out.println("Aggiunto treno ID: " + treno.getTrenoID());

        it.trenical.grpc.AddTrenoResponse response = it.trenical.grpc.AddTrenoResponse.newBuilder()
                .setSuccess(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getTreno(it.trenical.grpc.GetTrenoRequest request, StreamObserver<Treno> responseObserver) {
        int id = request.getTrenoID();
        System.out.println("Richiesta treno ID: " + id);

        it.trenical.grpc.Tratta tratta = it.trenical.grpc.Tratta.newBuilder()
                .setCodiceTratta("TRT001")
                .setStazionePartenza("Milano")
                .setStazioneArrivo("Roma")
                .setDataPartenza("2025-06-10 08:00")
                .setDataArrivo("2025-06-10 11:00")
                .setDistanza(600)
                .setTempoPercorrenza(180)
                .build();

        Treno treno = Treno.newBuilder()
                .setTrenoID(id)
                .setTipoTreno("Frecciarossa")
                .setTratta(tratta)
                .build();

        responseObserver.onNext(treno);
        responseObserver.onCompleted();
    }

    @Override
    public void removeTreno(it.trenical.grpc.RemoveTrenoRequest request, StreamObserver<it.trenical.grpc.RemoveTrenoResponse> responseObserver) {
        int id = request.getTrenoID();
        System.out.println("Rimozione treno ID: " + id);

        it.trenical.grpc.RemoveTrenoResponse response = it.trenical.grpc.RemoveTrenoResponse.newBuilder()
                .setSuccess(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}