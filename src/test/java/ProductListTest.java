import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class ProductListTest {
    private ProductList productList;
    private Product product1;
    private Product product2;

    @BeforeEach
    public void setUp() {
        productList = ProductList.instance();
        productList.clear();
        product1 = new Product("Test Product 1", 10.0, 5);
        product2 = new Product("Test Product 2", 20.0, 10);
    }

    @Test
    public void testProductListConstructor() {
        assertNotNull(productList);
        assertNotNull(productList.getProducts());
        assertFalse(productList.getProducts().hasNext());
    }

    @Test
    public void testAddProduct() {
        productList.addProduct(product1);
        Iterator<Product> iterator = productList.getProducts();
        assertTrue(iterator.hasNext());
        assertEquals(product1, iterator.next());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productList.addProduct(null);
        });
        assertEquals("Product cannot be null.", exception.getMessage());
    }

    @Test
    public void testFindProduct() {
        productList.addProduct(product1);
        productList.addProduct(product2);
        assertEquals(product1, productList.findProduct(product1.getId()));
        assertEquals(product2, productList.findProduct(product2.getId()));
        assertNull(productList.findProduct("X1"));
    }

    @Test
    public void testGetProducts() {
        productList.addProduct(product1);
        productList.addProduct(product2);
        List<Product> products = new ArrayList<>();
        productList.getProducts().forEachRemaining(products::add);
        assertEquals(2, products.size());
        assertTrue(products.contains(product1));
        assertTrue(products.contains(product2));
    }

    @Test
    public void testClear() {
        productList.addProduct(product1);
        productList.addProduct(product2);
        productList.clear();
        assertFalse(productList.getProducts().hasNext());
    }

    @Test
    public void testToString() {
        productList.addProduct(product1);
        String expected = "[" + product1.toString() + "]";
        assertEquals(expected, productList.toString());

        productList.addProduct(product2);
        expected = "[" + product1.toString() + ", " + product2.toString() + "]";
        assertEquals(expected, productList.toString());
    }
}
