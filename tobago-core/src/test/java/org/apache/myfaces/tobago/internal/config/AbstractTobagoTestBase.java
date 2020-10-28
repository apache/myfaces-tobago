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

package org.apache.myfaces.tobago.internal.config;

import org.apache.myfaces.test.base.junit4.AbstractJsfTestCase;
import org.apache.myfaces.test.config.ResourceBundleVarNames;
import org.apache.myfaces.test.mock.MockFacesContext;
import org.apache.myfaces.test.mock.MockHttpServletRequest;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.component.UIBadge;
import org.apache.myfaces.tobago.component.UIBox;
import org.apache.myfaces.tobago.component.UIButton;
import org.apache.myfaces.tobago.component.UIButtons;
import org.apache.myfaces.tobago.component.UIGridLayout;
import org.apache.myfaces.tobago.component.UIIn;
import org.apache.myfaces.tobago.component.UILink;
import org.apache.myfaces.tobago.component.UILinks;
import org.apache.myfaces.tobago.component.UIOut;
import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.component.UIPopup;
import org.apache.myfaces.tobago.component.UISection;
import org.apache.myfaces.tobago.component.UISegmentLayout;
import org.apache.myfaces.tobago.component.UISelectBooleanCheckbox;
import org.apache.myfaces.tobago.component.UISelectItem;
import org.apache.myfaces.tobago.component.UISelectManyCheckbox;
import org.apache.myfaces.tobago.component.UISelectManyListbox;
import org.apache.myfaces.tobago.component.UISelectOneChoice;
import org.apache.myfaces.tobago.component.UISelectOneRadio;
import org.apache.myfaces.tobago.component.UISeparator;
import org.apache.myfaces.tobago.component.UIStyle;
import org.apache.myfaces.tobago.context.TobagoContext;
import org.apache.myfaces.tobago.internal.renderkit.renderer.BadgeRenderer;
import org.apache.myfaces.tobago.internal.renderkit.renderer.BoxRenderer;
import org.apache.myfaces.tobago.internal.renderkit.renderer.ButtonRenderer;
import org.apache.myfaces.tobago.internal.renderkit.renderer.ButtonsRenderer;
import org.apache.myfaces.tobago.internal.renderkit.renderer.GridLayoutRenderer;
import org.apache.myfaces.tobago.internal.renderkit.renderer.InRenderer;
import org.apache.myfaces.tobago.internal.renderkit.renderer.LinkRenderer;
import org.apache.myfaces.tobago.internal.renderkit.renderer.LinksRenderer;
import org.apache.myfaces.tobago.internal.renderkit.renderer.OutRenderer;
import org.apache.myfaces.tobago.internal.renderkit.renderer.PanelRenderer;
import org.apache.myfaces.tobago.internal.renderkit.renderer.PopupRenderer;
import org.apache.myfaces.tobago.internal.renderkit.renderer.SectionRenderer;
import org.apache.myfaces.tobago.internal.renderkit.renderer.SegmentLayoutRenderer;
import org.apache.myfaces.tobago.internal.renderkit.renderer.SelectBooleanCheckboxRenderer;
import org.apache.myfaces.tobago.internal.renderkit.renderer.SelectManyCheckboxRenderer;
import org.apache.myfaces.tobago.internal.renderkit.renderer.SelectManyListboxRenderer;
import org.apache.myfaces.tobago.internal.renderkit.renderer.SelectOneChoiceRenderer;
import org.apache.myfaces.tobago.internal.renderkit.renderer.SelectOneRadioRenderer;
import org.apache.myfaces.tobago.internal.renderkit.renderer.SeparatorRenderer;
import org.apache.myfaces.tobago.internal.renderkit.renderer.StyleRenderer;
import org.apache.myfaces.tobago.internal.webapp.HtmlResponseWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import javax.faces.render.RenderKit;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static org.apache.myfaces.tobago.config.TobagoConfig.TOBAGO_CONFIG;
import static org.apache.myfaces.tobago.util.ResourceUtils.TOBAGO_RESOURCE_BUNDLE;

/**
 * <p>Abstract JUnit test case base class, which sets up the JavaServer Faces
 * mock object environment for a particular simulated request.
 * </p>
 * <p>
 * This is a port of the class AbstractJsfTestCase from myfaces-test12 to JUnit 4.
 * It also contains Tobago specifics.
 * </p>
 */

public abstract class AbstractTobagoTestBase extends AbstractJsfTestCase {

  private StringWriter stringWriter;
  private int last = 0;

