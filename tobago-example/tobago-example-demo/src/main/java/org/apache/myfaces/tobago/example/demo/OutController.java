package org.apache.myfaces.tobago.example.demo;

import javax.inject.Named;
import java.io.Serializable;

@Named
public class OutController implements Serializable {

  public String getHtml() {
    return "Text in <span style='color:#FF0000'>red</span>, "
        + "<span style='color:#00FF00'>green</span> and <span style='color:#0000FF'>blue</span>.";
  }

}
