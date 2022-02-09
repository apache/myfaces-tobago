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

package org.apache.myfaces.tobago.webapp;

import org.apache.commons.codec.binary.Base64;
import org.apache.myfaces.tobago.portlet.PortletUtils;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;

import javax.faces.context.FacesContext;
import javax.portlet.PortletSession;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Map;

public final class Secret implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final String KEY = Secret.class.getName();

  private static final SecureRandom RANDOM = new SecureRandom();

  private static final int SECRET_LENGTH = 16;

  private static final boolean COMMONS_CODEC_AVAILABLE = commonsCodecAvailable();

  private static boolean commonsCodecAvailable() {
    try {
      Base64.encodeBase64URLSafeString(new byte[0]);
      return true;
    } catch (final Error e) {
      return false;
    }
  }

  private String secret;

  private Secret() {
    final byte[] bytes = new byte[SECRET_LENGTH];
    RANDOM.nextBytes(bytes);
    secret = COMMONS_CODEC_AVAILABLE ? encodeBase64(bytes) : encodeHex(bytes);
  }

  private String encodeBase64(final byte[] bytes) {
    return Base64.encodeBase64URLSafeString(bytes);
  }

  private String encodeHex(final byte[] bytes) {
    final StringBuilder builder = new StringBuilder(SECRET_LENGTH * 2);
    for (final byte b : bytes) {
      builder.append(String.format("%02x", b));
    }
    return builder.toString();
  }

  /**
   * Checks that the request contains a parameter {@link org.apache.myfaces.tobago.webapp.Secret#KEY} which is equals to
   * a secret value in the session.
   */
  public static boolean check(final FacesContext facesContext) {
    final Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
    final String fromRequest = (String) requestParameterMap.get(Secret.KEY);
    final Object session = facesContext.getExternalContext().getSession(false);
    Secret secret = getSecret(session);
    return secret != null && secret.secret.equals(fromRequest);
  }

  private static Secret getSecret(Object session) {
    Secret secret = null;
    if (session != null) {
      if (session instanceof HttpSession) {
        secret = (Secret) ((HttpSession) session).getAttribute(Secret.KEY);
      } else if (PortletUtils.isPortletApiAvailable() && session instanceof PortletSession) {
        secret = (Secret) ((PortletSession) session).getAttribute(Secret.KEY, PortletSession.APPLICATION_SCOPE);
      } else {
        throw new IllegalArgumentException("Unknown session type: " + session);
      }
    }
    return secret;
  }

  /**
   * Encode a hidden field with the secret value from the session.
   */
  public static void encode(final FacesContext facesContext, final TobagoResponseWriter writer) throws IOException {
    writer.startElement(HtmlElements.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
    writer.writeAttribute(HtmlAttributes.NAME, Secret.KEY, false);
    writer.writeAttribute(HtmlAttributes.ID, Secret.KEY, false);
    final Object session = facesContext.getExternalContext().getSession(true);
    final Secret secret = getSecret(session);
    if (secret != null) {
      writer.writeAttribute(HtmlAttributes.VALUE, secret.secret, false);
    }
    writer.endElement(HtmlElements.INPUT);
  }

  /**
   * Create a secret attribute in the session. Should usually be called in a {@link
   * javax.servlet.http.HttpSessionListener}.
   */
  public static void create(final HttpSession session) {
    session.setAttribute(Secret.KEY, new Secret());
  }
}
