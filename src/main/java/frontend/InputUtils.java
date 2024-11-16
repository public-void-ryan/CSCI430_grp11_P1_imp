package frontend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputUtils {
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static boolean yesOrNo(String prompt) {
        String more = getToken(prompt + " (Y|N): ");
        return more.equalsIgnoreCase("Y");
    }

    public static String getToken(String prompt) {
        do {
            try {
                System.out.print(prompt);
                String line = reader.readLine();
                if (line != null && !line.isEmpty()) {
                    return line.trim();
                }
            } catch (IOException ioe) {
                System.exit(0);
            }
        } while (true);
    }

    public static int getNumber(String prompt) {
        do {
            try {
                String item = getToken(prompt);
                return Integer.parseInt(item);
            } catch (NumberFormatException nfe) {
                System.out.println("Please input a valid number.");
            }
        } while (true);
    }

    public static int getCommand(int minCommand, int maxCommand, String prompt) {
        do {
            int value = getNumber(prompt);
            if (value >= minCommand && value <= maxCommand) {
                return value;
            } else {
                System.out.println("Invalid command. Please enter a number between " + minCommand + " and " + maxCommand + ".");
            }
        } while (true);
    }
}
