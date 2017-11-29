package org.apache.myfaces.tobago.internal.context;

import org.apache.myfaces.tobago.internal.util.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.io.Serializable;

public class Nonce implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(Nonce.class);

  private static final String KEY = Nonce.class.getName();

  private Nonce() {
  }

  public static String getNonce(final FacesContext facesContext) {
    final UIViewRoot viewRoot = facesContext.getViewRoot();
    String nonce = (String) viewRoot.getViewMap().get(KEY);
    if (nonce == null) {
      nonce = RandomUtils.nextString();
      LOG.debug("Creating nonce='{}'", nonce);
      viewRoot.getViewMap().put(KEY, nonce);
    }
    return nonce;
  }
}
