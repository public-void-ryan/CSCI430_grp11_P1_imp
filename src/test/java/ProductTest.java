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
        assertNotNull(product.id());
        assertEquals("Test Product", product.name());
        assertEquals(99.99, product.price());
        assertEquals(10, product.stockLevel());
        assertNotNull(product.wishlist());
        assertNotNull(product.waitlist());
    }

    @Test
    public void testSetStockLevel() {
        product.setStockLevel(20);
        assertEquals(20, product.stockLevel());
    }

    @Test
    public void testUniqueProductId() {
        Product anotherProduct = new Product("Another Product", 49.99, 5);
        assertNotEquals(product.id(), anotherProduct.id());
    }
}