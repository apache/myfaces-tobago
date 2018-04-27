package org.apache.myfaces.tobago.example.demo;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class HeaderFooterController implements Serializable {

  private boolean renderHeader = true;
  private boolean fixHeader = true;
  private boolean renderFooter = true;
  private boolean fixFooter = true;

  public boolean isRenderHeader() {
    return renderHeader;
  }

  public void setRenderHeader(boolean renderHeader) {
    this.renderHeader = renderHeader;
  }

  public boolean isFixHeader() {
    return fixHeader;
  }

  public void setFixHeader(boolean fixHeader) {
    this.fixHeader = fixHeader;
  }

  public boolean isRenderFooter() {
    return renderFooter;
  }

  public void setRenderFooter(boolean renderFooter) {
    this.renderFooter = renderFooter;
  }

  public boolean isFixFooter() {
    return fixFooter;
  }

  public void setFixFooter(boolean fixFooter) {
    this.fixFooter = fixFooter;
  }
}
