package org.apache.myfaces.tobago.example.demo;

import org.apache.deltaspike.core.api.scope.WindowScoped;
import org.apache.myfaces.tobago.model.PageState;

import javax.inject.Named;
import java.io.Serializable;

/**
 * Bean to store the browsers windows size for better multi-window support.
 */
@Named
@WindowScoped
public class MultiWindow implements Serializable {

  private PageState state;

  public PageState getState() {
    return state;
  }

  public void setState(PageState state) {
    this.state = state;
  }
}
