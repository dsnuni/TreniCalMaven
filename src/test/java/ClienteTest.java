
import it.trenical.server.Cliente.Cliente;
import it.trenical.server.Cliente.ClienteConcr;
import it.trenical.server.Cliente.ClienteFactory;
import it.trenical.server.Cliente.ClienteImplDB;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ClienteTest {

    private ClienteConcr cliente;
    private ClienteImplDB db;
    private final String codiceFiscale = "CF123TEST";
    private final String codiceCliente = "CL12123";
    private final String nome = "Mario";
    private final String cognome = "Rossi";
    private final int età =23;

    @BeforeEach
    public void setup() {
        db = ClienteImplDB.getInstance();
        cliente = new ClienteConcr(codiceFiscale, nome, cognome, codiceCliente,età);
        db.removeCliente(codiceFiscale); // pulizia preventiva
    }

    @Test
    public void testSetAndGetCliente() {
        db.setCliente(cliente);
        System.out.println("Setto"+cliente);
        Cliente recuperato = db.getCliente(codiceFiscale);
        System.out.println("Getto"+recuperato);
        assertNotNull(recuperato);
        assertEquals(codiceFiscale, recuperato.getCodiceFiscale());
        assertEquals(nome, recuperato.getNome());
        assertEquals(cognome, recuperato.getCognome());
        assertEquals(codiceCliente, recuperato.getCodiceCliente());
    }

    @Test
    public void testRemoveCliente() {
        db.setCliente(cliente);
        assertTrue(db.removeCliente(codiceFiscale));

        Cliente rimosso = db.getCliente(codiceFiscale);
        assertNull(rimosso);
    }

    @Test
    public void testClienteFactoryGetClienteByCodiceFiscale() {
        db.setCliente(cliente);
        Cliente trovato = ClienteFactory.getClienteByCodiceFiscale(codiceFiscale);

        assertNotNull(trovato);
        assertEquals(codiceFiscale, trovato.getCodiceFiscale());
    }
}
