/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIGridLayout;
import com.atanion.tobago.component.UIPage;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.util.LayoutUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UIPanel;
import javax.faces.component.UISelectItems;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import java.io.IOException;

public class Color16ChooserRenderer extends RendererBase {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(Color16ChooserRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void decode(FacesContext facesContext, UIComponent component) {
    if (ComponentUtil.isOutputOnly(component)) {
      return;
    }

    UIInput uiInput = (UIInput) component;

    String newValue = ((ServletRequest)facesContext.getExternalContext().getRequest())
        .getParameter(uiInput.getClientId(facesContext) + "_textbox");
    uiInput.setValue(newValue);
    if (newValue != null) {
      uiInput.setValid(true);
    }
  }

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    UIInput component = (UIInput) uiComponent;

    // fixme
    //  String selectorProperty = component.getV alueRef();
    LOG.error("not correctly implemented!");
    String selectorProperty = component.getValue().toString();
    String valueReference = selectorProperty + ".value";
    String color = ComponentUtil.currentValue(component);
    color = color != null ? color : "";
    String clientId = component.getClientId(facesContext);
    String setColor1 = "setColorFromSelector('"+ clientId +"');";
    String setColor2 = "setColorFromTextBox('"+ clientId +"');";
    String selectSelectorColors =
        "setSelectorColors('"+ clientId + "')";
    String previewStyle =
        "width: 58px; height: 20px; float: left; border: 1px solid"+
        "; background-color: #" + color +";";


    UIComponent label = component.getFacet(TobagoConstants.FACET_LABEL);

    String labelValue = null;
    String columns="70px;60px;*";
    if (label instanceof UIOutput) {
      labelValue = (String) ((UIOutput)label).getValue();
      // fixme: remove next line when new i18n/properties ok
      if (labelValue == null) { labelValue = "Emergency-Label";}
      if (LOG.isDebugEnabled()) {
        LOG.debug("labelValue = " + labelValue);
      }
      columns = LayoutUtil.getLabelWidth(facesContext,component) + "px;" + columns;
    }

//    request.setAttribute(valueReference, color);

//  fixme:  onMouseDown="< %= selectSelectorColors % >"

    String selectorId = component.getId() + "_selector";
    String previewId = clientId + "_preview";
    String textboxId = component.getId() + "_textbox";

    //todo: use as valuebinding
    valueReference = "#{" + valueReference + "}";
    selectorProperty = "#{" + selectorProperty + "}";
    if (LOG.isDebugEnabled()) {
      LOG.debug("selectorId " + selectorId);
      LOG.debug("previewId  " + previewId);
      LOG.debug("textboxId  " + textboxId);
    }

    UIPage uiPage = ComponentUtil.findPage(component);
    uiPage.getScriptFiles().add("color16chooser.js", true);
    uiPage.getOnloadScripts().add(setColor2);

    Application application = facesContext.getApplication();
    UIPanel panel = (UIPanel)
        application.createComponent(UIPanel.COMPONENT_TYPE);
    prepareToRender(panel, component, false, "Panel");
    UIGridLayout layout = (UIGridLayout)
        application.createComponent(UIGridLayout.COMPONENT_TYPE);
    layout.getAttributes().put(TobagoConstants.ATTR_COLUMNS, columns);
    prepareToRender(layout, null, false, "GridLayout");
    LOG.info("parent of layout = " + layout.getParent() );
    panel.getFacets().put("layout", layout);

    UIOutput uiLabel = (UIOutput)
        application.createComponent(UIOutput.COMPONENT_TYPE);
    uiLabel.setValue(labelValue);
    if (LOG.isDebugEnabled()) {
      LOG.debug("labelValue =" + uiLabel.getValue());
    }
    label.getAttributes().put(TobagoConstants.ATTR_FOR, textboxId);
    prepareToRender(uiLabel,  panel, true, TobagoConstants.RENDERER_TYPE_LABEL);

    UIInput textbox = (UIInput)
        application.createComponent(UIInput.COMPONENT_TYPE);
    textbox.setValue(valueReference);
    textbox.getAttributes().put(TobagoConstants.ATTR_ONCHANGE, setColor2);
    textbox.setId(textboxId);
    prepareToRender(textbox,  panel, true, "TextBox");

    UIOutput verbatim = (UIOutput)
        application.createComponent(UIOutput.COMPONENT_TYPE);
    StringBuffer preview = new StringBuffer();
    preview.append("<div style=\"" + previewStyle + "\"");
    preview.append("     id=\"" + previewId + "\" >");
    preview.append("</div>");
    verbatim.setValue(preview.toString());
    prepareToRender(verbatim, panel, true, "Verbatim");

    UISelectOne singleSelect = (UISelectOne)
        application.createComponent(UISelectOne.COMPONENT_TYPE);
    singleSelect.setValue(selectorProperty);
    singleSelect.setId(selectorId);
    singleSelect.getAttributes().put(TobagoConstants.ATTR_ONCHANGE, setColor1);
    singleSelect.getAttributes().put(TobagoConstants.ATTR_I18N, Boolean.TRUE);
    prepareToRender(singleSelect, panel, true, "SingleSelect");

    UISelectItems selectItems = (UISelectItems)
        application.createComponent(UISelectItems.COMPONENT_TYPE);
    selectItems.setValue(selectorProperty);
    prepareToRender(selectItems, singleSelect, true, "SelectItems");

    RenderUtil.encode(facesContext, panel);

  }

  private void prepareToRender(UIComponent component, UIComponent parent,
      boolean addToParent, String rendererType) {

    component.setRendererType(rendererType);
    component.setRendered(true);

    if (addToParent) {
      if (parent != null) {
        parent.getChildren().add(component);
      }
    }
    else {
      component.setParent(parent);
    }

  }


// ///////////////////////////////////////////// bean getter + setter

}

