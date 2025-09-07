package it.trenical.server.notifiche;

import io.grpc.stub.StreamObserver;
import it.trenical.grpc.GetNotificaRequest;
import it.trenical.grpc.GetNotificaResponse;
import it.trenical.grpc.Notifica;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificaServiceImpl extends it.trenical.grpc.NotificaServiceGrpc.NotificaServiceImplBase {

    public void getNotifica(GetNotificaRequest request, StreamObserver<GetNotificaResponse> responseObserver) {
        try {
            String codiceFiscale = request.getCliente();

            NotificaDB db = NotificaDB.getInstance();
            List<it.trenical.server.notifiche.Notifica> listaNotifiche = db.getNotifica(codiceFiscale);

            GetNotificaResponse.Builder response = GetNotificaResponse.newBuilder();

            if (listaNotifiche != null) {
                for (it.trenical.server.notifiche.Notifica notifica : listaNotifiche) {
                    Notifica converted = convertiJavaInProto(notifica);
                    response.addNotifiche(converted);
                    System.out.println("Notifica aggiunta: " + converted);
                }
            }

            responseObserver.onNext(response.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Errore interno in getNotifica()")
                    .withCause(e)
                    .asRuntimeException());
        }
    }

    private Notifica convertiJavaInProto(it.trenical.server.notifiche.Notifica n) {
        return it.trenical.grpc.Notifica.newBuilder()
                .setCliente(n.getCliente())
                .setTreno(n.getTreno())
                .setPartenza(n.getPartenza())
                .setArrivo(n.getArrivo())
                .setTempo(n.getTempo())
                .setBiglietto(n.getBiglietto())
                .setStato(n.getStato())
                .setPosto(n.getPosto())
                .setBinario(n.getBinario())
                .setLog(n.getLog())
                .build();
    }
}
