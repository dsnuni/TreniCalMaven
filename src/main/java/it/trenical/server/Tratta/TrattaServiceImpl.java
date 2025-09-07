package it.trenical.server.Tratta;

import io.grpc.stub.StreamObserver;
import it.trenical.grpc.*;
import it.trenical.server.Tratta.TrattaImpl;
import it.trenical.server.Tratta.TrattaImplDB;
import it.trenical.server.Tratta.TrattaStandard;
import it.trenical.server.Treno.TrenoConcr;

import java.util.List;

public class TrattaServiceImpl extends TrattaServiceGrpc.TrattaServiceImplBase {

    private final TrattaImpl trattaImpl = TrattaImplDB.getInstance();

    @Override
    public void addTratta(AddTrattaRequest request, StreamObserver<AddTrattaResponse> responseObserver) {
        TrattaStandard trattaJava = (TrattaStandard) convertiProtoInJava(request.getTratta());
        trattaImpl.setTratta(trattaJava);

        AddTrattaResponse response = AddTrattaResponse.newBuilder()
                .setSuccess(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
   public void getTratta(GetTrattaRequest request, StreamObserver<it.trenical.grpc.TrattaStandard> responseObserver) {
        String codice = request.getCodiceTratta();

        it.trenical.server.Tratta.TrattaPrototype trattaJava = trattaImpl.getTratta(codice);
        it.trenical.grpc.TrattaStandard trattaProto = convertiJavaInProto(trattaJava);

        responseObserver.onNext(trattaProto);
        responseObserver.onCompleted();
    }

    @Override
    public void removeTratta(RemoveTrattaRequest request, StreamObserver<RemoveTrattaResponse> responseObserver) {
        String codice = request.getCodiceTratta();
        boolean success = trattaImpl.removeTratta(codice);

        RemoveTrattaResponse response = RemoveTrattaResponse.newBuilder()
                .setSuccess(success)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private it.trenical.grpc.TrattaStandard convertiJavaInProto(it.trenical.server.Tratta.TrattaPrototype trattaJava) {
        return it.trenical.grpc.TrattaStandard.newBuilder()
                .setCodiceTratta(trattaJava.getCodiceTratta())
                .setStazionePartenza(trattaJava.getStazionePartenza())
                .setStazioneArrivo(trattaJava.getStazioneArrivo())
                .setDataPartenza(trattaJava.getDataPartenza())
                .setDataArrivo(trattaJava.getDataArrivo())
                .setDistanza(trattaJava.getDistanza())
                .setTempoPercorrenza(trattaJava.getTempoPercorrenza())
                .build();
    }

    private it.trenical.server.Tratta.TrattaPrototype convertiProtoInJava(it.trenical.grpc.TrattaStandard trattaProto) {
        return new TrattaStandard(
                trattaProto.getCodiceTratta(),
                trattaProto.getStazionePartenza(),
                trattaProto.getStazioneArrivo(),
                trattaProto.getDataPartenza(),
                trattaProto.getDataArrivo(),
                trattaProto.getDistanza(),
                trattaProto.getTempoPercorrenza()
        );
    }

    @Override
    public void getAllTratte(it.trenical.grpc.GetAllTratteRequest request, StreamObserver<it.trenical.grpc.GetAllTratteResponse> responseObserver) {
        try {
            TrattaImpl db = TrattaImplDB.getInstance();
            List<it.trenical.server.Tratta.TrattaStandard> listaTreni = db.getAllTratte();
            System.out.println(listaTreni);
            GetAllTratteResponse.Builder response = GetAllTratteResponse.newBuilder();

            if (listaTreni != null) {
                for (it.trenical.server.Tratta.TrattaStandard trattaJava : listaTreni) {
                    it.trenical.grpc.TrattaStandard tratta = convertiJavaInProto(trattaJava);
                    response.addTratta(tratta);
                    System.out.println(tratta);
                }
            }

            responseObserver.onNext(response.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Errore interno in getAllTreni()")
                    .withCause(e)
                    .asRuntimeException());
        }
    }


}