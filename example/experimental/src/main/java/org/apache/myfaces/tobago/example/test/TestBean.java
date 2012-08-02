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

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.fileupload.FileItem;
import org.apache.myfaces.tobago.component.UIData;
import org.apache.myfaces.tobago.component.UIColumn;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UIMenu;
import org.apache.myfaces.tobago.component.UIMenuCommand;

import javax.faces.component.UIOutput;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.el.MethodBinding;
import javax.faces.application.Application;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.Map;
import java.util.Date;
import java.io.IOException;

/*
 * Date: 18.02.2006
 * Time: 11:08:45
 */
public class TestBean {
  private static final Log LOG = LogFactory.getLog(TestBean.class);

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
  private UIData table;
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
    ExportUIDataToWorkbookUtil.writeWorkbook(table, "workbook.xls", context);
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
        for (String[] aSTRINGS : STRINGS) {
          for (int j = 0; j < aSTRINGS.length; j++) {
            ps.setString(j + 1, aSTRINGS[j]);
          }
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


  public UIData getTable() {
    return table;
  }

  public void setTable(UIData table) {
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


  private static final String[][] STRINGS =
      {
          {"Sun", "-", "-", "0", "0", "0", "0", "-", "0"},
          {"Mercury", "I", "Sun", "57910", "87.97", "7.00", "0.21", "-", null},
          {"Venus", "II", "Sun", "108200", "224.70", "3.39", "0.01", "-", null},
          {"Earth", "III", "Sun", "149600", "365.26", "0.00", "0.02", "-", null},
          {"Mars", "IV", "Sun", "227940", "686.98", "1.85", "0.09", "-", null},
          {"Jupiter", "V", "Sun", "778330", "4332.71", "1.31", "0.05", "-", null},
          {"Saturn", "VI", "Sun", "1429400", "10759.50", "2.49", "0.06", "-", null},
          {"Uranus", "VII", "Sun", "2870990", "30685.00", "0.77", "0.05", "Herschel", "1781"},
          {"Neptune", "VIII", "Sun", "4504300", "60190.00", "1.77", "0.01", "Adams", "1846"},
          {"Pluto", "IX", "Sun", "5913520", "90800", "17.15", "0.25", "Tombaugh", "1930"},
          {"Moon", "I", "Earth", "384", "27.32", "5.14", "0.05", "-", null},
          {"Phobos", "I", "Mars", "9", "0.32", "1.00", "0.02", "Hall", "1877"},
          {"Deimos", "II", "Mars", "23", "1.26", "1.80", "0.00", "Hall", "1877"},
          {"Metis", "XVI", "Jupiter", "128", "0.29", "0.00", "0.00", "Synnott", "1979"},
          {"Adrastea", "XV", "Jupiter", "129", "0.30", "0.00", "0.00", "Jewitt", "1979"},
          {"Amalthea", "V", "Jupiter", "181", "0.50", "0.40", "0.00", "Barnard", "1892"},
          {"Thebe", "XIV", "Jupiter", "222", "0.67", "0.80", "0.02", "Synnott", "1979"},
          {"Io", "I", "Jupiter", "422", "1.77", "0.04", "0.00", "Galileo", "1610"},
          {"Europa", "II", "Jupiter", "671", "3.55", "0.47", "0.01", "Galileo", "1610"},
          {"Ganymede", "III", "Jupiter", "1070", "7.15", "0.19", "0.00", "Galileo", "1610"},
          {"Callisto", "IV", "Jupiter", "1883", "16.69", "0.28", "0.01", "Galileo", "1610"},
          {"Themisto", "XVIII", "Jupiter", "7507", "0", null, null, "Sheppard", "2000"},
          {"Leda", "XIII", "Jupiter", "11094", "238.72", "27.00", "0.15", "Kowal", "1974"},
          {"Himalia", "VI", "Jupiter", "11480", "250.57", "28.00", "0.16", "Perrine", "1904"},
          {"Lysithea", "X", "Jupiter", "11720", "259.22", "29.00", "0.11", "Nicholson", "1938"},
          {"Elara", "VII", "Jupiter", "11737", "259.65", "28.00", "0.21", "Perrine", "1905"},
          {"Ananke", "XII", "Jupiter", "21200", "-631", "147.00", "0.17", "Nicholson", "1951"},
          {"Carme", "XI", "Jupiter", "22600", "-692", "163.00", "0.21", "Nicholson", "1938"},
          {"Pasiphae", "VIII", "Jupiter", "23500", "-735", "147.00", "0.38", "Melotte", "1908"},
          {"Sinope", "IX", "Jupiter", "23700", "-758", "153.00", "0.28", "Nicholson", "1914"},
          {"Iocaste", "XXIV", "Jupiter", "20216", "0", null, null, "Sheppard", "2000"},
          {"Harpalyke", "XXII", "Jupiter", "21132", "0", null, null, "Sheppard", "2000"},
          {"Praxidike", "XXVII", "Jupiter", "20964", "0", null, null, "Sheppard", "2000"},
          {"Taygete", "XX", "Jupiter", "23312", "0", null, null, "Sheppard", "2000"},
          {"Chaldene", "XXI", "Jupiter", "23387", "0", null, null, "Sheppard", "2000"},
          {"Kalyke", "XXIII", "Jupiter", "23745", "0", null, null, "Sheppard", "2000"},
          {"Callirrhoe", "XVII", "Jupiter", "24100", "0", null, null, "Sheppard", "2000"},
          {"Megaclite", "XIX", "Jupiter", "23911", "0", null, null, "Sheppard", "2000"},
          {"Isonoe", "XXVI", "Jupiter", "23078", "0", null, null, "Sheppard", "2000"},
          {"Erinome", "XXV", "Jupiter", "23168", "0", null, null, "Sheppard", "2000"},
          {"Pan", "XVIII", "Saturn", "134", "0.58", "0.00", "0.00", "Showalter", "1990"},
          {"Atlas", "XV", "Saturn", "138", "0.60", "0.00", "0.00", "Terrile", "1980"},
          {"Prometheus", "XVI", "Saturn", "139", "0.61", "0.00", "0.00", "Collins", "1980"},
          {"Pandora", "XVII", "Saturn", "142", "0.63", "0.00", "0.00", "Collins", "1980"},
          {"Epimetheus", "XI", "Saturn", "151", "0.69", "0.34", "0.01", "Walker", "1980"},
          {"Janus", "X", "Saturn", "151", "0.69", "0.14", "0.01", "Dollfus", "1966"},
          {"Mimas", "I", "Saturn", "186", "0.94", "1.53", "0.02", "Herschel", "1789"},
          {"Enceladus", "II", "Saturn", "238", "1.37", "0.02", "0.00", "Herschel", "1789"},
          {"Tethys", "III", "Saturn", "295", "1.89", "1.09", "0.00", "Cassini", "1684"},
          {"Telesto", "XIII", "Saturn", "295", "1.89", "0.00", "0.00", "Smith", "1980"},
          {"Calypso", "XIV", "Saturn", "295", "1.89", "0.00", "0.00", "Pascu", "1980"},
          {"Dione", "IV", "Saturn", "377", "2.74", "0.02", "0.00", "Cassini", "1684"},
          {"Helene", "XII", "Saturn", "377", "2.74", "0.20", "0.01", "Laques", "1980"},
          {"Rhea", "V", "Saturn", "527", "4.52", "0.35", "0.00", "Cassini", "1672"},
          {"Titan", "VI", "Saturn", "1222", "15.95", "0.33", "0.03", "Huygens", "1655"},
          {"Hyperion", "VII", "Saturn", "1481", "21.28", "0.43", "0.10", "Bond", "1848"},
          {"Iapetus", "VIII", "Saturn", "3561", "79.33", "14.72", "0.03", "Cassini", "1671"},
          {"Phoebe", "IX", "Saturn", "12952", "-550.48", "175.30", "0.16", "Pickering", "1898"},
          {"Cordelia", "VI", "Uranus", "50", "0.34", "0.14", "0.00", "Voyager 2", "1986"},
          {"Ophelia", "VII", "Uranus", "54", "0.38", "0.09", "0.00", "Voyager 2", "1986"},
          {"Bianca", "VIII", "Uranus", "59", "0.43", "0.16", "0.00", "Voyager 2", "1986"},
          {"Cressida", "IX", "Uranus", "62", "0.46", "0.04", "0.00", "Voyager 2", "1986"},
          {"Desdemona", "X", "Uranus", "63", "0.47", "0.16", "0.00", "Voyager 2", "1986"},
          {"Juliet", "XI", "Uranus", "64", "0.49", "0.06", "0.00", "Voyager 2", "1986"},
          {"Portia", "XII", "Uranus", "66", "0.51", "0.09", "0.00", "Voyager 2", "1986"},
          {"Rosalind", "XIII", "Uranus", "70", "0.56", "0.28", "0.00", "Voyager 2", "1986"},
          {"Belinda", "XIV", "Uranus", "75", "0.62", "0.03", "0.00", "Voyager 2", "1986"},
          {"1986U10", "", "Uranus", "76", "0.64", null, null, "Karkoschka", "1999"},
          {"Puck", "XV", "Uranus", "86", "0.76", "0.31", "0.00", "Voyager 2", "1985"},
          {"Miranda", "V", "Uranus", "130", "1.41", "4.22", "0.00", "Kuiper", "1948"},
          {"Ariel", "I", "Uranus", "191", "2.52", "0.00", "0.00", "Lassell", "1851"},
          {"Umbriel", "II", "Uranus", "266", "4.14", "0.00", "0.00", "Lassell", "1851"},
          {"Titania", "III", "Uranus", "436", "8.71", "0.00", "0.00", "Herschel", "1787"},
          {"Oberon", "IV", "Uranus", "583", "13.46", "0.00", "0.00", "Herschel", "1787"},
          {"Caliban", "XVI", "Uranus", "7169", "-580", "140.", "0.08", "Gladman", "1997"},
          {"Stephano", "XX", "Uranus", "7948", "-674", "143.", "0.24", "Gladman", "1999"},
          {"Sycorax", "XVII", "Uranus", "12213", "-1289", "153.", "0.51", "Nicholson", "1997"},
          {"Prospero", "XVIII", "Uranus", "16568", "-2019", "152.", "0.44", "Holman", "1999"},
          {"Setebos", "XIX", "Uranus", "17681", "-2239", "158.", "0.57", "Kavelaars", "1999"},
          {"Naiad", "III", "Neptune", "48", "0.29", "0.00", "0.00", "Voyager 2", "1989"},
          {"Thalassa", "IV", "Neptune", "50", "0.31", "4.50", "0.00", "Voyager 2", "1989"},
          {"Despina", "V", "Neptune", "53", "0.33", "0.00", "0.00", "Voyager 2", "1989"},
          {"Galatea", "VI", "Neptune", "62", "0.43", "0.00", "0.00", "Voyager 2", "1989"},
          {"Larissa", "VII", "Neptune", "74", "0.55", "0.00", "0.00", "Reitsema", "1989"},
          {"Proteus", "VIII", "Neptune", "118", "1.12", "0.00", "0.00", "Voyager 2", "1989"},
          {"Triton", "I", "Neptune", "355", "-5.88", "157.00", "0.00", "Lassell", "1846"},
          {"Nereid", "II", "Neptune", "5513", "360.13", "29.00", "0.75", "Kuiper", "1949"},
          {"Charon", "I", "Pluto", "20", "6.39", "98.80", "0.00", "Christy", "1978"}
      };

}

