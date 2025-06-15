package it.trenical.server.igGenerator;

import it.trenical.server.Biglietto.BigliettoDB;
import it.trenical.server.Biglietto.BigliettoImpl;
import it.trenical.server.Cliente.ClienteImpl;
import it.trenical.server.Cliente.ClienteImplDB;
import it.trenical.server.Tratta.TrattaStandard;
import it.trenical.server.Treno.TrenoConcr;
import it.trenical.server.Treno.TrenoImpl;
import it.trenical.server.Treno.TrenoImplDB;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
    private static final AtomicInteger trenoCounter = new AtomicInteger(1000);
    private static final AtomicInteger bigliettoCounter = new AtomicInteger(1);

    public static String generaClienteID() {
        boolean flag = true;
        String id = " ";
        while(flag) {
        id = "CL-" + UUID.randomUUID().toString().substring(0, 8);
        ClienteImpl cl= ClienteImplDB.getInstance();
        if(cl.getCliente(id) == null) {
            flag=false;
        }
        }
        return id;
    }
    public static String generaCodiceCliente(boolean fidelizzato){
        boolean flag = true;
        String id = " ";
        System.out.println(fidelizzato);
        while(flag) {
            if(!fidelizzato) {
                id = "TRCL-" + UUID.randomUUID().toString().substring(0, 8);
            } else if(fidelizzato) {
                id = "FTRCL-" + UUID.randomUUID().toString().substring(0, 8);
            }
            ClienteImplDB cl = ClienteImplDB.getInstance();
            if (cl.getClienteByCodiceCLiente(id) == null) {
                flag = false;
            }
        }
        return id;
    }
    public static String generaBigliettoID() {
        boolean flag = true;
        String id = " ";
        while(flag) {
            id = "BGN" + UUID.randomUUID().toString().substring(0, 8);
            BigliettoImpl bg= BigliettoDB.getInstance();
            if(bg.getBiglietto(id) == null) {
                flag=false;
            }
        }
        return id;
    }

    public static String generaTrenoID() {
        boolean flag = true;
        String id = " ";
        while(flag) {
            id = "TRN-" + UUID.randomUUID().toString().substring(0, 8);
            ClienteImpl cl= ClienteImplDB.getInstance();
            if(cl.getCliente(id) == null) {
                flag=false;
            }
        }
        return id;
    }
    public static String generaTrattaID() {
        boolean flag = true;
        String id = " ";
        while(flag) {
            id = "TRT-" + UUID.randomUUID().toString().substring(0, 8);
            ClienteImpl cl= ClienteImplDB.getInstance();
            if(cl.getCliente(id) == null) {
                flag=false;
            }
        }
        return id;
    }
    public static String generaTipoTreno() {
        String[] treni = new String[]{"FrecciaArgento", "FrecciaRossa", "FrecciaBianca", "Regionale"};
        String treno= null;
        boolean flag = true;
        while(flag) {
            Random random = new Random();
            int t = random.nextInt(3) + 1;
            return treni[t];
        }
        return treno;
    }
public static TrenoConcr dividiPosti(TrenoConcr tr) {
        int posti = tr.getPostiTot();
        int postiPrima= (posti/100)*10;
        int postiSeconda = (posti/100)*40;
        int postiTerza = (posti/100)*50;
        return new TrenoConcr(tr.getTrenoID(),tr.getTipoTreno(), (TrattaStandard) tr.getTratta(),0,postiPrima,postiSeconda,postiTerza,posti);
}

public static String generaPromozioniID() {
    boolean flag = true;
    String id = " ";
    while(flag) {
        id = "PR-" + UUID.randomUUID().toString().substring(0, 8);
    }
    return id;
}
    public static void resetCounters() {
        trenoCounter.set(1000);
        bigliettoCounter.set(1);
    }
    public static void main(String[] args) {
        String clienteID = generaClienteID();
        String codiceCliente = generaCodiceCliente(false);
        String bigliettoID = generaBigliettoID();
        String trenoID = generaTrenoID();

        ClienteImplDB cl = ClienteImplDB.getInstance();
        BigliettoImpl bg = BigliettoDB.getInstance();
        TrenoImpl tr = TrenoImplDB.getInstance();

        System.out.println("TEST GENERAZIONE ID:");
        System.out.println("Cliente ID: " + clienteID + " -> nel DB? " + (cl.getCliente(clienteID) != null));
        System.out.println("Codice Cliente: " + codiceCliente + " -> nel DB? " + (cl.getClienteByCodiceCLiente(codiceCliente) != null));
        System.out.println("Biglietto ID: " + bigliettoID + " -> nel DB? " + (bg.getBiglietto(bigliettoID) != null));
        System.out.println("Treno ID: " + trenoID + " -> nel DB? " + (tr.getTreno(trenoID) != null));
    }
}
