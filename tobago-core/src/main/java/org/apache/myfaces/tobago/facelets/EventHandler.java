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

package org.apache.myfaces.tobago.facelets;

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.ClientBehaviors;
import org.apache.myfaces.tobago.internal.behavior.EventBehavior;
import org.apache.myfaces.tobago.internal.component.AbstractUIEvent;

import jakarta.el.MethodExpression;
import jakarta.faces.component.PartialStateHolder;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.behavior.ClientBehaviorHolder;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AbortProcessingException;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.AjaxBehaviorListener;
import jakarta.faces.view.BehaviorHolderAttachedObjectHandler;
import jakarta.faces.view.facelets.ComponentConfig;
import jakarta.faces.view.facelets.ComponentHandler;
import jakarta.faces.view.facelets.FaceletContext;
import jakarta.faces.view.facelets.TagAttribute;
import jakarta.faces.view.facelets.TagAttributeException;
import jakarta.faces.view.facelets.TagException;

import java.io.IOException;

/**
 * This tag creates an instance of AjaxBehavior, and associates it with the nearest
 * parent UIComponent that implements ClientBehaviorHolder interface. This tag can
 * be used on single or composite components.
 * <p>
 * Unless otherwise specified, all attributes accept static values or EL expressions.
 * </p>
 * <p>
 * According to the documentation, the tag handler implementing this tag should meet
 * the following conditions:
 * </p>
 * <ul>
 * <li>Since this tag attach objects to UIComponent instances, and those instances
 * implements Behavior interface, this component should implement
 * BehaviorHolderAttachedObjectHandler interface.</li>
 * <li>f:ajax does not support binding property. In theory we should do something similar
 * to f:convertDateTime tag does: extends from ConverterHandler and override setAttributes
 * method, but in this case BehaviorTagHandlerDelegate has binding property defined, so
 * if we extend from BehaviorHandler we add binding support to f:ajax.</li>
 * <li>This tag works as a attached object handler, but note on the api there is no component
 * to define a target for a behavior. See comment inside apply() method.</li>
 * </ul>
 *
 * @since 3.0.0
 */
public class EventHandler extends TobagoComponentHandler implements BehaviorHolderAttachedObjectHandler {

  public static final Class<?>[] AJAX_BEHAVIOR_LISTENER_SIG = new Class<?>[]{AjaxBehaviorEvent.class};

  private final TagAttribute event;

// todo (see original AjaxHandler impl)  private final boolean _wrapMode;

  public EventHandler(final ComponentConfig config) {
    super(config);
    event = getAttribute(Attributes.event.getName());
  }

  @Override
  public void apply(final FaceletContext ctx, final UIComponent parent)
      throws IOException {

    super.apply(ctx, parent);

    //Apply only if we are creating a new component
    if (!ComponentHandler.isNew(parent)) {
      return;
    }
    if (parent instanceof ClientBehaviorHolder) {
      //Apply this handler directly over the parent
      applyAttachedObject(ctx.getFacesContext(), parent);
//todo      } else if (UIComponent.isCompositeComponent(parent)) {
//todo        FaceletCompositionContext mctx = FaceletCompositionContext.getCurrentInstance(ctx);
      // It is supposed that for composite components, this tag should
      // add itself as a target, but note that on whole api does not exists
      // some tag that expose client behaviors as targets for composite
      // components. In RI, there exists a tag called composite:clientBehavior,
      // but does not appear on spec or javadoc, maybe because this could be
      // understand as an implementation detail, after all there exists a key
      // called AttachedObjectTarget.ATTACHED_OBJECT_TARGETS_KEY that could be
      // used to create a tag outside JSF implementation to attach targets.
//todo        mctx.addAttachedObjectHandler(parent, this);
    } else {
      throw new TagException(this.tag,
          "Parent is not composite component or of type ClientBehaviorHolder, type is: "
              + parent);
    }
  }

  /**
   * ViewDeclarationLanguage.retargetAttachedObjects uses it to check
   * if the the target to be processed is applicable for this handler
   */
  @Override
  public String getEventName() {
    if (event == null) {
      return null;
    } else {
      return event.getValue();
    }
  }

