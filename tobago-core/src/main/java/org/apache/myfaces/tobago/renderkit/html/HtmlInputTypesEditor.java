package org.apache.myfaces.tobago.renderkit.html;

import org.apache.myfaces.tobago.apt.annotation.Preliminary;

import java.beans.PropertyEditorSupport;

/**
 * XXX Preliminary: check if this is the right way
 *
 * Converter for {@link org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes}
 *
 * @since 5.0.0
 */
@Preliminary
public class HtmlInputTypesEditor extends PropertyEditorSupport {

  @Override
  public void setAsText(final String text) throws IllegalArgumentException {
    setValue(HtmlInputTypes.valueOf(text));
  }

}
