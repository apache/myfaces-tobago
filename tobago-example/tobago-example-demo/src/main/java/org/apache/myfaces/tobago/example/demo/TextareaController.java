package org.apache.myfaces.tobago.example.demo;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@SessionScoped
@Named
public class TextareaController implements Serializable {

  private String longText;

  public TextareaController() {
    longText = "The goal of Apache Tobago™ is to provide the community with a well designed set of user interface components based on JSF and run"
            + " on MyFaces.\n\n"
            + "Tobago is more than just a tag library. The following statements characterize Tobago and make it different from other"
            + " frameworks:\n"
            + "- The focus of Tobago is to create business applications without the need for HTML design. The development of Tobago pages follows"
            + " more the development of conventional user interfaces than the creation of web pages.\n"
            + "- The UI components are abstracted from HTML and any layout information that does not belong to the general page structure. The"
            + " final output format is determined by the client/user-agent.\n"
            + "- A theming mechanism makes it easy to change the look and feel and to provide special implementations for certain browsers. A"
            + " fallback solution ensures that as much code is reused for new themes as possible.\n"
            + "- A layout manager is used to arrange the components automatically. This means, no manual laying out with HTML tables or other"
            + " constructs is needed.\n"
            + "\n"
            + "The development of Tobago started in 2002.";
  }

  public String getLongText() {
    return longText;
  }

  public void setLongText(String longText) {
    this.longText = longText;
  }
}
