import java.util.*;
import java.lang.*;
import java.io.*;
public class Book implements Serializable  {
 
  private String title;
  private String author;
  private String id;
  private Member borrowedBy;
  private Calendar dueDate;

  public Book(String title, String author, String id) {
    this.title = title;
    this.author = author;
    this.id = id;
  }
  public boolean issue(Member member) {
    borrowedBy = member;
    dueDate = new GregorianCalendar();
    dueDate.setTimeInMillis(System.currentTimeMillis());
    dueDate.add(Calendar.MONTH, 1);
    return true;
  }
  public Member returnBook() {
    if (borrowedBy == null) {
      return null;
    } else {
      Member borrower = borrowedBy;
      borrowedBy = null;
      return borrower;
    }
  }
  
  public String getAuthor() {
    return author;
  }
  public String getTitle() {
    return title;
  }
  public String getId() {
    return id;
  }
  public Member getBorrower() {
    return borrowedBy;
  }
  public String getDueDate() {
      return (dueDate.getTime().toString());
  }
  public String toString() {
    return "title " + title + " author " + author + " id " + id + " borrowed by " + borrowedBy;
  }
}