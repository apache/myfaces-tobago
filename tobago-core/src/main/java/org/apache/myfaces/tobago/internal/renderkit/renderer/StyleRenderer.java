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

package org.apache.myfaces.tobago.internal.renderkit.renderer;

import org.apache.myfaces.tobago.internal.component.AbstractUIStyle;
import org.apache.myfaces.tobago.internal.context.Nonce;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.internal.util.StyleRenderUtils;
import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.layout.GridSpan;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.Overflow;
import org.apache.myfaces.tobago.layout.Position;
import org.apache.myfaces.tobago.layout.TextAlign;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.Styles;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class StyleRenderer extends RendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(StyleRenderer.class);

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {

    final AbstractUIStyle style = (AbstractUIStyle) component;
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    final String file = style.getFile();
    if (StringUtils.isNotBlank(file)) {

      writer.startElement(HtmlElements.LINK);
      writer.writeAttribute(HtmlAttributes.REL, "stylesheet", false);
      writer.writeAttribute(HtmlAttributes.HREF, file, true);
//    writer.writeAttribute(HtmlAttributes.MEDIA, "screen", false);
      writer.writeAttribute(HtmlAttributes.TYPE, "text/css", false);
      writer.endElement(HtmlElements.LINK);

/* tbd: check, if this
      writer.startElement(HtmlElements.STYLE);
      writer.writeAttribute(HtmlAttributes.TYPE, "text/css", false);
      writer.writeAttribute(HtmlAttributes.NONCE, Nonce.getNonce(facesContext), false);
//    writer.writeAttribute(HtmlAttributes.MEDIA, "screen", false);
      writer.writeText("@import url(");
      writer.writeText(file);
      writer.writeText(");");
      writer.endElement(HtmlElements.STYLE);
*/

    } else {

      final Measure width = style.getWidth();
      final Measure height = style.getHeight();
      final Measure minWidth = style.getMinWidth();
      final Measure minHeight = style.getMinHeight();
      final Measure maxWidth = style.getMaxWidth();
      final Measure maxHeight = style.getMaxHeight();
      final Measure left = style.getLeft();
      final Measure right = style.getRight();
      final Measure top = style.getTop();
      final Measure bottom = style.getBottom();
      final Measure paddingLeft = style.getPaddingLeft();
      final Measure paddingRight = style.getPaddingRight();
      final Measure paddingTop = style.getPaddingTop();
      final Measure paddingBottom = style.getPaddingBottom();
      final Measure marginLeft = style.getMarginLeft();
      final Measure marginRight = style.getMarginRight();
      final Measure marginTop = style.getMarginTop();
      final Measure marginBottom = style.getMarginBottom();
      final Overflow overflowX = style.getOverflowX();
      final Overflow overflowY = style.getOverflowY();
      final Display display = style.getDisplay();
      final Position position = style.getPosition();
      final TextAlign textAlign = style.getTextAlign();
      final String backgroundImage = style.getBackgroundImage();
      final Number flexGrow = style.getFlexGrow();
      final Number flexShrink = style.getFlexShrink();
      final Measure flexBasis = style.getFlexBasis();
      final String gridTemplateColumns = style.getGridTemplateColumns();
      final String gridTemplateRows = style.getGridTemplateRows();
      final GridSpan gridColumn = style.getGridColumn();
      final GridSpan gridRow = style.getGridRow();

      // todo: backgroundPosition and zIndex

      if (width != null
          || height != null
          || minWidth != null
          || minHeight != null
          || maxWidth != null
          || maxHeight != null
          || left != null
          || right != null
          || top != null
          || bottom != null
          || paddingLeft != null
          || paddingRight != null
          || paddingTop != null
          || paddingBottom != null
          || marginLeft != null
          || marginRight != null
          || marginTop != null
          || marginBottom != null
          || overflowX != null
          || overflowY != null
          || display != null
          || position != null
          || textAlign != null
          || backgroundImage != null
          || flexGrow != null
          || flexShrink != null
          || flexBasis != null
          || gridTemplateColumns != null
          || gridTemplateRows != null
          || gridColumn != null
          || gridRow != null) {

        writer.startElement(HtmlElements.STYLE);
        writer.writeAttribute(HtmlAttributes.NONCE, Nonce.getNonce(facesContext), false);
        writer.writeIdAttribute(style.getClientId(facesContext));
        final String selector = style.getSelector();
        if (selector != null) {
          StyleRenderUtils.writeSelector(writer, selector);
        } else {
          final String parentId = style.getParent().getClientId(facesContext);
          StyleRenderUtils.writeIdSelector(writer, parentId);
        }
        writer.writeText("{");
        if (width != null) {
          encodeStyle(writer, Styles.width, width.serialize());
        }
        if (height != null) {
          encodeStyle(writer, Styles.height, height.serialize());
        }
        if (minWidth != null) {
          encodeStyle(writer, Styles.minWidth, minWidth.serialize());
        }
        if (minHeight != null) {
          encodeStyle(writer, Styles.minHeight, minHeight.serialize());
        }
        if (maxWidth != null) {
          encodeStyle(writer, Styles.maxWidth, maxWidth.serialize());
        }
        if (maxHeight != null) {
          encodeStyle(writer, Styles.maxHeight, maxHeight.serialize());
        }
        if (left != null) {
          encodeStyle(writer, Styles.left, left.serialize());
        }
        if (right != null) {
          encodeStyle(writer, Styles.right, right.serialize());
        }
        if (top != null) {
          encodeStyle(writer, Styles.top, top.serialize());
        }
        if (bottom != null) {
          encodeStyle(writer, Styles.bottom, bottom.serialize());
        }
        if (paddingLeft != null) {
          encodeStyle(writer, Styles.paddingLeft, paddingLeft.serialize());
        }
        if (paddingRight != null) {
          encodeStyle(writer, Styles.paddingRight, paddingRight.serialize());
        }
        if (paddingTop != null) {
          encodeStyle(writer, Styles.paddingTop, paddingTop.serialize());
        }
        if (paddingBottom != null) {
          encodeStyle(writer, Styles.paddingBottom, paddingBottom.serialize());
        }
        if (marginLeft != null) {
          encodeStyle(writer, Styles.marginLeft, marginLeft.serialize());
        }
        if (marginRight != null) {
          encodeStyle(writer, Styles.marginRight, marginRight.serialize());
        }
        if (marginTop != null) {
          encodeStyle(writer, Styles.marginTop, marginTop.serialize());
        }
        if (marginBottom != null) {
          encodeStyle(writer, Styles.marginBottom, marginBottom.serialize());
        }
        if (overflowX != null) {
          encodeStyle(writer, Styles.overflowX, overflowX.name());
        }
        if (overflowY != null) {
          encodeStyle(writer, Styles.overflowY, overflowY.name());
        }
        if (display != null) {
          encodeStyle(writer, Styles.display, display.encode());
        }
        if (position != null) {
          encodeStyle(writer, Styles.position, position.name());
        }
        if (textAlign != null) {
          encodeStyle(writer, Styles.textAlign, textAlign.name());
        }
        if (backgroundImage != null) {
          encodeStyle(writer, Styles.backgroundImage, backgroundImage);
        }
        if (flexGrow != null) {
          encodeStyle(writer, Styles.flexGrow, String.valueOf(flexGrow));
        }
        if (flexShrink != null) {
          encodeStyle(writer, Styles.flexShrink, String.valueOf(flexShrink));
        }
        if (flexBasis != null) {
          encodeStyle(writer, Styles.flexBasis, flexBasis.serialize());
        }
        if (gridTemplateColumns != null) {
          encodeStyle(writer, Styles.gridTemplateColumns, gridTemplateColumns);
          encodeStyle(writer, "-ms-grid-columns", gridTemplateColumns);
        }
        if (gridTemplateRows != null) {
          encodeStyle(writer, Styles.gridTemplateRows, gridTemplateRows);
          encodeStyle(writer, "-ms-grid-rows", gridTemplateRows);
        }
        if (gridColumn != null) {
          encodeStyle(writer, Styles.gridColumn, gridColumn.encode());
          encodeStyle(writer, "-ms-grid-column", gridColumn.getStart());
          encodeStyle(writer, "-ms-grid-column-span", gridColumn.getSpan());
        }
        if (gridRow != null) {
          encodeStyle(writer, Styles.gridRow, gridRow.encode());
          encodeStyle(writer, "-ms-grid-row", gridRow.getStart());
          encodeStyle(writer, "-ms-grid-row-span", gridRow.getSpan());
        }
        writer.writeText("}");

        writer.endElement(HtmlElements.STYLE);
      }
    }
  }

  private void encodeStyle(final TobagoResponseWriter writer, final Styles name, final String value) throws IOException {
    writer.writeText(name.getCssName());
    writer.writeText(":");
    switch (name) {
      case backgroundImage:
        writer.writeText("url(");
        writer.write("'");
        writer.writeText(value);
        writer.write("'");
        writer.writeText(")");
        break;
      default:
        writer.writeText(value);

    }
    writer.writeText(";");
  }

// XXX remove me
  private void encodeStyle(final TobagoResponseWriter writer, final String name, final String value)
      throws IOException {
    writer.writeText(name);
    writer.writeText(":");
    writer.writeText(value);
    writer.writeText(";");
  }

// XXX remove me
  private void encodeStyle(final TobagoResponseWriter writer, final String name, final Integer value)
      throws IOException {
    if (value != null) {
      writer.writeText(name);
      writer.writeText(":");
      writer.writeText(value.toString());
      writer.writeText(";");
    }
  }
}
