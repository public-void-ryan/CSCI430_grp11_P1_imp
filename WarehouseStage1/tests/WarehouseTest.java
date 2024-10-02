package tests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class WarehouseTest {
    private Warehouse warehouse;

    @BeforeEach
    public void setUp() {
        warehouse = Warehouse.instance();
    }

    @Test
    public void testAddClient() {
        Client client = new Client("Client1");
        warehouse.addClient(client);
        Iterator<Client> clients = warehouse.getAllClients();
        boolean found = false;
        while (clients.hasNext()) {
            if (clients.next().equals(client)) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Client should be added to the warehouse");
    }

    @Test
    public void testAddProduct() {
        Product product = new Product("Product1", 100);
        warehouse.addProduct(product);
        List<Product> products = warehouse.getAllProducts();
        assertTrue(products.contains(product), "Product should be added to the warehouse");
    }

    @Test
    public void testProcessOrder() {
        Client client = new Client("Client2");
        Product product = new Product("Product2", 50);
        warehouse.addClient(client);
        warehouse.addProduct(product);

        warehouse.processOrder(client, product.getId(), 10);
        assertEquals(40, product.stockLevel(), "Product stock level should be reduced by the order quantity");
    }

    @Test
    public void testProcessOrderInsufficientStock() {
        Client client = new Client("Client3");
        Product product = new Product("Product3", 5);
        warehouse.addClient(client);
        warehouse.addProduct(product);

        warehouse.processOrder(client, product.getId(), 10);
        assertEquals(5, product.stockLevel(), "Product stock level should remain the same if order quantity exceeds stock");
        assertTrue(product.waitlist().contains(client), "Client should be added to the waitlist if stock is insufficient");
    }

    @Test
    public void testFindProduct() {
        Product product = new Product("Product4", 20);
        warehouse.addProduct(product);
        Product foundProduct = warehouse.findProduct(product.getId());
        assertEquals(product, foundProduct, "Should find the product by ID");
    }

    @Test
    public void testSaveAndRetrieve() {
        Warehouse.save();
        Warehouse retrievedWarehouse = Warehouse.retrieve();
        assertNotNull(retrievedWarehouse, "Retrieved warehouse should not be null");
        assertEquals(warehouse, retrievedWarehouse, "Retrieved warehouse should be equal to the saved warehouse");
    }
}