package it.trenical.server.Treno;

import io.grpc.stub.StreamObserver;
import it.trenical.grpc.GetAllTreniResponse;
import it.trenical.grpc.Tratta;
import it.trenical.grpc.Treno;
import it.trenical.server.Tratta.TrattaPrototype;
import it.trenical.server.Tratta.TrattaStandard;

import java.util.List;


public class TrenoServiceImpl extends it.trenical.grpc.TrenoServiceGrpc.TrenoServiceImplBase {
    private final TrenoImpl db = new TrenoImplDB();

    @Override
    public void addTreno(it.trenical.grpc.AddTrenoRequest request, StreamObserver<it.trenical.grpc.AddTrenoResponse> responseObserver) {
        TrenoConcr trenoJava = convertiProtoInJava(request.getTreno());

        db.setTreno(trenoJava);
        it.trenical.grpc.AddTrenoResponse response = it.trenical.grpc.AddTrenoResponse.newBuilder()
                .setSuccess(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getTreno(it.trenical.grpc.GetTrenoRequest request, StreamObserver<Treno> responseObserver) {
        it.trenical.server.Treno.Treno trenoJava = db.getTreno(request.getTrenoID());

        Treno trenoProto = convertiJavaInProto(trenoJava);

        responseObserver.onNext(trenoProto);
        responseObserver.onCompleted();

    }

    @Override
    public void removeTreno(it.trenical.grpc.RemoveTrenoRequest request, StreamObserver<it.trenical.grpc.RemoveTrenoResponse> responseObserver) {
        boolean successo = db.removeTreno(request.getTrenoID());

        it.trenical.grpc.RemoveTrenoResponse response = it.trenical.grpc.RemoveTrenoResponse.newBuilder()
                .setSuccess(successo)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    @Override
    public void getAllTreni(it.trenical.grpc.GetAllTreniRequest request, StreamObserver<it.trenical.grpc.GetAllTreniResponse> responseObserver) {
        List<it.trenical.server.Treno.Treno> listaTreni = db.getAllTreno();

        GetAllTreniResponse.Builder response = GetAllTreniResponse.newBuilder();
        for (it.trenical.server.Treno.Treno trenoJava : listaTreni) {
            Treno trenoProto = convertiJavaInProto(trenoJava);
            response.addTreni(trenoProto);
        }
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }
    private TrenoConcr convertiProtoInJava(it.trenical.grpc.Treno trenoProto) {
        Tratta tratta = trenoProto.getTratta();
        TrattaPrototype trattaJava = new TrattaStandard(
                tratta.getCodiceTratta(),
                tratta.getStazionePartenza(),
                tratta.getStazioneArrivo(),
                tratta.getDataPartenza(),
                tratta.getDataArrivo(),
                tratta.getDistanza(),
                tratta.getTempoPercorrenza()
        );
        return new TrenoConcr(trenoProto.getTrenoID(), trenoProto.getTipoTreno(), trattaJava);
    }

    private it.trenical.grpc.Treno convertiJavaInProto(it.trenical.server.Treno.Treno trenoJava) {
        TrattaPrototype tratta = trenoJava.getTratta();
        Tratta trattaProto = Tratta.newBuilder()
                .setCodiceTratta(tratta.getCodiceTratta())
                .setStazionePartenza(tratta.getStazionePartenza())
                .setStazioneArrivo(tratta.getStazioneArrivo())
                .setDataPartenza(tratta.getDataPartenza())
                .setDataArrivo(tratta.getDataArrivo())
                .setDistanza(tratta.getDistanza())
                .setTempoPercorrenza(tratta.getTempoPercorrenza())
                .build();

        return it.trenical.grpc.Treno.newBuilder()
                .setTrenoID(trenoJava.getTrenoID())
                .setTipoTreno(trenoJava.getTipoTreno())
                .setTratta(trattaProto)
                .build();
    }
}