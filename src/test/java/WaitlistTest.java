import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WaitlistTest {
    private Waitlist waitlist;
    private Client client1;
    private Client client2;

    @BeforeEach
    public void setUp() {
        waitlist = new Waitlist();
        client1 = new Client("Client One", "123 fake street", "111-111-1111");
        client2 = new Client("Client Two", "345 Main Street", "222-222-2222");
    }

    @Test
    public void testAddClient() {
        waitlist.addClient(client1, 5);
        waitlist.addClient(client2, 3);

        List<Client> clients = new ArrayList<>();
        Iterator<Waitlist.WaitlistItem> iterator = waitlist.getWaitlistItems();
        while (iterator.hasNext()) {
            clients.add(iterator.next().getClient());
        }

        assertTrue(clients.contains(client1));
        assertTrue(clients.contains(client2));
    }

    @Test
    public void testRemoveClient() {
        waitlist.addClient(client1, 5);
        waitlist.addClient(client2, 3);
        waitlist.removeClient(client1);

        List<Client> clients = new ArrayList<>();
        Iterator<Waitlist.WaitlistItem> iterator = waitlist.getWaitlistItems();
        while (iterator.hasNext()) {
            clients.add(iterator.next().getClient());
        }

        assertFalse(clients.contains(client1));
        assertTrue(clients.contains(client2));
    }

    @Test
    public void testClear() {
        waitlist.addClient(client1, 5);
        waitlist.addClient(client2, 3);
        waitlist.clear();

        assertFalse(waitlist.getWaitlistItems().hasNext());
    }
}