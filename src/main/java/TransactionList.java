import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

public class TransactionList implements Serializable {
    private static final long serialVersionUID = 1L;
    private final LinkedList<TransactionItem> transactions;
    private static int idCounter = 1; // ID counter for unique transaction IDs

    // Constructor
    public TransactionList() {
        transactions = new LinkedList<>();
    }

    private static String generateId() {
        return "T" + idCounter++;
    }

    public static class TransactionItem implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String id;
        private final String content;

        public TransactionItem(String id, String content) {
            if (content == null || content.isEmpty()) {
                throw new IllegalArgumentException("Content cannot be null or empty.");
            }
            this.id = id;
            this.content = content;
        }

        // Getters
        public String getId() {
            return id;
        }

        public String getContent() {
            return content;
        }

        @Override
        public String toString() {
            return String.format("TransactionItem [ID=%s, Content=%s]", id, content);
        }
    }

    public void addTransaction(String content) {
        String id = generateId();
        transactions.add(new TransactionItem(id, content));
    }

    public boolean removeTransaction(String id) {
        return transactions.removeIf(item -> item.getId().equals(id));
    }

    public Iterator<TransactionItem> getTransactions() {
        return transactions.iterator();
    }

    public void clear() {
        transactions.clear();
    }
}
