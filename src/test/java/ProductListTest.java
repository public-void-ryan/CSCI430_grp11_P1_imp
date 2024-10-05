import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class ProductListTest {
    private ProductList productList;

    @BeforeEach
    public void setUp() {
        productList = new ProductList();
    }

    @Test
    public void testAddProduct() {
        Product product = new Product("Test Product", 10.0, 1);
        productList.addProduct(product);
        Iterator<Product> productIterator = productList.getProducts();
        assertTrue(productIterator.hasNext());
        assertEquals(product, productIterator.next());
    }

    @Test
    public void testAddMultipleProducts() {
        Product product1 = new Product("Test Product 1", 10.0, 1);
        Product product2 = new Product("Test Product 2", 20.0, 2);
        productList.addProduct(product1);
        productList.addProduct(product2);
        List<Product> products = new ArrayList<>();
        productList.getProducts().forEachRemaining(products::add);
        assertEquals(2, products.size());
        assertTrue(products.contains(product1));
        assertTrue(products.contains(product2));
    }
}