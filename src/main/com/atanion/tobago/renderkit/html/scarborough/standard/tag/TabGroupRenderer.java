/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIPage;
import com.atanion.tobago.component.UITabGroup;
import com.atanion.tobago.context.TobagoResource;
import com.atanion.tobago.renderkit.DirectRenderer;
import com.atanion.tobago.renderkit.HeightLayoutRenderer;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.renderkit.StyleAttribute;
import com.atanion.tobago.renderkit.html.HtmlDefaultLayoutManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.servlet.ServletRequest;
import java.io.IOException;

public class TabGroupRenderer extends RendererBase
    implements HeightLayoutRenderer, DirectRenderer {

// ///////////////////////////////////////////// constant

  private static Log LOG = LogFactory.getLog(TabGroupRenderer.class);

  public static final String ACTIVE_INDEX_POSTFIX
      = TobagoConstants.SUBCOMPONENT_SEP + "activeIndex";

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code
  public void decode(FacesContext facesContext, UIComponent component) {
    if (ComponentUtil.isOutputOnly(component)) {
      return;
    }

    String clientId = component.getClientId(facesContext);
    String newValue
        = ((ServletRequest) facesContext.getExternalContext().getRequest())
        .getParameter(clientId + ACTIVE_INDEX_POSTFIX);
    try {
      int activeIndex = Integer.parseInt(newValue);
      ((UITabGroup) component).setActiveIndex(activeIndex);
      ((UITabGroup) component).updateState(facesContext);

    } catch (NumberFormatException e) {
      LOG.error("Can't parse activeIndex: '" + newValue + "'");
    }
  }

  public int getHeaderHeight(FacesContext facesContext, UIComponent component) {
    return getConfiguredValue(facesContext, component, "headerHeight");
  }

  public void encodeDirectEnd(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {


    UITabGroup component = (UITabGroup) uiComponent;
    String image1x1 = TobagoResource.getImage(facesContext, "1x1.gif");

    UIPanel[] tabs = component.getTabs();
    layoutTabs(facesContext, component, tabs);

    int activeIndex;
    ValueBinding stateBinding
        = component.getValueBinding(TobagoConstants.ATTR_STATE_BINDING);

    Object state
        = stateBinding != null ? stateBinding.getValue(facesContext) : null;
    if (state == null) {
      activeIndex = component.getActiveIndex();
    } else if (state instanceof Integer) {
      activeIndex = ((Integer) state).intValue();
    } else {
      LOG.warn("Illegal class in stateBinding: " + state.getClass().getName());
      activeIndex = component.getActiveIndex();
    }
    String hiddenId = component.getClientId(facesContext)
        + TabGroupRenderer.ACTIVE_INDEX_POSTFIX;
    String bodyStyle = (String)
        component.getAttributes().get(TobagoConstants.ATTR_STYLE_BODY);


    UIPage page = ComponentUtil.findPage(component);
    page.getScriptFiles().add("tab.js", true);

    ResponseWriter writer = facesContext.getResponseWriter();

    writer.startElement("input", null);
    writer.writeAttribute("type", "hidden", null);
    writer.writeAttribute("value", Integer.toString(activeIndex), null);
    writer.writeAttribute("name", hiddenId, null);
    writer.writeAttribute("id", hiddenId, null);
    writer.endElement("input");

    // if a outer uiPage is presend, the virtual tab will go over all
    // tabs and render it as they are selected, and it will
    // selected with stylesheet.

    boolean serverSideTab = ComponentUtil.getBooleanAttribute(component,
        TobagoConstants.ATTR_SERVER_SIDE_TABS);

    for (int virtualTab = 0; virtualTab < tabs.length; virtualTab++) {

      if (!serverSideTab || virtualTab == activeIndex) {

        StyleAttribute oStyle = new StyleAttribute(
            (String) component.getAttributes().get(TobagoConstants.ATTR_STYLE));
        if (virtualTab != activeIndex) {
          oStyle.add("display", "none");
        }
        writer.startElement("div", null);
        writer.writeComment("empty div fix problem with mozilla and fieldset");
        writer.endElement("div");

        writer.startElement("table", null);
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute("cellpadding", "0", null);
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("summary", "", null);
        writer.writeAttribute("id",
            component.getClientId(facesContext) + '.' + virtualTab, null);
        writer.writeAttribute("style", oStyle.toString(), null);

        writer.startElement("tr", null);
        writer.writeAttribute("valign", "bottom", null);

        writer.startElement("td", null);

        writer.startElement("table", component);
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute("cellpadding", "0", null);
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("summary", "", null);
        writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE_HEADER);

        writer.startElement("tr", null);
        writer.writeAttribute("valign", "bottom", null);

        UIPanel activeTab = null;

        for (int i = 0; i < tabs.length; i++) {
          UIPanel tab = tabs[i];

          String url;

          if (serverSideTab) {
            url = "javascript:tobago_requestTab('"
                + component.getClientId(facesContext) + "'," + i + ",'"
                + ComponentUtil.findPage(component).getFormId(facesContext) + "')";
          } else {
            url = "javascript:tobago_selectTab('"
                + component.getClientId(facesContext) + "'," + i + ','
                + tabs.length + ')';
          }

          UIComponent label = tab.getFacet(TobagoConstants.FACET_LABEL);
          String labelString
              = (String) tab.getAttributes().get(TobagoConstants.ATTR_LABEL);

          String outerClass;
          String innerClass;
          if (virtualTab == i) {
            outerClass = "tobago-tab-selected-outer";
            innerClass = "tobago-tab-selected-inner";
            activeTab = tab;
          } else {
            outerClass = "tobago-tab-unselected-outer";
            innerClass = "tobago-tab-unselected-inner";
          }

          writer.startElement("td", null);

          writer.startElement("div", null);
          writer.writeAttribute("class", outerClass, null);

          writer.startElement("div", null);
          writer.writeAttribute("class", innerClass, null);

          writer.startElement("a", null);
          writer.writeAttribute("class", "tobago-tab-link", null);
          writer.writeAttribute("href", url, null);
          if (label != null || labelString != null) {
            writer.writeText("", null);
            if (label !=null) {
              RenderUtil.encode(facesContext, label);
            } else {
              writer.writeText(labelString, null);
            }
          } else {
            writer.writeText(Integer.toString(i+1), null);
          }
          writer.endElement("a");

          writer.endElement("div");
          writer.endElement("div");
          writer.endElement("td");

        }

        writer.startElement("td", null);
        writer.writeAttribute("width", "100%", null);

        writer.startElement("div", null);
        writer.writeAttribute("class", "tobago-tab-fulfill", null);

        writer.startElement("img", null);
        writer.writeAttribute("src", image1x1, null);
        writer.writeAttribute("alt", "", null);
        writer.endElement("img");

        writer.endElement("div");
        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("table");

        writer.endElement("td");
        writer.endElement("tr");

        writer.startElement("tr", null);

        writer.startElement("td", null);
        writer.writeAttribute("class", "tobago-tab-content", null);
        writer.writeAttribute("style", bodyStyle, null);

        writer.writeText("", null);
        RenderUtil.encodeChildren(facesContext, activeTab);


        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("table");
      }
    }
  }

  private void layoutTabs(FacesContext facesContext, UITabGroup component,
      UIPanel[] tabs) {
    String layoutWidth = (String)
        component.getAttributes().get(TobagoConstants.ATTR_LAYOUT_WIDTH);
    String layoutHeight = (String)
        component.getAttributes().get(TobagoConstants.ATTR_LAYOUT_HEIGHT);

    for (int i = 0; i < tabs.length; i++) {
      UIPanel tab = tabs[i];
      if (layoutWidth != null) {
        HtmlDefaultLayoutManager.layoutSpace(tab, facesContext, true);
      }
      if (layoutHeight != null) {
        HtmlDefaultLayoutManager.layoutSpace(tab, facesContext, false);
      }
    }


  }
// ///////////////////////////////////////////// bean getter + setter

}

