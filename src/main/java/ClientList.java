import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ClientList implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<Client> clients = new LinkedList<>();
    private static ClientList clientList;

    private ClientList() {
    }

    public static ClientList instance() {
        if (clientList == null) {
            clientList = new ClientList();
        }
        return clientList;
    }

    public Client addClient(Client client) {
        clients.add(client);
        return client;
    }

    public Client findClient(String clientId) {
        for (Client client : clients) {
            if (client.getId().equals(clientId)) {
                return client;
            }
        }
        return null;
    }

    public Iterator<Client> getClients() {
        return clients.iterator();
    }

    private void writeObject(ObjectOutputStream output) {
        try {
            output.defaultWriteObject();
            output.writeObject(clientList);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void readObject(ObjectInputStream input) {
        try {
            if (clientList == null) {
                input.defaultReadObject();
                clientList = (ClientList) input.readObject();
            }
        } catch (IOException | ClassNotFoundException ioe) {
            ioe.printStackTrace();
        }
    }

    public String toString() {
        return clients.toString();
    }
}
