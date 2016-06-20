package org.apache.myfaces.tobago.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class ExecuteController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(ExecuteController.class);

  private String value1;
  private String value2;
  private String value3;
  private String value4;

  public String clear() {
    LOG.info("action");
    value1=null;
    value2=null;
    value3=null;
    value4=null;
    log();
    return "/content/40-test/50000-java/20-ajax-execute/ajax-execute.xhtml";
  }

  public String reload() {
    LOG.info("reload");
    log();
    return "/content/40-test/50000-java/20-ajax-execute/ajax-execute.xhtml";
  }

  public String action() {
    LOG.info("action");
    log();
    return null;
  }

  private void log() {
    LOG.info("value1='{}'", value1);
    LOG.info("value2='{}'", value2);
    LOG.info("value3='{}'", value3);
    LOG.info("value4='{}'", value4);
  }

  public String getValue1() {
    return value1;
  }

  public void setValue1(String value1) {
    this.value1 = value1;
  }

  public String getValue2() {
    return value2;
  }

  public void setValue2(String value2) {
    this.value2 = value2;
  }

  public String getValue3() {
    return value3;
  }

  public void setValue3(String value3) {
    this.value3 = value3;
  }

  public String getValue4() {
    return value4;
  }

  public void setValue4(String value4) {
    this.value4 = value4;
  }
}
