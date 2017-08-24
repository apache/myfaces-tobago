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

package org.apache.myfaces.tobago.internal.util;

import org.apache.myfaces.tobago.component.ClientBehaviors;
import org.apache.myfaces.tobago.internal.component.AbstractUICommand;
import org.apache.myfaces.tobago.internal.component.AbstractUICommandBase;
import org.apache.myfaces.tobago.internal.component.AbstractUIData;
import org.apache.myfaces.tobago.internal.renderkit.Command;
import org.apache.myfaces.tobago.internal.renderkit.CommandMap;
import org.apache.myfaces.tobago.model.ExpandedState;
import org.apache.myfaces.tobago.model.SelectedState;
import org.apache.myfaces.tobago.model.TreePath;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.ViewHandler;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.ValueHolder;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.render.ClientBehaviorRenderer;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public final class RenderUtils {

  private static final Logger LOG = LoggerFactory.getLogger(RenderUtils.class);

  private RenderUtils() {
    // to prevent instantiation
  }

  /**
   * @deprecated since 4.0.0. Use {@link ArrayUtils#contains(Object[], Object)}
   */
  @Deprecated
  public static boolean contains(final Object[] list, final Object value) {
    return ArrayUtils.contains(list, value);
  }

  /**
   * @deprecated since 4.0.0. Use {@link UIComponent#encodeChildren(FacesContext)}
   */
  @Deprecated
  public static void encodeChildren(final FacesContext facesContext, final UIComponent panel) throws IOException {
    for (final UIComponent child : panel.getChildren()) {
      child.encodeAll(facesContext);
    }
  }

  /**
   * @deprecated since 4.0.0. Use {@link UIComponent#encodeAll(FacesContext)}
   */
  @Deprecated
  public static void encode(final FacesContext facesContext, final UIComponent component) throws IOException {
    component.encodeAll(facesContext);
  }

  /**
   * @deprecated since 4.0.0. Use {@link UIComponent#encodeAll(FacesContext)}
   */
  @Deprecated
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

  /**
   * @deprecated since 4.0.0
   */
  @Deprecated
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

  public static String generateUrl(final FacesContext facesContext, final AbstractUICommandBase component) {

    final ExternalContext externalContext = facesContext.getExternalContext();
    final String outcome = component.getOutcome();
    final String link = component.getLink();

    String url = null;

    if (outcome != null) {
      final ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
      url = viewHandler.getBookmarkableURL(
          facesContext,
          outcome,
          null,
          true);
    } else if (link != null) {
      if (StringUtils.isUrl(link)) { // external link
        url = link;
      } else { // internal link
        url = externalContext.encodeResourceURL(link);
      }
    }

    if (link != null || outcome != null) {
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
          if (value != null) {
            final String characterEncoding = facesContext.getResponseWriter().getCharacterEncoding();
            try {
              builder.append(URLEncoder.encode(value.toString(), characterEncoding));
            } catch (UnsupportedEncodingException e) {
              LOG.error("", e);
            }
          }
        }
      }

      final String fragment = component.getFragment();
      if (StringUtils.isNotBlank(fragment)) {
        builder.append("#").append(fragment.trim());
      }

      url = builder.toString();
    }

    return url;
  }

  public static CommandMap getBehaviorCommands(final FacesContext facesContext, final ClientBehaviorHolder holder) {
    CommandMap map = null;
    final Map<String, List<ClientBehavior>> behaviors = holder.getClientBehaviors();
    for (Map.Entry<String, List<ClientBehavior>> behaviorMap : behaviors.entrySet()) {
      final String key = behaviorMap.getKey();
      final ClientBehaviorContext context = ClientBehaviorContext.createClientBehaviorContext(
          facesContext, (UIComponent) holder, key, ((UIComponent) holder).getClientId(facesContext), null);
      for (ClientBehavior clientBehavior : behaviorMap.getValue()) {
        if (clientBehavior instanceof ClientBehaviorBase) {
          String type = ((ClientBehaviorBase) clientBehavior).getRendererType();
          // XXX this is to use a different renderer for Tobago components and other components.
          if (type.equals(AjaxBehavior.BEHAVIOR_ID)) {
            type = "org.apache.myfaces.tobago.behavior.Ajax";
          }
          final ClientBehaviorRenderer renderer = facesContext.getRenderKit().getClientBehaviorRenderer(type);
          final String dummy = renderer.getScript(context, clientBehavior);
          if (dummy != null) {
            map = CommandMap.merge(map, CommandMap.restoreCommandMap(facesContext));
          }
        } else {
          LOG.warn("Ignoring: '{}'", clientBehavior);
        }
      }
    }

    // if there is no explicit behavior (with f:ajax or tc:event), use the command properties as default.
    // tbd: think about refactoring: put this into TobagoClientBehaviorRenderer
    if ((map == null || map.isEmpty()) && holder instanceof AbstractUICommand) {
      if (map == null) {
        map = new CommandMap();
      }
      map.addCommand(ClientBehaviors.click, new Command(facesContext, (AbstractUICommand) holder));
    }

    return map;
  }

  public static void decodeClientBehaviors(final FacesContext facesContext, final UIComponent component) {
    if (component instanceof ClientBehaviorHolder) {
      final ClientBehaviorHolder clientBehaviorHolder = (ClientBehaviorHolder) component;
      final Map<String, List<ClientBehavior>> clientBehaviors = clientBehaviorHolder.getClientBehaviors();
      if (clientBehaviors != null && !clientBehaviors.isEmpty()) {
        final Map<String, String> paramMap = facesContext.getExternalContext().getRequestParameterMap();
        final String behaviorEventName = paramMap.get("javax.faces.behavior.event");
        if (behaviorEventName != null) {
          final List<ClientBehavior> clientBehaviorList = clientBehaviors.get(behaviorEventName);
          if (clientBehaviorList != null && !clientBehaviorList.isEmpty()) {
            final String clientId = paramMap.get("javax.faces.source");
            if (component.getClientId(facesContext).equals(clientId)) {
              for (ClientBehavior clientBehavior : clientBehaviorList) {
                clientBehavior.decode(facesContext, component);
              }
            }
          }
        }
      }
    }
  }
}
