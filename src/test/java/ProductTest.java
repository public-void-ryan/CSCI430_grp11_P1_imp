import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductTest {
    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product("Test Product", 99.99, 10);
    }

    @Test
    public void testProductInitialization() {
        assertNotNull(product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals(99.99, product.getPrice(), 0.001);
        assertEquals(10, product.getStockLevel());
    }

    @Test
    public void testSetStockLevel() {
        product.setStockLevel(20);
        assertEquals(20, product.getStockLevel());
    }

    @Test
    public void testSetName() {
        product.setName("Updated Product");
        assertEquals("Updated Product", product.getName());
    }

    @Test
    public void testSetPrice() {
        product.setPrice(79.99);
        assertEquals(79.99, product.getPrice(), 0.001);
    }

    @Test
    public void testUniqueProductId() {
        Product anotherProduct = new Product("Another Product", 49.99, 5);
        assertNotEquals(product.getId(), anotherProduct.getId());
    }

    @Test
    public void testToString() {
        String productString = product.toString();
        assertTrue(productString.contains("Name=Test Product"));
        assertTrue(productString.contains("Price=99.99"));
        assertTrue(productString.contains("StockLevel=10"));
        assertTrue(productString.contains("ID=" + product.getId()));
    }
}
