import it.trenical.server.Biglietto.*;
import it.trenical.server.Cliente.ClienteConcr;
import it.trenical.server.Cliente.ClienteImplDB;
import it.trenical.server.Tratta.TrattaStandard;
import it.trenical.server.Treno.TrenoConcr;
import it.trenical.server.Treno.TrenoFactory;
import it.trenical.server.Treno.TrenoImplDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BigliettoTest {

    String bigliettoID = "24C43";
    ClienteConcr titolareBiglietto = new ClienteConcr("QNTDVD03P24D086Q", "Davide", "Iaquinta", "GAY!!!");
    TrenoConcr trenoBiglietto = new TrenoConcr(
            0011,
            "787887",
            new TrattaStandard("11111", "Lesbo", "Veminchia", "09-06-2025 14:00", "09-06-2025 00:00", 9, 1000)
    );
    String carrozza = "111";
    String posto = "G1";
    List<String> priorità = new ArrayList<>();
    int prezzo = 259000;

    private BigliettoDB db;
    private BTerzaClasse bterzaClasse;

    @BeforeEach
    public void setup() {
        db = new BigliettoDB();
        ClienteImplDB dbCl = new ClienteImplDB();
        TrenoImplDB trenoCl = new TrenoImplDB();
        dbCl.setCliente(titolareBiglietto);
        trenoCl.setTreno(trenoBiglietto);
        priorità.clear();
        bterzaClasse = new BTerzaClasse(bigliettoID, titolareBiglietto, trenoBiglietto, carrozza, posto, priorità, prezzo);
        db.removeBiglietto(bigliettoID);
        db.removeAll();
        //TrenoFactory factory = new TrenoFactory(new TrenoImplDB());
    }

    @Test
    public void testSetAndGetBiglietto() {
        db.setBiglietto(bterzaClasse);
        Biglietto recuperato = db.getBiglietto(bigliettoID);
            System.out.println(recuperato.toString());
            System.out.println(bterzaClasse.toString());
          assertNotNull(recuperato);
          assertEquals(bigliettoID, recuperato.getBigliettoID());
          assertEquals(titolareBiglietto, recuperato.getTitolareBiglietto());
          assertEquals(trenoBiglietto, recuperato.getTrenoBiglietto());
          assertEquals(carrozza, recuperato.getCarrozza());
          assertEquals(posto, recuperato.getPosto());
          assertEquals(prezzo, recuperato.getPrezzo());
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

        BTerzaClasse b = new BTerzaClasse(bigliettoID, titolareBiglietto, trenoBiglietto, carrozza, posto, priorità, prezzo);
        db.setBiglietto(b);

        Biglietto recuperato = db.getBiglietto(bigliettoID);
        assertNotNull(recuperato);
        assertEquals(priorità, recuperato.getPriorità());
    }

    @Test
    public void testClasseBigliettoDiversa() {
        BSecondaClasse bSeconda = new BSecondaClasse(bigliettoID, titolareBiglietto, trenoBiglietto, carrozza, posto, priorità, prezzo);
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

        BTerzaClasse modificato = new BTerzaClasse(bigliettoID, titolareBiglietto, trenoBiglietto, "NUOVA", "Z9", priorità, prezzo + 100);
        db.setBiglietto(modificato);

        Biglietto recuperato = db.getBiglietto(bigliettoID);
        assertEquals("NUOVA", recuperato.getCarrozza());
        assertEquals("Z9", recuperato.getPosto());
        assertEquals(prezzo + 100, recuperato.getPrezzo());
    }
}
