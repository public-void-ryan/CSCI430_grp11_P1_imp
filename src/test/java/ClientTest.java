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

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            client.setName(null);
        });
        assertEquals("Name cannot be null or empty.", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            client.setName("");
        });
        assertEquals("Name cannot be null or empty.", exception.getMessage());
    }

    @Test
    public void testSetAddress() {
        client.setAddress("456 Elm St");
        assertEquals("456 Elm St", client.getAddress());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            client.setAddress(null);
        });
        assertEquals("Address cannot be null or empty.", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            client.setAddress("");
        });
        assertEquals("Address cannot be null or empty.", exception.getMessage());
    }

    @Test
    public void testSetPhone() {
        client.setPhone("555-5678");
        assertEquals("555-5678", client.getPhone());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            client.setPhone(null);
        });
        assertEquals("Phone cannot be null or empty.", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            client.setPhone("");
        });
        assertEquals("Phone cannot be null or empty.", exception.getMessage());
    }

    @Test
    public void testToString() {
        String expected = String.format(
                "Client [ID=%s, Name=John Doe, Address=123 Main St, Phone=555-1234, Balance=%.2f]", client.getId(),
                0.0);
        assertEquals(expected, client.toString());
    }

    @Test
    public void testAddToWishlist() {
        Product product = new Product("Test Product", 10.0, 5);
        Wishlist.WishlistItem item = client.addToWishlist(product, 2);

        assertNotNull(item);
        assertEquals(product, item.getProduct());
        assertEquals(2, item.getQuantity());
    }
}
