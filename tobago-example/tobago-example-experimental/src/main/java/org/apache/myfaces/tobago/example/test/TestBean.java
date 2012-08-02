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

import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.component.UIColumn;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UIMenu;
import org.apache.myfaces.tobago.component.UIMenuCommand;
import org.apache.myfaces.tobago.component.UISheet;
import org.apache.myfaces.tobago.example.data.SolarObject;

import javax.faces.application.Application;
import javax.faces.component.UIOutput;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Date;
import java.util.Map;

/*
 * Date: 18.02.2006
 * Time: 11:08:45
 */
public class TestBean {
  private static final Logger LOG = LoggerFactory.getLogger(TestBean.class);

  private ResultSet resultSet = null;
  private Connection connection = null;

  private String name;
  private String number;
  private String orbit;
  private String distance;
  private String period;
  private String incl;
  private String eccen;
  private String discoverer;
  private String discoverYear;
  private FileItem file;
  private UISheet table;
  private String value;
  private Date date;
  private Date date1;
  private UIMenu fileMenu;


  public UIPanel getFileMenu() {
    if (fileMenu == null) {
      FacesContext context = FacesContext.getCurrentInstance();

      fileMenu = (UIMenu) context.getApplication().createComponent(UIMenu.COMPONENT_TYPE);

      fileMenu.getAttributes().put("label", "File");
    }
    if (fileMenu.getChildCount() == 0) {
      for(int i = 0; i < 5; i++) {
        addMenuCommand(fileMenu);
      }
    }

    return fileMenu;

  }

  private void addMenuCommand(UIMenu fileMenu) {
    UIMenuCommand command = (UIMenuCommand)
        FacesContext.getCurrentInstance().getApplication().createComponent(UIMenuCommand.COMPONENT_TYPE);
    command.getAttributes().put("label", "test"+fileMenu.getChildCount());
    // TODO setAction
    fileMenu.getChildren().add(command);
  }

  public String layout() {
    this.date1 = date;
    return "layout";
  }

