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

package org.apache.myfaces.tobago.renderkit.html;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPopup;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.event.PopupActionListener;

import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.ValueHolder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ACTION_LINK;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ACTION_ONCLICK;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DEFAULT_COMMAND;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DISABLED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_POPUP_CLOSE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_RESOURCE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TARGET;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TRANSITION;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_CONFIRMATION;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_POPUP;

public class CommandRendererHelper {

  private static final Log LOG = LogFactory.getLog(CommandRendererHelper.class);

  private String onclick;
  private boolean disabled;
  private String href;
  private String target;

  public CommandRendererHelper(FacesContext facesContext, UICommand component) {
    initOnclick(facesContext, component, null);
  }

  public CommandRendererHelper(FacesContext facesContext, UICommand component, Tag tag) {
    initOnclick(facesContext, component, tag);
  }

  private void initOnclick(FacesContext facesContext, UICommand command, Tag tag) {

    disabled = ComponentUtil.getBooleanAttribute(command, ATTR_DISABLED);
    href = getEmptyHref(facesContext);

    if (disabled) {
      onclick = "";
      href = "";
    } else {

      UIPopup popup = (UIPopup) command.getFacet(FACET_POPUP);
      if (popup != null) {
        if (!ComponentUtil.containsPopupActionListener(command)) {
          command.addActionListener(new PopupActionListener(popup));
        }
      }

      String clientId = command.getClientId(facesContext);
      boolean defaultCommand = ComponentUtil.getBooleanAttribute(command, ATTR_DEFAULT_COMMAND);
      boolean transition = ComponentUtil.getBooleanAttribute(command, ATTR_TRANSITION);

      if (command.getAttributes().get(ATTR_ACTION_LINK) != null 
          || command.getAttributes().get(ATTR_RESOURCE) != null) {
        String url = generateUrl(facesContext, command);
        if (tag == Tag.ANCHOR) {
          onclick = null;
          href = url;
          target = ComponentUtil.getStringAttribute(command, ATTR_TARGET);
        } else {
          onclick = "Tobago.navigateToUrl('" + url + "');";
        }
      } else if (command.getAttributes().get(ATTR_ACTION_ONCLICK) != null) {
        onclick = prepareOnClick(facesContext, command);
      } else if (command instanceof org.apache.myfaces.tobago.component.UICommand
          && ((org.apache.myfaces.tobago.component.UICommand) command).getRenderedPartially().length > 0) {

        String[] componentId = ((org.apache.myfaces.tobago.component.UICommand) command).getRenderedPartially();

        if (componentId != null && componentId.length == 1) {
          // TODO find a better way
          boolean popupAction = ComponentUtil.containsPopupActionListener(command);
          if (popupAction) {
            onclick = "Tobago.openPopupWithAction2(this, '"
                + HtmlRendererUtil.getComponentId(facesContext, command, componentId[0])
                + "', '" + clientId + "', null)";
          } else {
            onclick = "Tobago.reloadComponent2(this, '"
                + HtmlRendererUtil.getComponentId(facesContext, command, componentId[0])
                + "','" + clientId + "', {});";
          }
        } else {
          LOG.error("more than one partially rendered component is currently not supported "
              + Arrays.toString(componentId));
          onclick = "Tobago.submitAction2(this, '" + clientId + "', " + transition + ", null);";
        }

      } else if (defaultCommand) {
        ComponentUtil.findPage(facesContext, command).setDefaultActionId(clientId);
        onclick = null;
      } else {
        String target = ComponentUtil.getStringAttribute(command, ATTR_TARGET);
        if (target == null) {
          onclick = "Tobago.submitAction2(this, '" + clientId + "', " + transition + ", null);";
        } else {
          onclick = "Tobago.submitAction2(this, '" + clientId + "', " + transition + ", '" + target + "');";
        }
      }

      if (command.getAttributes().get(ATTR_POPUP_CLOSE) != null
          && ComponentUtil.isInPopup(command)) {
        String value = (String) command.getAttributes().get(ATTR_POPUP_CLOSE);
        if (value.equals("immediate")) {
          onclick = "Tobago.closePopup(this);";
        } else if (value.equals("afterSubmit")
            && command instanceof org.apache.myfaces.tobago.component.UICommand
            && ((org.apache.myfaces.tobago.component.UICommand) command).getRenderedPartially().length > 0) {
          onclick += "Tobago.closePopup(this);";
        }

      }

      onclick = appendConfirmationScript(onclick, command);
    }
  }

