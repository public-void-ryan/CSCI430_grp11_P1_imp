import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransactionTest {
    private Client client;
    private Product product;

    @BeforeEach
    public void setUp() {
        Transaction.resetIdCounter(); // Reset the idCounter before each test
        client = new Client("John Doe", "john.doe@example.com", "111-111-1111");
        product = new Product("Laptop", 999.99, 0);
    }

    @Test
    public void testTransactionCreation() {
        Transaction transaction = new Transaction(client, product, 2);
        assertNotNull(transaction);
        assertEquals(1, transaction.getId());
        assertEquals(client, transaction.getClient());
        assertEquals(product, transaction.getProduct());
        assertEquals(2, transaction.getQuantity());
    }

    @Test
    public void testTransactionIdIncrement() {
        Transaction transaction1 = new Transaction(client, product, 1);
        Transaction transaction2 = new Transaction(client, product, 3);
        assertEquals(1, transaction1.getId());
        assertEquals(2, transaction2.getId());
    }

    @Test
    public void testTransactionToString() {
        Transaction transaction = new Transaction(client, product, 2);
        String expected = "Transaction{id=1, client=" + client + ", product=" + product + ", quantity=2}";
        assertEquals(expected, transaction.toString());
    }
}
