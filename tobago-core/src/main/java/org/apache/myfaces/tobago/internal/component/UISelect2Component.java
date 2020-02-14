package org.apache.myfaces.tobago.internal.component;

import javax.faces.component.StateHelper;
import javax.faces.convert.Converter;

public interface UISelect2Component {

  AbstractUISuggest getSuggest();

  boolean isAllowCustom();

  Converter getConverter();

  StateHelper getComponentStateHelper();


}
