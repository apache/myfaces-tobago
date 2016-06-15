package org.apache.myfaces.tobago.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;

@Named
public class CurrentValueController {

  private static final Logger LOG = LoggerFactory.getLogger(CurrentValueController.class);

  public String string;
  public Date date;
  public Currency currency;

  public CurrentValueController() {

    string = "simple string";

    try {
      this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1969-07-24 16:50:35");
    } catch (ParseException e) {
      LOG.error("", e);
    }

    currency = Currency.getInstance("TTD");
  }

  public String toUpperCase(final String text) {
    return text != null ? text.toUpperCase() : null;
  }

  public Date plus50(final Date base) {
    if (date == null) {
      return null;
    }
    final GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(date);
    calendar.add(Calendar.YEAR, 50);
    return calendar.getTime();
  }

  public Currency toCurrency(String string) {
    return Currency.getInstance(string);
  }

  public String getString() {
    return string;
  }

  public Date getDate() {
    return date;
  }

  public Currency getCurrency() {
    return currency;
  }
}
