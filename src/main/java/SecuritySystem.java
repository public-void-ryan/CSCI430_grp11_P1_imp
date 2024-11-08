import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SecuritySystem {
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public String[] promptCredentials() {
        try {
            System.out.print("Enter username: ");
            String username = reader.readLine();
            System.out.print("Enter password: ");
            String password = reader.readLine();
            return new String[] { username, password };
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean validateClientCredentials(String clientId, String[] credentials) {
        return clientId.equals(credentials[0]) && clientId.equals(credentials[1]);
    }

    public boolean validateClerkCredentials(String[] credentials) {
        return "clerk".equals(credentials[0]) && "clerk".equals(credentials[1]);
    }

    public boolean validateManagerCredentials(String[] credentials) {
        return "manager".equals(credentials[0]) && "manager".equals(credentials[1]);
    }
}