  public String export() throws IOException {
    FacesContext context =  FacesContext.getCurrentInstance();
    ExportUIDataToWorkbookUtils.writeWorkbook(table, "workbook.xls", context);
    context.responseComplete();
    return null;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Date getDate1() {
    return date1;
  }

  public void setDate1(Date date1) {
    this.date1 = date1;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public TestBean() {

    try {
      //Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
      Class.forName("org.hsqldb.jdbcDriver").newInstance();
      LOG.info("Loaded the appropriate driver.");

      //Properties props = new Properties();
      //props.put("user", "user1");
      //props.put("password", "user1");

      /*
        The connection specifies create=true to cause
        the database to be created. To remove the database,
        remove the directory derbyDB and its contents.
        The directory derbyDB will be created under
        the directory that the system property
        derby.system.home points to, or the current
        directory if derby.system.home is not set.
      */
      //Connection conn = DriverManager.getConnection("jdbc:derby:" +
      //    "derbyDB;create=true", props);
      connection = DriverManager.getConnection("jdbc:hsqldb:mem:aname", "sa", "");

      LOG.info("Connected to and created database derbyDB");

      connection.setAutoCommit(false);

      Statement statement = connection.createStatement();
      try {
        statement.execute("create table solarObject(name varchar(10), number varchar(5), "
            + "orbit varchar(10), distance INTEGER, period FLOAT, "
            + "incl FLOAT, eccen FLOAT, discoverer varchar(20), "
            + "discoverYear INTEGER)");
        PreparedStatement ps =
            connection.prepareStatement("insert into solarObject values (?,?,?,?,?,?,?,?,?)");
        for (SolarObject solarObject : SolarObject.DATA) {
          ps.setString(1, solarObject.getName());
          ps.setString(2, solarObject.getNumber());
          ps.setString(3, solarObject.getOrbit());
          ps.setInt(4, solarObject.getDistance());
          ps.setDouble(5, solarObject.getPeriod());
          ps.setDouble(6, solarObject.getIncl());
          ps.setString(7, solarObject.getPopulation());
          ps.setString(8, solarObject.getDiscoverer());
          
          int inserted = ps.executeUpdate();
          if (LOG.isDebugEnabled()) {
            LOG.debug(inserted + " Row(s) inserted");
          }
        }
        connection.commit();
      } catch (SQLException e) {
        LOG.error("", e);
      }

      Statement query = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
      resultSet = query.executeQuery("select * from solarObject");


    } catch (Exception e) {
      LOG.error("", e);
    }
  }


  public UISheet getTable() {
    return table;
  }

  public void setTable(UISheet table) {
    this.table = table;
    if (table.getChildCount() == 0) {
      if (table.getVar() == null) {
        table.setVar("solarObject");
      }
      Application application = FacesContext.getCurrentInstance().getApplication();
      try {
        ResultSetMetaData metaData = resultSet.getMetaData();
        String columns = "";
        for (int i = 1; i <= metaData.getColumnCount(); i++) {

          UIColumn column = (UIColumn) application.createComponent(UIColumn.COMPONENT_TYPE);
          String name = metaData.getColumnName(i);
          int displaySize = metaData.getColumnDisplaySize(i);

          if (i > 1) {
            columns += ";";
          }
          if (displaySize < 10) {
            columns += "1*";
          } else if (displaySize < 20) {
            columns += "2*";
          } else {
            columns += "4*";
          }
          if (metaData.getColumnType(i) == Types.INTEGER || metaData.getColumnType(i) == Types.FLOAT) {
            column.setAlign("right");
          }
          column.setLabel(name);
          String ref = "#{" + table.getVar() + "." + name + "}";
          ValueBinding binding = application.createValueBinding(ref);
          if (name.equals("NAME")) {
            UICommand command = (UICommand) application.createComponent(UICommand.COMPONENT_TYPE);
            command.setRendererType("Link");
            command.setValueBinding("label", binding);
            MethodBinding action = application.createMethodBinding("#{test.select}", new Class[0]);
            command.setAction(action);
            column.getChildren().add(command);
          } else {
            UIOutput output = (UIOutput) application.createComponent(UIOutput.COMPONENT_TYPE);
            output.setValueBinding("value", binding);
            column.getChildren().add(output);
          }
          table.getChildren().add(column);
        }

        table.setColumns(columns);
      } catch (SQLException e) {
        LOG.error("", e);
      }

    }
  }

  public String getName() {
    return name;
  }

  public String getNumber() {
    return number;
  }

  public String getOrbit() {
    return orbit;
  }

  public String getDistance() {
    return distance;
  }

  public String getPeriod() {
    return period;
  }

  public String getIncl() {
    return incl;
  }

  public String getEccen() {
    return eccen;
  }

  public String getDiscoverer() {
    return discoverer;
  }

  public String getDiscoverYear() {
    return discoverYear;
  }

  public String select() {
    try {
      Map rowData = (Map) table.getRowData();

      name = (String) rowData.get("NAME");
      number = (String) rowData.get("NUMBER");
      orbit = (String) rowData.get("ORBIT");
      distance = (String) rowData.get("DISTANCE");
      period = (String) rowData.get("PERIOD");
      incl = (String) rowData.get("INCL");
      eccen = (String) rowData.get("ECCEN");
      discoverer = (String) rowData.get("DISCOVERER");
      discoverYear = (String) rowData.get("DISCOVERYEAR");
    } catch (Exception e) {
      LOG.error("", e);
    }
    return "solarDetail";
  }

  public String select(String id) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = connection.prepareStatement("select * from solarObject where name = ? ");
      ps.setString(1, id);
      rs = ps.executeQuery();
      if (rs.next()) {
        name = rs.getString(1);
        number = rs.getString(2);
        orbit = rs.getString(3);
        distance = rs.getString(4);
        period = rs.getString(5);
        incl = rs.getString(6);
        eccen = rs.getString(7);
        discoverer = rs.getString(8);
        discoverYear = rs.getString(9);
        return "solarDetail";
      } else {
        name = null;
        number = null;
        orbit = null;
        distance = null;
        period = null;
        incl = null;
        eccen = null;
        discoverer = null;
        discoverYear = null;
      }
    } catch (SQLException e) {
      LOG.error("", e);
    } finally {
      if (rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {
          // ignore
        }
      }
      if (ps != null) {
        try {
          ps.close();
        } catch (SQLException e) {
          // ignore
        }
      }
    }

    return "solarList";

  }

  public FileItem getFile() {
    return file;
  }

  public void setFile(FileItem file) {
    LOG.error("Setting fileItem " + file);
    this.file = file;
  }

  public ResultSet getSolarObjects() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("getting solar objects");
    }
    return resultSet;
  }
}
