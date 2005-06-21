/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIForm;
import com.atanion.tobago.component.UIGridLayout;
import com.atanion.tobago.component.UILayout;
import com.atanion.tobago.component.UIPage;
import com.atanion.tobago.component.UIPanel;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.html.HtmlRendererUtil;
import com.atanion.tobago.util.LayoutInfo;
import com.atanion.tobago.util.LayoutUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.awt.*;

public class GridLayoutRenderer extends DefaultLayoutRenderer {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(GridLayoutRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public Dimension getFixedSize(FacesContext facesContext, UIComponent component) {
    Dimension dimension = null;

    int height = getFixedHeight(facesContext, component);
    int width = -1; // todo. implement getFixedWidth



    dimension = new Dimension(width, height);

    return dimension;
  }


  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    UIGridLayout layout = (UIGridLayout) component;
    int height = calculateLayoutHeight(facesContext, component, false);

    RendererBase containerRenderer =
        ComponentUtil.getRenderer(facesContext, layout.getParent());
    height += containerRenderer.getHeaderHeight(facesContext, layout.getParent());
    height += containerRenderer.getPaddingHeight(facesContext, layout.getParent());
    return height;
  }

  public int calculateLayoutHeight(
      FacesContext facesContext, UIComponent component, boolean minimum) {
    UIGridLayout layout = (UIGridLayout) component;
    final List rows = layout.ensureRows();
    String rowLayout
        = (String) layout.getAttributes().get(TobagoConstants.ATTR_ROWS);

    if (rowLayout == null && ! minimum && LOG.isDebugEnabled()) {
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
    for (int i = 0; i < size; i++) {
      height += getCellPadding(facesContext, layout,  i);
      String token = layoutTokens[i];
      if (token.matches("\\d+px")) {
        height += Integer.parseInt(token.replaceAll("\\D", ""));
      }
      else if (token.equals("fixed")) {
        height += getMaxHeight(facesContext, (UIGridLayout.Row) rows.get(i), false);
      }
      else if (token.equals("minimum")) {
        height += getMaxHeight(facesContext, (UIGridLayout.Row) rows.get(i), true);
      }
      else {
        if (! minimum && LOG.isWarnEnabled()) {
          LOG.warn("Unable to calculate Height for token '" + token
              + "'! using " + (minimum ? "'minimum'" : "'fixed'") + " , component:"
              + component.getClientId(facesContext) + " is "
              + component.getRendererType());
        }
        height += getMaxHeight(facesContext, (UIGridLayout.Row) rows.get(i), minimum);
      }
    }

    return height;
  }

  public void encodeChildrenOfComponent(FacesContext facesContext, UIComponent component)
      throws IOException {
    // encode table with component's children

    UIGridLayout layout =  (UIGridLayout) UILayout.getLayout(component);
    HtmlRendererUtil.prepareRender(facesContext, layout);

    layoutEnd(facesContext, layout);
    layoutMargins(layout);

    final Map attributes = layout.getAttributes();
    List columnWidths =  (List) attributes.get(TobagoConstants.ATTR_WIDTH_LIST);

    ResponseWriter writer = facesContext.getResponseWriter();

    writer.startElement("table", layout);
    writer.writeAttribute("border", null, TobagoConstants.ATTR_BORDER);
    writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
    writer.writeAttribute("style", null, TobagoConstants.ATTR_LAYOUT_TABLE_STYLE);
    writer.writeAttribute("cellspacing", "0", null);
    writer.writeAttribute("cellpadding", "0", null);
    writer.writeAttribute("summary", "", null);

    if (columnWidths != null) {
      writer.startElement("colgroup", null);
      for (int i = 0; i < columnWidths.size(); i++) {
        int cellWidth
            = ((Integer)columnWidths.get(i)).intValue();
        if (cellWidth != LayoutInfo.HIDE) {
          cellWidth += getCellPadding(facesContext, layout, i);
          writer.startElement("col", null);
          writer.writeAttribute("width", Integer.toString(cellWidth), null);
          writer.endElement("col");
        }
      }
      writer.endElement("colgroup");
    }


    List rows = layout.ensureRows();
    for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
      UIGridLayout.Row row = (UIGridLayout.Row) rows.get(rowIndex);
      if (! row.isHidden()) {
        writer.startElement("tr", null);

        List cells = row.getElements();
        for (int columnIndex = 0; columnIndex < cells.size(); columnIndex++) {
          boolean hide = false;

          if (columnWidths != null) {
            Integer columWidth = ((Integer)columnWidths.get(columnIndex));
            hide = columWidth.intValue() == LayoutInfo.HIDE;
          }
          if (! hide) {

            Object object = cells.get(columnIndex);
            if (object.toString().equals(UIGridLayout.USED)) {
              continue; // ignore the markers UIGridLayout.Used
            }
            if (object.equals(UIGridLayout.FREE)) {
              if (LOG.isWarnEnabled() && ! layout.isIgnoreFree()) {
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
                (String) attributes.get(TobagoConstants.ATTR_STYLE_CLASS);
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
              Integer  layoutHeight = LayoutUtil.getLayoutHeight(cell);
              if (layoutHeight != null) {
                cellHeight = layoutHeight.intValue();
              }
            } catch (Exception e) {
            } // ignore, use 0

            int topPadding = getCellPadding(facesContext, layout, rowIndex);
            String cellStyle
                = (cellWidth != -1 ? "width: " + cellWidth + "px;" : "")
                + (cellHeight != -1 ?
                " height: " + (cellHeight + topPadding) + "px;" : ""
                );
            cellStyle += getOverflow(cell);


            writer.startElement("td", null);
            writer.writeAttribute("class", "tobago-gridLayout-cell-td", null);
            writer.writeAttribute("style", cellStyle, null);
            if (spanX > 1) {
              writer.writeAttribute("colspan", Integer.toString(spanX), null);
            }
            if (spanY > 1) {
              writer.writeAttribute("rowspan", Integer.toString(spanY), null);
            }

            writer.writeText("", null);

            writer.startElement("div", null);
            writer.writeAttribute("class", cellClasses, null);
            writer.writeAttribute("style", cellStyle, null);

            RenderUtil.encode(facesContext, cell);

            writer.endElement("div");
            writer.endElement("td");
          }
        }

        writer.endElement("tr");
      }
    }
    writer.endElement("table");
  }

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component) throws IOException {
    if (component.getParent() instanceof UIPage) {
      LOG.error("XXXXXXXXXXXXXXXXXXXXXXX  never XXXXXXXXXXXXXXXXXXXXXX", new Exception());
    }
    else {
      encodeChildrenOfComponent(facesContext, component.getParent());
    }
  }

