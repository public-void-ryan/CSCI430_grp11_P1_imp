import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class LibContext {

  private static int currentState;
  private static Library library;
  private static LibContext context;
  private int currentUser;
  private String userID;
  private static JFrame LibFrame;
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  public static final int IsClerk = 0;
  public static final int IsUser = 1;
  private static LibState[] states;
  private int[][] nextState;

  public String getToken(String prompt) {
    do {
      try {
        System.out.println(prompt);
        String line = reader.readLine();
        StringTokenizer tokenizer = new StringTokenizer(line, "\n\r\f");
        if (tokenizer.hasMoreTokens()) {
          return tokenizer.nextToken();
        }
      } catch (IOException ioe) {
        System.exit(0);
      }
    } while (true);
  }

  private boolean yesOrNo(String prompt) {
    String more = getToken(prompt + " (Y|y)[es] or anything else for no");
    if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
      return false;
    }
    return true;
  }

  private void retrieve() {
    try {
      Library tempLibrary = Library.retrieve();
      if (tempLibrary != null) {
        System.out.println(" The library has been successfully retrieved from the file LibraryData \n");
        library = tempLibrary;
      } else {
        System.out.println("File doesnt exist; creating new library");
        library = Library.instance();
      }
    } catch (Exception cnfe) {
      cnfe.printStackTrace();
    }
  }

  public void setLogin(int code) {
    currentUser = code;
  }

  public void setUser(String uID) {
    userID = uID;
  }

  public int getLogin() {
    return currentUser;
  }

  public String getUser() {
    return userID;
  }

  public JFrame getFrame() {
    return LibFrame;
  }

  private LibContext() { // constructor
    System.out.println("In Libcontext constructor");
    if (yesOrNo("Look for saved data and  use it?")) {
      retrieve();
    } else {
      library = Library.instance();
    }
    // set up the FSM and transition table;
    states = new LibState[3];
    states[0] = Clerkstate.instance();
    states[1] = Userstate.instance();
    states[2] = Loginstate.instance();
    nextState = new int[3][3];
    nextState[0][0] = 2;
    nextState[0][1] = 1;
    nextState[0][2] = -2;
    nextState[1][0] = 2;
    nextState[1][1] = 0;
    nextState[1][2] = -2;
    nextState[2][0] = 0;
    nextState[2][1] = 1;
    nextState[2][2] = -1;
    currentState = 2;
    JFrame.setDefaultLookAndFeelDecorated(true);
    LibFrame = new JFrame("Library GUI");
    LibFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    LibFrame.setSize(400, 400);
    LibFrame.setLocation(400, 400);
  }

  public void changeState(int transition) {
    // System.out.println("current state " + currentState + " \n \n ");
    currentState = nextState[currentState][transition];
    if (currentState == -2) {
      System.out.println("Error has occurred");
      terminate();
    }
    if (currentState == -1) { // System.out.println("current state " + currentState + " \n \n ");
      terminate();
    }
    states[currentState].run();
  }

  private void terminate() {
    if (yesOrNo("Save data?")) {
      if (library.save()) {
        System.out.println(" The library has been successfully saved in the file LibraryData \n");
      } else {
        System.out.println(" There has been an error in saving \n");
      }
    }
    System.out.println(" Goodbye \n ");
    System.exit(0);
  }

  public static LibContext instance() {
    if (context == null) {
      System.out.println("calling constructor");
      context = new LibContext();
    }
    return context;
  }

  public void process() {
    states[currentState].run();
  }

  public static void main(String[] args) {
    LibContext.instance().process();
    states[currentState].run();
  }

}
