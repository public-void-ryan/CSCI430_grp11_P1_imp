import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class ProductList implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Product> _products;

    public ProductList() {
        _products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        _products.add(product);
    }

    public Product findProduct(String id) {
        for (Product product : _products) {
            if (product.id().equals(id)) {
                return product;
            }
        }
        return null;
    }

    public List<Product> getProducts() {
        return new ArrayList<>(_products);
    }
}
