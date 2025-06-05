
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
    private final String codiceCliente = "CL123";
    private final String nome = "Mario";
    private final String cognome = "Rossi";

    @BeforeEach
    public void setup() {
        db = new ClienteImplDB();
        cliente = new ClienteConcr(codiceFiscale, nome, cognome, codiceCliente);
        db.removeCliente(codiceFiscale); // pulizia preventiva
    }

    @Test
    public void testSetAndGetCliente() {
        db.setCliente(cliente);
        Cliente recuperato = db.getCliente(codiceFiscale);

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
