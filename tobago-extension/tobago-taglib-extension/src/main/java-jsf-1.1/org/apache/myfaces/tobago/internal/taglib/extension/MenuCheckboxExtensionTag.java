package org.apache.myfaces.tobago.internal.taglib.extension;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.tobago.apt.annotation.ExtensionTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.internal.component.UICommandBase;
import org.apache.myfaces.tobago.internal.taglib.MenuCommandTag;
import org.apache.myfaces.tobago.internal.taglib.SelectBooleanCheckboxTag;
import org.apache.myfaces.tobago.internal.taglib.declaration.AbstractCommandTagDeclaration;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasBooleanValue;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabel;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDisabled;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.FacetTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Renders a menu item like a check box.
 * <pre>
 * &lt;tx:menuCheckbox/></pre>
 * is the short form of
 * <pre>
 * &lt;tc:menuCommand>
 *   &lt;f:facet name="checkbox">
 *     &lt;tc:selectBooleanCheckbox/>
 *   &lt;/f:facet>
 * &lt;/tc:menuCommand></pre>
 */
@Tag(name = "menuCheckbox", tagExtraInfoClassName = "org.apache.myfaces.tobago.internal.taglib.component.CommandTagExtraInfo")
@ExtensionTag(baseClassName = "org.apache.myfaces.tobago.internal.taglib.component.MenuCheckboxTag")
public class MenuCheckboxExtensionTag extends BodyTagSupport implements AbstractCommandTagDeclaration,
    HasIdBindingAndRendered, IsDisabled, HasBooleanValue, HasLabel {
  private String rendered;
  private String value;

  private MenuCommandTag menuCommandTag;
  private SelectBooleanCheckboxTag selectBooleanCheckbox;
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
  private String target;
  private String transition;
  private String renderedPartially;

  @Override
  public int doStartTag() throws JspException {

    menuCommandTag = new MenuCommandTag();
    menuCommandTag.setPageContext(pageContext);
    menuCommandTag.setParent(getParent()); // ???

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
    if (target != null) {
      menuCommandTag.setTarget(target);
    }
    if (transition != null) {
      menuCommandTag.setTransition(transition);
    }
    if (renderedPartially != null) {
      menuCommandTag.setRenderedPartially(renderedPartially);
    }
    menuCommandTag.doStartTag();

    facetTag = new FacetTag();
    facetTag.setPageContext(pageContext);
    facetTag.setParent(menuCommandTag);
    facetTag.setName(Facets.CHECKBOX);

    facetTag.doStartTag();
    selectBooleanCheckbox = new SelectBooleanCheckboxTag();
    selectBooleanCheckbox.setPageContext(pageContext);
    if (value != null) {
      selectBooleanCheckbox.setValue(value);
    }
    selectBooleanCheckbox.setParent(facetTag);
    selectBooleanCheckbox.doStartTag();
    return super.doStartTag();
  }

  @Override
  public int doEndTag() throws JspException {

    if (renderedPartially == null) {
      // Move attribute renderedPartially from selectOne to menuCommand component
      UIComponent selectBooleanComponent = selectBooleanCheckbox.getComponentInstance();
      UICommandBase command = (UICommandBase) menuCommandTag.getComponentInstance();
      ValueBinding binding = selectBooleanComponent.getValueBinding(Attributes.RENDERED_PARTIALLY);
      if (binding != null) {
        command.setValueBinding(Attributes.RENDERED_PARTIALLY, binding);
      } else {
        Object renderedPartially = selectBooleanComponent.getAttributes().get(Attributes.RENDERED_PARTIALLY);
        command.setRenderedPartially(StringUtils.split((String) renderedPartially, ", "));
      }
    }

    selectBooleanCheckbox.doEndTag();
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

  public void setTarget(String target) {
    this.target = target;
  }

  public void setTransition(String transition) {
    this.transition = transition;
  }

  public void setRenderedPartially(String renderedPartially) {
    this.renderedPartially = renderedPartially;
  }

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
    target = null;
    transition = null;
    renderedPartially = null;
    menuCommandTag = null;
    facetTag = null;
    selectBooleanCheckbox = null;
  }

}
