package org.apache.myfaces.tobago.internal.component;

import javax.faces.component.StateHelper;
import javax.faces.component.UIComponent;
import javax.faces.convert.Converter;
import java.util.HashSet;
import java.util.Map;

public interface UISelect2Component {

  AbstractUISuggest getSuggest();

  boolean isAllowCustom();

  Converter getConverter();

  StateHelper getComponentStateHelper();


}
