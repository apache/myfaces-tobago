package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LAYOUT_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LAYOUT_WIDTH;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_BODY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_HEADER;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import static org.apache.myfaces.tobago.TobagoConstants.SUBCOMPONENT_SEP;
import org.apache.myfaces.tobago.ajax.api.AjaxRenderer;
import org.apache.myfaces.tobago.ajax.api.AjaxUtils;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.component.UITabGroup;
import static org.apache.myfaces.tobago.component.UITabGroup.SWITCH_TYPE_CLIENT;
import static org.apache.myfaces.tobago.component.UITabGroup.SWITCH_TYPE_RELOAD_PAGE;
import static org.apache.myfaces.tobago.component.UITabGroup.SWITCH_TYPE_RELOAD_TAB;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.event.TabChangeEvent;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlStyleMap;
import org.apache.myfaces.tobago.util.AccessKeyMap;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

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

  private static final Log LOG = LogFactory.getLog(TabGroupRenderer.class);

  public static final String ACTIVE_INDEX_POSTFIX
      = SUBCOMPONENT_SEP + "activeIndex";

  public void decode(FacesContext facesContext, UIComponent component) {
    if (ComponentUtil.isOutputOnly(component)) {
      return;
    }

    int oldIndex = ((UITabGroup) component).getRenderedIndex();

    String clientId = component.getClientId(facesContext);
    final Map parameters
        = facesContext.getExternalContext().getRequestParameterMap();
    String newValue = (String) parameters.get(clientId + ACTIVE_INDEX_POSTFIX);
    try {
      int activeIndex = Integer.parseInt(newValue);
      if (activeIndex != oldIndex) {
        ((UITabGroup) component).setActiveIndex(activeIndex);
        TabChangeEvent event = new TabChangeEvent(component,
            new Integer(oldIndex), new Integer(activeIndex));
        event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
        component.queueEvent(event);
      }

    } catch (NumberFormatException e) {
      LOG.error("Can't parse activeIndex: '" + newValue + "'");
    }
  }

  public void encodeEnd(FacesContext facesContext,
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

    final String switchType = component.getSwitchType();

    UIPage page = ComponentUtil.findPage(component);
    final String[] scripts = new String[]{
        "script/tab.js",
        "script/tabgroup.js"
    };
    for (String script : scripts) {
      page.getScriptFiles().add(script);
    }
    if (TobagoConfig.getInstance(facesContext).isAjaxEnabled()) {
      HtmlRendererUtil.writeScriptLoader(facesContext, scripts, new String[0]);
    }


    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();

    writer.startElement(HtmlConstants.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", null);
    writer.writeAttribute(HtmlAttributes.VALUE, Integer.toString(activeIndex), null);
    writer.writeNameAttribute(hiddenId);
    writer.writeIdAttribute(hiddenId);
    writer.endElement(HtmlConstants.INPUT);

    // if a outer uiPage is presend, the virtual tab will go over all
    // tabs and render it as they are selected, and it will
    // selected with stylesheet.

    for (int virtualTab = 0; virtualTab < tabs.length; virtualTab++) {

      if (SWITCH_TYPE_CLIENT.equals(switchType) || virtualTab == activeIndex) {



        if (virtualTab != activeIndex) {
          HtmlRendererUtil.replaceStyleAttribute(component, "display", "none");
        }
        writer.startElement(HtmlConstants.DIV, null);
        writer.writeComment("empty div fix problem with mozilla and fieldset");
        writer.endElement(HtmlConstants.DIV);

        writer.startElement(HtmlConstants.DIV, null);
        writer.writeIdAttribute(clientId);
        renderTabGroupView(facesContext, writer, component, virtualTab,
            (HtmlStyleMap) component.getAttributes().get(ATTR_STYLE),
            switchType, image1x1);
        writer.endElement(HtmlConstants.DIV);

        if (TobagoConfig.getInstance(facesContext).isAjaxEnabled()
            && SWITCH_TYPE_RELOAD_TAB.equals(switchType)) {
          final String[] cmds = {
              "new Tobago.TabGroup(",
              "    '" + clientId + "', ",
              "    '" + activeIndex + "');"
          };
          HtmlRendererUtil.writeScriptLoader(facesContext, new String[0], cmds);
        }
      }
    }
  }

  private void renderTabGroupView(
      FacesContext facesContext, TobagoResponseWriter writer, UITabGroup component,
      int virtualTab, HtmlStyleMap oStyle, String switchType, String image1x1)
      throws IOException {
    UIPanel[] tabs = component.getTabs();
    writer.startElement(HtmlConstants.TABLE, null);
    writer.writeAttribute(HtmlAttributes.BORDER, "0", null);
    writer.writeAttribute(HtmlAttributes.CELLPADDING, "0", null);
    writer.writeAttribute(HtmlAttributes.CELLSPACING, "0", null);
    writer.writeAttribute(HtmlAttributes.SUMMARY, "", null);
    final String clientId = component.getClientId(facesContext);
    writer.writeIdAttribute(clientId + '.' + virtualTab);
    writer.writeAttribute(HtmlAttributes.STYLE, oStyle, null);

    writer.startElement(HtmlConstants.TR, null);
    writer.writeAttribute(HtmlAttributes.VALIGN, "bottom", null);

    writer.startElement(HtmlConstants.TD, null);

    writer.startElement(HtmlConstants.TABLE, component);
    writer.writeAttribute(HtmlAttributes.BORDER, "0", null);
    writer.writeAttribute(HtmlAttributes.CELLPADDING, "0", null);
    writer.writeAttribute(HtmlAttributes.CELLSPACING, "0", null);
    writer.writeAttribute(HtmlAttributes.SUMMARY, "", null);
    writer.writeAttribute(HtmlAttributes.STYLE, null, ATTR_STYLE_HEADER);

    writer.startElement(HtmlConstants.TR, null);
    writer.writeAttribute(HtmlAttributes.VALIGN, "bottom", null);

    UIPanel activeTab = null;

    for (int i = 0; i < tabs.length; i++) {
      UIPanel tab = tabs[i];

      String onclick;

      if (TobagoConfig.getInstance(facesContext).isAjaxEnabled()
          && SWITCH_TYPE_RELOAD_TAB.equals(switchType)) {
        onclick = null;
      }  else if (SWITCH_TYPE_RELOAD_PAGE.equals(switchType)
          || SWITCH_TYPE_RELOAD_TAB.equals(switchType)) {
        onclick = "tobago_requestTab('"
            + clientId + "'," + i + ",'"
            + ComponentUtil.findPage(component).getFormId(facesContext) + "')";
      } else {   //  SWITCH_TYPE_CLIENT
        onclick = "tobago_selectTab('"
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

      writer.startElement(HtmlConstants.TD, tab);
      writer.writeAttribute(HtmlAttributes.TITLE, null, ATTR_TIP);

      writer.startElement(HtmlConstants.DIV, null);
      writer.writeClassAttribute(outerClass);

      writer.startElement(HtmlConstants.DIV, null);
      writer.writeClassAttribute(innerClass);

      writer.startElement(HtmlConstants.SPAN, null);
      writer.writeClassAttribute("tobago-tab-link");
      String tabId = clientId + "." + virtualTab + SUBCOMPONENT_SEP + i;
      writer.writeIdAttribute(tabId);
      if (onclick != null) {
        writer.writeAttribute(HtmlAttributes.ONCLICK, onclick, null);
      }
      if (label.getText() != null) {
        HtmlRendererUtil.writeLabelWithAccessKey(writer, label);
      } else {
        writer.writeText(Integer.toString(i+1), null);
      }
      writer.endElement(HtmlConstants.SPAN);

      if (label.getAccessKey() != null) {
        if (LOG.isWarnEnabled()
            && !AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
          LOG.warn("dublicated accessKey : " + label.getAccessKey());
        }
      HtmlRendererUtil.addClickAcceleratorKey(
          facesContext, tabId, label.getAccessKey());
      }
      writer.endElement(HtmlConstants.DIV);
      writer.endElement(HtmlConstants.DIV);
      writer.endElement(HtmlConstants.TD);

    }

    writer.startElement(HtmlConstants.TD, null);
    writer.writeAttribute(HtmlAttributes.WIDTH, "100%", null);

    writer.startElement(HtmlConstants.DIV, null);
    writer.writeClassAttribute("tobago-tab-fulfill");

    writer.startElement(HtmlConstants.IMG, null);
    writer.writeAttribute(HtmlAttributes.SRC, image1x1, null);
    writer.writeAttribute(HtmlAttributes.ALT, "", null);
    writer.endElement(HtmlConstants.IMG);

    writer.endElement(HtmlConstants.DIV);
    writer.endElement(HtmlConstants.TD);
    writer.endElement(HtmlConstants.TR);
    writer.endElement(HtmlConstants.TABLE);

    writer.endElement(HtmlConstants.TD);
    writer.endElement(HtmlConstants.TR);

    encodeContent(writer, facesContext, activeTab);

    writer.endElement(HtmlConstants.TABLE);
  }

  protected void encodeContent(TobagoResponseWriter writer,
      FacesContext facesContext, UIPanel activeTab) throws IOException {

    HtmlStyleMap bodyStyle = (HtmlStyleMap)
        activeTab.getParent().getAttributes().get(ATTR_STYLE_BODY);
    writer.startElement(HtmlConstants.TR, null);
    writer.startElement(HtmlConstants.TD, null);
    writer.writeClassAttribute("tobago-tab-content");
    writer.writeAttribute(HtmlAttributes.STYLE, bodyStyle, null);
    writer.writeText("", null);
    RenderUtil.encodeChildren(facesContext, activeTab);
    writer.endElement(HtmlConstants.TD);
    writer.endElement(HtmlConstants.TR);
  }

  public void encodeAjax(FacesContext context, UIComponent component) throws IOException {
    AjaxUtils.checkParamValidity(context, component, UITabGroup.class);

    renderTabGroupView(context,
        (TobagoResponseWriter) context.getResponseWriter(),
        (UITabGroup) component,
        ((UITabGroup) component).getActiveIndex(),
        (HtmlStyleMap) component.getAttributes().get(ATTR_STYLE),
        SWITCH_TYPE_RELOAD_TAB,
        ResourceManagerUtil.getImageWithPath(context, "image/1x1.gif"));
    context.responseComplete();
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

  public class TabController extends MethodBinding {

    public static final String ID_PREFIX = "tab_";

    public Object invoke(FacesContext facesContext, Object[] objects)
        throws EvaluationException {

      if (objects[0] instanceof ActionEvent) {
        UICommand command  = (UICommand) ((ActionEvent) objects[0]).getSource();
        if (LOG.isDebugEnabled()) {
          LOG.debug("Id = " + command.getId());
        }

        if (command.getId() != null && command.getId().startsWith(ID_PREFIX)) {
          try {
            int newTab =
                Integer.parseInt(command.getId().substring(ID_PREFIX.length()));
          } catch (Exception e) {
            // ignore
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

