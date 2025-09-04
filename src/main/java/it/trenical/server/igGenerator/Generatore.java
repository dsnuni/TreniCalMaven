package it.trenical.server.igGenerator;

import it.trenical.server.Biglietto.*;
import it.trenical.server.Cliente.ClienteConcr;
import it.trenical.server.Cliente.ClienteImpl;
import it.trenical.server.Cliente.ClienteImplDB;
import it.trenical.server.Tratta.TrattaImplDB;
import it.trenical.server.Tratta.TrattaStandard;
import it.trenical.server.Treno.Treno;
import it.trenical.server.Treno.TrenoConcr;
import it.trenical.server.Treno.TrenoImplDB;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import static it.trenical.server.Cliente.ClienteImplDB.getClienteByRowIndex;
import static it.trenical.server.igGenerator.IdGenerator.dividiPosti;

public class Generatore {
    private static final String[] nomi = {
            "Luca", "Giulia", "Marco", "Chiara", "Francesco", "Alice",
            "Matteo", "Sara", "Davide", "Elena", "Andrea", "Martina",
            "Gabriele", "Anna", "Simone", "Laura", "Alessandro", "Federica",
            "Emanuele", "Ilaria", "Stefano", "Marta", "Nicola", "Valentina",
            "Paolo", "Francesca", "Leonardo", "Beatrice", "Giovanni", "Noemi"
    };
    private static final String[] cognomi = {
            "Rossi", "Russo", "Ferrari", "Esposito", "Bianchi", "Romano",
            "Colombo", "Ricci", "Marino", "Greco", "Bruno", "Gallo",
            "Conti", "De Luca", "Mancini", "Costa", "Giordano", "Rizzo",
            "Lombardi", "Moretti", "Barbieri", "Fontana", "Santoro", "Mariani",
            "Rinaldi", "Caruso", "Ferrara", "Gatti", "Martini", "Testa"
    };
    private static LocalTime adesso = LocalTime.now();
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss:SSS");

    private static void generaCliente() {
        String nome = nomi[interoCasuale(30)];
        String cognome = cognomi[interoCasuale(30)];
        int eta = interoCasuale(100);
        String cf = IdGenerator.generaCodiceFiscale(nome, cognome, eta);
        String cc = IdGenerator.generaCodiceCliente(booleanCasuale());
        ClienteImpl db = ClienteImplDB.getInstance();
        db.setCliente(new ClienteConcr(cf,nome,cognome,cc,eta));

    }
    private static void generaTratta() {
        TrattaStandard prototipoBase = new TrattaStandard();
        Generator generatore = new GeneratorConcr(prototipoBase);
        generatore.genera();
    }
    private static void generaTreno() {
        TrenoImplDB dbt= TrenoImplDB.getInstance();
        TrattaImplDB dbt2 = TrattaImplDB.getInstance();
        int numTratteTotali = dbt2.countTratte();
        if (numTratteTotali == 0) {
            System.out.println("Nessuna tratta disponibile.");
            return;
        }
        Random random = new Random();
        TrenoConcr tr = new TrenoConcr(
            IdGenerator.generaTrenoID(),
            IdGenerator.generaTipoTreno(),
            dbt2.getTrattaByIndex(interoCasuale(numTratteTotali-1)),
            interoCasuale(200),
            0,
            0,
            0,
            interoCasuale(150));
        System.out.println("Treno appena generato LOG <"+adesso.format(formatter)+">");
            dbt.setTreno(dividiPosti(tr));
    }
    private static void generaBiglietto(int numCLi, int numTre) {
        BigliettoImpl bdb = BigliettoDB.getInstance();
        TrenoImplDB dbt = TrenoImplDB.getInstance();
        ClienteImpl cdb = ClienteImplDB.getInstance();
        int classe= interoCasuale(3);
        Biglietto biglietto = null;

        Treno tr= dbt.getTrenoByRowIndex(interoCasuale(numTre-1));
        switch (classe) {
            case 1:
                biglietto = new BPrimaClasse.Builder()
                        .bigliettoID(IdGenerator.generaBigliettoID())
                        .titolareBiglietto(getClienteByRowIndex(interoCasuale(numCLi)))
                        .trenoBiglietto(tr)
                        .carrozza("A")
                        .posto(Math.abs(tr.getPostiPrima())+"A")
                        .prezzo(tr.getPrezzo()+((tr.getPrezzo()/100)*25))
                        .implementazione(bdb)
                        .build();
                tr.setPostiPrima(tr.getPostiPrima()-1);
                break;
            case 2:
                biglietto = new BSecondaClasse.Builder()
                        .bigliettoID(IdGenerator.generaBigliettoID())
                        .titolareBiglietto(getClienteByRowIndex(interoCasuale(numCLi)))
                        .trenoBiglietto(tr)
                        .carrozza("B")
                        .posto(Math.abs(tr.getPostiSeconda())+"B")
                        .prezzo(tr.getPrezzo()+((tr.getPrezzo()/100)*15))
                        .implementazione(bdb)
                        .build();
                tr.setPostiSeconda(tr.getPostiSeconda()-1);
                break;
            default:
                biglietto = new BTerzaClasse.Builder()
                        .bigliettoID(IdGenerator.generaBigliettoID())
                        .titolareBiglietto(getClienteByRowIndex(interoCasuale(numCLi)))
                        .trenoBiglietto(tr)
                        .carrozza("C")
                        .posto(Math.abs(tr.getPostiTerza())+"C")
                        .prezzo(tr.getPrezzo())
                        .implementazione(bdb)
                        .build();
                tr.setPostiTerza(tr.getPostiTerza()-1);
                break;

        }
        tr.setPostiTot(tr.getPostiTot()-1);
        //dbt.setTreno(tr);
        if(biglietto != null) {
            bdb.setBiglietto(biglietto);
        } else {
            throw new RuntimeException("Biglietto non valido.");
        }
    }
    public static void genera( int numCli, int numTrt, int numTre, int Bgl) {
        System.out.println("Inizio generazione dati "+" <"+formatter.toString()+" >");
        for(int i = 0; i < numCli; i++) {
            System.out.println("generazione cliente numero "+i+ " log "+" <"+adesso.format(formatter)+" >");
            generaCliente();

        }
        for(int i = 0; i < numTrt; i++) {
            System.out.println("generazione tratta numero "+i+ " log "+" <"+adesso.format(formatter)+" >");
            generaTratta();
        }
        for(int i = 0; i < numTre; i++) {
            System.out.println("generazione treno numero "+i+ " log "+" <"+adesso.format(formatter)+" >");
            generaTreno();
        }
        for(int i = 0; i < Bgl; i++) {
            System.out.println("generazione biglietto numero "+i+ " log "+" <"+adesso.format(formatter)+" >");
            generaBiglietto(numCli, numTre);
        }
    }
    private static int interoCasuale(int num) {
        return (int) (Math.random() * num); // genera tra 0 e 29
    }
    private static boolean booleanCasuale() {
        return Math.random() < 0.5; // 50% true, 50% false
    }

    public static void main(String[] args) {
        genera(50,30,100,70);
    }
}
