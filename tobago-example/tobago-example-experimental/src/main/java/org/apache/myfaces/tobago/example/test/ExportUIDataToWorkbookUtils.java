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

package org.apache.myfaces.tobago.example.test;

import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.myfaces.tobago.component.UIColumnSelector;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.commons.lang.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIColumn;
import javax.faces.component.ValueHolder;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;


public class ExportUIDataToWorkbookUtils {

  public static void writeWorkbook(final UIData data, final String attachmentName, final FacesContext context)
      throws IOException {
    final HSSFWorkbook workbook = createWorkbook(data, context);
    final Object response = context.getExternalContext().getResponse();
    if (response instanceof HttpServletResponse) {
      final HttpServletResponse servletResponse = (HttpServletResponse) response;
      servletResponse.setContentType("application/vnd.ms-excel");
      if (StringUtils.isNotEmpty(attachmentName)) {
        servletResponse.setHeader("Content-Disposition", "attachment; filename=" + attachmentName);
      }
      workbook.write(servletResponse.getOutputStream());
    } else {
      //TODO PortletResponse
    }
  }

  private static HSSFWorkbook createWorkbook(final UIData table, final FacesContext context) {
    final HSSFWorkbook workbook = new HSSFWorkbook();
    final HSSFSheet sheet = workbook.createSheet(table.getId());
    final List<UIColumn> columns = getColumns(table);
    final int currentRowIndex = table.getRowIndex();
    addColumnHeaders(sheet, columns, context);
    addColumnValues(sheet, columns, table, context);
    table.setRowIndex(currentRowIndex);
    return workbook;
  }

  private static List<UIColumn> getColumns(final UIData table) {
    final List<UIColumn> columns = new ArrayList<UIColumn>();
    for (int i = 0; i < table.getChildCount(); i++) {
      final UIComponent child = table.getChildren().get(i);
      if (child instanceof UIColumn && !(child instanceof UIColumnSelector)) {
        columns.add((UIColumn) child);
      }
    }
    return columns;
  }

  private static void addColumnValue(
      final HSSFRow rowHeader, final UIComponent component, final int index, final FacesContext context) {
    final HSSFCell cell = rowHeader.createCell((short) index);
    cell.setEncoding(HSSFCell.ENCODING_UTF_16);
    if (component instanceof ValueHolder) {
      final String stringValue = RenderUtils.getFormattedValue(context, component);
      cell.setCellValue(stringValue);
    } else if (component instanceof org.apache.myfaces.tobago.component.UIColumn
        || component instanceof UICommand) {
      final String value = component.getAttributes().get("label") != null
          ? component.getAttributes().get("label").toString() : "";
      cell.setCellValue(value);
    }
  }

  private static void addColumnHeaders(
      final HSSFSheet sheet, final List<UIColumn> columns, final FacesContext context) {
    final HSSFRow rowHeader = sheet.createRow(0);
    for (int i = 0; i < columns.size(); i++) {
      final UIColumn column = columns.get(i);
      addColumnValue(rowHeader, column, i, context);
    }
  }

  private static void addColumnValues(
      final HSSFSheet sheet, final List<UIColumn> columns, final UIData table, final FacesContext context) {
    final int rowCount = table.getRowCount();
    if (rowCount == -1) {
      int index = 0;
      table.setRowIndex(index);
      while (table.isRowAvailable()) {
        addRow(sheet, index, columns, context);
        table.setRowIndex(++index);
      }
    } else {
      for (int i = 0; i < table.getRowCount(); i++) {
        table.setRowIndex(i);
        addRow(sheet, i, columns, context);
      }
    }
  }

  private static void addRow(
      final HSSFSheet sheet, final int index, final List<UIColumn> columns, final FacesContext context) {
    final HSSFRow row = sheet.createRow(1 + index);
    for (int j = 0; j < columns.size(); j++) {
      final UIColumn column = columns.get(j);
      addColumnValue(row, column.getChildren().get(0), j, context);
    }
  }
}
