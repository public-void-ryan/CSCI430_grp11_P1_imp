import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class WishlistTest {
    private Wishlist wishlist;
    private Product product1;
    private Product product2;

    @BeforeEach
    public void setUp() {
        wishlist = new Wishlist();
        product1 = new Product("Product 1", 0, 0);
        product2 = new Product("Product 2", 0, 0);
    }

    @Test
    public void testAddProduct() {
        wishlist.addProduct(product1);
        List<Product> products = wishlist.getWishlist();
        assertEquals(1, products.size());
        assertTrue(products.contains(product1));
    }

    @Test
    public void testRemoveProduct() {
        wishlist.addProduct(product1);
        wishlist.removeProduct(product1);
        List<Product> products = wishlist.getWishlist();
        assertEquals(0, products.size());
        assertFalse(products.contains(product1));
    }

    @Test
    public void testGetWishlist() {
        wishlist.addProduct(product1);
        wishlist.addProduct(product2);
        List<Product> products = wishlist.getWishlist();
        assertEquals(2, products.size());
        assertTrue(products.contains(product1));
        assertTrue(products.contains(product2));
    }

    @Test
    public void testGetWishlistReturnsCopy() {
        wishlist.addProduct(product1);
        List<Product> products = wishlist.getWishlist();
        products.remove(product1);
        List<Product> productsAfterModification = wishlist.getWishlist();
        assertEquals(1, productsAfterModification.size());
        assertTrue(productsAfterModification.contains(product1));
    }
}