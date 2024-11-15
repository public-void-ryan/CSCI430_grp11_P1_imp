import java.util.*;
import java.text.*;
import java.io.*;
public class Clerkstate extends LibState {
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Library library;
  private LibContext context;
  private static Clerkstate instance;
  private static final int EXIT = 0;
  private static final int ADD_MEMBER = 1;
  private static final int ADD_BOOKS = 2;
  private static final int ISSUE_BOOKS = 3;
  private static final int RETURN_BOOKS = 4;
  private static final int REMOVE_BOOKS = 6;
  private static final int PLACE_HOLD = 7;
  private static final int REMOVE_HOLD = 8;
  private static final int PROCESS_HOLD = 9;
  private static final int GET_TRANSACTIONS = 10;
  private static final int USERMENU = 11;
  private static final int HELP = 13;
  private Clerkstate() {
      super();
      library = Library.instance();
      //context = LibContext.instance();
  }

  public static Clerkstate instance() {
    if (instance == null) {
      instance = new Clerkstate();
    }
    return instance;
  }

  public String getToken(String prompt) {
    do {
      try {
        System.out.println(prompt);
        String line = reader.readLine();
        StringTokenizer tokenizer = new StringTokenizer(line,"\n\r\f");
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
  public int getNumber(String prompt) {
    do {
      try {
        String item = getToken(prompt);
        Integer num = Integer.valueOf(item);
        return num.intValue();
      } catch (NumberFormatException nfe) {
        System.out.println("Please input a number ");
      }
    } while (true);
  }
  public Calendar getDate(String prompt) {
    do {
      try {
        Calendar date = new GregorianCalendar();
        String item = getToken(prompt);
        DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
        date.setTime(df.parse(item));
        return date;
      } catch (Exception fe) {
        System.out.println("Please input a date as mm/dd/yy");
      }
    } while (true);
  }
  public int getCommand() {
    do {
      try {
        int value = Integer.parseInt(getToken("Enter command:" + HELP + " for help"));
        if (value >= EXIT && value <= HELP) {
          return value;
        }
      } catch (NumberFormatException nfe) {
        System.out.println("Enter a number");
      }
    } while (true);
  }

  public void help() {
    System.out.println("Enter a number between 0 and 12 as explained below:");
    System.out.println(EXIT + " to Exit\n");
    System.out.println(ADD_MEMBER + " to add a member");
    System.out.println(ADD_BOOKS + " to  add books");
    System.out.println(RETURN_BOOKS + " to  return books ");
    System.out.println(REMOVE_BOOKS + " to  remove books");
    System.out.println(PROCESS_HOLD + " to  process holds");
    System.out.println(USERMENU + " to  switch to the user menu");
    System.out.println(HELP + " for help");
  }

  public void addMember() {
    String name = getToken("Enter member name");
    String address = getToken("Enter address");
    String phone = getToken("Enter phone");
    Member result;
    result = library.addMember(name, address, phone);
    if (result == null) {
      System.out.println("Could not add member");
    }
    System.out.println(result);
  }

  public void addBooks() {
    Book result;
    do {
      String title = getToken("Enter  title");
      String bookID = getToken("Enter id");
      String author = getToken("Enter author");
      result = library.addBook(title, author, bookID);
      if (result != null) {
        System.out.println(result);
      } else {
        System.out.println("Book could not be added");
      }
      if (!yesOrNo("Add more books?")) {
        break;
      }
    } while (true);
  }
 
  public void returnBooks() {
    int result;
    do {
      String bookID = getToken("Enter book id");
      result = library.returnBook(bookID);
      switch(result) {
        case Library.BOOK_NOT_FOUND:
          System.out.println("No such Book in Library");
          break;
        case Library.BOOK_NOT_ISSUED:
          System.out.println(" Book  was not checked out");
          break;
        case Library.BOOK_HAS_HOLD:
          System.out.println("Book has a hold");
          break;
        case Library.OPERATION_FAILED:
          System.out.println("Book could not be returned");
          break;
        case Library.OPERATION_COMPLETED:
          System.out.println(" Book has been returned");
          break;
        default:
          System.out.println("An error has occurred");
      }
      if (!yesOrNo("Return more books?")) {
        break;
      }
    } while (true);
  }
  public void removeBooks() {
    int result;
    do {
      String bookID = getToken("Enter book id");
      result = library.removeBook(bookID);
      switch(result){
        case Library.BOOK_NOT_FOUND:
          System.out.println("No such Book in Library");
          break;
        case Library.BOOK_ISSUED:
          System.out.println(" Book is currently checked out");
          break;
        case Library.BOOK_HAS_HOLD:
          System.out.println("Book has a hold");
          break;
        case Library.OPERATION_FAILED:
          System.out.println("Book could not be removed");
          break;
        case Library.OPERATION_COMPLETED:
          System.out.println(" Book has been removed");
          break;
        default:
          System.out.println("An error has occurred");
      }
      if (!yesOrNo("Remove more books?")) {
        break;
      }
    } while (true);
  }

  public void processHolds() {
    Member result;
    do {
      String bookID = getToken("Enter book id");
      result = library.processHold(bookID);
      if (result != null) {
        System.out.println(result);
      } else {
        System.out.println("No valid holds left");
      }
      if (!yesOrNo("Process more books?")) {
        break;
      }
    } while (true);
  }

  public boolean usermenu()
  {
    String userID = getToken("Please input the user id: ");
    if (Library.instance().searchMembership(userID) != null){
      (LibContext.instance()).setUser(userID);      
      return true;
    }
    else 
      System.out.println("Invalid user id."); return false;
  }

  public void terminate(int exitcode)
  {
    (LibContext.instance()).changeState(exitcode); // exit with a code 
  }
 

  public void process() {
    int command, exitcode = -1;
    help();
    boolean done = false;
    while (!done) {
      switch (getCommand()) {
        case ADD_MEMBER:        addMember();
                                break;
        case ADD_BOOKS:         addBooks();
                                break;
        case RETURN_BOOKS:      returnBooks();
                                break;
        case REMOVE_BOOKS:      removeBooks();
                                break;
        case PROCESS_HOLD:      processHolds();
                                break;
        case USERMENU:          if (usermenu())
                                  {exitcode = 1;
                                   done = true;}
                                break;
        case HELP:              help();
                                break;
        case EXIT:              exitcode = 0;
                                done = true; break;
      }
    }
    terminate(exitcode);
  }
  public void run() {
    process();
  }
}
