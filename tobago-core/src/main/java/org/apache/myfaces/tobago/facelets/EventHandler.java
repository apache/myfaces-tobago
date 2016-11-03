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

import org.apache.myfaces.tobago.internal.behavior.EventBehavior;
import org.apache.myfaces.tobago.component.Attributes;

import javax.el.MethodExpression;
import javax.faces.component.PartialStateHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.AjaxBehaviorListener;
import javax.faces.view.BehaviorHolderAttachedObjectHandler;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagAttributeException;
import javax.faces.view.facelets.TagException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
 * @author Leonardo Uribe (latest modification by $Author: lu4242 $)
 * @version $Revision: 1177714 $ $Date: 2011-09-30 15:51:51 +0000 (Fri, 30 Sep 2011) $
 */
public class EventHandler extends TobagoComponentHandler implements BehaviorHolderAttachedObjectHandler {

  public final static Class<?>[] AJAX_BEHAVIOR_LISTENER_SIG = new Class<?>[]{AjaxBehaviorEvent.class};

  private final TagAttribute _event;

// todo (see original AjaxHandler impl)  private final boolean _wrapMode;

  public EventHandler(ComponentConfig config) {
    super(config);
    _event = getAttribute(Attributes.event.getName());
  }

  public void apply(FaceletContext ctx, UIComponent parent)
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
        // used to create a tag outside jsf implementation to attach targets.
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
  public String getEventName() {
    if (_event == null) {
      return null;
    } else {
      return _event.getValue();
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
  public void applyAttachedObject(FacesContext context, UIComponent parent) {
    // Retrieve the current FaceletContext from FacesContext object
    FaceletContext faceletContext = (FaceletContext) context
        .getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);

    // cast to a ClientBehaviorHolder
    ClientBehaviorHolder cvh = (ClientBehaviorHolder) parent;

    String eventName = getEventName();
    if (eventName == null) {
      eventName = cvh.getDefaultEventName();
      if (eventName == null) {
          throw new TagAttributeException(_event, "eventName could not be defined for f:ajax tag with no wrap mode.");
      }
    } else if (!cvh.getEventNames().contains(eventName)) {
        throw new TagAttributeException(_event, "event it is not a valid eventName defined for this component");
    }

    Map<String, List<ClientBehavior>> clientBehaviors = cvh.getClientBehaviors();

    List<ClientBehavior> clientBehaviorList = clientBehaviors.get(eventName);
    if (clientBehaviorList != null && !clientBehaviorList.isEmpty()) {
      for (ClientBehavior cb : clientBehaviorList) {
        if (cb instanceof AjaxBehavior) {
          // The most inner one has been applied, so according to
          // jsf 2.0 spec section 10.4.1.1 it is not necessary to apply
          // this one, because the inner one has precendece over
          // the outer one.
          return;
        }
      }
    }

    EventBehavior ajaxBehavior = createBehavior(context);

    cvh.addClientBehavior(eventName, ajaxBehavior);
  }

  protected EventBehavior createBehavior(FacesContext context) {
    return (EventBehavior) context.getApplication().createBehavior(EventBehavior.BEHAVIOR_ID);
  }

  /**
   * The documentation says this attribute should not be used since it is not
   * taken into account. Instead, getEventName is used on
   * ViewDeclarationLanguage.retargetAttachedObjects.
   */
  public String getFor() {
    return null;
  }

  /**
   * Wraps a method expression in a AjaxBehaviorListener
   */
  public final static class AjaxBehaviorListenerImpl implements
      AjaxBehaviorListener, PartialStateHolder {
    private MethodExpression _expr;
    private boolean _transient;
    private boolean _initialStateMarked;

    public AjaxBehaviorListenerImpl() {
    }

    public AjaxBehaviorListenerImpl(MethodExpression expr) {
      _expr = expr;
    }

    public void processAjaxBehavior(AjaxBehaviorEvent event)
        throws AbortProcessingException {
      _expr.invoke(FacesContext.getCurrentInstance().getELContext(),
          new Object[]{event});
    }

    public boolean isTransient() {
      return _transient;
    }

    public void restoreState(FacesContext context, Object state) {
      if (state == null) {
        return;
      }
      _expr = (MethodExpression) state;
    }

    public Object saveState(FacesContext context) {
      if (initialStateMarked()) {
        return null;
      }
      return _expr;
    }

    public void setTransient(boolean newTransientValue) {
      _transient = newTransientValue;
    }

    public void clearInitialState() {
      _initialStateMarked = false;
    }

    public boolean initialStateMarked() {
      return _initialStateMarked;
    }

    public void markInitialState() {
      _initialStateMarked = true;
    }
  }
}
