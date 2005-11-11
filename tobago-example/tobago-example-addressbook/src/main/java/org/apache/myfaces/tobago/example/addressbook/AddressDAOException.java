package org.apache.myfaces.tobago.example.addressbook;

public class AddressDAOException extends Exception {
  
  public AddressDAOException() {
  }

  public AddressDAOException(String message) {
    super(message);
  }

  public AddressDAOException(String message, Throwable cause) {
    super(message, cause);
  }

  public AddressDAOException(Throwable cause) {
    super(cause);
  }

}
