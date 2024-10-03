import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WarehouseTest {
    private Warehouse warehouse;
    private Client client;
    private Product product;

    @BeforeEach
    public void setUp() {
        warehouse = Warehouse.instance();
        client = new Client("Test Client", "123 Test St", "555-1234");
        product = new Product("Test Product", 100.0, 10);
    }

    @Test
    public void testAddClient() {
        warehouse.addClient(client);
        assertNotNull(warehouse.getAllClients());
        assertTrue(warehouse.getAllClients().contains(client));
    }

    @Test
    public void testAddProduct() {
        warehouse.addProduct(product);
        assertNotNull(warehouse.getAllProducts());
        assertTrue(warehouse.getAllProducts().contains(product));
    }

    @Test
    public void testFindProduct() {
        warehouse.addProduct(product);
        Product foundProduct = warehouse.findProduct(product.id());
        assertNotNull(foundProduct);
        assertEquals(product, foundProduct);
    }

    @Test
    public void testProcessOrder() {
        warehouse.addClient(client);
        warehouse.addProduct(product);
        int initialStock = product.stockLevel();
        warehouse.processOrder(client, product.id(), 2);
        assertEquals(initialStock - 2, product.stockLevel());
        assertFalse(warehouse.getTransactions().isEmpty());
    }

    @Test
    public void testAddToWaitlist() {
        warehouse.addClient(client);
        warehouse.addProduct(product);
        warehouse.addToWaitlist(client, product);
        assertTrue(product.waitlist().getClients().contains(client));
    }

    @Test
    public void testAddToWishlist() {
        warehouse.addClient(client);
        warehouse.addProduct(product);
        warehouse.addToWishlist(client, product);
        assertTrue(client.getWishlist().getWishlist().contains(product));
    }
}