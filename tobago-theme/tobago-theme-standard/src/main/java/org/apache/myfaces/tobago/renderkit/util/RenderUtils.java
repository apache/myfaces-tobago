/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.renderkit.util;

import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.internal.component.AbstractUICommand;
import org.apache.myfaces.tobago.internal.component.AbstractUIData;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.model.ExpandedState;
import org.apache.myfaces.tobago.model.SelectedState;
import org.apache.myfaces.tobago.model.TreePath;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.ValueHolder;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.render.ClientBehaviorRenderer;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RenderUtils {

  private static final Logger LOG = LoggerFactory.getLogger(RenderUtils.class);

  private RenderUtils() {
    // to prevent instantiation
  }

  public static boolean contains(final Object[] list, final Object value) {
    if (list == null) {
      return false;
    }
    for (final Object aList : list) {
      if (aList != null && aList.equals(value)) {
        return true;
      }
    }
    return false;
  }

  public static void encodeChildren(final FacesContext facesContext, final UIComponent panel) throws IOException {
    for (final UIComponent child : panel.getChildren()) {
      encode(facesContext, child);
    }
  }

  public static void encode(final FacesContext facesContext, final UIComponent component) throws IOException {
    encode(facesContext, component, null);
  }

  public static void encode(
      final FacesContext facesContext, final UIComponent component,
      final List<? extends Class<? extends UIComponent>> only)
      throws IOException {

    if (only != null && !matchFilter(component, only)) {
      return;
    }

    if (component.isRendered()) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("rendering " + component.getRendererType() + " " + component);
      }
      component.encodeBegin(facesContext);
      if (component.getRendersChildren()) {
        component.encodeChildren(facesContext);
      } else {
        for (final UIComponent child : component.getChildren()) {
          encode(facesContext, child, only);
        }
      }
      component.encodeEnd(facesContext);
    }
  }

  private static boolean matchFilter(
      final UIComponent component, final List<? extends Class<? extends UIComponent>> only) {
    for (final Class<? extends UIComponent> clazz : only) {
      if (clazz.isAssignableFrom(component.getClass())) {
        return true;
      }
    }
    return false;
  }

  public static String currentValue(final UIComponent component) {
    String currentValue = null;
    if (component instanceof ValueHolder) {
      Object value;
      if (component instanceof EditableValueHolder) {
        value = ((EditableValueHolder) component).getSubmittedValue();
        if (value != null) {
          return (String) value;
        }
      }

      value = ((ValueHolder) component).getValue();
      if (value != null) {
        currentValue = ComponentUtils.getFormattedValue(FacesContext.getCurrentInstance(), component, value);
      }
    }
    return currentValue;
  }

  public static void decodedStateOfTreeData(final FacesContext facesContext, final AbstractUIData data) {

    if (!data.isTreeModel()) {
      return;
    }

    // selected
    final List<Integer> selectedIndices = decodeIndices(facesContext, data, AbstractUIData.SUFFIX_SELECTED);

    // expanded
    final List<Integer> expandedIndices = decodeIndices(facesContext, data, AbstractUIData.SUFFIX_EXPANDED);

    final int last = data.isRowsUnlimited() ? Integer.MAX_VALUE : data.getFirst() + data.getRows();
    for (int rowIndex = data.getFirst(); rowIndex < last; rowIndex++) {
      data.setRowIndex(rowIndex);
      if (!data.isRowAvailable()) {
        break;
      }

      final TreePath path = data.getPath();

      // selected
      if (selectedIndices != null) {
        final SelectedState selectedState = data.getSelectedState();
        final boolean oldSelected = selectedState.isSelected(path);
        final boolean newSelected = selectedIndices.contains(rowIndex);
        if (newSelected != oldSelected) {
          if (newSelected) {
            selectedState.select(path);
          } else {
            selectedState.unselect(path);
          }
        }
      }

      // expanded
      if (expandedIndices != null) {
        final ExpandedState expandedState = data.getExpandedState();
        final boolean oldExpanded = expandedState.isExpanded(path);
        final boolean newExpanded = expandedIndices.contains(rowIndex);
        if (newExpanded != oldExpanded) {
          if (newExpanded) {
            expandedState.expand(path);
          } else {
            expandedState.collapse(path);
          }
        }
      }

    }
    data.setRowIndex(-1);
  }

  private static List<Integer> decodeIndices(
      final FacesContext facesContext, final AbstractUIData data, final String suffix) {
    String string = null;
    final String key = data.getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR + suffix;
    try {
      string = facesContext.getExternalContext().getRequestParameterMap().get(key);
      if (string != null) {
        return StringUtils.parseIntegerList(string);
      }
    } catch (final Exception e) {
      // should not happen
      LOG.warn("Can't parse " + suffix + ": '" + string + "' from parameter '" + key + "'", e);
    }
    return null;
  }

  public static String generateUrl(final FacesContext facesContext, final AbstractUICommand component) {

    final Application application = facesContext.getApplication();
    final ViewHandler viewHandler = application.getViewHandler();
    final ExternalContext externalContext = facesContext.getExternalContext();

    String url = null;

    if (component.getResource() != null) {
      final boolean jsfResource = component.isJsfResource();
      url = ResourceManagerUtils.getPageWithoutContextPath(facesContext, component.getResource());
      if (url != null) {
        if (jsfResource) {
          url = viewHandler.getActionURL(facesContext, url);
          url = externalContext.encodeActionURL(url);
        } else {
          url = viewHandler.getResourceURL(facesContext, url);
          url = externalContext.encodeResourceURL(url);
        }
      } else {
        url = "";
      }
    } else if (component.getLink() != null) {

      final String link = component.getLink();
      if (link.startsWith("/")) { // internal absolute link
        url = externalContext.encodeResourceURL(externalContext.getRequestContextPath() + link);
      } else if (StringUtils.isUrl(link)) { // external link
        url = link;
      } else { // internal relative link
        url = externalContext.encodeResourceURL(link);
      }

      final StringBuilder builder = new StringBuilder(url);
      boolean firstParameter = !url.contains("?");
      for (final UIComponent child : component.getChildren()) {
        if (child instanceof UIParameter) {
          final UIParameter parameter = (UIParameter) child;
          if (firstParameter) {
            builder.append("?");
            firstParameter = false;
          } else {
            builder.append("&");
          }
          builder.append(parameter.getName());
          builder.append("=");
          final Object value = parameter.getValue();
          // TODO encoding
          builder.append(value != null ? URLDecoder.decode(value.toString()) : null);
        }
      }
      url = builder.toString();
    }

    return url;
  }

  public static String getBehaviorCommands(final FacesContext facesContext, final ClientBehaviorHolder holder) {
    final Map<String, List<ClientBehavior>> behaviors;
    if (holder instanceof AbstractUICommand) {
      behaviors = holder.getClientBehaviors();
    } else {
      // search for clientBehaviorHolder in facets
      behaviors = new HashMap<String, List<ClientBehavior>>();
      for (Map.Entry<String, UIComponent> facetEntry : ((UIComponent) holder).getFacets().entrySet()) {

        if (!Facets.isEvent(facetEntry.getKey())) {
          break;
        }

        if (facetEntry.getValue() instanceof ClientBehaviorHolder) {
          final ClientBehaviorHolder behaviorHolder = (ClientBehaviorHolder) facetEntry.getValue();
          final Map<String, List<ClientBehavior>> behaviorsMap = behaviorHolder.getClientBehaviors();
          final List<ClientBehavior> clientBehaviors = new ArrayList<ClientBehavior>();
          final String facetName = facetEntry.getKey();
          if (behaviorsMap.get(facetName) != null) {
            clientBehaviors.addAll(behaviorsMap.get(facetName));
          }
          // if facet name != "click" and the behaviorCommands are added as default they must moved to the correct map
          // "click" is default behavior of tc:command which should be the facet
          if (clientBehaviors.isEmpty() && behaviorsMap.get("click") != null) {
            final List<ClientBehavior> clickBehaviors = behaviorsMap.get("click");
            for (ClientBehavior behavior : clickBehaviors) {
              behaviorHolder.addClientBehavior(facetName, behavior);
              clientBehaviors.add(behavior);
            }
            clickBehaviors.clear();
          }
          behaviors.put(facetName, clientBehaviors);
        }
      }
      // add f:ajax behaviors, note:facet behaviors overrules f:ajax
      if (behaviors.isEmpty()) {
        behaviors.putAll(holder.getClientBehaviors());
      }
    }
    for (Map.Entry<String, List<ClientBehavior>> behavior : behaviors.entrySet()) {
      final String key = behavior.getKey();
      final ClientBehaviorContext context = ClientBehaviorContext.createClientBehaviorContext(
          facesContext, (UIComponent) holder, key, ((UIComponent) holder).getClientId(facesContext), null);
      for (ClientBehavior clientBehavior : behavior.getValue()) {
        if (clientBehavior instanceof ClientBehaviorBase) {
          final String type = ((ClientBehaviorBase) clientBehavior).getRendererType();
          final ClientBehaviorRenderer clientBehaviorRenderer
              = facesContext.getRenderKit().getClientBehaviorRenderer(type);
          final String commands = clientBehaviorRenderer.getScript(context, clientBehavior);
          return commands;
        } else {
          LOG.warn("Ignoring: '{}'", clientBehavior);
        }
      }
    }
    return null;
  }

  public static void decodeClientBehaviors(final FacesContext facesContext, final UIComponent component) {
    if (component instanceof ClientBehaviorHolder) {
      final Map<String, String> paramMap = facesContext.getExternalContext().getRequestParameterMap();
      final String behaviorEventName = paramMap.get("javax.faces.behavior.event");
      if (behaviorEventName == null) {
        return;
      }

      final ClientBehaviorHolder clientBehaviorHolder;

      if (component.getFacet(behaviorEventName) instanceof ClientBehaviorHolder) {
        clientBehaviorHolder = (ClientBehaviorHolder) component.getFacet(behaviorEventName);
      } else {
        clientBehaviorHolder = (ClientBehaviorHolder) component;
      }
      final Map<String, List<ClientBehavior>> clientBehaviors = clientBehaviorHolder.getClientBehaviors();
      if (clientBehaviors != null && !clientBehaviors.isEmpty()) {
        List<ClientBehavior> clientBehaviorList = clientBehaviors.get(behaviorEventName);
        if (clientBehaviorList != null && !clientBehaviorList.isEmpty()) {
          final String clientId = paramMap.get("javax.faces.source");
          if (component.getClientId(facesContext).equals(clientId)) {
            for (ClientBehavior clientBehavior : clientBehaviorList) {
              clientBehavior.decode(facesContext, component);
            }
            ((UIComponent) clientBehaviorHolder).queueEvent(new ActionEvent((UIComponent) clientBehaviorHolder));
          }
        }
      }
    }
  }
}
