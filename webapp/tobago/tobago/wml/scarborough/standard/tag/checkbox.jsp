<%-- Copyright (c) 2002 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id$
  --%><%@ page import="com.atanion.tobago.renderkit.HtmlUtils,
                       com.atanion.tobago.component.UIPage,
                       javax.faces.context.FacesContext,
                       javax.faces.component.UIComponent,
                       com.atanion.tobago.TobagoConstants,
                       com.atanion.tobago.component.ComponentUtil,
                       javax.faces.component.UISelectBoolean,
                       com.atanion.tobago.TobagoConstants,
                       com.atanion.tobago.renderkit.RenderUtil,
                       com.atanion.tobago.renderkit.HtmlUtils,
                       com.atanion.util.KeyValuePair"
%><%@ taglib uri="http://www.atanion.com/tobago/component" prefix="tobago"
%><%
  UISelectBoolean component = (UISelectBoolean) RenderUtil.getComponent(request);
  UIPage uiPage = ComponentUtil.findPage(component);

  FacesContext facesContext = FacesContext.getCurrentInstance();

  String clientId = component.getClientId(facesContext);

  if (uiPage != null){
    uiPage.getPostfields().add(new KeyValuePair(clientId, clientId));
  }

  Boolean currentValue = (Boolean)component.getValue();
  boolean value = currentValue != null ? currentValue.booleanValue() : false;
  String state = value ? "on" : "off";

%><select <%= HtmlUtils.generateAttribute("name", clientId)
%> <%= HtmlUtils.generateAttribute("id", clientId)
%> multiple="true">
     <option
   <%= HtmlUtils.generateAttribute("value", state)

%>><%
  UIComponent label = component.getFacet(TobagoConstants.FACET_LABEL);
  if (label != null) {
    RenderUtil.encode(facesContext, label);
  }
%></option>
</select>