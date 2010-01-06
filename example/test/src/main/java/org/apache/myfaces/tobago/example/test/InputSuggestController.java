package org.apache.myfaces.tobago.example.test;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.UIInput;

import java.util.ArrayList;
import java.util.List;


public class InputSuggestController {

  private static final Log LOG = LogFactory.getLog(InputSuggestController.class);

  public List<String> getInputSuggestItems(UIInput component) {
    String prefix = (String) component.getSubmittedValue();
    LOG.info("Creating items for prefix :\"" + prefix + "\"");
    List<String> li = new ArrayList<String>();
    li.add(prefix + 1);
    li.add(prefix + 2);
    li.add(prefix + 3);
    li.add(prefix + 4);
    li.add(prefix + 5);
    li.add(prefix + 6);
    return li;
  }
  
}
