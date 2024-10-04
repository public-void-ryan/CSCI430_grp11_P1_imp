import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Wishlist implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<WishlistItem> wishlist = new LinkedList<>();

    public Wishlist() {
    }

    public static class WishlistItem implements Serializable {
        private static final long serialVersionUID = 1L;
        private final Product product;
        private int quantity;

        public WishlistItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        // Getters
        public Product getProduct() {
            return product;
        }

        public int getQuantity() {
            return quantity;
        }

        // Setters
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        @Override
        public String toString() {
            return "WishlistItem [Product=" + product + ", Quantity=" + quantity + "]";
        }
    }

    public WishlistItem addProduct(Product product, int quantity) {
        for (WishlistItem item : wishlist) {
            if (item.getProduct().getId().equals(product.getId())) {
                // Update the quantity if product exists
                item.setQuantity(item.getQuantity() + quantity);
                return item;
            }
        }
        WishlistItem newItem = new WishlistItem(product, quantity);
        wishlist.add(newItem);
        return newItem;
    }

    public boolean removeProduct(Product product) {
        return wishlist.removeIf(item -> item.getProduct().getId().equals(product.getId()));
    }

    public Iterator<WishlistItem> getWishlistItems() {
        return wishlist.iterator();
    }
}
