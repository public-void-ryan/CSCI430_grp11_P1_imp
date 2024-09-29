public class TransactionTest {
    private static Client client;
    private static Product product;

    public static void main(String[] args) {
        setUp();
        testTransactionCreation();
        testTransactionIdIncrement();
        testGetId();
        testGetClient();
        testGetProduct();
        testGetQuantity();
        testNegativeQuantity();
        testZeroQuantity();
        testDifferentClients();
        testDifferentProducts();
    }

    public static void setUp() {
        client = new Client("John Doe");
        product = new Product("Laptop", null, 999.99, 0);
    }

    public static void testTransactionCreation() {
        Transaction transaction = new Transaction(client, product, 5);
        if (transaction != null && transaction.getClient().equals(client) && transaction.getProduct().equals(product)
                && transaction.getQuantity() == 5) {
            System.out.println("testTransactionCreation passed");
        } else {
            System.out.println("testTransactionCreation failed");
        }
    }

    public static void testTransactionIdIncrement() {
        Transaction transaction1 = new Transaction(client, product, 5);
        Transaction transaction2 = new Transaction(client, product, 10);
        if (transaction2.getId() > transaction1.getId()) {
            System.out.println("testTransactionIdIncrement passed");
        } else {
            System.out.println("testTransactionIdIncrement failed");
        }
    }

    public static void testGetId() {
        Transaction transaction = new Transaction(client, product, 5);
        if (transaction.getId() == 1) {
            System.out.println("testGetId passed");
        } else {
            System.out.println("testGetId failed");
        }
    }

    public static void testGetClient() {
        Transaction transaction = new Transaction(client, product, 5);
        if (transaction.getClient().equals(client)) {
            System.out.println("testGetClient passed");
        } else {
            System.out.println("testGetClient failed");
        }
    }

    public static void testGetProduct() {
        Transaction transaction = new Transaction(client, product, 5);
        if (transaction.getProduct().equals(product)) {
            System.out.println("testGetProduct passed");
        } else {
            System.out.println("testGetProduct failed");
        }
    }

    public static void testGetQuantity() {
        Transaction transaction = new Transaction(client, product, 5);
        if (transaction.getQuantity() == 5) {
            System.out.println("testGetQuantity passed");
        } else {
            System.out.println("testGetQuantity failed");
        }
    }

    public static void testNegativeQuantity() {
        try {
            new Transaction(client, product, -5);
            System.out.println("testNegativeQuantity failed");
        } catch (IllegalArgumentException e) {
            System.out.println("testNegativeQuantity passed");
        }
    }

    public static void testZeroQuantity() {
        Transaction transaction = new Transaction(client, product, 0);
        if (transaction.getQuantity() == 0) {
            System.out.println("testZeroQuantity passed");
        } else {
            System.out.println("testZeroQuantity failed");
        }
    }

    public static void testDifferentClients() {
        Client client2 = new Client("Jane Doe");
        Transaction transaction1 = new Transaction(client, product, 5);
        Transaction transaction2 = new Transaction(client2, product, 5);
        if (!transaction1.getClient().equals(transaction2.getClient())) {
            System.out.println("testDifferentClients passed");
        } else {
            System.out.println("testDifferentClients failed");
        }
    }

    public static void testDifferentProducts() {
        Product product2 = new Product("Smartphone", null, 499.99, 0);
        Transaction transaction1 = new Transaction(client, product, 5);
        Transaction transaction2 = new Transaction(client, product2, 5);
        if (!transaction1.getProduct().equals(transaction2.getProduct())) {
            System.out.println("testDifferentProducts passed");
        } else {
            System.out.println("testDifferentProducts failed");
        }
    }
}