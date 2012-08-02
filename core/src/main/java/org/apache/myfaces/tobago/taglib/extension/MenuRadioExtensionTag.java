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

package org.apache.myfaces.tobago.taglib.extension;

import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.apt.annotation.ExtensionTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.taglib.component.AbstractCommandTagDeclaration;
import org.apache.myfaces.tobago.taglib.component.MenuCommandTag;
import org.apache.myfaces.tobago.taglib.component.SelectOneRadioTag;
import org.apache.myfaces.tobago.taglib.decl.HasConverter;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasLabel;
import org.apache.myfaces.tobago.taglib.decl.HasValue;
import org.apache.myfaces.tobago.taglib.decl.IsDisabled;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.FacetTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/*
 * Date: 09.05.2006
 * Time: 17:41:39
 */

/**
 * Renders a submenu with select one items (like a radio button).
 */

@Tag(name = "menuRadio", tagExtraInfoClassName = "org.apache.myfaces.tobago.taglib.component.CommandTagExtraInfo")
@ExtensionTag(baseClassName = "org.apache.myfaces.tobago.taglib.component.MenuRadioTag")
public class MenuRadioExtensionTag extends BodyTagSupport implements AbstractCommandTagDeclaration,
    HasIdBindingAndRendered, HasLabel, IsDisabled, HasValue, HasConverter {

  private String rendered;
  private String value;

  private MenuCommandTag menuCommandTag;
  private SelectOneRadioTag selectOneRadio;
  private FacetTag facetTag;
  private String action;
  private String actionListener;
  private String onclick;
  private String link;
  private String resource;
  private String jsfResource;
  private String disabled;
  private String binding;
  private String label;
  private String immediate;
  private String transition;
  private String converter;

  @Override
  public int doStartTag() throws JspException {

    menuCommandTag = new MenuCommandTag();
    menuCommandTag.setPageContext(pageContext);
    menuCommandTag.setParent(getParent());

    if (rendered != null) {
      menuCommandTag.setRendered(rendered);
    }
    if (action != null) {
      menuCommandTag.setAction(action);
    }
    if (actionListener != null) {
      menuCommandTag.setActionListener(actionListener);
    }
    if (onclick != null) {
      menuCommandTag.setOnclick(onclick);
    }
    if (link != null) {
      menuCommandTag.setLink(link);
    }
    if (resource != null) {
      menuCommandTag.setResource(resource);
    }
    if (jsfResource != null) {
      menuCommandTag.setJsfResource(jsfResource);
    }
    if (disabled != null) {
      menuCommandTag.setDisabled(disabled);
    }
    if (binding != null) {
      menuCommandTag.setBinding(binding);
    }
    if (label != null) {
      menuCommandTag.setLabel(label);
    }
    if (immediate != null) {
      menuCommandTag.setImmediate(immediate);
    }
    if (transition != null) {
      menuCommandTag.setTransition(transition);
    }
    menuCommandTag.doStartTag();

    facetTag = new FacetTag();
    facetTag.setPageContext(pageContext);
    facetTag.setParent(menuCommandTag);
    facetTag.setName(org.apache.myfaces.tobago.TobagoConstants.FACET_ITEMS);

    facetTag.doStartTag();
    selectOneRadio = new SelectOneRadioTag();
    selectOneRadio.setPageContext(pageContext);
    selectOneRadio.setParent(facetTag);
    if (converter != null) {
      selectOneRadio.setConverter(converter);
    }
    if (value != null) {
      selectOneRadio.setValue(value);
    }
    selectOneRadio.doStartTag();

    return super.doStartTag();
  }

  @Override
  public int doEndTag() throws JspException {

    // Move attribute renderedPartially from selectOne to menuCommand component
    UIComponent selectOneComponent = selectOneRadio.getComponentInstance();
    UICommand command = (UICommand) menuCommandTag.getComponentInstance();
    ValueBinding binding = selectOneComponent.getValueBinding(TobagoConstants.ATTR_RENDERED_PARTIALLY);
    if (binding != null) {
      command.setValueBinding(TobagoConstants.ATTR_RENDERED_PARTIALLY, binding);
    } else {
      Object renderedPartially = selectOneComponent.getAttributes().get(TobagoConstants.ATTR_RENDERED_PARTIALLY);
      ComponentUtil.setRenderedPartially(command, (String) renderedPartially);
    }
    
    selectOneRadio.doEndTag();
    facetTag.doEndTag();
    menuCommandTag.doEndTag();

    return super.doEndTag();
  }

  public void setAction(String action) {
    this.action = action;
  }

  public void setActionListener(String actionListener) {
    this.actionListener = actionListener;
  }

  public void setOnclick(String onclick) {
    this.onclick = onclick;
  }

  public void setLink(String navigate) {
    this.link = navigate;
  }

  public void setResource(String resource) {
    this.resource = resource;
  }

  public void setJsfResource(String jsfResource) {
    this.jsfResource = jsfResource;
  }

  public void setBinding(String binding) throws JspException {
    this.binding = binding;
  }

  public void setRendered(String rendered) {
    this.rendered = rendered;
  }

  public void setDisabled(String disabled) {
    this.disabled = disabled;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public void setImmediate(String immediate) {
    this.immediate = immediate;
  }

  public void setTransition(String transition) {
    this.transition = transition;
  }

  public void setConverter(String converter) {
    this.converter = converter;
  }

  @Override
  public void release() {
    super.release();
    rendered = null;
    value = null;
    action = null;
    actionListener = null;
    onclick = null;
    link = null;
    resource = null;
    jsfResource = null;
    disabled = null;
    binding = null;
    label = null;
    immediate = null;
    transition = null;
    converter = null;
    menuCommandTag = null;
    facetTag = null;
    selectOneRadio = null;
  }

}
