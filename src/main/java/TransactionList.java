import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class TransactionList implements Serializable {
    private static final long serialVersionUID = 1L;
    private final LinkedList<TransactionItem> transactions;

    // Constructor
    public TransactionList() {
        transactions = new LinkedList<>();
    }

    public static class TransactionItem implements Serializable {
        private static final long serialVersionUID = 1L;
        private static final String TRANSACTION_STRING = "T";
        private static int idCounter = 1;
        private final String id;
        private String content;

        public TransactionItem(String content) {
            this.id = TRANSACTION_STRING + idCounter++;
            setContent(content);
        }

        // Getters
        public String getId() {
            return id;
        }

        public String getContent() {
            return content;
        }

        // Setters
        public void setContent(String content) {
            if (content == null || content.isEmpty()) {
                throw new IllegalArgumentException("Content cannot be null or empty.");
            }
            this.content = content;
        }

        @Override
        public String toString() {
            return String.format("TransactionItem [ID=%s, Content=%s]", id, content);
        }
    }

    public String addTransaction(String content) {
        TransactionItem item = new TransactionItem(content);
        transactions.add(item);
        return item.getId();
    }

    public boolean removeTransaction(String transactionId) {
        return transactions.removeIf(item -> item.getId().equals(transactionId));
    }

    public String getTransaction(String transactionId) {
        for (TransactionItem item : transactions) {
            if (item.getId().equals(transactionId)) {
                return item.toString();
            }
        }

        throw new NoSuchElementException("Transaction with ID " + transactionId + " not found.");
    }

    public Iterator<TransactionItem> getTransactions() {
        return transactions.iterator();
    }

    public void clear() {
        transactions.clear();
    }
}
