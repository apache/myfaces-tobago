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

package org.apache.myfaces.tobago.renderkit.html.standard.standard.tag;

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.SupportsMarkup;
import org.apache.myfaces.tobago.component.UIButton;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UIFileDrop;
import org.apache.myfaces.tobago.component.UILink;
import org.apache.myfaces.tobago.component.UIOut;
import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.internal.component.AbstractUIFile;
import org.apache.myfaces.tobago.internal.component.AbstractUIFileDrop.VisibleType;
import org.apache.myfaces.tobago.internal.component.AbstractUIPanel;
import org.apache.myfaces.tobago.layout.LayoutBase;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionListener;
import java.io.IOException;

public class FileDropRenderer extends FileRenderer {

  private static final String FILE_DROP_DATA_ATTRIBUTE_NAME = "tobago-file-drop";

  @Override
  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);
    UIFileDrop fileDrop = (UIFileDrop) component;
    if (fileDrop.getFacet("change") == null) {
      createActionFacet(facesContext, fileDrop);
    }
    VisibleType visibleType = getVisibleType(fileDrop);
    switch (visibleType) {
      case DROP_ZONE:
        UIOut view = (UIOut) fileDrop.getFacet(visibleType.name());
        if (view == null) {
          view = (UIOut) createViewComponent(facesContext, fileDrop, UIOut.COMPONENT_TYPE, RendererTypes.OUT);
          fileDrop.getFacets().put(visibleType.name(), view);
        }
        prepareRender(facesContext, view, fileDrop);
        break;
      case BUTTON:
        UIButton button = (UIButton) fileDrop.getFacet(visibleType.name());
        if (button == null) {
          button
              = (UIButton) createViewComponent(facesContext, fileDrop, UIButton.COMPONENT_TYPE, RendererTypes.BUTTON);
          fileDrop.getFacets().put(visibleType.name(), button);
        }
        prepareRender(facesContext, button, fileDrop);
        break;
      case LINK:
        UILink image = (UILink) fileDrop.getFacet(visibleType.name());
        if (image == null) {
          image = (UILink) createViewComponent(facesContext, fileDrop, UILink.COMPONENT_TYPE, RendererTypes.LINK);
          fileDrop.getFacets().put(visibleType.name(), image);
        }
        prepareRender(facesContext, image, fileDrop);
        break;
      default:
    }
  }

  private void prepareRender(FacesContext facesContext, UIComponent command, UIFileDrop fileDrop)
      throws IOException {
    super.prepareRender(facesContext, command);
  }

  @Override
  protected void writeDataAttributes(FacesContext facesContext, TobagoResponseWriter writer, AbstractUIFile file)
      throws IOException {
    String dataValue = buildDataValue(facesContext, (UIFileDrop) file);
    ComponentUtils.putDataAttribute(file, FILE_DROP_DATA_ATTRIBUTE_NAME, dataValue);
    super.writeDataAttributes(facesContext, writer, file);
  }

  private String buildDataValue(FacesContext facesContext, UIFileDrop fileDrop) {
    return "{\"dropZoneId\":\"" + calculateDropZoneId(facesContext, fileDrop) + "\"}";
  }

  private String calculateDropZoneId(FacesContext facesContext, UIFileDrop fileDrop) {
    String dropZoneId = fileDrop.getDropZoneId();
    if ("@this".equals(dropZoneId)) {
      dropZoneId = ":" + fileDrop.getClientId(facesContext);
    } else if ("@parent".equals(dropZoneId)) {
      dropZoneId = ":" + fileDrop.getParent().getClientId(facesContext);
    } else if ("@panel".equals(dropZoneId)) {
      dropZoneId = ":" + findPanel(fileDrop).getClientId(facesContext);
    }
    return dropZoneId;
  }

  private AbstractUIPanel findPanel(UIComponent component) {
    if (component instanceof AbstractUIPanel) {
      return (AbstractUIPanel) component;
    } else if (component != null) {
      return findPanel(component.getParent());
    } else {
      throw new IllegalStateException("No parent tc:panel found!");
    }
  }

  private void createActionFacet(FacesContext facesContext, UIFileDrop fileDrop) {
    UICommand command = (UICommand) facesContext.getApplication()
        .createComponent(facesContext, UICommand.COMPONENT_TYPE, RendererTypes.COMMAND);
    command.setId(fileDrop.getId() + "-command-facet");

    ValueExpression valueExpression = fileDrop.getValueExpression(Attributes.RENDERED_PARTIALLY);
    if (valueExpression != null) {
      command.setValueExpression(Attributes.RENDERED_PARTIALLY, valueExpression);
    } else {
      String[] renderedPartially = fileDrop.getRenderedPartially();
      if (renderedPartially != null && renderedPartially.length != 0) {
        command.setRenderedPartially(renderedPartially);
      } else {
        valueExpression = fileDrop.getValueExpression("dropZoneId");
        if (valueExpression != null) {
          command.setValueExpression(Attributes.RENDERED_PARTIALLY, valueExpression);
        } else {
          command.setRenderedPartially(new String[]{calculateDropZoneId(facesContext, fileDrop)});
        }
      }
    }

    MethodExpression actionExpression = fileDrop.getActionExpression();
    if (actionExpression != null) {
      command.setActionExpression(actionExpression);
    }

    ActionListener[] actionListeners = fileDrop.getActionListeners();
    if (actionListeners != null) {
      for (ActionListener listener : actionListeners) {
        command.addActionListener(listener);
      }
    }

    fileDrop.getFacets().put("change", command);
  }

  protected UIComponent createViewComponent(FacesContext facesContext, UIFileDrop fileDrop, String componentType,
                                            String rendererType) {
    UIComponent viewComponent = (UIComponent) facesContext.getApplication()
        .createComponent(facesContext, componentType, rendererType);
    viewComponent.setId(fileDrop.getId() + "-view-facet");

    setOmit(viewComponent);

    String label = fileDrop.getLabel();
    String image = fileDrop.getImage();

    ValueExpression valueExpression = fileDrop.getValueExpression(Attributes.LABEL);
    if (valueExpression != null) {
      setLabelExpression(viewComponent, valueExpression);
    } else {
      if (label != null) {
        setLabel(viewComponent, label);
      } else if (image == null) {
        setLabel(viewComponent, getDefaultLabel(facesContext));
      }
    }

    valueExpression = fileDrop.getValueExpression(Attributes.IMAGE);
    if (valueExpression != null) {
      viewComponent.setValueExpression(Attributes.IMAGE, valueExpression);
    } else {
      setImage(viewComponent, image);
    }

    return viewComponent;
  }

  private void setImage(UIComponent command, String image) {
    if (command instanceof UIButton) {
      ((UIButton) command).setImage(image);
    } else if (command instanceof UILink) {
      ((UILink) command).setImage(image);
    }
  }

  private void setOmit(UIComponent command) {
    if (command instanceof UIButton) {
      ((UIButton) command).setOmit(true);
    } else if (command instanceof UILink) {
      ((UILink) command).setOmit(true);
    }
  }

  private void setLabelExpression(UIComponent viewComponent, ValueExpression valueExpression) {
    if (viewComponent instanceof UIOut) {
      viewComponent.setValueExpression(Attributes.VALUE, valueExpression);
    } else {
      viewComponent.setValueExpression(Attributes.LABEL, valueExpression);
    }
  }

  private void setLabel(UIComponent command, String label) {
    if (command instanceof UIButton) {
      ((UIButton) command).setLabel(label);
    } else if (command instanceof UILink) {
      ((UILink) command).setLabel(label);
    } else if (command instanceof UIOut) {
      ((UIOut) command).setValue(label);
    }
  }

  private String getDefaultLabel(FacesContext facesContext) {
    return ResourceManagerUtils.getPropertyNotNull(facesContext, "tobago", "tobago.fileDrop.defaultLabel");
  }

  @Override
  protected void writeVisibleInput(FacesContext facesContext, TobagoResponseWriter writer, AbstractUIFile file,
                                   String clientId, Style style) throws IOException {
    UIFileDrop fileDrop = (UIFileDrop) file;
    VisibleType visibleType = getVisibleType(fileDrop);
    switch (visibleType) {
      case DROP_ZONE:
        writeDropZone(facesContext, writer, fileDrop);
        break;
      case FILE:
        super.writeVisibleInput(facesContext, writer, file, clientId, style);
        break;
      case BUTTON:
        writeButton(facesContext, writer, fileDrop);
        break;
      case LINK:
        writeImage(facesContext, writer, fileDrop);
        break;
      default:
        // NONE
    }

  }

  private void writeDropZone(FacesContext facesContext, TobagoResponseWriter writer, UIFileDrop fileDrop)
      throws IOException {
    writeComponent(facesContext, fileDrop.getFacet(VisibleType.DROP_ZONE.name()), fileDrop);
  }

  private void writeButton(FacesContext facesContext, TobagoResponseWriter writer, UIFileDrop fileDrop)
      throws IOException {
    writeComponent(facesContext, fileDrop.getFacet(VisibleType.BUTTON.name()), fileDrop);
  }

  private void writeImage(FacesContext facesContext, TobagoResponseWriter writer, UIFileDrop fileDrop)
      throws IOException {
    writeComponent(facesContext, fileDrop.getFacet(VisibleType.LINK.name()), fileDrop);
  }

  private void writeComponent(FacesContext facesContext, UIComponent component, UIFileDrop fileDrop)
      throws IOException {
    if (VisibleType.DROP_ZONE != getVisibleType(fileDrop) && component instanceof LayoutBase) {
      ((LayoutBase) component).setCurrentWidth(fileDrop.getCurrentWidth());
      ((LayoutBase) component).setCurrentHeight(fileDrop.getCurrentHeight());
      ((LayoutBase) component).setLeft(Measure.valueOf(0));
      ((LayoutBase) component).setTop(Measure.valueOf(0));
    }
    component.encodeBegin(facesContext);
    component.encodeChildren(facesContext);
    component.encodeEnd(facesContext);
  }

  @Override
  public Measure getHeight(FacesContext facesContext, Configurable component) {
    VisibleType visibleType = getVisibleType((UIFileDrop) component);
    switch (visibleType) {
      case FILE:
        return getResourceManager()
            .getThemeMeasure(facesContext, RendererTypes.FILE, null, Attributes.MINIMUM_HEIGHT);
      case BUTTON:
        return getResourceManager()
            .getThemeMeasure(facesContext, RendererTypes.BUTTON, null, Attributes.MINIMUM_HEIGHT);
      case LINK:
        return getResourceManager()
            .getThemeMeasure(facesContext, RendererTypes.LINK, null, Attributes.MINIMUM_HEIGHT);
      default:
        return null;
    }
  }

  @Override
  public Measure getMinimumHeight(FacesContext facesContext, Configurable component) {
    VisibleType visibleType = getVisibleType((UIFileDrop) component);
    switch (visibleType) {
      case DROP_ZONE:
        super.getMinimumHeight(facesContext, component);
      case FILE:
        return getResourceManager()
            .getThemeMeasure(facesContext, RendererTypes.FILE, null, Attributes.MINIMUM_HEIGHT);
      case BUTTON:
        return getResourceManager()
            .getThemeMeasure(facesContext, RendererTypes.BUTTON, null, Attributes.MINIMUM_HEIGHT);
      case LINK:
        return getResourceManager()
            .getThemeMeasure(facesContext, RendererTypes.LINK, null, Attributes.MINIMUM_HEIGHT);
      default:
        return Measure.ZERO;
    }
  }

  @Override
  public Measure getPreferredHeight(FacesContext facesContext, Configurable component) {
    VisibleType visibleType = getVisibleType((UIFileDrop) component);
    switch (visibleType) {
      case DROP_ZONE:
        super.getPreferredHeight(facesContext, component);
      case FILE:
        return getResourceManager()
            .getThemeMeasure(facesContext, RendererTypes.FILE, null, Attributes.PREFERRED_HEIGHT);
      case BUTTON:
        return getResourceManager()
            .getThemeMeasure(facesContext, RendererTypes.BUTTON, null, Attributes.PREFERRED_HEIGHT);
      case LINK:
        return getResourceManager()
            .getThemeMeasure(facesContext, RendererTypes.LINK, null, Attributes.PREFERRED_HEIGHT);
      default:
        return Measure.ZERO;
    }
  }

  @Override
  public Measure getMaximumHeight(FacesContext facesContext, Configurable component) {
    VisibleType visibleType = getVisibleType((UIFileDrop) component);
    switch (visibleType) {
      case FILE:
        return getResourceManager()
            .getThemeMeasure(facesContext, RendererTypes.FILE, null, Attributes.MAXIMUM_HEIGHT);
      case BUTTON:
        return getResourceManager()
            .getThemeMeasure(facesContext, RendererTypes.BUTTON, null, Attributes.MAXIMUM_HEIGHT);
      case LINK:
        return getResourceManager()
            .getThemeMeasure(facesContext, RendererTypes.LINK, null, Attributes.MAXIMUM_HEIGHT);
      default:
        return null;
    }
  }

  @Override
  public Measure getPreferredWidth(FacesContext facesContext, Configurable component) {
    VisibleType visibleType = getVisibleType((UIFileDrop) component);
    switch (visibleType) {
      case BUTTON:
        UIButton button = (UIButton) ((UIComponent) component).getFacet(visibleType.name());
        return button.getPreferredWidth();
      default:
        return super.getPreferredWidth(facesContext, component);
    }
  }

  @Override
  protected Measure getPrettyWidthSub(FacesContext facesContext, AbstractUIFile file) {
    return getResourceManager().getThemeMeasure(facesContext, RendererTypes.FILE, null, "prettyWidthSub");
  }


  @Override
  protected Classes getCssClasses(UIComponent component, String sub) {
    return super.getCssClasses(getCssComponent(component, sub), sub);
  }

  private UIComponent getCssComponent(UIComponent component, String sub) {
    VisibleType visibleType = getVisibleType((UIFileDrop) component);
    if ("real".equals(sub) || visibleType.equals(VisibleType.FILE)) {
      return createCssComponent(RendererTypes.FILE, component);
    } else {
      switch (visibleType) {
        case DROP_ZONE:
          return component;
        default:
          return createCssComponent(RendererTypes.PANEL, component);
      }
    }
  }

  private UIComponent createCssComponent(String rendererType, UIComponent component) {
    UIComponent cssComponent;
    if (component instanceof SupportsMarkup) {
      SupportsMarkup supportsMarkup = (SupportsMarkup) component;
      UIOut uiOut = new UIOut();
      uiOut.setMarkup(supportsMarkup.getMarkup());
      cssComponent = uiOut;
    } else {
      cssComponent = new UIOutput();
    }
    cssComponent.setRendererType(rendererType);
    return cssComponent;
  }

  private VisibleType getVisibleType(UIFileDrop fileDrop) {
    return VisibleType.asEnum(fileDrop.getVisibleType());
  }
}
