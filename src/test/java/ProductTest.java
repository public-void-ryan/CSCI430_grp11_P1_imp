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
    public void testProductConstructor() {
        assertNotNull(product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals(99.99, product.getPrice(), 0.001);
        assertEquals(10, product.getStockLevel());
    }

    @Test
    public void testSetName() {
        product.setName("Updated Product");
        assertEquals("Updated Product", product.getName());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            product.setName(null);
        });
        assertEquals("Name cannot be null or empty.", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            product.setName("");
        });
        assertEquals("Name cannot be null or empty.", exception.getMessage());
    }

    @Test
    public void testSetPrice() {
        product.setPrice(79.99);
        assertEquals(79.99, product.getPrice(), 0.001);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            product.setPrice(-10.0);
        });
        assertEquals("Price cannot be negative.", exception.getMessage());
    }

    @Test
    public void testSetStockLevel() {
        product.setStockLevel(20);
        assertEquals(20, product.getStockLevel());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            product.setStockLevel(-5);
        });
        assertEquals("Stock level cannot be negative.", exception.getMessage());
    }

    @Test
    public void testUniqueProductId() {
        Product anotherProduct = new Product("Another Product", 49.99, 5);
        assertNotEquals(product.getId(), anotherProduct.getId());
    }

}
