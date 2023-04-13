package org.apache.myfaces.tobago.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.lang.invoke.MethodHandles;

@RequestScoped
@Named
public class NonceController {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private boolean ajax = false;

  public void ajax() {
    LOG.info("AJAX action called.");
    ajax = true;
  }

  public boolean isAjax() {
    return ajax;
  }
}
