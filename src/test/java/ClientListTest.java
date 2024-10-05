import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Iterator;

public class ClientListTest {
    private ClientList clientList;
    private Client client1;
    private Client client2;

    @BeforeEach
    public void setUp() {
        Client.resetIdCounter(); // Reset the idCounter before each test
        clientList = ClientList.instance();
        clientList.clear(); // Clear the ClientList before each test
        client1 = new Client("Client One", "123 fake street", null);
        client2 = new Client("Client Two", "345 Main Street", "555-5555");
    }

    @Test
    public void testAddClient() {
        clientList.addClient(client1);
        Iterator<Client> iterator = clientList.getClients();
        assertTrue(iterator.hasNext());
        assertEquals(client1, iterator.next());
    }

    @Test
    public void testGetClients() {
        clientList.addClient(client1);
        clientList.addClient(client2);
        Iterator<Client> iterator = clientList.getClients();
        assertTrue(iterator.hasNext());
        assertEquals(client1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(client2, iterator.next());
    }

    @Test
    public void testFindClient() {
        clientList.addClient(client1);
        clientList.addClient(client2);
        assertEquals(client1, clientList.findClient(client1.getId()));
        assertEquals(client2, clientList.findClient(client2.getId()));
        assertNull(clientList.findClient("C3"));
    }
}
