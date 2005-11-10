/*
 * Copyright 2002-2005 The Apache Software Foundation.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */
package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.*;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.component.UITabGroup;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.event.StateChangeEvent;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.StyleAttribute;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.util.AccessKeyMap;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.apache.myfaces.tobago.ajax.api.AjaxRenderer;
import org.apache.myfaces.tobago.ajax.api.AjaxUtils;
import org.apache.myfaces.tobago.TobagoConstants;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import java.io.IOException;
import java.util.Map;

public class TabGroupRenderer extends RendererBase implements AjaxRenderer {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(TabGroupRenderer.class);

  public static final String ACTIVE_INDEX_POSTFIX
      = SUBCOMPONENT_SEP + "activeIndex";

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code
  public void decode(FacesContext facesContext, UIComponent component) {
    if (ComponentUtil.isOutputOnly(component)) {
      return;
    }

    int oldIndex = ((UITabGroup) component).getActiveIndex();

    String clientId = component.getClientId(facesContext);
    final Map parameters
        = facesContext.getExternalContext().getRequestParameterMap();
    String newValue = (String)parameters.get(clientId + ACTIVE_INDEX_POSTFIX);
    try {
      int activeIndex = Integer.parseInt(newValue);
      if (activeIndex != oldIndex) {
        ((UITabGroup) component).setActiveIndex(activeIndex);
        StateChangeEvent event = new StateChangeEvent(component,
            new Integer(oldIndex), new Integer(activeIndex));
        event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
        component.queueEvent(event);
      }

    } catch (NumberFormatException e) {
      LOG.error("Can't parse activeIndex: '" + newValue + "'");
    }
  }

  public void encodeEndTobago(FacesContext facesContext,
                              UIComponent uiComponent) throws IOException {


    UITabGroup component = (UITabGroup) uiComponent;

    HtmlRendererUtil.createHeaderAndBodyStyles(facesContext, component);

    String image1x1
        = ResourceManagerUtil.getImageWithPath(facesContext, "image/1x1.gif");

     UIPanel[] tabs = component.getTabs();
    layoutTabs(facesContext, component, tabs);

    final int activeIndex = component.getActiveIndex();

    final String clientId = component.getClientId(facesContext);
    final String hiddenId = clientId + TabGroupRenderer.ACTIVE_INDEX_POSTFIX;
    final boolean serverSideTab
            = ComponentUtil.getBooleanAttribute(component, ATTR_SERVER_SIDE_TABS);

    UIPage page = ComponentUtil.findPage(component);
    page.getScriptFiles().add("script/tab.js");
    if (AJAX_ENABLED && ! serverSideTab) {
      HtmlRendererUtil.writeScriptLoader(facesContext, "script/tab.js");

    }

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();

    writer.startElement("input", null);
    writer.writeAttribute("type", "hidden", null);
    writer.writeAttribute("value", Integer.toString(activeIndex), null);
    writer.writeNameAttribute(hiddenId);
    writer.writeIdAttribute(hiddenId);
    writer.endElement("input");

    // if a outer uiPage is presend, the virtual tab will go over all
    // tabs and render it as they are selected, and it will
    // selected with stylesheet.

    for (int virtualTab = 0; virtualTab < tabs.length; virtualTab++) {

      if (!serverSideTab || virtualTab == activeIndex) {

        StyleAttribute oStyle = new StyleAttribute(
            (String) component.getAttributes().get(ATTR_STYLE));
        if (virtualTab != activeIndex) {
          oStyle.add("display", "none");
        }
        writer.startElement("div", null);
        writer.writeComment("empty div fix problem with mozilla and fieldset");
        writer.endElement("div");

        writer.startElement("div", null);
        renderTabGroupView(facesContext, writer, component, virtualTab, oStyle,
            serverSideTab, image1x1);
        writer.endElement("div");

        if (AJAX_ENABLED && serverSideTab) {
          final String formId
              = ComponentUtil.findPage(component).getFormId(facesContext);
          final String[] scripts = new String[]{"script/tabgroup.js"};
          final String[] cmds = {
              "new Tobago.TabGroup(",
              "    '" + clientId + "', ",
              "    '" + activeIndex + "', ",
              "    '" + formId + "', ",
              "    '" + AjaxUtils.createUrl(facesContext, clientId) + "');"
          };
          HtmlRendererUtil.writeScriptLoader(facesContext, scripts, cmds);
        }
      }
    }
  }

