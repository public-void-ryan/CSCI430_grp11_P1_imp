import java.util.HashMap;
import java.util.Map;

public class Wishlist {
    private Map<Client, Product> wishlist;

    public Wishlist() {
        wishlist = new HashMap<>();
    }

    public void addProduct(Client client, Product product) {
        wishlist.put(client, product);
    }

    public Map<Client, Product> getWishlist() {
        return wishlist;
    }
}