  private String getEmptyHref(FacesContext facesContext) {
    ClientProperties clientProperties = ClientProperties.getInstance(facesContext);
    return clientProperties.getUserAgent().isMsie() ? "#" : "javascript:;";
  }

  private String prepareOnClick(FacesContext facesContext, UIComponent component) {
    String onclick;
    onclick = (String) component.getAttributes().get(ATTR_ACTION_ONCLICK);
    if (onclick.contains("@autoId")) {
      onclick = StringUtils.replace(onclick, "@autoId", component.getClientId(facesContext));
    }
    return onclick;
  }

  private String appendConfirmationScript(String onclick, UIComponent component) {
    ValueHolder confirmation = (ValueHolder) component.getFacet(FACET_CONFIRMATION);
    if (confirmation != null) {
      StringBuilder script = new StringBuilder();
      script.append("return confirm('");
      script.append(confirmation.getValue());
      script.append("')");
      if (onclick != null) {
        script.append(" && ");
        script.append(onclick);
      }
      onclick = script.toString();
    }
    return onclick;
  }

  private String generateUrl(FacesContext facesContext, UIComponent component) {
    String url;
    Application application = facesContext.getApplication();
    ViewHandler viewHandler = application.getViewHandler();
    ExternalContext externalContext = facesContext.getExternalContext();

    if (component.getAttributes().get(ATTR_RESOURCE) != null) {
      String resource = (String) component.getAttributes().get(ATTR_RESOURCE);
      boolean jsfResource = ComponentUtil.getBooleanAttribute(component, TobagoConstants.ATTR_JSF_RESOURCE);
      url = ResourceManagerUtil.getPageWithoutContextPath(facesContext, resource);
      if (url != null) {
        if (jsfResource) {
          url = viewHandler.getActionURL(facesContext, url);
          url = externalContext.encodeActionURL(url);
        } else {
          url = viewHandler.getResourceURL(facesContext, url);
          url = externalContext.encodeResourceURL(url);
        }
      } else {
        url = "";
      }
    } else if (component.getAttributes().get(ATTR_ACTION_LINK) != null) {

      final String link = (String) component.getAttributes().get(ATTR_ACTION_LINK);
      if (link.startsWith("/")) { // internal absolute link
        url = externalContext.encodeResourceURL(externalContext.getRequestContextPath() + link);
      } else if (org.apache.myfaces.tobago.util.StringUtils.isUrl(link)) { // external link
        url = link;
      } else { // internal relative link
        url = externalContext.encodeResourceURL(link);
      }

      StringBuilder builder = new StringBuilder(url);
      boolean firstParameter = !url.contains("?");
      for (UIComponent child : (List<UIComponent>) component.getChildren()) {
        if (child instanceof UIParameter) {
          UIParameter parameter = (UIParameter) child;
          if (firstParameter) {
            builder.append("?");
            firstParameter = false;
          } else {
            builder.append("&");
          }
          builder.append(parameter.getName());
          builder.append("=");
          Object value = parameter.getValue();
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
      url = builder.toString();
    } else {
      throw new AssertionError("Needed " + ATTR_ACTION_LINK + " or " + ATTR_RESOURCE);
    }

    return url;
  }


  public String getOnclick() {
    return onclick;
  }

  public String getOnclickDoubleQuoted() {
    return onclick.replace('\'', '\"');
  }

  public boolean isDisabled() {
    return disabled;
  }

  public String getHref() {
    return href;
  }

  public String getTarget() {
    return target;
  }

  public static enum Tag {
    ANCHOR, BUTTON
  }
}
