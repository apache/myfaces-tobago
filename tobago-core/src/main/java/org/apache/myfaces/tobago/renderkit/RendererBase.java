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

package org.apache.myfaces.tobago.renderkit;

import org.apache.myfaces.tobago.component.ClientBehaviors;
import org.apache.myfaces.tobago.internal.behavior.EventBehavior;
import org.apache.myfaces.tobago.internal.component.AbstractUICommand;
import org.apache.myfaces.tobago.internal.component.AbstractUIEvent;
import org.apache.myfaces.tobago.internal.component.AbstractUIReload;
import org.apache.myfaces.tobago.internal.renderkit.Collapse;
import org.apache.myfaces.tobago.internal.renderkit.Command;
import org.apache.myfaces.tobago.internal.renderkit.CommandMap;
import org.apache.myfaces.tobago.internal.util.RenderUtils;
import org.apache.myfaces.tobago.internal.webapp.TobagoResponseWriterWrapper;
import org.apache.myfaces.tobago.renderkit.html.CustomAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.render.ClientBehaviorRenderer;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;

public class RendererBase extends Renderer {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  protected String getCurrentValue(final FacesContext facesContext, final UIComponent component) {

    if (component instanceof ValueHolder) {
      final ValueHolder valueHolder = (ValueHolder) component;
      if (valueHolder instanceof EditableValueHolder) {
        final EditableValueHolder editableValueHolder = (EditableValueHolder) component;
        final Object submittedValue = editableValueHolder.getSubmittedValue();
        if (submittedValue != null || !editableValueHolder.isValid()) {
          return (String) submittedValue;
        }
      }
      String currentValue = null;
      final Object result = ((ValueHolder) component).getValue();
      if (result != null) {
        currentValue = ComponentUtils.getFormattedValue(facesContext, component, result);
      }
      return currentValue;
    } else {
      return null;
    }
  }

  @Override
  public Object getConvertedValue(final FacesContext context, final UIComponent component, final Object submittedValue)
      throws ConverterException {
    if (!(submittedValue instanceof String)) {
      return submittedValue;
    }
    final Converter converter = ComponentUtils.getConverter(context, component, submittedValue);
    if (converter != null) {
      return converter.getAsObject(context, component, (String) submittedValue);
    } else {
      return submittedValue;
    }
  }

  public void onComponentCreated(
      final FacesContext facesContext, final UIComponent component, final UIComponent parent) {
  }

  protected TobagoResponseWriter getResponseWriter(final FacesContext facesContext) {
    final ResponseWriter writer = facesContext.getResponseWriter();
    if (writer instanceof TobagoResponseWriter) {
      return (TobagoResponseWriter) writer;
    } else {
      return new TobagoResponseWriterWrapper(writer);
    }
  }

  /**
   * Special implementation for the reload facet (e.g. for tc:panel and tc:sheet.
   */
  public void encodeReload(FacesContext facesContext, AbstractUIReload reload) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.write("{\"reload\":{\"frequency\":" + reload.getFrequency() + "}}");
  }

  /**
   * Renders the tobago-behavior tag.
   *
   * @since 5.0
   */
  protected void encodeBehavior(
      final TobagoResponseWriter writer, final FacesContext facesContext, final ClientBehaviorHolder holder)
      throws IOException {
    if (holder != null) {
      final CommandMap behaviorCommands = getBehaviorCommands(facesContext, holder);
      encodeBehavior(writer, behaviorCommands);
    }
  }

  /**
   * Renders the tobago-behavior tag.
   *
   * @since 5.0
   */
  protected void encodeBehavior(
      final TobagoResponseWriter writer, final CommandMap behaviorCommands)
      throws IOException {
    if (behaviorCommands != null) {
      final Command click = behaviorCommands.getClick();
      if (click != null) {
        encodeBehavior(writer, ClientBehaviors.click, click);
      }
      final Map<ClientBehaviors, Command> other = behaviorCommands.getOther();
      if (other != null) {
        for (Map.Entry<ClientBehaviors, Command> entry : other.entrySet()) {
          encodeBehavior(writer, entry.getKey(), entry.getValue());
        }
      }
    }
  }

  private void encodeBehavior(
      final TobagoResponseWriter writer, final ClientBehaviors behaviors, final Command command)
      throws IOException {
    writer.startElement(HtmlElements.TOBAGO_BEHAVIOR);
    writer.writeAttribute(CustomAttributes.EVENT, behaviors.name(), false);
    writer.writeAttribute(HtmlAttributes.ACTION, command.getAction(), false); // tbd: rename to actionId?
    writer.writeAttribute(CustomAttributes.EXECUTE, command.getExecute(), false);
    writer.writeAttribute(CustomAttributes.RENDER, command.getRender(), false);
    writer.writeAttribute(CustomAttributes.OMIT, command.getOmit());
    writer.writeAttribute(CustomAttributes.CONFIRMATION, command.getConfirmation(), true);
    writer.writeAttribute(CustomAttributes.DECOUPLED, command.getTransition() != null ? command.getTransition() : false);
    final Collapse collapse = command.getCollapse();
    if (collapse != null) {
      writer.writeAttribute(CustomAttributes.COLLAPSE_ACTION, collapse.getAction().name(), false);
      writer.writeAttribute(CustomAttributes.COLLAPSE_TARGET, collapse.getFor(), false);
    }
    writer.writeAttribute(CustomAttributes.DELAY, command.getDelay());
    writer.writeAttribute(CustomAttributes.FOCUS_ID, command.getFocus(), false);
    writer.writeAttribute(HtmlAttributes.TARGET, command.getTarget(), true);

    // todo: all the other attributes
    writer.endElement(HtmlElements.TOBAGO_BEHAVIOR);
  }

  protected CommandMap getBehaviorCommands(final FacesContext facesContext,
                                               final ClientBehaviorHolder clientBehaviorHolder) {
    CommandMap commandMap = null;

    for (final Map.Entry<String, List<ClientBehavior>> entry : clientBehaviorHolder.getClientBehaviors().entrySet()) {
      final String eventName = entry.getKey();
      final ClientBehaviorContext clientBehaviorContext
          = getClientBehaviorContext(facesContext, clientBehaviorHolder, eventName);

      for (final ClientBehavior clientBehavior : entry.getValue()) {
        if (clientBehavior instanceof EventBehavior) {
          final EventBehavior eventBehavior = (EventBehavior) clientBehavior;
          final AbstractUIEvent abstractUIEvent
              = RenderUtils.getAbstractUIEvent((UIComponent) clientBehaviorHolder, eventBehavior);

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
      commandMap.addCommand(ClientBehaviors.click, new Command(facesContext, (AbstractUICommand) clientBehaviorHolder));
    }

    return commandMap;
  }

  private static ClientBehaviorContext getClientBehaviorContext(final FacesContext facesContext,
                                                                final ClientBehaviorHolder clientBehaviorHolder, final String eventName) {
    UIComponent component = (UIComponent) clientBehaviorHolder;
    return ClientBehaviorContext.createClientBehaviorContext(facesContext, component, eventName,
        component.getClientId(facesContext), null);
  }

  private static CommandMap getCommandMap(final FacesContext facesContext,
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

  protected void decodeClientBehaviors(final FacesContext facesContext, final UIComponent component) {
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
              for (final ClientBehavior clientBehavior : clientBehaviorList) {
                clientBehavior.decode(facesContext, component);
              }
            }
          }
        }
      }
    }
  }

}
