<%-- Copyright (c) 2002 Atanion GmbH, Germany
  -- All rights reserved.
  -- $Id$
  --%><%@ page import="com.atanion.tobago.component.BodyContentHandler,
                       com.atanion.tobago.TobagoConstants,
                       javax.faces.component.UIPanel,
                       com.atanion.tobago.renderkit.RenderUtil"
%><%
  UIPanel component = (UIPanel) RenderUtil.getComponent(request) ;
  BodyContentHandler bodyContentHandler = (BodyContentHandler)
      component.getAttributes().get(TobagoConstants.ATTR_BODY_CONTENT);
%><%= bodyContentHandler.getBodyContent() %><%--</card> fixme ? --%>