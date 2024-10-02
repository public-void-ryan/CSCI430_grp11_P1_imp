
public class WaitlistTest {
    public static void main(String[] args) {
        Waitlist waitlist = new Waitlist();

        // Test adding a client
        Client client1 = new Client("Client1");
        waitlist.addClient(client1);
        if (waitlist.getClients().contains(client1)) { // Use getClients() method
            System.out.println("Test passed: Client1 is in the waitlist");
        } else {
            System.out.println("Test failed: Client1 should be in the waitlist");
        }

        // Test removing a client
        waitlist.removeClient(client1);
        if (!waitlist.getClients().contains(client1)) { // Use getClients() method
            System.out.println("Test passed: Client1 is not in the waitlist");
        } else {
            System.out.println("Test failed: Client1 should not be in the waitlist");
        }

        // Test adding multiple clients
        Client client2 = new Client("Client2");
        Client client3 = new Client("Client3");
        waitlist.addClient(client2);
        waitlist.addClient(client3);
        if (waitlist.getClients().contains(client2)) { // Use getClients() method
            System.out.println("Test passed: Client2 is in the waitlist");
        } else {
            System.out.println("Test failed: Client2 should be in the waitlist");
        }
        if (waitlist.getClients().contains(client3)) { // Use getClients() method
            System.out.println("Test passed: Client3 is in the waitlist");
        } else {
            System.out.println("Test failed: Client3 should be in the waitlist");
        }

        // Test removing one of the multiple clients
        waitlist.removeClient(client2);
        if (!waitlist.getClients().contains(client2)) { // Use getClients() method
            System.out.println("Test passed: Client2 is not in the waitlist");
        } else {
            System.out.println("Test failed: Client2 should not be in the waitlist");
        }
        if (waitlist.getClients().contains(client3)) { // Use getClients() method
            System.out.println("Test passed: Client3 is still in the waitlist");
        } else {
            System.out.println("Test failed: Client3 should still be in the waitlist");
        }

        System.out.println("All tests completed.");
    }
}

class Client {
    private String name;

    public Client(String name) {
        this.name = name;
    }

    // Override equals and hashCode to ensure proper comparison in the list
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Client client = (Client) obj;
        return name.equals(client.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
