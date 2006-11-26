package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

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

/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_BORDER;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_CELLSPACING;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_COLUMNS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_INNER_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_INNER_WIDTH;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LAYOUT_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LAYOUT_WIDTH;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ROWS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SCROLLBARS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_CLASS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_WIDTH_LIST;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UICell;
import org.apache.myfaces.tobago.component.UIForm;
import org.apache.myfaces.tobago.component.UIGridLayout;
import org.apache.myfaces.tobago.component.UILayout;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlStyleMap;
import org.apache.myfaces.tobago.util.LayoutInfo;
import org.apache.myfaces.tobago.util.LayoutUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GridLayoutRenderer extends DefaultLayoutRenderer {

  private static final Log LOG = LogFactory.getLog(GridLayoutRenderer.class);

  public Dimension getFixedSize(FacesContext facesContext, UIComponent component) {
    Dimension dimension = null;

    int height = getFixedHeight(facesContext, component);
    int width = -1; // TODO. implement getFixedWidth

    dimension = new Dimension(width, height);

    return dimension;
  }


  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    int height = calculateLayoutHeight(facesContext, component, false);

    RendererBase containerRenderer =
        ComponentUtil.getRenderer(facesContext, component);
    height += containerRenderer.getHeaderHeight(facesContext, component);
    height += containerRenderer.getPaddingHeight(facesContext, component);
    return height;
  }

  public int calculateLayoutHeight(
      FacesContext facesContext, UIComponent component, boolean minimum) {
    UIGridLayout layout = (UIGridLayout) UILayout.getLayout(component);
    final List<UIGridLayout.Row> rows = layout.ensureRows();
    String rowLayout
        = (String) layout.getAttributes().get(ATTR_ROWS);

    if (rowLayout == null && !minimum && LOG.isDebugEnabled()) {
      LOG.debug("No rowLayout found using " + (minimum ? "'minimum'" : "'fixed'")
          + " for all " + rows.size() + " rows of "
          + layout.getClientId(facesContext) + " !");
    }
    String[] layoutTokens
        = LayoutInfo.createLayoutTokens(rowLayout, rows.size(),
            minimum ? "minimum" : "fixed");

    if (rows.size() != layoutTokens.length) {
      LOG.warn("Unbalanced layout: rows.size()=" + rows.size()
          + " != layoutTokens.length=" + layoutTokens.length
          + " rowLayout='" + rowLayout + "'");
    }
    int size = Math.min(rows.size(), layoutTokens.length);

    int height = 0;
    height += getMarginAsInt(layout.getMarginTop());
    height += getMarginAsInt(layout.getMarginBottom());
    for (int i = 0; i < size; i++) {
      if (!rowIsRendered(rows.get(i))) {
        continue;
      }
      height += getCellPadding(facesContext, layout,  i);
      String token = layoutTokens[i];
      if (token.matches("\\d+px")) {
        height += Integer.parseInt(token.replaceAll("\\D", ""));
      } else if (token.equals("fixed")) {
        height += getMaxHeight(facesContext, rows.get(i), false);
      } else if (token.equals("minimum")) {
        height += getMaxHeight(facesContext, rows.get(i), true);
      } else {
        if (!minimum && LOG.isWarnEnabled()) {
          LOG.warn("Unable to calculate Height for token '" + token
              + "'! using " + (minimum ? "'minimum'" : "'fixed'") + " , component:"
              + layout.getClientId(facesContext) + " is "
              + layout.getRendererType());
        }
        height += getMaxHeight(facesContext, rows.get(i), minimum);
      }
    }

    return height;
  }

  private boolean rowIsRendered(UIGridLayout.Row row) {
    for (Object element : row.getElements()) {
      if (element instanceof UIComponent) {
        UIComponent component = (UIComponent) element;
        if (component.isRendered()) {
          return true;
        }
      }
    }
    return false;
  }

  public void encodeChildrenOfComponent(FacesContext facesContext, UIComponent component)
      throws IOException {
    // encode table with component's children

    UIGridLayout layout =  (UIGridLayout) UILayout.getLayout(component);
    HtmlRendererUtil.prepareRender(facesContext, layout);

    layoutEnd(facesContext, layout);
    layoutMargins(layout);

    final Map attributes = layout.getAttributes();
    List columnWidths =  (List) attributes.get(ATTR_WIDTH_LIST);


    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();
    writer.startElement(HtmlConstants.TABLE, layout);
    writer.writeAttribute(HtmlAttributes.BORDER, null, ATTR_BORDER);
    writer.writeComponentClass();
    writer.writeAttribute(HtmlAttributes.STYLE, null, ATTR_STYLE);
    writer.writeAttribute(HtmlAttributes.CELLSPACING, "0", null);
    writer.writeAttribute(HtmlAttributes.CELLPADDING, "0", null);
    writer.writeAttribute(HtmlAttributes.SUMMARY, "", null);

    if (columnWidths != null) {
      writer.startElement(HtmlConstants.COLGROUP, null);
      for (int i = 0; i < columnWidths.size(); i++) {
        int cellWidth = ((Integer) columnWidths.get(i)).intValue();
        if (cellWidth != LayoutInfo.HIDE) {
          cellWidth += getCellPadding(facesContext, layout, i);
          writer.startElement(HtmlConstants.COL, null);
          writer.writeAttribute(HtmlAttributes.WIDTH, Integer.toString(cellWidth), null);
          writer.endElement(HtmlConstants.COL);
        }
      }
      writer.endElement(HtmlConstants.COLGROUP);
    }


    List<UIGridLayout.Row> rows = layout.ensureRows();
    for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
      UIGridLayout.Row row = rows.get(rowIndex);
      if (!row.isHidden()) {
        writer.startElement(HtmlConstants.TR, null);

        List cells = row.getElements();
        for (int columnIndex = 0; columnIndex < cells.size(); columnIndex++) {
          boolean hide = false;

          if (columnWidths != null) {
            Integer columWidth = ((Integer) columnWidths.get(columnIndex));
            hide = columWidth.intValue() == LayoutInfo.HIDE;
          }
          if (!hide) {

            Object object = cells.get(columnIndex);
            if (object.toString().equals(UIGridLayout.USED)) {
              continue; // ignore the markers UIGridLayout.Used
            }
            if (object.equals(UIGridLayout.FREE)) {
              if (LOG.isWarnEnabled() && !layout.isIgnoreFree()) {
                LOG.warn("There are free blocks in the layout: id='"
                    + layout.getClientId(facesContext)
                    + "'");
              }
              continue;
            }
            UIComponent cell = (UIComponent) object;


            int spanX = UIGridLayout.getSpanX(cell);
            int spanY = UIGridLayout.getSpanY(cell);
            String cssClasses =
                (String) attributes.get(ATTR_STYLE_CLASS);
            cssClasses = (cssClasses == null ? "" : cssClasses);
            String cellClasses = "";
            if (rowIndex == 0) {
              cellClasses += " tobago-gridLayout-first-row";
            }
            if (columnIndex == 0) {
              cellClasses += " tobago-gridLayout-first-column";
            }
            cellClasses = cssClasses + cellClasses;

            int cellWidth = -1;
            if (columnWidths != null) {
              cellWidth = 0;
              for (int i = columnIndex;
                   i < columnIndex + spanX && i < columnWidths.size(); i++) {
                cellWidth += ((Integer) columnWidths.get(i)).intValue()
                    + getCellPadding(facesContext, layout, i);
              }
            }


            int cellHeight = -1;
            try {
              Integer layoutHeight = LayoutUtil.getLayoutHeight(cell);
              if (layoutHeight != null) {
                cellHeight = layoutHeight.intValue();
              }
            } catch (Exception e) {
              // ignore
            } // ignore, use 0

            int topPadding = getCellPadding(facesContext, layout, rowIndex);
            String cellStyle =
                (cellWidth != -1 ? "width: " + cellWidth + "px;" : "")
                + (cellHeight != -1 ? " height: " + (cellHeight + topPadding) + "px;" : "");
            cellStyle += getOverflow(cell);


            writer.startElement(HtmlConstants.TD, null);
            writer.writeClassAttribute("tobago-gridLayout-cell-td");
            writer.writeAttribute(HtmlAttributes.STYLE, cellStyle, null);
            if (spanX > 1) {
              writer.writeAttribute(HtmlAttributes.COLSPAN, Integer.toString(spanX), null);
            }
            if (spanY > 1) {
              writer.writeAttribute(HtmlAttributes.ROWSPAN, Integer.toString(spanY), null);
            }

            writer.writeText("", null);

            if (ComponentUtil.getAttribute(layout, ATTR_CELLSPACING) != null) {
              cellStyle += " padding: " + getCellSpacing(facesContext, layout) + "px;";
            }

            writer.startElement(HtmlConstants.DIV, null);
            writer.writeClassAttribute(cellClasses);
            writer.writeAttribute(HtmlAttributes.STYLE, cellStyle, null);

            RenderUtil.encode(facesContext, cell);

            writer.endElement(HtmlConstants.DIV);
            writer.endElement(HtmlConstants.TD);
          }
        }

        writer.endElement(HtmlConstants.TR);
      }
    }
    writer.endElement(HtmlConstants.TABLE);
  }

  public void encodeEnd(FacesContext facesContext,
      UIComponent component) throws IOException {
    if (component.getParent() instanceof UIPage) {
      LOG.error("XXXXXXXXXXXXXXXXXXXXXXX  never XXXXXXXXXXXXXXXXXXXXXX", new Exception());
    } else {
      encodeChildrenOfComponent(facesContext, component.getParent());
    }
  }

  private String getOverflow(UIComponent cell) {
    String overflow = "";

    String scrollbars = (String) cell.getAttributes().get(ATTR_SCROLLBARS);
    if (scrollbars != null) {
      if (scrollbars.equals("false")) {
        overflow = " overflow: hidden;";
      } else if (scrollbars.equals("true")) {
        overflow = " overflow: scroll;";
      } else if (scrollbars.equals("auto")) {
        overflow = " overflow: auto;";
      } else {
        if (LOG.isWarnEnabled()) {
          LOG.warn("Illegal value for attribute 'scrollbars' : " + scrollbars);
        }
      }
    }

    return overflow;
  }

  private int getCellPadding(
      FacesContext facesContext, UIComponent component, int i) {
    return i == 0 ? 0 : getCellSpacing(facesContext, component);
  }

  private int getBorder(UIComponent component) {
    int border = 0;
    String borderWidth
        = (String) component.getAttributes().get(ATTR_BORDER);
    try {
      if (borderWidth != null) {
        border = Integer.parseInt(borderWidth);
      }
    } catch (NumberFormatException e) {
      if (LOG.isWarnEnabled()) {
        LOG.warn("Can't parse border, using 0!");
      }
    }

    return border;
  }

  private int getSpacingSum(UIComponent component, FacesContext facesContext,
      int count) {
    int space = 0;
    int border = getBorder(component);
    int cellSpacing = getCellSpacing(facesContext, component);
    if (border != 0) {
      space = (border * 2) + count * 2;
    }
    space += (count - 1) * cellSpacing;
    if (LOG.isDebugEnabled()) {
      LOG.debug("extra count =" + count + ",  space = " + space);
    }
    return space;
  }

  private int getWidthSpacingSum(UIGridLayout component,
      FacesContext facesContext) {
    int spacingSum
        = getSpacingSum(component, facesContext, component.getColumnCount());
    spacingSum += getComponentExtraWidth(facesContext, component);
    return spacingSum;
  }

  private int getCellSpacing(FacesContext facesContext, UIComponent component) {
    String cellspacing = (String) component.getAttributes().get(ATTR_CELLSPACING);
    if (cellspacing instanceof String) {
      try {
        return Integer.parseInt(cellspacing);
      } catch (NumberFormatException e) {
        if (LOG.isWarnEnabled()) {
          LOG.warn("Illegal value for cellspacing : " + cellspacing
              + " using default");
        }
        // ignore and return defaut value
      }
    }
    return getConfiguredValue(facesContext,  component, "cellSpacing");
  }

  private void layoutEnd(FacesContext facesContext, UIComponent component) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("doLayout end");
    }
    UIGridLayout layout = (UIGridLayout) component;
    final Map attributes = layout.getParent().getAttributes();


    boolean needVerticalScroolbar = false;
    Integer innerHeight =
          (Integer) attributes.get(ATTR_INNER_HEIGHT);
    if (innerHeight != null && innerHeight.intValue() > 0) {
      int value = innerHeight.intValue();
      int minimum = calculateLayoutHeight(facesContext, layout.getParent(), true);
      if (minimum > value) {
        value = minimum;
        needVerticalScroolbar = true;
      }
      value -= getHeightSpacingSum(layout, facesContext);
      layoutHeight(new Integer(value), layout, facesContext);
    }


    Integer innerWidth =
          (Integer) attributes.get(ATTR_INNER_WIDTH);
    if (innerWidth != null && innerWidth.intValue() != -1) {
      int value
          = innerWidth.intValue() - getWidthSpacingSum(layout, facesContext);
      if (needVerticalScroolbar) {
        value -= getConfiguredValue(facesContext, component, "scrollbarWidth");
        HtmlRendererUtil.replaceStyleAttribute(layout, "width", value);
      }
      layoutWidth(new Integer(value), layout, facesContext);
    }

  }

  private int getHeightSpacingSum(UIGridLayout layout,
      FacesContext facesContext) {
    int spacingSum
        = getSpacingSum(layout, facesContext, layout.ensureRows().size());
    spacingSum += getComponentExtraHeight(facesContext, layout);
    return spacingSum;
  }

  private void layoutWidth(Integer innerWidth, UIGridLayout layout,
      FacesContext facesContext) {

    final List<UIGridLayout.Row> rows = layout.ensureRows();
    final int columnCount = layout.getColumnCount();
    final String[] layoutTokens = LayoutInfo.createLayoutTokens((String)
        layout.getAttributes().get(ATTR_COLUMNS), columnCount);


    if (!rows.isEmpty()) {
      UIGridLayout.Row row = rows.get(0);
      final List cells = row.getElements();

      for (int i = 0; i < cells.size(); i++) {
        Object cell = cells.get(i);
        boolean hidden = false;
        if (isHidden(cell)) {
          hidden = true;
          for (int j = 1; j < rows.size(); j++) {
            hidden &= isHidden(rows.get(j).getElements().get(i));
          }
        }
        if (hidden) {
          layoutTokens[i] = LayoutInfo.HIDE_CELL;
        }
      }
    }


    LayoutInfo layoutInfo =
        new LayoutInfo(columnCount, innerWidth.intValue(), layoutTokens,
            layout.isIgnoreFree());

    parseFixedWidth(layoutInfo, layout, facesContext);
    layoutInfo.parseColumnLayout(innerWidth.doubleValue(),
        getCellSpacing(facesContext, layout));

    setColumnWidths(layout, layoutInfo, facesContext);
    layout.getAttributes().put(ATTR_WIDTH_LIST,
        layoutInfo.getSpaceList());
  }

  private boolean isHidden(Object cell) {
    return (cell instanceof UIComponent && !((UIComponent) cell).isRendered())
              || (cell instanceof UIGridLayout.Marker
                  && !((UIGridLayout.Marker) cell).isRendered());
  }

  private void layoutHeight(Integer innerHeight, UIGridLayout layout,
      FacesContext facesContext) {

    final List<UIGridLayout.Row> rows = layout.ensureRows();
    String[] layoutTokens = LayoutInfo.createLayoutTokens(
        (String) layout.getAttributes().get(ATTR_ROWS),
        rows.size(), rows.size() == 1 ? "1*" : "fixed");

    for (int i = 0; i < rows.size(); i++) {
      boolean hidden = true;
      UIGridLayout.Row row = rows.get(i);
      List cells = row.getElements();
      for (int j = 0; j < cells.size(); j++) {
        hidden &= isHidden(cells.get(j));
      }
      row.setHidden(hidden);
      if (hidden) {
        layoutTokens[i] = LayoutInfo.HIDE_CELL;
      }
    }


    LayoutInfo layoutInfo =
        new LayoutInfo(rows.size(), innerHeight.intValue(),
            layoutTokens, layout.isIgnoreFree());

    if (layoutInfo.hasLayoutTokens()) {
      parseFixedHeight(layoutInfo, layout, facesContext);
      layoutInfo.parseColumnLayout(innerHeight.doubleValue(),
          getCellSpacing(facesContext, layout));
    }

    setColumnHeights(layout, layoutInfo, facesContext);

  }

  private void parseFixedWidth(LayoutInfo layoutInfo, UIGridLayout layout,
      FacesContext facesContext) {
    parseFixedSpace(layoutInfo, layout, true, facesContext);
  }
  private void parseFixedHeight(LayoutInfo layoutInfo, UIGridLayout layout,
      FacesContext facesContext) {
    parseFixedSpace(layoutInfo, layout, false, facesContext);
  }
  private void parseFixedSpace(LayoutInfo layoutInfo, UIGridLayout layout,
                               boolean width, FacesContext facesContext) {

    String[] tokens = layoutInfo.getLayoutTokens();
    for (int i = 0; i < tokens.length; i++) {
      if (tokens[i].equals("fixed")) {
        int max = 0;
        final List<UIGridLayout.Row> rows = layout.ensureRows();
        if (!rows.isEmpty()) {
          if (width) {
            max = getMaxWidth(facesContext, rows, i, false);
          } else {
            if (i < rows.size()) {      //
              UIGridLayout.Row row = rows.get(i);
              max = getMaxHeight(facesContext, row, false);
            } else {
              layoutInfo.update(0, i);
              if (LOG.isWarnEnabled()) {
                LOG.warn("More LayoutTokens found than rows! skipping! tokens = "
                    + LayoutInfo.tokensToString(tokens) + "  components = "
                    + rows.size());
              }
            }
          }
          layoutInfo.update(max, i);
        }
        if (LOG.isDebugEnabled()) {
          LOG.debug("set column " + i + " from fixed to with " + max);
        }
      }
    }
  }

  private int getMaxHeight(FacesContext facesContext, UIGridLayout.Row row, boolean minimum) {
    int maxHeight = 0;
    List cells = row.getElements();
    for (Object cell : cells) {
      Object object = cell;

      if (object instanceof UIComponent) {
        UIComponent component = (UIComponent) object;
        int height = -1;
        if (minimum) {
          height = (int) LayoutUtil.getMinimumSize(facesContext, component).getHeight();
        } else {
          RendererBase renderer = ComponentUtil.getRenderer(facesContext, component);
          if (renderer != null) {
            height = renderer.getFixedHeight(facesContext, component);
          }
        }
        maxHeight = Math.max(maxHeight, height);
      } else if (object instanceof UIGridLayout.Marker) {
        // ignore 
      } else {
        // TODO is this needed?
        LOG.error("Object is not instanceof UIComponent " + object.getClass().getName());
      }
    }
    return maxHeight;
  }

  private int getMaxWidth(FacesContext facesContext, List<UIGridLayout.Row> rows, int column, boolean minimum) {
    int maxWidth = 0;

    for (UIGridLayout.Row row : rows) {
      if (column < row.getElements().size()) {
        Object object = row.getElements().get(column);

        if (object instanceof UIComponent) {
         UIComponent component = (UIComponent) object;

          int max = -1;
          if (minimum) {
            max = (int) LayoutUtil.getMinimumSize(facesContext, component).getWidth();
          } else {
            RendererBase renderer = ComponentUtil.getRenderer(facesContext, component);
            if (renderer != null) {
              max = renderer.getFixedWidth(facesContext, component);
            }
          }
          maxWidth = Math.max(maxWidth, max);
        } else {
          // TODO is this needed?
          LOG.error("Object is not instanceof UIComponent " + object.getClass().getName());
        }
      }
    }
    return maxWidth;
  }

  private void setColumnWidths(UIGridLayout layout, LayoutInfo layoutInfo,
      FacesContext facesContext) {
    List<UIGridLayout.Row> rows = layout.ensureRows();

    for (Iterator iter = rows.iterator(); iter.hasNext();) {
      UIGridLayout.Row row = (UIGridLayout.Row) iter.next();
      List cells = row.getElements();
      int columnCount = layout.getColumnCount();
      for (int i = 0; i < columnCount; i++) {
        if (cells.get(i) instanceof UIComponent) {
          UIComponent cell = (UIComponent) cells.get(i);
          int spanX = UIGridLayout.getSpanX(cell);
          int cellWidth = 0;
          for (int j = i; j < i + spanX; j++) {
            cellWidth += layoutInfo.getSpaceForColumn(j);
          }
          if (spanX > 1 && getBorder(layout) > 0) {
            cellWidth += ((spanX - 1) * 2);
          }
          cellWidth += (spanX - 1) * getCellSpacing(facesContext, layout);
          LayoutUtil.maybeSetLayoutAttribute(cell,
              ATTR_LAYOUT_WIDTH, new Integer(cellWidth));
        }
      }
    }
  }

  private void setColumnHeights(UIGridLayout layout, LayoutInfo layoutInfo,
      FacesContext facesContext) {
    List<UIGridLayout.Row> rows = layout.ensureRows();

    for (int i = 0; i < rows.size(); i++) {
      UIGridLayout.Row row = rows.get(i);
      List cells = row.getElements();
      int columnCount = layout.getColumnCount();
      for (int j = 0; j < columnCount; j++) {
        if (cells.get(j) instanceof UIComponent) {
          UIComponent cell = (UIComponent) cells.get(j);
          int spanY = UIGridLayout.getSpanY(cell);
          int cellHeight = 0;
          for (int k = i; k < i + spanY; k++) {
            cellHeight += layoutInfo.getSpaceForColumn(k);
          }
          if (spanY > 1 && getBorder(layout) > 0) {
            cellHeight += ((spanY - 1) * 2);
          }
          cellHeight += (spanY - 1) * getCellSpacing(facesContext, layout);
          if (LOG.isDebugEnabled()) {
            LOG.debug("set height of " + cellHeight + "px to "
                + cell.getRendererType());
          }
          cell.getAttributes().put(ATTR_LAYOUT_HEIGHT,
              new Integer(cellHeight));
          cell.getAttributes().remove(ATTR_INNER_HEIGHT);
          if (cell instanceof UICell || cell instanceof UIForm) {
            List children = LayoutUtil.addChildren(new ArrayList(), cell);
            for (Iterator childs = children.iterator(); childs.hasNext();) {
              UIComponent component = (UIComponent) childs.next();
              if (LOG.isDebugEnabled()) {
                LOG.debug("set height of " + cellHeight + "px to "
                    + component.getRendererType());
              }
              component.getAttributes().put(ATTR_LAYOUT_HEIGHT,
                  new Integer(cellHeight));
              component.getAttributes().remove(ATTR_INNER_HEIGHT);

            }
          }
        }
      }
    }
  }


  public void layoutBegin(FacesContext facesContext, UIComponent component) {
    LOG.info("############################## layoutBegin +++++++++++++++++++++++++++++++++++++++++");
    HtmlRendererUtil.layoutSpace(facesContext, component, true);
    HtmlRendererUtil.layoutSpace(facesContext, component, false);

    if (component instanceof UIGridLayout) {
      layoutMargins((UIGridLayout) component);
    }
  }

  private void layoutMargins(UIGridLayout layout) {

    HtmlStyleMap style = (HtmlStyleMap) layout.getAttributes().get(ATTR_STYLE);

    if (style != null) {
      int marginTop = getMarginAsInt(layout.getMarginTop());
      int marginRight = getMarginAsInt(layout.getMarginRight());
      int marginBottom = getMarginAsInt(layout.getMarginBottom());
      int marginLeft = getMarginAsInt(layout.getMarginLeft());
      if(marginTop > 0) {
        style.put("margin-top", marginTop);
      }
      if(marginRight > 0) {
        style.put("margin-right", marginRight);
      }
      if(marginBottom > 0) {
        style.put("margin-bottom", marginBottom);
      }
      if(marginLeft > 0) {
        style.put("margin-left", marginLeft);
      }

      //layout.getAttributes().put(ATTR_LAYOUT_TABLE_STYLE, style);
    }
  }

  public int getComponentExtraWidth(FacesContext facesContext,
      UIComponent component) {
    int extra = 0;
    UIGridLayout layout = (UIGridLayout) component;

    extra += getMarginAsInt(layout.getMarginRight());
    extra += getMarginAsInt(layout.getMarginLeft());

    return extra;
  }

  public int getComponentExtraHeight(FacesContext facesContext,
      UIComponent component) {
    int extra = 0;
    UIGridLayout layout = (UIGridLayout) component;

    extra += getMarginAsInt(layout.getMarginTop());
    extra += getMarginAsInt(layout.getMarginBottom());
    return extra;
  }

  private int getMarginAsInt(String margin) {
    if (margin != null) {
      margin = removePx(margin);
      try {
        return Integer.parseInt(margin);
      } catch (NumberFormatException e) {
        if (LOG.isWarnEnabled()) {
          LOG.warn("Illegal Margin : " + margin
              + " exception : " + e.getMessage(), e);
        }
      }
    }
    return 0;
  }

  private String removePx(String margin) {
    if (margin!=null&&margin.endsWith("px")) {
      margin = margin.substring(0, margin.length() - 2);
    }
    return margin;
  }
}