  /**
   * <p>Set up instance variables required by Tobago test cases.</p>
   */
  @Override
  @BeforeEach
  public void setUp() throws Exception {

    super.setUp();

    stringWriter = new StringWriter();
    getFacesContext().setResponseWriter(new HtmlResponseWriter(stringWriter, "", StandardCharsets.UTF_8));

    // Tobago specific extensions
    final TobagoConfigImpl tobagoConfig = TobagoConfigMergingUnitTest.load("tobago-config-for-unit-tests.xml");
    tobagoConfig.initDefaultValidatorInfo();
    servletContext.setAttribute(TOBAGO_CONFIG, tobagoConfig);
    facesContext.getExternalContext().getApplicationMap().put(TOBAGO_CONFIG, tobagoConfig);

    final TobagoContext tobagoContext = new TobagoContext();
    tobagoContext.setTheme(tobagoConfig.getDefaultTheme());
    facesContext.getViewRoot().setLocale(Locale.ENGLISH);
    request.setAttribute(TobagoContext.BEAN_NAME, tobagoContext);

    // XXX is there a better way? Get it from Tobagos generated faces-config.xml?
    application.addComponent(Tags.in.componentType(), UIIn.class.getName());
    application.addComponent(Tags.out.componentType(), UIOut.class.getName());
    application.addComponent(Tags.panel.componentType(), UIPanel.class.getName());
    application.addComponent(Tags.link.componentType(), UILink.class.getName());
    application.addComponent(Tags.links.componentType(), UILinks.class.getName());
    application.addComponent(Tags.box.componentType(), UIBox.class.getName());
    application.addComponent(Tags.button.componentType(), UIButton.class.getName());
    application.addComponent(Tags.buttons.componentType(), UIButtons.class.getName());
    application.addComponent(Tags.popup.componentType(), UIPopup.class.getName());
    application.addComponent(Tags.separator.componentType(), UISeparator.class.getName());
    application.addComponent(Tags.style.componentType(), UIStyle.class.getName());
    application.addComponent(Tags.section.componentType(), UISection.class.getName());
    application.addComponent(Tags.selectItem.componentType(), UISelectItem.class.getName());
    application.addComponent(Tags.selectBooleanCheckbox.componentType(), UISelectBooleanCheckbox.class.getName());
    application.addComponent(Tags.selectManyCheckbox.componentType(), UISelectManyCheckbox.class.getName());
    application.addComponent(Tags.selectManyListbox.componentType(), UISelectManyListbox.class.getName());
    application.addComponent(Tags.selectOneRadio.componentType(), UISelectOneRadio.class.getName());
    application.addComponent(Tags.selectOneChoice.componentType(), UISelectOneChoice.class.getName());
    application.addComponent(Tags.gridLayout.componentType(), UIGridLayout.class.getName());
    application.addComponent(Tags.segmentLayout.componentType(), UISegmentLayout.class.getName());
    application.addComponent(Tags.badge.componentType(), UIBadge.class.getName());

    final RenderKit renderKit = facesContext.getRenderKit();
    renderKit.addRenderer(UIIn.COMPONENT_FAMILY, RendererTypes.IN, new InRenderer());
    renderKit.addRenderer(UIOut.COMPONENT_FAMILY, RendererTypes.OUT, new OutRenderer());
    renderKit.addRenderer(UIPanel.COMPONENT_FAMILY, RendererTypes.PANEL, new PanelRenderer());
    renderKit.addRenderer(UILink.COMPONENT_FAMILY, RendererTypes.LINK, new LinkRenderer());
    renderKit.addRenderer(UILinks.COMPONENT_FAMILY, RendererTypes.LINKS, new LinksRenderer());
    renderKit.addRenderer(UIBox.COMPONENT_FAMILY, RendererTypes.BOX, new BoxRenderer());
    renderKit.addRenderer(UIButton.COMPONENT_FAMILY, RendererTypes.BUTTON, new ButtonRenderer());
    renderKit.addRenderer(UIButtons.COMPONENT_FAMILY, RendererTypes.BUTTONS, new ButtonsRenderer());
    renderKit.addRenderer(UIPopup.COMPONENT_FAMILY, RendererTypes.POPUP, new PopupRenderer());
    renderKit.addRenderer(UISeparator.COMPONENT_FAMILY, RendererTypes.SEPARATOR, new SeparatorRenderer());
    renderKit.addRenderer(UIStyle.COMPONENT_FAMILY, RendererTypes.STYLE, new StyleRenderer());
    renderKit.addRenderer(UISection.COMPONENT_FAMILY, RendererTypes.SECTION, new SectionRenderer());
    renderKit.addRenderer(
        UISelectBooleanCheckbox.COMPONENT_FAMILY, RendererTypes.SELECT_BOOLEAN_CHECKBOX,
        new SelectBooleanCheckboxRenderer());
    renderKit.addRenderer(
        UISelectManyCheckbox.COMPONENT_FAMILY, RendererTypes.SELECT_MANY_CHECKBOX, new SelectManyCheckboxRenderer());
    renderKit.addRenderer(
        UISelectManyListbox.COMPONENT_FAMILY, RendererTypes.SELECT_MANY_LISTBOX, new SelectManyListboxRenderer());
    renderKit.addRenderer(
        UISelectOneRadio.COMPONENT_FAMILY, RendererTypes.SELECT_ONE_RADIO, new SelectOneRadioRenderer());
    renderKit.addRenderer(
        UISelectOneChoice.COMPONENT_FAMILY, RendererTypes.SELECT_ONE_CHOICE, new SelectOneChoiceRenderer());
    renderKit.addRenderer(UIGridLayout.COMPONENT_FAMILY, RendererTypes.GRID_LAYOUT, new GridLayoutRenderer());
    renderKit.addRenderer(UISegmentLayout.COMPONENT_FAMILY, RendererTypes.SEGMENT_LAYOUT, new SegmentLayoutRenderer());
    renderKit.addRenderer(UIBadge.COMPONENT_FAMILY, RendererTypes.BADGE, new BadgeRenderer());

    application.setMessageBundle("org.apache.myfaces.tobago.context.TobagoMessageBundle");

    ResourceBundleVarNames.addVarName(TOBAGO_RESOURCE_BUNDLE,
        "org.apache.myfaces.tobago.context.TobagoResourceBundle");

    tobagoConfig.lock();
  }

  @Override
  @AfterEach
  public void tearDown() throws Exception {
    super.tearDown();
  }

  public MockFacesContext getFacesContext() {
    return facesContext;
  }

  public MockHttpServletRequest getRequest() {
    return request;
  }

  public String getLastWritten() throws IOException {
    getFacesContext().getResponseWriter().flush(); // is this needed
    final String full = stringWriter.toString();
    final String result = full.substring(last);
    last = full.length();
    return result;
  }
}
