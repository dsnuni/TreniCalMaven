package it.trenical.server.Biglietto;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import it.trenical.grpc.*;
import it.trenical.server.Cliente.Cliente;
import it.trenical.server.Cliente.ClienteImpl;
import it.trenical.server.Cliente.ClienteImplDB;
import it.trenical.server.Treno.Treno;
import it.trenical.server.Treno.TrenoImpl;
import it.trenical.server.Treno.TrenoImplDB;
import it.trenical.server.igGenerator.CreatoreBiglietto;

import java.util.ArrayList;
import java.util.List;

public class BigliettoServiceImpl extends it.trenical.grpc.BigliettoServiceGrpc.BigliettoServiceImplBase {

    private final BigliettoImpl db = BigliettoDB.getInstance();
    private final TrenoImpl trenoDB = TrenoImplDB.getInstance();
    private final ClienteImpl clienteDB = ClienteImplDB.getInstance();

    @Override
    public void addBiglietto(it.trenical.grpc.AddBigliettoRequest request, StreamObserver<it.trenical.grpc.AddBigliettoResponse> responseObserver) {
        try {
            System.out.println("Biglietto ricevuto: " + request.getBiglietto());
            Biglietto bigliettoJava = convertiProtoInJava(request.getBiglietto());
            System.out.println("Biglietto convertito: " + bigliettoJava);

            db.setBiglietto(bigliettoJava);
            System.out.println("Biglietto salvato con successo.");

            it.trenical.grpc.AddBigliettoResponse response = it.trenical.grpc.AddBigliettoResponse.newBuilder()
                    .setSuccess(true)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            System.err.println("ERRORE in addBiglietto:");
            e.printStackTrace();
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Errore interno durante l'aggiunta del biglietto")
                            .withCause(e)
                            .asRuntimeException()
            );
        }
    }



    @Override
    public void getBiglietto(it.trenical.grpc.GetBigliettoRequest request, StreamObserver<it.trenical.grpc.Biglietto> responseObserver) {
        it.trenical.server.Biglietto.Biglietto bigliettoJava = db.getBiglietto(request.getBigliettoID());
        it.trenical.grpc.Biglietto bigliettoProto = convertiJavaInProto(bigliettoJava);

        responseObserver.onNext(bigliettoProto);
        responseObserver.onCompleted();
    }

    @Override
    public void removeBiglietto(it.trenical.grpc.RemoveBigliettoRequest request, StreamObserver<it.trenical.grpc.RemoveBigliettoResponse> responseObserver) {
        boolean success = db.removeBiglietto(request.getBigliettoID());

        it.trenical.grpc.RemoveBigliettoResponse response = it.trenical.grpc.RemoveBigliettoResponse.newBuilder()
                .setSuccess(success)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private Biglietto convertiProtoInJava(it.trenical.grpc.Biglietto proto) {
        Treno treno = trenoDB.getTreno(proto.getTrenoID());
        System.out.println("treno: " + treno);
        System.out.println("CLienteID: " + proto.getClienteID());
        Cliente cliente = clienteDB.getCliente(proto.getClienteID());
        System.out.println("cliente: " + cliente);
        if (treno == null) {
            throw new IllegalArgumentException("Treno con ID " + proto.getTrenoID() + " non trovato.");
        }

        if (cliente == null) {
            throw new IllegalArgumentException("Cliente con ID " + proto.getClienteID() + " non trovato.");
        }

        List<String> priorita = proto.getPrioritaList();

        return switch (proto.getClasse()) {
            case "PrimaClasse" -> new BPrimaClasse.Builder()
                    .bigliettoID(proto.getBigliettoID())
                    .titolareBiglietto(cliente)
                    .trenoBiglietto(treno)
                    .carrozza(proto.getCarrozza())
                    .posto(proto.getPosto())
                    .priorità(priorita)
                    .prezzo(proto.getPrezzo())
                    .implementazione(db)
                    .build();

            case "SecondaClasse" -> new BSecondaClasse.Builder()
                    .bigliettoID(proto.getBigliettoID())
                    .titolareBiglietto(cliente)
                    .trenoBiglietto(treno)
                    .carrozza(proto.getCarrozza())
                    .posto(proto.getPosto())
                    .priorità(priorita)
                    .prezzo(proto.getPrezzo())
                    .implementazione(db)
                    .build();

            default -> new BTerzaClasse.Builder()
                    .bigliettoID(proto.getBigliettoID())
                    .titolareBiglietto(cliente)
                    .trenoBiglietto(treno)
                    .carrozza(proto.getCarrozza())
                    .posto(proto.getPosto())
                    .priorità(priorita)
                    .prezzo(proto.getPrezzo())
                    .implementazione(db)
                    .build();
        };
    }

    private it.trenical.grpc.Biglietto convertiJavaInProto(Biglietto java) {
        return it.trenical.grpc.Biglietto.newBuilder()
                .setBigliettoID(java.getBigliettoID())
                .setClasse(java.getClass().getSimpleName().replace("B", ""))
                .setTrenoID(java.getTrenoBiglietto().getTrenoID())
                .setCarrozza(java.getCarrozza())
                .setPosto(java.getPosto())
                .setClienteID(java.getTitolareBiglietto().getCodiceCliente())
                .addAllPriorita(java.getPriorità())
                .setPrezzo(java.getPrezzo())
                .build();
    }

    @Override
    public void getBigliettiByFiltro(GetBigliettiByFiltroRequest request,
                                     StreamObserver<GetBigliettiByFiltroResponse> responseObserver) {
        try {
            String colonna = request.getColonna();
            String valore = request.getValore();

            System.out.println("Ricevuta richiesta getBigliettiByFiltro: colonna=" + colonna + ", valore=" + valore);

            List<it.trenical.server.Biglietto.Biglietto> risultati = db.getByFiltro(colonna, valore);
            GetBigliettiByFiltroResponse.Builder responseBuilder = GetBigliettiByFiltroResponse.newBuilder();

            for (it.trenical.server.Biglietto.Biglietto b : risultati) {
                it.trenical.grpc.Biglietto proto = convertiJavaInProto(b);
                responseBuilder.addBiglietti(proto);
            }

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            System.err.println("Errore in getBigliettiByFiltro: " + e.getMessage());
            e.printStackTrace();
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Errore durante il filtro dei biglietti")
                    .withCause(e)
                    .asRuntimeException());
        }
    }

    @Override
    public void creaBiglietto(CreaBigliettoRequest request, StreamObserver<CreaBigliettoResponse> responseObserver) {
        ArrayList<String> dati = new ArrayList<>(request.getDatiList());
        String esito = CreatoreBiglietto.creaBiglietto(dati);
        if (esito != null) {
            Biglietto biglietto = BigliettoDB.getInstance().getBiglietto(esito);
            int prezzoFinale = biglietto.getPrezzo();

            CreaBigliettoResponse response = CreaBigliettoResponse.newBuilder()
                    .setSuccess(true)
                    .setBigliettoID(esito)
                    .setPrezzoFinale(prezzoFinale)
                    .build();


            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
    @Override
    public void creazionePrezzoFinale(CreazionePrezzoFinaleRequest request, StreamObserver<CreazionePrezzoFinaleResponse> responseObserver) {
        String trenoID = request.getTrenoID();
        String classe = request.getClasse();
        int prezzoFInale = CreatoreBiglietto.calcoloPrezzoPrePagamento(trenoID, classe);

        CreazionePrezzoFinaleResponse response = CreazionePrezzoFinaleResponse.newBuilder()
                .setPrezzoFinale(prezzoFInale)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}