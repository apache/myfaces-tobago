package org.apache.myfaces.tobago.example.demo;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;
import java.io.Serializable;

@SessionScoped
@Named
public class SelectBoolean implements Serializable {

  private boolean a;
  private boolean b;
  private boolean c;

  private boolean d;
  private boolean e;
  private boolean f;

  public String submit() {
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
        "Selected items: "
            + (a ? "a" : "") + "" + (b ? "b" : "") + "" + (c ? "c" : "")
            + (d ? "d" : "") + "" + (e ? "e" : "") + "" + (f ? "f" : ""), null));
    return null;
  }

  public void action(AjaxBehaviorEvent event) {
    submit();
  }

  public boolean isA() {
    return a;
  }

  public void setA(boolean a) {
    this.a = a;
  }

  public boolean isB() {
    return b;
  }

  public void setB(boolean b) {
    this.b = b;
  }

  public boolean isC() {
    return c;
  }

  public void setC(boolean c) {
    this.c = c;
  }

  public boolean isD() {
    return d;
  }

  public void setD(boolean d) {
    this.d = d;
  }

  public boolean isE() {
    return e;
  }

  public void setE(boolean e) {
    this.e = e;
  }

  public boolean isF() {
    return f;
  }

  public void setF(boolean f) {
    this.f = f;
  }
}
