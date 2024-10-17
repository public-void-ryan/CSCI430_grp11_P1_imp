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

        Warehouse.resetInstance();
        warehouse = Warehouse.instance();

        ClientList.instance().clear();
        ProductList.instance().clear();
    }

    @Test
    void testWarehouseConstructor() {
        assertNotNull(warehouse);
        assertFalse(warehouse.getClients().hasNext());
        assertFalse(warehouse.getProducts().hasNext());
    }

    @Test
    void testAddClientAndGetClients() {
        Client client = warehouse.addClient("John Doe", "123 Main St", "555-1234");
        assertNotNull(client);
        assertEquals("John Doe", client.getName());

        Iterator<Client> clients = warehouse.getClients();
        assertTrue(clients.hasNext());
        assertEquals(client, clients.next());
    }

    @Test
    void testAddProductAndGetProducts() {
        Product product = warehouse.addProduct("Widget", 9.99, 100);
        assertNotNull(product);
        assertEquals("Widget", product.getName());

        Iterator<Product> products = warehouse.getProducts();
        assertTrue(products.hasNext());
        assertEquals(product, products.next());
    }

    @Test
    void testAddProductToClientWishlist() {
        Client client = warehouse.addClient("Jane Smith", "456 Elm St", "555-5678");
        Product product = warehouse.addProduct("Gadget", 19.99, 50);

        Wishlist.WishlistItem wishlistItem = warehouse.addProductToClientWishlist(client.getId(), product.getId(), 2);
        assertNotNull(wishlistItem);
        assertEquals(2, wishlistItem.getQuantity());
        assertEquals(product, wishlistItem.getProduct());

        Iterator<Wishlist.WishlistItem> wishlistItems = warehouse.getClientWishlistItems(client.getId());
        assertTrue(wishlistItems.hasNext());
        assertEquals(wishlistItem, wishlistItems.next());
    }

    @Test
    void testGetClients() {
        warehouse.addClient("John Doe", "123 Main St", "555-1234");
        warehouse.addClient("Jane Smith", "456 Elm St", "555-5678");

        Iterator<Client> clients = warehouse.getClients();
        assertTrue(clients.hasNext());

        Client client1 = clients.next();
        assertEquals("John Doe", client1.getName());

        Client client2 = clients.next();
        assertEquals("Jane Smith", client2.getName());
    }

    @Test
    void testGetProducts() {
        warehouse.addProduct("Widget", 9.99, 100);
        warehouse.addProduct("Gadget", 19.99, 50);

        Iterator<Product> products = warehouse.getProducts();
        assertTrue(products.hasNext());

        Product product1 = products.next();
        assertEquals("Widget", product1.getName());

        Product product2 = products.next();
        assertEquals("Gadget", product2.getName());
    }

    @Test
    void testGetClientWishlistItems() {
        Client client = warehouse.addClient("Jane Smith", "456 Elm St", "555-5678");
        Product product = warehouse.addProduct("Gadget", 19.99, 50);
        warehouse.addProductToClientWishlist(client.getId(), product.getId(), 2);

        Iterator<Wishlist.WishlistItem> wishlistItems = warehouse.getClientWishlistItems(client.getId());
        assertTrue(wishlistItems.hasNext());

        Wishlist.WishlistItem item = wishlistItems.next();
        assertEquals(product, item.getProduct());
        assertEquals(2, item.getQuantity());
    }

    @Test
    void testSaveAndRetrieve() {
        Client client = warehouse.addClient("Test Client", "Test Address", "555-9999");
        Product product = warehouse.addProduct("Test Product", 49.99, 10);
        warehouse.addProductToClientWishlist(client.getId(), product.getId(), 1);

        assertTrue(Warehouse.save());

        Warehouse retrievedWarehouse = Warehouse.retrieve();
        assertNotNull(retrievedWarehouse);

        // Verify the client
        Iterator<Client> clients = retrievedWarehouse.getClients();
        assertTrue(clients.hasNext());
        assertEquals("Test Client", clients.next().getName());

        // Verify the product
        Iterator<Product> products = retrievedWarehouse.getProducts();
        assertTrue(products.hasNext());
        assertEquals("Test Product", products.next().getName());

        // Verify the wishlist
        Iterator<Wishlist.WishlistItem> wishlistItems = retrievedWarehouse.getClientWishlistItems(client.getId());
        assertTrue(wishlistItems.hasNext());
        Wishlist.WishlistItem item = wishlistItems.next();
        assertEquals(1, item.getQuantity());
        assertEquals(product.getId(), item.getProduct().getId());
    }
}
