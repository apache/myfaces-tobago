/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Dec 3, 2002 at 2:04:26 PM.
 * $Id$
 */
package com.atanion.tobago.component;

import com.atanion.tobago.el.ValueBindingImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.ValueHolder;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CopyUtil {

  private static final Log LOG = LogFactory.getLog(CopyUtil.class);

  public static UIComponent copy(UIComponent source, String valueRefPrefix,
      String idPrefix) {

    try {
      UIComponent destination;

      destination = (UIComponent) source.getClass().newInstance();
      destination.setId(source.getId());
      destination.setParent(source.getParent());
      destination.setRendered(source.isRendered());
      destination.setRendererType(source.getRendererType());

      // this may not be nice, because it works possibly only with out implementation of ValueBinding
      if (source.getValueBinding("value") != null) {
//        String newReference = valueRefPrefix
//            + source.getValueBinding("value").getExpressionString();
//        if (LOG.isDebugEnabled()) {
//          LOG.debug("newReference = '" + newReference + "'");
//        }
//        ValueBinding valueBinding = new ValueBindingImpl(newReference);
//        destination.setValueBinding("value", valueBinding);
        destination.setValueBinding("value", source.getValueBinding("value"));
      } else if (source instanceof ValueHolder) {
        ((ValueHolder)destination).setValue(((ValueHolder)source).getValue());
      }

      if (source instanceof UIParameter) {
        ((UIParameter)destination).setName(((UIParameter)source).getName());
      }

      if (source instanceof UICommand) {

        MethodBinding action = ((UICommand)source).getAction();
        ((UICommand)destination).setAction(action);

//        ((UICommand)destination).removeActionListener(((UICommand)destination).getActionListeners()[0]);
        LOG.debug("length of sourcelist " + ((UICommand)source).getActionListeners().length);
        LOG.debug("length of dest list " + ((UICommand)destination).getActionListeners().length);

//        ActionListener[] actionListeners = ((UICommand) source).getActionListeners();
//        for (int i = 0; i < actionListeners.length; i++) {
//          LOG.debug("copy actionListener: " + actionListeners[i]);
//          ((UICommand) destination).addActionListener(actionListeners[i]);
//        }

        MethodBinding actionListener = ((UICommand)source).getActionListener();
        if (actionListener != null) {
          LOG.debug("copy special: " + actionListener.getExpressionString());
        }
        ((UICommand)destination).setActionListener(actionListener);

        LOG.debug("length of dest list " + ((UICommand)destination).getActionListeners().length);
      }

      // copy attributes
      Map oldAttributes = source.getAttributes();
      Map newAttributes = destination.getAttributes();
      Iterator names = oldAttributes.keySet().iterator();
      while (names.hasNext()) {
        String name = (String) names.next();
        newAttributes.put(name, oldAttributes.get(name));
      }

      // parent
      destination.setParent(null);

      // id
      if (source.getId() != null) {
        destination.setId(idPrefix + source.getId());
      }

      // children
      copyChildren(source, destination, valueRefPrefix, idPrefix);

      return destination;

    } catch (Exception e) {
      String error = "Cannot copy UIComponent: id='" + source.getId() +
          "' component='" + source + "'";
      LOG.error(error, e);
      throw new RuntimeException(error, e);
    }
  }

  protected static void copyChildren(UIComponent source, UIComponent destination,
      String valueRefPrefix, String idPrefix) {
    Iterator kids = source.getChildren().iterator();
    List destinationKids = destination.getChildren();
    while (kids.hasNext()) {
      UIComponent kid = (UIComponent) kids.next();
      UIComponent copy = copy(kid, valueRefPrefix, idPrefix);
      destinationKids.add(copy);
    }
  }

}
