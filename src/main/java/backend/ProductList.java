package backend;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class ProductList implements Serializable {
    private static final long serialVersionUID = 1L;
    private static ProductList productList;

    private LinkedList<Product> products;

    private ProductList() {
        products = new LinkedList<>();
    }

    public static ProductList instance() {
        if (productList == null) {
            productList = new ProductList();
        }
        return productList;
    }

    public Product addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        products.add(product);
        return product;
    }

    public Product findProduct(String productId) {
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                return product;
            }
        }

        throw new NoSuchElementException("Product with ID " + productId + " not found.");
    }

    public Iterator<Product> getProducts() {
        return new LinkedList<>(products).iterator();
    }

    public void clear() {
        products.clear();
    }

    public String toString() {
        return products.toString();
    }

    private void writeObject(ObjectOutputStream output) throws IOException {
        output.defaultWriteObject();
    }

    private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
        input.defaultReadObject();
        if (products == null) {
            products = new LinkedList<>();
        }
    }
}
