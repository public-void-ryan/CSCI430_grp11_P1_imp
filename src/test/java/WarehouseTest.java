import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Iterator;

class WarehouseTest {
    private static final String DATA_FILE = "WarehouseData";
    private Warehouse warehouse;

    @BeforeEach
    void setUp() {
        // Delete the data file if it exists
        File file = new File(DATA_FILE);
        if (file.exists()) {
            file.delete();
        }

        // Reset the Warehouse instance
        Warehouse.resetInstance();
        warehouse = Warehouse.instance();

        // Clear ClientList and ProductList
        ClientList.instance().clear();
        ProductList.instance().clear();
    }

    @Test
    void testAddClientAndGetClients() {
        Client client = warehouse.addClient("John Doe", "123 Main St", "555-1234");
        assertNotNull(client);

        Iterator<Client> clients = warehouse.getClients();
        assertTrue(clients.hasNext());
        assertEquals("John Doe", clients.next().getName());
    }

    @Test
    void testAddProductAndGetProducts() {
        Product product = warehouse.addProduct("Widget", 9.99, 100);
        assertNotNull(product);

        Iterator<Product> products = warehouse.getProducts();
        assertTrue(products.hasNext());
        assertEquals("Widget", products.next().getName());
    }

    @Test
    void testAddProductToClientWishlist() {
        Client client = warehouse.addClient("Jane Smith", "456 Elm St", "555-5678");
        Product product = warehouse.addProduct("Gadget", 19.99, 50);

        Wishlist.WishlistItem wishlistItem = warehouse.addProductToClientWishlist(client.getId(), product.getId(), 2);
        assertNotNull(wishlistItem);

        Iterator<Wishlist.WishlistItem> wishlistItems = warehouse.getClientWishlistItems(client.getId());
        assertTrue(wishlistItems.hasNext());
        assertEquals(product.getName(), wishlistItems.next().getProduct().getName());
    }

    @Test
    void testSaveAndRetrieve() {
        warehouse.addClient("Test Client", "Test Address", "555-9999");
        assertTrue(Warehouse.save());

        Warehouse retrievedWarehouse = Warehouse.retrieve();
        assertNotNull(retrievedWarehouse);

        Iterator<Client> clients = retrievedWarehouse.getClients();
        assertTrue(clients.hasNext());
        assertEquals("Test Client", clients.next().getName());
    }
}