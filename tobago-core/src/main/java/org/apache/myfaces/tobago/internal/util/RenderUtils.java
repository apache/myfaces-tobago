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
import org.apache.myfaces.tobago.internal.behavior.EventBehavior;
import org.apache.myfaces.tobago.internal.component.AbstractUICommand;
import org.apache.myfaces.tobago.internal.component.AbstractUICommandBase;
import org.apache.myfaces.tobago.internal.component.AbstractUIData;
import org.apache.myfaces.tobago.internal.component.AbstractUIEvent;
import org.apache.myfaces.tobago.internal.component.AbstractUITreeNodeBase;
import org.apache.myfaces.tobago.internal.renderkit.Command;
import org.apache.myfaces.tobago.internal.renderkit.CommandMap;
import org.apache.myfaces.tobago.internal.renderkit.renderer.TobagoClientBehaviorRenderer;
import org.apache.myfaces.tobago.model.ExpandedState;
import org.apache.myfaces.tobago.model.SelectedState;
import org.apache.myfaces.tobago.model.TreePath;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.application.ViewHandler;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIPanel;
import jakarta.faces.component.UIParameter;
import jakarta.faces.component.behavior.AjaxBehavior;
import jakarta.faces.component.behavior.ClientBehavior;
import jakarta.faces.component.behavior.ClientBehaviorBase;
import jakarta.faces.component.behavior.ClientBehaviorContext;
import jakarta.faces.component.behavior.ClientBehaviorHolder;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.render.ClientBehaviorRenderer;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class RenderUtils {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private RenderUtils() {
    // to prevent instantiation
  }

  /**
   * @deprecated Use {@link ArrayUtils#contains(Object[], Object)}
   */
  @Deprecated(since = "4.0.0", forRemoval = true)
  public static boolean contains(final Object[] list, final Object value) {
    return ArrayUtils.contains(list, value);
  }

  /**
   * @deprecated Use {@link UIComponent#encodeChildren(FacesContext)}
   */
  @Deprecated(since = "4.0.0", forRemoval = true)
  public static void encodeChildren(final FacesContext facesContext, final UIComponent panel) throws IOException {
    for (final UIComponent child : panel.getChildren()) {
      child.encodeAll(facesContext);
    }
  }

  /**
   * @deprecated Use {@link UIComponent#encodeAll(FacesContext)}
   */
  @Deprecated(since = "4.0.0", forRemoval = true)
  public static void encode(final FacesContext facesContext, final UIComponent component) throws IOException {
    component.encodeAll(facesContext);
  }

  /**
   * @deprecated Use {@link UIComponent#encodeAll(FacesContext)}
   */
  @Deprecated(since = "4.0.0", forRemoval = true)
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

  @Deprecated(since = "4.0.0", forRemoval = true)
  private static boolean matchFilter(
      final UIComponent component, final List<? extends Class<? extends UIComponent>> only) {
    for (final Class<? extends UIComponent> clazz : only) {
      if (clazz.isAssignableFrom(component.getClass())) {
        return true;
      }
    }
    return false;
  }

  /**
   * @deprecated Please use {@link RendererBase#getCurrentValue}
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  public static String currentValue(final UIComponent component) {
    return new RendererBase<UIComponent>() {
      public String fake(final UIComponent component) {
        return getCurrentValue(FacesContext.getCurrentInstance(), component);
      }
    }.fake(component);
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

      // if the node is not rendered, the state must not be evaluated.
      boolean skip = false;
      for (final UIComponent uiComponent : data.getChildren()) {
        if (uiComponent instanceof AbstractUITreeNodeBase && !uiComponent.isRendered()) {
          skip = true;
          break;
        }
      }
      if (skip) {
        continue;
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
      return JsonUtils.decodeIntegerArray(string);
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
          Collections.emptyMap(),
          true);
    } else if (link != null) {
      if (StringUtils.isUrl(link)) { // external link
        url = link;
      } else { // internal link
        url = externalContext.encodeResourceURL(link);
      }
    }

    if (link != null || outcome != null) {
      final String characterEncoding = facesContext.getResponseWriter().getCharacterEncoding();
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
          appendUrlEncoded(builder, parameter.getName(), characterEncoding);
          builder.append("=");
          appendUrlEncoded(builder, parameter.getValue(), characterEncoding);
        }
      }

      final String fragment = component.getFragment();
      if (StringUtils.isNotBlank(fragment)) {
        builder.append("#");
        appendUrlEncoded(builder, fragment.trim(), characterEncoding);
      }

      url = builder.toString();
    }

    return url;
  }

  private static void appendUrlEncoded(
      final StringBuilder builder, final Object value, final String characterEncoding) {

    if (value != null) {
      try {
        final String encode = URLEncoder.encode(value.toString(), characterEncoding);
        // URLEncoder.encode may return + instead of %20 for a space, but this is not good in some cases, e.g. mailto:
        builder.append(encode.replace("+", "%20"));
      } catch (final UnsupportedEncodingException e) {
        LOG.error("string='" + value + "'", e);
      }
    }
  }

  @Deprecated(since = "5.0.0", forRemoval = true)
  public static CommandMap getBehaviorCommands(
      final FacesContext facesContext,
      final ClientBehaviorHolder clientBehaviorHolder) {
    CommandMap commandMap = null;

    for (final Map.Entry<String, List<ClientBehavior>> entry : clientBehaviorHolder.getClientBehaviors().entrySet()) {
      final String eventName = entry.getKey();
      final ClientBehaviorContext clientBehaviorContext
          = getClientBehaviorContext(facesContext, clientBehaviorHolder, eventName);

      for (final ClientBehavior clientBehavior : entry.getValue()) {
        if (clientBehavior instanceof EventBehavior) {
          final EventBehavior eventBehavior = (EventBehavior) clientBehavior;
          final AbstractUIEvent abstractUIEvent = getAbstractUIEvent((UIComponent) clientBehaviorHolder, eventBehavior);

          if (abstractUIEvent != null && abstractUIEvent.isRendered() && !abstractUIEvent.isDisabled()) {
            for (List<ClientBehavior> children : abstractUIEvent.getClientBehaviors().values()) {
              for (ClientBehavior child : children) {
                final CommandMap childMap = getCommandMap(facesContext, clientBehaviorContext, child);
                commandMap = CommandMap.merge(commandMap, childMap);
              }
            }
          }
        }

        final CommandMap map = getCommandMap(facesContext, clientBehaviorContext, clientBehavior);
        commandMap = CommandMap.merge(commandMap, map);
      }
    }

    // if there is no explicit behavior (with f:ajax or tc:event), use the command properties as default.
    if ((commandMap == null || commandMap.isEmpty()) && clientBehaviorHolder instanceof AbstractUICommand) {
      if (commandMap == null) {
        commandMap = new CommandMap();
      }
      final AbstractUICommand holder = (AbstractUICommand) clientBehaviorHolder;
      commandMap.addCommand(ClientBehaviors.click, new Command(
          holder.getClientId(facesContext),
          holder.getFieldId(facesContext),
          holder.isTransition(),
          holder.getTarget(),
          null,
          null,
          ComponentUtils.getConfirmation(holder),
          null,
          TobagoClientBehaviorRenderer.createCollapsible(facesContext, holder),
          holder.isOmit(),
          null,
          null,
          holder.isImmediate()));
    }

    return commandMap;
  }

  @Deprecated(since = "5.0.0", forRemoval = true)
  private static ClientBehaviorContext getClientBehaviorContext(
      final FacesContext facesContext,
      final ClientBehaviorHolder clientBehaviorHolder, final String eventName) {
    UIComponent component = (UIComponent) clientBehaviorHolder;
    return ClientBehaviorContext.createClientBehaviorContext(facesContext, component, eventName,
        component.getClientId(facesContext), null);
  }

  public static AbstractUIEvent getAbstractUIEvent(
      final UIComponent parent,
      final EventBehavior eventBehavior) {
    return (AbstractUIEvent) parent.getChildren().stream()
        .filter(child -> child instanceof AbstractUIEvent)
        .filter(child -> Objects.equals(child.getId(), eventBehavior.getFor()))
        .findFirst().orElse(null);
  }

  @Deprecated(since = "5.0.0", forRemoval = true)
  private static CommandMap getCommandMap(
      final FacesContext facesContext,
      final ClientBehaviorContext clientBehaviorContext, final ClientBehavior clientBehavior) {
    if (clientBehavior instanceof ClientBehaviorBase) {
      String type = ((ClientBehaviorBase) clientBehavior).getRendererType();

      // this is to use a different renderer for Tobago components and other components.
      if (type.equals(AjaxBehavior.BEHAVIOR_ID)) {
        type = "org.apache.myfaces.tobago.behavior.Ajax";
      }
      final ClientBehaviorRenderer renderer = facesContext.getRenderKit().getClientBehaviorRenderer(type);
      final String dummy = renderer.getScript(clientBehaviorContext, clientBehavior);
      if (dummy != null) {
        return CommandMap.restoreCommandMap(facesContext);
      }
    } else {
      LOG.warn("Ignoring: '{}'", clientBehavior);
    }
    return null;
  }

  public static void decodeClientBehaviors(final FacesContext facesContext, final UIComponent component) {
    if (component instanceof ClientBehaviorHolder) {
      final ClientBehaviorHolder clientBehaviorHolder = (ClientBehaviorHolder) component;
      final Map<String, List<ClientBehavior>> clientBehaviors = clientBehaviorHolder.getClientBehaviors();
      if (clientBehaviors != null && !clientBehaviors.isEmpty()) {
        final Map<String, String> paramMap = facesContext.getExternalContext().getRequestParameterMap();
        final String behaviorEventName = paramMap.get(ClientBehaviorContext.BEHAVIOR_EVENT_PARAM_NAME);
        if (behaviorEventName != null) {
          final List<ClientBehavior> clientBehaviorList = clientBehaviors.get(behaviorEventName);
          if (clientBehaviorList != null && !clientBehaviorList.isEmpty()) {
            final String clientId = paramMap.get(ClientBehaviorContext.BEHAVIOR_SOURCE_PARAM_NAME);
            if (component.getClientId(facesContext).equals(clientId)) {
              for (final ClientBehavior clientBehavior : clientBehaviorList) {
                clientBehavior.decode(facesContext, component);
              }
            }
          }
        }
      }
    }
  }

  public static List<UIComponent> getFacetChildren(UIComponent facet) {
    if (facet instanceof UIPanel) {
      return facet.getChildren();
    } else {
      return Collections.singletonList(facet);
    }
  }
}
