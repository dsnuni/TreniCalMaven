package it.trenical.server.notifiche;


import io.grpc.stub.StreamObserver;
import it.trenical.grpc.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NotificaServiceImpl extends NotificaServiceGrpc.NotificaServiceImplBase {

    @Override
    public void getNotifiche(GetNotificheRequest request, StreamObserver<GetNotificheResponse> responseObserver) {
        String clienteID = request.getClienteID();
        List<Notifica> notificheList = estraiNotifichePerCliente(clienteID);

        GetNotificheResponse.Builder responseBuilder = GetNotificheResponse.newBuilder();
        responseBuilder.addAllNotifiche(notificheList);

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    private List<Notifica> estraiNotifichePerCliente(String clienteID) {
        // 🔁 Simulazione: questo andrà sostituito con il tuo vero sistema notifiche
        List<Notifica> notifiche = new ArrayList<>();

        Notifica notifica = Notifica.newBuilder()
                .setClienteID(clienteID)
                .setMessaggio("Il tuo treno partirà tra meno di 1 ora!")
                .setDataNotifica(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                .build();

        notifiche.add(notifica);
        return notifiche;
    }
}

