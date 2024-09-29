import java.util.ArrayList;
import java.util.List;

public class Wishlist {
    private List<Product> wishlist;

    public Wishlist() {
        wishlist = new ArrayList<>();
    }

    public void addProduct(Product product) {
        wishlist.add(product);
    }

    public List<Product> getWishlist() {
        return new ArrayList<>(wishlist);
    }
}