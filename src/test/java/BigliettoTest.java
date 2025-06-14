import it.trenical.server.Biglietto.*;
import it.trenical.server.Cliente.ClienteConcr;
import it.trenical.server.Cliente.ClienteImplDB;
import it.trenical.server.Tratta.GeneratorConcr;
import it.trenical.server.Tratta.TrattaPrototype;
import it.trenical.server.Tratta.TrattaStandard;
import it.trenical.server.Treno.TrenoConcr;
import it.trenical.server.Treno.TrenoImplDB;
import it.trenical.server.igGenerator.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BigliettoTest {

    String bigliettoID = "24C43";
    ClienteConcr titolareBiglietto = new ClienteConcr("QNTDVD03P24D086Q", "Davide", "Iaquinta", "GAY!!!",20);
    TrattaStandard tratta = new TrattaStandard("TRT-8c3eae47", "Terni", "Bressanone", "12-06-2025 10:00", "12-06-2025 12:00 ", 468, 2);
    TrenoConcr trenoBiglietto = new TrenoConcr(IdGenerator.generaTrenoID(),IdGenerator.generaTipoTreno(), tratta,100,0,0,0,0);
    String carrozza = "111";
    String posto = "G1";
    List<String> priorità = new ArrayList<>();
    int prezzo = 259;

    private BigliettoDB db;
    private BTerzaClasse bterzaClasse;

    @BeforeEach
    public void setup() {
        db = BigliettoDB.getInstance();
        ClienteImplDB dbCl = ClienteImplDB.getInstance();
        TrenoImplDB trenoCl = TrenoImplDB.getInstance();
        dbCl.setCliente(titolareBiglietto);
        trenoCl.setTreno(trenoBiglietto);
        priorità.clear();
        bterzaClasse = new BTerzaClasse.Builder()
                .bigliettoID(bigliettoID)
                .titolareBiglietto(titolareBiglietto)
                .trenoBiglietto(trenoBiglietto)
                .carrozza(carrozza)
                .posto(posto)
                .priorità(priorità)
                .prezzo(prezzo)
                .implementazione(db)
                .build();
        db.removeBiglietto(bigliettoID);
        db.removeAll();
    }

    @Test
    public void testSetAndGetBiglietto() {
        db.setBiglietto(bterzaClasse);
        Biglietto recuperato = db.getBiglietto(bigliettoID);
        assertNotNull(recuperato);
        assertEquals(bigliettoID, recuperato.getBigliettoID());
        assertEquals(titolareBiglietto, recuperato.getTitolareBiglietto());
        assertEquals(trenoBiglietto, recuperato.getTrenoBiglietto());
        assertEquals(carrozza, recuperato.getCarrozza());
        assertEquals(posto, recuperato.getPosto());
        assertEquals(prezzo, recuperato.getPrezzo());
        //System.out.println(prezzo);
        //System.out.println(recuperato.getPrezzo());
    }

    @Test
    public void testRemoveBiglietto() {
        db.setBiglietto(bterzaClasse);
        assertTrue(db.removeBiglietto(bigliettoID));
        assertNull(db.getBiglietto(bigliettoID));
    }

    @Test
    public void testPrioritaSerializzazione() {
        priorità.add("Finestrino");
        priorità.add("Silenzio");

        BTerzaClasse b = new BTerzaClasse.Builder()
                .bigliettoID(bigliettoID)
                .titolareBiglietto(titolareBiglietto)
                .trenoBiglietto(trenoBiglietto)
                .carrozza(carrozza)
                .posto(posto)
                .priorità(priorità)
                .prezzo(prezzo)
                .implementazione(db)
                .build();

        db.setBiglietto(b);
        Biglietto recuperato = db.getBiglietto(bigliettoID);
        assertNotNull(recuperato);
        assertEquals(priorità, recuperato.getPriorità());
    }

    @Test
    public void testClasseBigliettoDiversa() {
        BSecondaClasse bSeconda = new BSecondaClasse.Builder()
                .bigliettoID(bigliettoID)
                .titolareBiglietto(titolareBiglietto)
                .trenoBiglietto(trenoBiglietto)
                .carrozza(carrozza)
                .posto(posto)
                .priorità(priorità)
                .prezzo(prezzo)
                .implementazione(db)
                .build();

        db.setBiglietto(bSeconda);
        Biglietto recuperato = db.getBiglietto(bigliettoID);
        assertTrue(recuperato instanceof BSecondaClasse);
    }

    @Test
    public void testGetBigliettoInesistente() {
        assertNull(db.getBiglietto("ID_NON_PRESENTE"));
    }

    @Test
    public void testSovrascritturaBiglietto() {
        db.setBiglietto(bterzaClasse);

        BTerzaClasse modificato = new BTerzaClasse.Builder()
                .bigliettoID(bigliettoID)
                .titolareBiglietto(titolareBiglietto)
                .trenoBiglietto(trenoBiglietto)
                .carrozza("NUOVA")
                .posto("Z9")
                .priorità(priorità)
                .prezzo(prezzo + 100)
                .implementazione(db)
                .build();

        db.setBiglietto(modificato);

        Biglietto recuperato = db.getBiglietto(bigliettoID);
        assertEquals("NUOVA", recuperato.getCarrozza());
        assertEquals("Z9", recuperato.getPosto());
        assertEquals(prezzo + 100, recuperato.getPrezzo());
    }
}
