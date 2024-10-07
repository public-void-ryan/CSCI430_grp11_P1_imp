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
        clientList = ClientList.instance();
        clientList.clear();
        client1 = new Client("Client One", "123 fake street", "111-111-1111");
        client2 = new Client("Client Two", "345 Main Street", "222-222-2222");
    }

    @Test
    public void testClientListConstructor() {
        assertNotNull(clientList);
        assertNotNull(clientList.getClients());
        assertFalse(clientList.getClients().hasNext());
    }

    @Test
    public void testAddClient() {
        clientList.addClient(client1);
        Iterator<Client> iterator = clientList.getClients();
        assertTrue(iterator.hasNext());
        assertEquals(client1, iterator.next());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            clientList.addClient(null);
        });
        assertEquals("Client cannot be null.", exception.getMessage());
    }

    @Test
    public void testFindClient() {
        clientList.addClient(client1);
        clientList.addClient(client2);
        assertEquals(client1, clientList.findClient(client1.getId()));
        assertEquals(client2, clientList.findClient(client2.getId()));
        assertNull(clientList.findClient("X1"));
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
    public void testClear() {
        clientList.addClient(client1);
        clientList.addClient(client2);
        clientList.clear();
        assertFalse(clientList.getClients().hasNext());
    }

    @Test
    public void testToString() {
        clientList.addClient(client1);
        String expected = "[" + client1.toString() + "]";
        assertEquals(expected, clientList.toString());

        clientList.addClient(client2);
        expected = "[" + client1.toString() + ", " + client2.toString() + "]";
        assertEquals(expected, clientList.toString());
    }
}
