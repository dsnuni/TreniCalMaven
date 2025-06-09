package it.trenical.server.igGenerator;

import it.trenical.server.Biglietto.BigliettoDB;
import it.trenical.server.Biglietto.BigliettoImpl;
import it.trenical.server.Cliente.ClienteImpl;
import it.trenical.server.Cliente.ClienteImplDB;
import it.trenical.server.Treno.TrenoImpl;
import it.trenical.server.Treno.TrenoImplDB;

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
        ClienteImpl cl= new ClienteImplDB();
        if(cl.getCliente(id) == null) {
            flag=false;
        }
        }
        return id;
    }
    public static String generaCodiceCliente(){
        boolean flag = true;
        String id = " ";
        while(flag) {
            id = "TRCL-" + UUID.randomUUID().toString().substring(0, 8);
            ClienteImplDB cl = new ClienteImplDB();
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
            BigliettoImpl bg= new BigliettoDB();
            if(bg.getBiglietto(id) == null) {
                flag=false;
            }
        }
        return id;
    }

    public static int generaTrenoID() {
        boolean flag = true;
        int id = 0;
        while(flag) {
            id= trenoCounter.getAndIncrement();
            TrenoImpl tr= new TrenoImplDB();
            if(tr.getTreno(id) == null) {
                flag=false;
            }
        }
        return id;
    }


    public static void resetCounters() {
        trenoCounter.set(1000);
        bigliettoCounter.set(1);
    }
    public static void main(String[] args) {
        String clienteID = generaClienteID();
        String codiceCliente = generaCodiceCliente();
        String bigliettoID = generaBigliettoID();
        int trenoID = generaTrenoID();

        ClienteImplDB cl = new ClienteImplDB();
        BigliettoImpl bg = new BigliettoDB();
        TrenoImpl tr = new TrenoImplDB();

        System.out.println("TEST GENERAZIONE ID:");
        System.out.println("Cliente ID: " + clienteID + " -> nel DB? " + (cl.getCliente(clienteID) != null));
        System.out.println("Codice Cliente: " + codiceCliente + " -> nel DB? " + (cl.getClienteByCodiceCLiente(codiceCliente) != null));
        System.out.println("Biglietto ID: " + bigliettoID + " -> nel DB? " + (bg.getBiglietto(bigliettoID) != null));
        System.out.println("Treno ID: " + trenoID + " -> nel DB? " + (tr.getTreno(trenoID) != null));
    }
}
