package org.apache.myfaces.tobago.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Named
public class CurrentValueController {

  private static final Logger LOG = LoggerFactory.getLogger(CurrentValueController.class);

  public Date date;

  public CurrentValueController() {
    try {
      this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1969-07-24 16:50:35");
    } catch (ParseException e) {
      LOG.error("", e);
    }
  }

  public String getString() {
    return "simple string";
  }

  public Date getDate() {
    return date;
  }

  public String toUpperCase(final String text) {
    return text != null ? text.toUpperCase() : null;
  }
}