  private String getOverflow(UIComponent cell) {
    String overflow = "";

    String scrollbars = (String) cell.getAttributes().get(ATTR_SCROLLBARS);
    if (scrollbars != null) {
      if (scrollbars.equals("false") ) {
        overflow = " overflow: hidden;";
      }
      else if (scrollbars.equals("true") ) {
        overflow = " overflow: scroll;";
      }
      else if (scrollbars.equals("auto") ) {
        overflow = " overflow: auto;";
      }
      else {
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
        = (String) component.getAttributes().get(TobagoConstants.ATTR_BORDER);
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
    String cellspacing = (String) component.getAttributes().get(
        TobagoConstants.ATTR_CELLSPACING);
    if (cellspacing instanceof String) {
      try {
        return Integer.parseInt(cellspacing);
      } catch (NumberFormatException e) {
        if (LOG.isWarnEnabled()) {
          LOG.warn("Illegal value for cellspacing : " + cellspacing +
              " using default");
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
          (Integer) attributes.get(TobagoConstants.ATTR_INNER_HEIGHT);
    if (innerHeight != null && innerHeight.intValue() != -1) {
      int value = innerHeight.intValue();
      int minimum = calculateLayoutHeight(facesContext, layout, true);
      if (minimum > value) {
        value = minimum;
        needVerticalScroolbar = true;
      }
      value -= getHeightSpacingSum(layout, facesContext);
      layoutHeight(new Integer(value), layout, facesContext);
    }


    Integer innerWidth =
          (Integer) attributes.get(TobagoConstants.ATTR_INNER_WIDTH);
    if (innerWidth != null && innerWidth.intValue() != -1) {
      int value
          = innerWidth.intValue() - getWidthSpacingSum(layout, facesContext);
      if (needVerticalScroolbar) {
        value -= getConfiguredValue(facesContext, component, "scrollbarWidth");
        String style = (String) layout.getAttributes().get(ATTR_STYLE);
        style = HtmlRendererUtil.replaceStyleAttribute(style, "width",
            Integer.toString(value) + "px");
        layout.getAttributes().put(ATTR_STYLE, style);
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

    final List rows = layout.ensureRows();
    final int columnCount = layout.getColumnCount();
    final String[] layoutTokens = LayoutInfo.createLayoutTokens((String)
        layout.getAttributes().get(TobagoConstants.ATTR_COLUMNS), columnCount);


    if (! rows.isEmpty()) {
      UIGridLayout.Row row = (UIGridLayout.Row) rows.get(0);
      final List cells = row.getElements();

      for (int i = 0; i< cells.size() ; i++) {
        Object cell = cells.get(i);
        boolean hidden = false;
        if (isHidden(cell)) {
          hidden = true;
          for (int j = 1; j < rows.size(); j++) {
            hidden &= isHidden(((UIGridLayout.Row)rows.get(j)).getElements().get(i));
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

    layoutInfo.parseColumnLayout(innerWidth.doubleValue(),
        getCellSpacing(facesContext, layout));

    setColumnWidths(layout, layoutInfo, facesContext);
    layout.getAttributes().put(TobagoConstants.ATTR_WIDTH_LIST,
        layoutInfo.getSpaceList());
  }

  private boolean isHidden(Object cell) {
    return (cell instanceof UIComponent && !((UIComponent)cell).isRendered())
              || (cell instanceof UIGridLayout.Marker
                  && ! ((UIGridLayout.Marker)cell).isRendered());
  }

  private void layoutHeight(Integer innerHeight, UIGridLayout layout,
      FacesContext facesContext) {

    final List rows = layout.ensureRows();
    String[] layoutTokens = LayoutInfo.createLayoutTokens(
        (String) layout.getAttributes().get(TobagoConstants.ATTR_ROWS),
        rows.size(), rows.size() == 1 ? "*" : "fixed");

    for (int i = 0; i < rows.size(); i++) {
      boolean hidden = true;
      UIGridLayout.Row row = ((UIGridLayout.Row)rows.get(i));
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

  private void parseFixedHeight(LayoutInfo layoutInfo, UIGridLayout layout,
      FacesContext facesContext) {
    String[] tokens = layoutInfo.getLayoutTokens();
    for (int i = 0; i < tokens.length; i++) {
      if (tokens[i].equals("fixed")) {
        int height = 0;
        final List rows = layout.ensureRows();
        if (! rows.isEmpty()) {
          if (i < rows.size()) {
            UIGridLayout.Row row = (UIGridLayout.Row) rows.get(i);
            height = getMaxHeight(facesContext, row, false);
            layoutInfo.update(height, i);
          }
          else {
            layoutInfo.update(0, i);
            if (LOG.isWarnEnabled()) {
              LOG.warn("More LayoutTokens found than rows! skipping!");
            }
          }
        }
        if (LOG.isDebugEnabled()) {
          LOG.debug("set column " + i + " from fixed to with " + height);
        }
      }
    }
  }

  private int getMaxHeight(FacesContext facesContext, UIGridLayout.Row row, boolean minimum) {
    int maxHeight = 0;
    List cells = row.getElements();
    for (int j = 0; j < cells.size(); j++) {
      Object object = cells.get(j);

      if (object instanceof UIComponent) {
        UIComponent component = (UIComponent) object;
        int height = -1;
        if (minimum) {
          height = (int) LayoutUtil.getMinimumSize(facesContext, component).getHeight();
        } else {
          RendererBase renderer = ComponentUtil.getRenderer(facesContext, component);
          if (renderer instanceof RendererBase) {
            height = renderer.getFixedHeight(facesContext, component);
          }
        }
        maxHeight = Math.max(maxHeight, height);
      }
    }
    return maxHeight;
  }

  private void setColumnWidths(UIGridLayout layout, LayoutInfo layoutInfo,
      FacesContext facesContext) {
    List rows = layout.ensureRows();

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
              TobagoConstants.ATTR_LAYOUT_WIDTH, new Integer(cellWidth));
        }
      }
    }
  }

  private void setColumnHeights(UIGridLayout layout, LayoutInfo layoutInfo,
      FacesContext facesContext) {
    List rows = layout.ensureRows();

    for (int i = 0; i < rows.size(); i++) {
      UIGridLayout.Row row = (UIGridLayout.Row) rows.get(i);
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
          cell.getAttributes().put(TobagoConstants.ATTR_LAYOUT_HEIGHT,
              new Integer(cellHeight));
          cell.getAttributes().remove(ATTR_INNER_HEIGHT);
          if (cell instanceof UIPanel
              && ComponentUtil.getBooleanAttribute(cell,
                  TobagoConstants.ATTR_LAYOUT_DIRECTIVE)
          || cell instanceof UIForm) {
            List children = LayoutUtil.addChildren(new ArrayList(), cell);
            for (Iterator childs = children.iterator(); childs.hasNext();) {
              UIComponent component = (UIComponent) childs.next();
              if (LOG.isDebugEnabled()) {
                LOG.debug("set height of " + cellHeight + "px to "
                    + component.getRendererType());
              }
              component.getAttributes().put(TobagoConstants.ATTR_LAYOUT_HEIGHT,
                  new Integer(cellHeight));
              component.getAttributes().remove(ATTR_INNER_HEIGHT);

            }
          }
        }
      }
    }
  }

// ///////////////////////////////////////////// LayoutManager implementation

  public void layoutBegin(FacesContext facesContext, UIComponent component) {
    LOG.info("############################## layoutBegin +++++++++++++++++++++++++++++++++++++++++");
    HtmlRendererUtil.layoutSpace(facesContext, component, true);
    HtmlRendererUtil.layoutSpace(facesContext, component, false);

    if (component instanceof UIGridLayout) {
      layoutMargins((UIGridLayout) component);
    }
  }

  private void layoutMargins(UIGridLayout layout) {
    String margin
        = (String) layout.getAttributes().get(
            TobagoConstants.ATTR_LAYOUT_MARGIN);
    String marginTop
        = getMargin(layout, TobagoConstants.ATTR_LAYOUT_MARGIN_TOP, margin);
    String marginRight
        = getMargin(layout, TobagoConstants.ATTR_LAYOUT_MARGIN_RIGHT, margin);
    String marginBottom
        = getMargin(layout, TobagoConstants.ATTR_LAYOUT_MARGIN_BOTTOM, margin);
    String marginLeft
        = getMargin(layout, TobagoConstants.ATTR_LAYOUT_MARGIN_LEFT, margin);

    String style = (String) layout.getAttributes().get(
        TobagoConstants.ATTR_STYLE);

    if (style != null) {
      style = addStyle(style, "margin-top", marginTop);
      style = addStyle(style, "margin-right", marginRight);
      style = addStyle(style, "margin-bottom", marginBottom);
      style = addStyle(style, "margin-left", marginLeft);

      layout.getAttributes().put(TobagoConstants.ATTR_LAYOUT_TABLE_STYLE,
          style);
    }
  }

  private String addStyle(String style, String attribute, String value) {
    if (value != null) {
      style += " " + attribute + ": " + value + ";";
    }
    return style;
  }

  private String getMargin(UIGridLayout layout, String attribute,
      String defaultMargin) {
    String margin = (String) layout.getAttributes().get(attribute);
    if (margin == null && defaultMargin != null) {
      margin = defaultMargin;
    }
    return margin;
  }

  public int getComponentExtraWidth(FacesContext facesContext,
      UIComponent component) {
    int extra = 0;
    UIGridLayout layout = (UIGridLayout) component;
    String margin
        = (String) layout.getAttributes().get(
            TobagoConstants.ATTR_LAYOUT_MARGIN);

    extra += getMaginAsInt(
        getMargin(layout, TobagoConstants.ATTR_LAYOUT_MARGIN_RIGHT, margin));
    extra += getMaginAsInt(
        getMargin(layout, TobagoConstants.ATTR_LAYOUT_MARGIN_LEFT, margin));

    return extra;
  }

  public int getComponentExtraHeight(FacesContext facesContext,
      UIComponent component) {
    int extra = 0;
    UIGridLayout layout = (UIGridLayout) component;
    String margin
        = (String) layout.getAttributes().get(
            TobagoConstants.ATTR_LAYOUT_MARGIN);

    extra += getMaginAsInt(
        getMargin(layout, TobagoConstants.ATTR_LAYOUT_MARGIN_TOP, margin));
    extra += getMaginAsInt(
        getMargin(layout, TobagoConstants.ATTR_LAYOUT_MARGIN_BOTTOM, margin));
    return extra;
  }

  private int getMaginAsInt(String margin) {
    int intValue = 0;
    if (margin != null) {
      try {
        intValue += Integer.parseInt(margin.replaceAll("\\D", ""));
      } catch (NumberFormatException e) {
        if (LOG.isWarnEnabled()) {
          LOG.warn("Illegal Margin : " + margin
              + " exception : " + e.getMessage(), e);
        }
      }
    }
    return intValue;
  }

// ///////////////////////////////////////////// bean getter + setter


}

