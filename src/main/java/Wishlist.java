import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Wishlist implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Product> wishlist;

    public Wishlist() {
        wishlist = new ArrayList<>();
    }

    public void addProduct(Product product) {
        wishlist.add(product);
    }

    public void removeProduct(Product product) {
        wishlist.remove(product);
    }

    public List<Product> getWishlist() {
        return new ArrayList<>(wishlist);
    }

    public String getProducts() {
        return wishlist.toString();
    }
}