  /**
   * This method should create an AjaxBehavior object and attach it to the
   * parent component.
   * <p>
   * Also, it should check if the parent can apply the selected AjaxBehavior
   * to the selected component through ClientBehaviorHolder.getEventNames() or
   * ClientBehaviorHolder.getDefaultEventName()
   */
  @Override
  public void applyAttachedObject(final FacesContext context, final UIComponent parent) {
    // Retrieve the current FaceletContext from FacesContext object
    final FaceletContext faceletContext = (FaceletContext) context
        .getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);

    final ClientBehaviorHolder clientBehaviorHolder = (ClientBehaviorHolder) parent;
    final UIComponent lastChild = parent.getChildren().stream().skip(parent.getChildCount() - 1)
        .findFirst().orElse(null);
    final AbstractUIEvent abstractUIEvent = lastChild instanceof AbstractUIEvent ? (AbstractUIEvent) lastChild : null;

    if (abstractUIEvent != null) {
      String eventName = getEventName();
      if (eventName == null) {
        eventName = clientBehaviorHolder.getDefaultEventName();
        if (eventName == null) {
          throw new TagAttributeException(event, "eventName could not be defined for f:ajax tag with no wrap mode.");
        }
      } else if (!clientBehaviorHolder.getEventNames().contains(eventName)) {
        throw new TagAttributeException(event, "event it is not a valid eventName defined for this component");
      }

      final EventBehavior eventBehavior = createBehavior(context);
      eventBehavior.setFor(abstractUIEvent.getId());

      clientBehaviorHolder.addClientBehavior(eventName, eventBehavior);
    }
  }

  protected EventBehavior createBehavior(final FacesContext context) {
    return (EventBehavior) context.getApplication().createBehavior(EventBehavior.BEHAVIOR_ID);
  }

  @Override
  public void onComponentCreated(
      final FaceletContext faceletContext, final UIComponent component, final UIComponent parent) {
    super.onComponentCreated(faceletContext, component, parent);

    final AbstractUIEvent uiEvent = (AbstractUIEvent) component;
    if (uiEvent.getEvent() == null) {
      final ClientBehaviorHolder holder = (ClientBehaviorHolder) parent;
      uiEvent.setEvent(ClientBehaviors.getEnum(holder.getDefaultEventName()));
    }
  }

  /**
   * The documentation says this attribute should not be used since it is not
   * taken into account. Instead, getEventName is used on
   * ViewDeclarationLanguage.retargetAttachedObjects.
   */
  @Override
  public String getFor() {
    return null;
  }

  /**
   * Wraps a method expression in a AjaxBehaviorListener
   */
  public static final class AjaxBehaviorListenerImpl implements
      AjaxBehaviorListener, PartialStateHolder {
    private MethodExpression expression;
    private boolean transientBoolean;
    private boolean initialStateMarked;

    public AjaxBehaviorListenerImpl() {
    }

    public AjaxBehaviorListenerImpl(final MethodExpression expr) {
      expression = expr;
    }

    @Override
    public void processAjaxBehavior(final AjaxBehaviorEvent event)
        throws AbortProcessingException {
      expression.invoke(FacesContext.getCurrentInstance().getELContext(),
          new Object[]{event});
    }

    @Override
    public boolean isTransient() {
      return transientBoolean;
    }

    @Override
    public void restoreState(final FacesContext context, final Object state) {
      if (state == null) {
        return;
      }
      expression = (MethodExpression) state;
    }

    @Override
    public Object saveState(final FacesContext context) {
      if (initialStateMarked()) {
        return null;
      }
      return expression;
    }

    @Override
    public void setTransient(final boolean newTransientValue) {
      transientBoolean = newTransientValue;
    }

    @Override
    public void clearInitialState() {
      initialStateMarked = false;
    }

    @Override
    public boolean initialStateMarked() {
      return initialStateMarked;
    }

    @Override
    public void markInitialState() {
      initialStateMarked = true;
    }
  }
}
