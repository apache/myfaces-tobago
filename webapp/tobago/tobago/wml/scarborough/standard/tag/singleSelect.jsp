<%-- Copyright (c) 2002 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id$
  --%><%@ page import="com.atanion.tobago.renderkit.HtmlUtils,
                       java.util.List,
                       com.atanion.tobago.component.UIPage,
                       javax.faces.context.FacesContext,
                       javax.faces.component.UIComponent,
                       com.atanion.tobago.TobagoConstants,
                       com.atanion.tobago.component.ComponentUtil,
                       javax.faces.component.UISelectOne,
                       javax.faces.model.SelectItem,
                       com.atanion.tobago.renderkit.RenderUtil,
                       com.atanion.tobago.renderkit.HtmlUtils,
                       com.atanion.util.KeyValuePair,
                       javax.faces.component.ValueHolder"
%><%@ taglib uri="http://www.atanion.com/tobago/component" prefix="tobago"
%><%

  UISelectOne component = (UISelectOne) RenderUtil.getComponent(request);
  UIPage uiPage = ComponentUtil.findPage(component);

  FacesContext facesContext = FacesContext.getCurrentInstance();

  String clientId = component.getClientId(facesContext);

  if (uiPage != null){
    uiPage.getPostfields().add(new KeyValuePair(clientId, clientId));
  }

  ValueHolder label
      = (ValueHolder) component.getFacet(TobagoConstants.FACET_LABEL);
  if (label != null) {
    %><%= label %><%
  }
  List items = ComponentUtil.getSelectItems(component);
  String value = ComponentUtil.currentValue(component);

%><select <%= HtmlUtils.generateAttribute("name", clientId)
%> <%= HtmlUtils.generateAttribute("id", clientId)
%> <%= HtmlUtils.generateAttribute("value", value)
%> multiple="false">
<%
  for (int i = 0; i < items.size(); i++) {
    SelectItem item = (SelectItem) items.get(i);
%>      <option
   <%= HtmlUtils.generateAttribute("value", item.getValue())
%>><%= item.getLabel() %></option>
<%
  }
%>
</select>