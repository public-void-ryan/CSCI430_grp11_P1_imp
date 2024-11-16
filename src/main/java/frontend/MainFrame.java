package frontend;

import javax.swing.*;

public class MainFrame extends JFrame {
    private static MainFrame instance;

    private MainFrame() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        setTitle("Warehouse Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    public static MainFrame getInstance() {
        if (instance == null) {
            instance = new MainFrame();
        }
        return instance;
    }
}
