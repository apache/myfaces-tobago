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

package org.apache.myfaces.tobago.internal.renderkit.renderer;

import org.apache.myfaces.tobago.application.Toast;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUIReload;
import org.apache.myfaces.tobago.internal.component.AbstractUIStyle;
import org.apache.myfaces.tobago.internal.component.AbstractUIToasts;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.RenderUtils;
import org.apache.myfaces.tobago.layout.Placement;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.Arias;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlRoleValues;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.ResourceUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class ToastsRenderer<T extends AbstractUIToasts> extends RendererBase<T> {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final String SUFFIX_STATES = "states";

  @Override
  public void decodeInternal(final FacesContext facesContext, final T component) {
    super.decodeInternal(facesContext, component);

    final String clientId = component.getClientId(facesContext);
    final String statesInputId = clientId + ComponentUtils.SUB_SEPARATOR + SUFFIX_STATES;

    final Map<String, String> requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
    if (requestParameterMap.containsKey(statesInputId)) {
      final String json = requestParameterMap.get(statesInputId);
      final Map<String, StateData> states = decodeStates(json);
      final Collection<Toast> toasts = component.getValue();
      final List<String> toastIds = toasts.stream()
          .map(toast -> clientId + ComponentUtils.SUB_SEPARATOR + toast.getId())
          .collect(Collectors.toList());

      final List<String> idsForRemoval = new ArrayList<>();
      states.forEach((key, value) -> {
        if (value.state.equals(StateEnum.closed)
            || value.hideTime != null && value.hideTime.isBefore(LocalTime.now())
            || !toastIds.contains(key)) {
          idsForRemoval.add(key);
        }
      });

      toasts.removeIf(toast -> idsForRemoval.contains(clientId + ComponentUtils.SUB_SEPARATOR + toast.getId()));
      states.entrySet().removeIf(entry -> idsForRemoval.contains(entry.getKey()));

      component.setStates(states);
    }
  }

  private Map<String, StateData> decodeStates(final String input) {
    if (input == null) {
      return null;
    }
    String string = input.trim().replaceAll("&quot;", "\"");
    final Map<String, StateData> result = new HashMap<>();
    if (string.length() < 2 || string.charAt(0) != '{' || string.charAt(string.length() - 1) != '}') {
      LOG.warn("Can't parse: no surrounding curly brackets: '{}'", string);
    } else {
      string = string.substring(1, string.length() - 1);

      final StringTokenizer tokenizer = new StringTokenizer(string, "\",{}");

      String id = null;
      StateEnum state = null;
      LocalTime hideTime = null;

      while (tokenizer.hasMoreTokens()) {
        final String token = tokenizer.nextToken().trim();

        if (!token.equals(":") && !token.equals("state") && !token.equals("hideTime")) {
          if (id == null) {
            id = token;
          } else if (state == null) {
            state = StateEnum.valueOf(token);
          } else {
            if (!token.equals(":null")) {
              hideTime = LocalTime.parse(token);
            }

            result.put(id, new StateData(state, hideTime));
            id = null;
            state = null;
            hideTime = null;
          }
        }
      }
    }
    return result;
  }

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final String clientId = component.getClientId(facesContext);
    final Placement placement = component.getPlacement();
    final Integer disposeDelay = component.getDisposeDelay();
    final Markup markup = component.getMarkup() != null ? component.getMarkup() : Markup.NULL;
    final Map<String, StateData> states = component.getStates();

    insideBegin(facesContext, HtmlElements.TOBAGO_TOASTS);
    writer.startElement(HtmlElements.TOBAGO_TOASTS);
    writer.writeIdAttribute(clientId);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);

    final AbstractUIReload reload = ComponentUtils.getReloadFacet(component);
    if (reload != null) {
      reload.encodeAll(facesContext);
    }

    final Collection<Toast> toasts = component.getValue();
    final UIComponent header = ComponentUtils.getFacet(component, Facets.header);
    final List<UIComponent> children = component.getChildren();

    if (toasts.size() == component.getRowCount()) {
      int rowIndex = component.getFirst();
      for (Toast toast : toasts) {
        component.setRowIndex(rowIndex);
        if (!component.isRowAvailable()) {
          break;
        }

        final String toastId = clientId + ComponentUtils.SUB_SEPARATOR + toast.getId();

        if (!states.containsKey(toastId)) {
          states.put(toastId, new StateData(StateEnum.created, null));
        }
        final StateData stateData = states.get(toastId);

        writer.startElement(HtmlElements.DIV);
        writer.writeIdAttribute(toastId);
        writer.writeClassAttribute(
            BootstrapClass.TOAST,
            component.getCustomClass(),
            markup.contains(Markup.HIDE_CLOSE_BUTTON) ? TobagoClass.HIDE_CLOSE_BUTTON : null);
        writer.writeAttribute(HtmlAttributes.NAME, toastId, false);
        writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.ALERT.toString(), false);
        writer.writeAttribute(Arias.LIVE, "assertive", false);
        writer.writeAttribute(Arias.ATOMIC, true);
        writer.writeAttribute(DataAttributes.PLACEMENT, placement.name(), false);

        if (stateData.state.equals(StateEnum.created)) {
          writer.writeAttribute(DataAttributes.DISPOSE_DELAY, disposeDelay);
          if (disposeDelay > 0) {
            LocalTime hideTime = LocalTime.now().plus(disposeDelay, ChronoUnit.MILLIS);
            states.put(toastId, new StateData(StateEnum.created, hideTime));
          }
        } else if (stateData.state.equals(StateEnum.showed)) {
          if (stateData.hideTime != null) {
            int decreasedDisposeDelay = (int) LocalTime.now().until(stateData.hideTime, ChronoUnit.MILLIS);
            writer.writeAttribute(DataAttributes.DISPOSE_DELAY, Math.max(decreasedDisposeDelay, 0));

            if (decreasedDisposeDelay <= 0) {
              states.put(toastId, new StateData(StateEnum.closed, null));
            }
          } else {
            writer.writeAttribute(DataAttributes.DISPOSE_DELAY, disposeDelay);
          }
        }

        if (header != null) {
          writer.startElement(HtmlElements.DIV);
          writer.writeClassAttribute(BootstrapClass.TOAST_HEADER);
          insideBegin(facesContext, Facets.header);
          for (final UIComponent child : RenderUtils.getFacetChildren(header)) {
            child.encodeAll(facesContext);
          }
          encodeCloseButton(facesContext, writer);
          insideEnd(facesContext, Facets.header);
          writer.endElement(HtmlElements.DIV);
        }

        writer.startElement(HtmlElements.DIV);
        writer.writeClassAttribute(BootstrapClass.TOAST_BODY);
        if (header == null) {
          encodeCloseButton(facesContext, writer);
        }
        for (UIComponent child : children) {
          child.encodeAll(facesContext);
        }
        writer.endElement(HtmlElements.DIV);
        writer.endElement(HtmlElements.DIV);

        rowIndex++;
      }
    }

    component.setRowIndex(-1);

    final String statesInputId = clientId + ComponentUtils.SUB_SEPARATOR + SUFFIX_STATES;
    writer.startElement(HtmlElements.INPUT);
    writer.writeIdAttribute(statesInputId);
    writer.writeNameAttribute(statesInputId);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
    writer.writeAttribute(HtmlAttributes.VALUE, encodeStates(states), false);
    writer.endElement(HtmlElements.INPUT);
  }

  private void encodeCloseButton(final FacesContext facesContext, final TobagoResponseWriter writer)
      throws IOException {
    writer.startElement(HtmlElements.BUTTON);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.BUTTON);
    writer.writeClassAttribute(BootstrapClass.BTN_CLOSE, TobagoClass.CLOSE);
    writer.writeAttribute(DataAttributes.BS_DISMISS, "toast", false);
    writer.writeAttribute(Arias.LABEL, ResourceUtils.getString(facesContext, "toast.close"), false);
    writer.endElement(HtmlElements.BUTTON);
  }

  private String encodeStates(final Map<String, StateData> states) {
    if (states == null) {
      return null;
    }

    final StringBuilder builder = new StringBuilder();
    boolean firstEntry = true;

    builder.append("{");
    for (Map.Entry<String, StateData> entry : states.entrySet()) {
      if (firstEntry) {
        firstEntry = false;
      } else {
        builder.append(",");
      }

      builder.append("\"");
      builder.append(entry.getKey());
      builder.append("\":{\"state\":\"");
      builder.append(entry.getValue().state);
      builder.append("\",\"hideTime\":");

      if (entry.getValue().hideTime != null) {
        builder.append("\"");
        builder.append(entry.getValue().hideTime);
        builder.append("\"");
      } else {
        builder.append("null");
      }
      builder.append("}");
    }
    builder.append("}");

    return builder.toString();
  }

  @Override
  public void encodeEndInternal(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.TOBAGO_TOASTS);
    insideEnd(facesContext, HtmlElements.TOBAGO_TOASTS);
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  @Override
  public void encodeChildrenInternal(final FacesContext facesContext, final T component) throws IOException {
    for (final UIComponent child : component.getChildren()) {
      if (child instanceof AbstractUIStyle) {
        child.encodeAll(facesContext);
      }
    }
  }

  public static class StateData {
    private final StateEnum state;
    private final LocalTime hideTime;

    public StateData(StateEnum state, LocalTime hideTime) {
      this.state = state;
      this.hideTime = hideTime;
    }

    public StateEnum getState() {
      return state;
    }

    public LocalTime getHideTime() {
      return hideTime;
    }
  }

  private enum StateEnum {
    created, showed, closed
  }
}
