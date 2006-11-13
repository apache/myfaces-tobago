package org.apache.myfaces.tobago.example.reference;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

/**
 * User: lofwyr
 * Date: 07.11.2006 13:36:09
 */
public class OnOffConverter implements Converter {

  private static final Log LOG = LogFactory.getLog(OnOffConverter.class);

  public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
    LOG.info("getAsObject" + value);
    return Boolean.parseBoolean(value) ? "on" : "off";
  }

  public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
    LOG.info("getAsString" + value);
    return "on".equals(value) ? Boolean.TRUE.toString() : Boolean.FALSE.toString();
  }
}
