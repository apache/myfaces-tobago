<%-- Copyright (c) 2002 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id$
  --%><%@ page import="com.atanion.tobago.renderkit.HtmlUtils,
                       com.atanion.tobago.TobagoConstants,
                       com.atanion.tobago.component.UIPage,
                       com.atanion.tobago.component.ComponentUtil,
                       javax.faces.component.UIComponent,
                       javax.faces.component.UIInput,
                       javax.faces.context.FacesContext,
                       com.atanion.tobago.TobagoConstants,
                       com.atanion.tobago.renderkit.RenderUtil,
                       com.atanion.tobago.renderkit.HtmlUtils,
                       com.atanion.util.KeyValuePair"
%><%

  UIInput component = (UIInput)RenderUtil.getComponent(request);
  UIPage uiPage = ComponentUtil.findPage(component);

  FacesContext facesContext = FacesContext.getCurrentInstance();

  String clientId = component.getClientId(facesContext);

  if (uiPage != null){
    uiPage.getPostfields().add(new KeyValuePair(clientId, clientId));
  }

  UIComponent label = component.getFacet(TobagoConstants.FACET_LABEL);
  if (label != null) {
    RenderUtil.encode(facesContext, label);
  }

  String currentValue = ComponentUtil.currentValue(component);

  String type = ComponentUtil.getBooleanAttribute(
      component, TobagoConstants.ATTR_PASSWORD) ? "password" : "text";

%><input <%= HtmlUtils.generateAttribute("type", type)
%> <%= HtmlUtils.generateAttribute("name", clientId)
%> <%= HtmlUtils.generateAttribute("id", clientId)
%> <%= HtmlUtils.generateAttribute("value", currentValue)
%> />