package it.trenical.server.Treno;

import io.grpc.stub.StreamObserver;
import it.trenical.grpc.GetAllTreniResponse;
import it.trenical.grpc.GetTreniByTrattaIDResponse;
import it.trenical.grpc.Treno;
import it.trenical.server.Tratta.TrattaImpl;
import it.trenical.server.Tratta.TrattaImplDB;
import it.trenical.server.Tratta.TrattaPrototype;
import it.trenical.server.Tratta.TrattaStandard;

import java.util.List;


public class TrenoServiceImpl extends it.trenical.grpc.TrenoServiceGrpc.TrenoServiceImplBase {
    private final TrenoImpl db = TrenoImplDB.getInstance();
    private final TrattaImpl trattadb = TrattaImplDB.getInstance();
    @Override
    public void addTreno(it.trenical.grpc.AddTrenoRequest request, StreamObserver<it.trenical.grpc.AddTrenoResponse> responseObserver) {
        TrenoConcr trenoJava = convertiProtoInJava(request.getTreno());
        System.out.println("SET TRENO");
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

        Treno trenoProto = convertiJavaInProto((TrenoConcr) trenoJava);

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
        try {
            List<it.trenical.server.Treno.Treno> listaTreni = db.getAllTreno();
            System.out.println(listaTreni);
            GetAllTreniResponse.Builder response = GetAllTreniResponse.newBuilder();

            if (listaTreni != null) {
                for (it.trenical.server.Treno.Treno trenoJava : listaTreni) {
                    Treno trenoProto = convertiJavaInProto((TrenoConcr) trenoJava);
                    response.addTreni(trenoProto);
                    System.out.println(trenoProto);
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

    @Override
    public void getTreniByTrattaID(it.trenical.grpc.GetTreniByTrattaIDRequest request,
                                   StreamObserver<GetTreniByTrattaIDResponse> responseObserver) {
        try {
            String trattaID = request.getTrattaID();
            List<it.trenical.server.Treno.Treno> listaTreni = db.getTrenoByTrattaID(trattaID);
            System.out.println(listaTreni);
            GetTreniByTrattaIDResponse.Builder response = GetTreniByTrattaIDResponse.newBuilder();
            if (listaTreni != null) {
                for (it.trenical.server.Treno.Treno trenoJava : listaTreni) {
                    Treno trenoProto = convertiJavaInProto((TrenoConcr) trenoJava);
                    response.addTreni(trenoProto);
                    System.out.println(trenoProto);
                }
            }

            responseObserver.onNext(response.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Errore interno in getTrenoByTattaID()")
                    .withCause(e)
                    .asRuntimeException());
        }
    }
    private TrenoConcr convertiProtoInJava(Treno t) {
        String trattaID = t.getTrattaID();
        TrattaPrototype trattaProto = trattadb.getTratta(trattaID) ;
        TrattaStandard tratta = new TrattaStandard(
                trattaProto.getCodiceTratta(),
                trattaProto.getStazionePartenza(),
                trattaProto.getStazioneArrivo(),
                trattaProto.getDataPartenza(),
                trattaProto.getDataArrivo(),
                trattaProto.getDistanza(),
                trattaProto.getTempoPercorrenza()
        );

        return new TrenoConcr(
                t.getTrenoID(),
                t.getTipoTreno(),
                tratta,
                t.getPrezzo(),
                t.getPostiPrima(),
                t.getPostiSeconda(),
                t.getPostiTerza(),
                t.getPostiTot(),
                t.getBinario(),
                t.getPromozione()

        );

    }

    private Treno convertiJavaInProto(TrenoConcr t) {
        TrattaStandard tratta = (TrattaStandard) t.getTratta();

        return Treno.newBuilder()
                .setTrenoID(t.getTrenoID())
                .setTipoTreno(t.getTipoTreno())
                .setTrattaID(tratta.getCodiceTratta())
                .setPrezzo(t.getPrezzo())
                .setPostiPrima(t.getPostiPrima())
                .setPostiSeconda(t.getPostiSeconda())
                .setPostiTerza(t.getPostiTerza())
                .setPostiTot(t.getPostiTot())
                .setBinario(t.getBinario())
                .setPromozione(t.getPromozione())
                .build();
    }


}