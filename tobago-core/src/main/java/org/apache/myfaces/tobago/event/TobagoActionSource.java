package org.apache.myfaces.tobago.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.ActionSource2;
import javax.faces.el.MethodBinding;

public interface TobagoActionSource extends ActionSource2 {

  Logger LOG = LoggerFactory.getLogger(TobagoActionSource.class);

  /**
   * @deprecated Replaced by ActionSource2.getActionExpression
   */
  @Override
  @Deprecated
  default MethodBinding getAction() {
    LOG.warn("No longer supported!");
    return null;
  }

  /**
   * @deprecated Replaced by ActionSource2.setActionExpression
   */
  @Override
  @Deprecated
  default void setAction(MethodBinding action) {
    LOG.warn("No longer supported!");
  }

  /**
   * @deprecated Replaced by getActionListeners
   */
  @Override
  @Deprecated
  default MethodBinding getActionListener() {
    LOG.warn("No longer supported!");
    return null;
  }

  /**
   * @deprecated Replaced by getActionListeners
   */
  @Override
  @Deprecated
  default void setActionListener(MethodBinding actionListener) {
    LOG.warn("No longer supported!");
  }
}
