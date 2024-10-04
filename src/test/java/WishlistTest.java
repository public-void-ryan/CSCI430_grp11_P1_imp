import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class WishlistTest {
    private Wishlist wishlist;
    private Product product1;
    private Product product2;

    @BeforeEach
    public void setUp() {
        wishlist = new Wishlist();
        product1 = new Product("Product 1", 10.0, 5);
        product2 = new Product("Product 2", 15.0, 3);
    }

    @Test
    public void testAddProduct() {
        Wishlist.WishlistItem item = wishlist.addProduct(product1, 2);
        assertNotNull(item);
        assertEquals(product1, item.getProduct());
        assertEquals(2, item.getQuantity());

        // Verify that the wishlist contains the item
        Iterator<Wishlist.WishlistItem> iterator = wishlist.getWishlistItems();
        List<Wishlist.WishlistItem> items = new ArrayList<>();
        iterator.forEachRemaining(items::add);

        assertEquals(1, items.size());
        assertEquals(product1, items.get(0).getProduct());
        assertEquals(2, items.get(0).getQuantity());
    }

    @Test
    public void testAddProductExisting() {
        wishlist.addProduct(product1, 2);
        Wishlist.WishlistItem item = wishlist.addProduct(product1, 3);
        assertNotNull(item);
        assertEquals(product1, item.getProduct());
        assertEquals(5, item.getQuantity());

        // Verify that the wishlist contains the updated item
        Iterator<Wishlist.WishlistItem> iterator = wishlist.getWishlistItems();
        List<Wishlist.WishlistItem> items = new ArrayList<>();
        iterator.forEachRemaining(items::add);

        assertEquals(1, items.size());
        assertEquals(product1, items.get(0).getProduct());
        assertEquals(5, items.get(0).getQuantity());
    }

    @Test
    public void testRemoveProduct() {
        wishlist.addProduct(product1, 2);
        boolean removed = wishlist.removeProduct(product1);
        assertTrue(removed);

        // Verify that the wishlist is empty
        Iterator<Wishlist.WishlistItem> iterator = wishlist.getWishlistItems();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testRemoveProductNonExisting() {
        boolean removed = wishlist.removeProduct(product1);
        assertFalse(removed); // Should return false since product was not in the wishlist
    }

    @Test
    public void testGetWishlistItems() {
        wishlist.addProduct(product1, 2);
        wishlist.addProduct(product2, 3);

        Iterator<Wishlist.WishlistItem> iterator = wishlist.getWishlistItems();
        List<Wishlist.WishlistItem> items = new ArrayList<>();
        iterator.forEachRemaining(items::add);

        assertEquals(2, items.size());

        boolean foundProduct1 = false;
        boolean foundProduct2 = false;

        for (Wishlist.WishlistItem item : items) {
            if (item.getProduct().equals(product1)) {
                foundProduct1 = true;
                assertEquals(2, item.getQuantity());
            } else if (item.getProduct().equals(product2)) {
                foundProduct2 = true;
                assertEquals(3, item.getQuantity());
            }
        }

        assertTrue(foundProduct1);
        assertTrue(foundProduct2);
    }

    @Test
    public void testWishlistItemToString() {
        Wishlist.WishlistItem item = wishlist.addProduct(product1, 2);
        String expected = "WishlistItem [Product=" + product1 + ", Quantity=2]";
        assertEquals(expected, item.toString());
    }
}
