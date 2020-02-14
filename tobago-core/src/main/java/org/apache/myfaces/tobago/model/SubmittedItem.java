package org.apache.myfaces.tobago.model;

import org.apache.myfaces.tobago.context.Markup;

public class SubmittedItem extends SelectItem {

  public SubmittedItem(String value) {
    super(value, value);
    setMarkup(Markup.ERROR);
  }
}
