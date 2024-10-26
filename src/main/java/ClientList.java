import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class ClientList implements Serializable {
    private static final long serialVersionUID = 1L;
    private static ClientList clientList;

    private List<Client> clients;

    private ClientList() {
        clients = new LinkedList<>();
    }

    public static ClientList instance() {
        if (clientList == null) {
            clientList = new ClientList();
        }
        return clientList;
    }

    public Client addClient(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null.");
        }
        clients.add(client);
        return client;
    }

    public Client findClient(String clientId) {
        for (Client client : clients) {
            if (client.getId().equals(clientId)) {
                return client;
            }
        }

        throw new NoSuchElementException("Client with ID " + clientId + " not found.");
    }

    public Iterator<Client> getClients() {
        return clients.iterator();
    }

    public void clear() {
        clients.clear();
    }

    public String toString() {
        return clients.toString();
    }

    private void writeObject(ObjectOutputStream output) throws IOException {
        output.defaultWriteObject();
    }

    private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
        input.defaultReadObject();
        if (clients == null) {
            clients = new LinkedList<>();
        }
    }
}
