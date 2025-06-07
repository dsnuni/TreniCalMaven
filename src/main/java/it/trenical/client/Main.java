package it.trenical.client;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;


public class Main {
    public static void main(String[] args) {

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();


        it.trenical.grpc.ClienteServiceGrpc.ClienteServiceBlockingStub clienteStub = it.trenical.grpc.ClienteServiceGrpc.newBlockingStub(channel);
        it.trenical.grpc.TrenoServiceGrpc.TrenoServiceBlockingStub trenoStub = it.trenical.grpc.TrenoServiceGrpc.newBlockingStub(channel);
        it.trenical.grpc.BigliettoServiceGrpc.BigliettoServiceBlockingStub bigliettoStub = it.trenical.grpc.BigliettoServiceGrpc.newBlockingStub(channel);


        it.trenical.grpc.Cliente cliente = it.trenical.grpc.Cliente.newBuilder()
                .setCodiceFiscale("RSSMRA80A01H501Z")
                .setNome("Mario")
                .setCognome("Rossi")
                .setCodiceCliente("TRNCL123")
                .setEta(42)
                .build();

        it.trenical.grpc.AddClienteResponse clienteResponse = clienteStub.addCliente(it.trenical.grpc.AddClienteRequest.newBuilder()
                .setCliente(cliente).build());
        System.out.println("✔ Cliente aggiunto: " + clienteResponse.getSuccess());

        it.trenical.grpc.Tratta tratta = it.trenical.grpc.Tratta.newBuilder()
                .setCodiceTratta("TR001")
                .setStazionePartenza("Milano")
                .setStazioneArrivo("Roma")
                .setDataPartenza("2025-06-15 08:00")
                .setDataArrivo("2025-06-15 11:00")
                .setDistanza(570)
                .setTempoPercorrenza(180)
                .build();

        it.trenical.grpc.Treno treno = it.trenical.grpc.Treno.newBuilder()
                .setTrenoID(1001)
                .setTipoTreno("Frecciarossa")
                .setTratta(tratta)
                .build();

        it.trenical.grpc.AddTrenoResponse trenoResponse = trenoStub.addTreno(it.trenical.grpc.AddTrenoRequest.newBuilder().setTreno(treno).build());
        System.out.println("✔ Treno aggiunto: " + trenoResponse.getSuccess());

        it.trenical.grpc.Biglietto biglietto = it.trenical.grpc.Biglietto.newBuilder()
                .setBigliettoID("B001")
                .setClasse("SecondaClasse")
                .setTrenoID(1001)
                .setCarrozza("A")
                .setPosto("12A")
                .setClienteID("TRNCL123")
                .addPriorita("Finestrino")
                .setPrezzo(49)
                .build();

        it.trenical.grpc.AddBigliettoResponse bigliettoResponse = bigliettoStub.addBiglietto(
                it.trenical.grpc.AddBigliettoRequest.newBuilder().setBiglietto(biglietto).build());
        System.out.println("✔ Biglietto aggiunto: " + bigliettoResponse.getSuccess());

        channel.shutdown();
    }
}
