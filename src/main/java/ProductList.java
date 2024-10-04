import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ProductList implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<Product> products = new LinkedList<>();
    private static ProductList productList;

    private ProductList() {
    }

    public static ProductList instance() {
        if (productList == null) {
            productList = new ProductList();
        }
        return productList;
    }

    public Product addProduct(Product product) {
        products.add(product);
        return product;
    }

    public Product findProduct(String productId) {
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    public Iterator<Product> getProducts() {
        return products.iterator();
    }

    private void writeObject(ObjectOutputStream output) {
        try {
            output.defaultWriteObject();
            output.writeObject(productList);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void readObject(ObjectInputStream input) {
        try {
            if (productList == null) {
                input.defaultReadObject();
                productList = (ProductList) input.readObject();
            }
        } catch (IOException | ClassNotFoundException ioe) {
            ioe.printStackTrace();
        }
    }

    public String toString() {
        return products.toString();
    }
}