  private void renderTabGroupView(
      FacesContext facesContext, TobagoResponseWriter writer, UITabGroup component,
      int virtualTab, StyleAttribute oStyle, boolean serverSideTab, String image1x1)
      throws IOException {
    UIPanel[] tabs = component.getTabs();
    writer.startElement("table", null);
    writer.writeAttribute("border", "0", null);
    writer.writeAttribute("cellpadding", "0", null);
    writer.writeAttribute("cellspacing", "0", null);
    writer.writeAttribute("summary", "", null);
    final String clientId = component.getClientId(facesContext);
    writer.writeIdAttribute(clientId + '.' + virtualTab);
    writer.writeAttribute("style", oStyle.toString(), null);

    writer.startElement("tr", null);
    writer.writeAttribute("valign", "bottom", null);

    writer.startElement("td", null);

    writer.startElement("table", component);
    writer.writeAttribute("border", "0", null);
    writer.writeAttribute("cellpadding", "0", null);
    writer.writeAttribute("cellspacing", "0", null);
    writer.writeAttribute("summary", "", null);
    writer.writeAttribute("style", null, ATTR_STYLE_HEADER);

    writer.startElement("tr", null);
    writer.writeAttribute("valign", "bottom", null);

    UIPanel activeTab = null;

    for (int i = 0; i < tabs.length; i++) {
      UIPanel tab = tabs[i];

      String url;

      if (AJAX_ENABLED && serverSideTab) {
        url = "#";
      }  else if (serverSideTab) {
        url = "javascript:tobago_requestTab('"
            + clientId + "'," + i + ",'"
            + ComponentUtil.findPage(component).getFormId(facesContext) + "')";
      } else {
        url = "javascript:tobago_selectTab('"
            + clientId + "'," + i + ','
            + tabs.length + ')';
      }

      LabelWithAccessKey label = new LabelWithAccessKey(tab);

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

      writer.startElement("td", tab);
      writer.writeAttribute("title", null, ATTR_TIP);

      writer.startElement("div", null);
      writer.writeClassAttribute(outerClass);

      writer.startElement("div", null);
      writer.writeClassAttribute(innerClass);

      writer.startElement("a", null);
      writer.writeClassAttribute("tobago-tab-link");
      writer.writeIdAttribute(clientId + "." + virtualTab + SUBCOMPONENT_SEP + i);
      writer.writeAttribute("href", url, null);
      if (label.getAccessKey() != null) {
        if (LOG.isWarnEnabled() && ! AccessKeyMap.addAccessKey(
            facesContext, label.getAccessKey())) {
          LOG.warn("dublicated accessKey : " + label.getAccessKey());
        }
        writer.writeAttribute("accesskey", label.getAccessKey(), null);
//        writer.writeAttribute("onfocus", "this.click();", null);
      }
      if (label.getText() != null) {
        HtmlRendererUtil.writeLabelWithAccessKey(writer, label);
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
    writer.writeClassAttribute("tobago-tab-fulfill");

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



    encodeContent(writer, facesContext, activeTab);


    writer.endElement("table");
  }

  protected void encodeContent(TobagoResponseWriter writer, FacesContext facesContext, UIPanel activeTab) throws IOException {

    String bodyStyle = (String)
        activeTab.getParent().getAttributes().get(ATTR_STYLE_BODY);
    writer.startElement("tr", null);
    writer.startElement("td", null);
    writer.writeClassAttribute("tobago-tab-content");
    writer.writeAttribute("style", bodyStyle, null);writer.writeText("", null);
    RenderUtil.encodeChildren(facesContext, activeTab);
    writer.endElement("td");
    writer.endElement("tr");
  }

  public void encodeAjax(FacesContext context, UIComponent component) throws IOException {
    AjaxUtils.checkParamValidity(context, component, UITabGroup.class);

    renderTabGroupView(context,
        (TobagoResponseWriter)context.getResponseWriter(),
        (UITabGroup)component,
        ((UITabGroup)component).getActiveIndex(),
        new StyleAttribute((String) component.getAttributes().get(ATTR_STYLE)),
        ComponentUtil.getBooleanAttribute(component, ATTR_SERVER_SIDE_TABS),
        ResourceManagerUtil.getImageWithPath(context, "image/1x1.gif"));
  }

  public int getFixedHeight(FacesContext facesContext, UIComponent uiComponent) {
    UITabGroup component = (UITabGroup) uiComponent;
    int height =
        ComponentUtil.getIntAttribute(component, ATTR_HEIGHT, -1);

    int fixedHeight;
    if (height != -1) {
      fixedHeight = height;
    } else {
      UIPanel[] tabs = component.getTabs();
      fixedHeight = 0;
      for (int i = 0; i < tabs.length; i++) {
        UIPanel tab = tabs[i];
        RendererBase renderer = ComponentUtil.getRenderer(facesContext, tab);
        fixedHeight
            = Math.max(fixedHeight, renderer.getFixedHeight(facesContext, tab));
      }
      fixedHeight += getConfiguredValue(facesContext, component, "headerHeight");
      fixedHeight += getConfiguredValue(facesContext, component, "paddingHeight");
    }
    return fixedHeight;
  }

  private void layoutTabs(FacesContext facesContext, UITabGroup component,
                          UIPanel[] tabs) {
    Object layoutWidth =
        component.getAttributes().get(ATTR_LAYOUT_WIDTH);
    Object layoutHeight =
        component.getAttributes().get(ATTR_LAYOUT_HEIGHT);

    for (int i = 0; i < tabs.length; i++) {
      UIPanel tab = tabs[i];
      if (layoutWidth != null) {
        HtmlRendererUtil.layoutSpace(facesContext, tab, true);
      }
      if (layoutHeight != null) {
        HtmlRendererUtil.layoutSpace(facesContext, tab, false);
      }
    }


  }
// ///////////////////////////////////////////// bean getter + setter

  public class TabController extends MethodBinding {

    public static final String ID_PREFIX = "tab_";

    public Object invoke(FacesContext facesContext, Object[] objects)
        throws EvaluationException, MethodNotFoundException {

      if (objects[0] instanceof ActionEvent) {
        UICommand command  = (UICommand) ((ActionEvent)objects[0]).getSource();
        if (LOG.isDebugEnabled()) {
          LOG.debug("Id = " + command.getId());
        }

        if (command.getId() != null && command.getId().startsWith(ID_PREFIX)) {
          try {
            int newTab =
                Integer.parseInt(command.getId().substring(ID_PREFIX.length()));
          }
          catch (Exception e) {

          }
        }
      }
      return null;
    }

    public Class getType(FacesContext facesContext)
        throws MethodNotFoundException {
      return String.class;
    }

  }

}

