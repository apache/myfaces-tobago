package org.apache.myfaces.tobago.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Model;

@Model
public class ResizeController {

  private static final Logger LOG = LoggerFactory.getLogger(ResizeController.class);

  public String resize() {
    LOG.info("Method resize() was called!");
    return null;
  }
}
