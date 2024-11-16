package backend;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class TransactionList implements Serializable {
    private static final long serialVersionUID = 1L;
    private final LinkedList<TransactionItem> transactions;

    public TransactionList() {
        transactions = new LinkedList<>();
    }

    public static class TransactionItem implements Serializable {
        private static final long serialVersionUID = 1L;
        private static final String TRANSACTION_STRING = "T";
        private static int idCounter = 1;

        private final String id;
        private final LocalDate date;
        private String description;
        private double dollarAmount;

        public TransactionItem(String description, double dollarAmount) {
            this.id = TRANSACTION_STRING + idCounter++;
            this.date = LocalDate.now();
            setDescription(description);
            setDollarAmount(dollarAmount);
        }

        // Getters
        public String getId() {
            return id;
        }

        public LocalDate getDate() {
            return date;
        }

        public String getDescription() {
            return description;
        }

        public double getDollarAmount() {
            return dollarAmount;
        }

        // Setters
        public void setDescription(String description) {
            if (description == null || description.isEmpty()) {
                throw new IllegalArgumentException("Description cannot be null or empty.");
            }
            this.description = description;
        }

        public void setDollarAmount(double dollarAmount) {
            if (dollarAmount < 0) {
                throw new IllegalArgumentException("Dollar amount cannot be negative.");
            }
            this.dollarAmount = dollarAmount;
        }

        @Override
        public String toString() {
            return String.format(
                    "TransactionItem [ID=%s, Date=%s, Description=%s, DollarAmount=%.2f]",
                    id, date, description, dollarAmount);
        }
    }

    public String addTransaction(String description, double dollarAmount) {
        TransactionItem item = new TransactionItem(description, dollarAmount);
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
        return new LinkedList<>(transactions).iterator();
    }

    public void clear() {
        transactions.clear();
    }
}
