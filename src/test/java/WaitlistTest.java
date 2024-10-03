import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class WaitlistTest {
    private Waitlist waitlist;
    private Client client1;
    private Client client2;

    @BeforeEach
    public void setUp() {
        waitlist = new Waitlist();
        client1 = new Client("Client1", null, null);
        client2 = new Client("Client2", null, null);
    }

    @Test
    public void testAddClient() {
        waitlist.addClient(client1);
        List<Client> clients = waitlist.getClients();
        assertEquals(1, clients.size());
        assertTrue(clients.contains(client1));
    }

    @Test
    public void testRemoveClient() {
        waitlist.addClient(client1);
        waitlist.removeClient(client1);
        List<Client> clients = waitlist.getClients();
        assertEquals(0, clients.size());
        assertFalse(clients.contains(client1));
    }

    @Test
    public void testGetClients() {
        waitlist.addClient(client1);
        waitlist.addClient(client2);
        List<Client> clients = waitlist.getClients();
        assertEquals(2, clients.size());
        assertTrue(clients.contains(client1));
        assertTrue(clients.contains(client2));
    }
}