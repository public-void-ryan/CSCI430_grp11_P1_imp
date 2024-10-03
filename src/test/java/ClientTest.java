import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClientTest {
    private Client client;

    @BeforeEach
    public void setUp() {
        client = new Client("John Doe", "123 Main St", "555-1234");
    }

    @Test
    public void testClientConstructor() {
        assertEquals("John Doe", client.getName());
        assertEquals("123 Main St", client.getAddress());
        assertEquals("555-1234", client.getPhone());
        assertNotNull(client.getId());
        assertNotNull(client.getWishlist());
    }

    @Test
    public void testSetName() {
        client.setName("Jane Doe");
        assertEquals("Jane Doe", client.getName());
    }

    @Test
    public void testSetAddress() {
        client.setAddress("456 Elm St");
        assertEquals("456 Elm St", client.getAddress());
    }

    @Test
    public void testSetPhone() {
        client.setPhone("555-5678");
        assertEquals("555-5678", client.getPhone());
    }
}