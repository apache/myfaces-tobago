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

package org.apache.myfaces.tobago.example.addressbook.web;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.myfaces.tobago.component.UIInput;
import org.apache.myfaces.tobago.example.addressbook.Log4jUtils;
import org.apache.myfaces.tobago.model.SelectItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

@Named("logging")
@SessionScoped
public class LoggingController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(LoggingController.class);

  private List<AppenderModel> appenders = new ArrayList<AppenderModel>();
  private List<CategoryModel> categories = new ArrayList<CategoryModel>();
  private List<SelectItem> levels = new ArrayList<SelectItem>();
  private String category;
  private UIInput categoryControl;
  private String level;
  private UIParameter currentCategory;
  private UIParameter currentAppender;

  private int catergoriesFirst;
  private int catergoriesRows;

  public LoggingController() {
      LOG.debug("initializing...");
      catergoriesRows = 8;
      initAppenders();
      initCategories();

      levels.add(new SelectItem(Level.FATAL.toString()));
      levels.add(new SelectItem(Level.ERROR.toString()));
      levels.add(new SelectItem(Level.WARN.toString()));
      levels.add(new SelectItem(Level.INFO.toString()));
      levels.add(new SelectItem(Level.DEBUG.toString()));
      levels.add(new SelectItem(Level.TRACE.toString()));
  }

  private void initCategories() {
      categories.clear();
      Enumeration enumeration = LogManager.getCurrentLoggers();
      while (enumeration.hasMoreElements()) {
          org.apache.log4j.Logger logger = (org.apache.log4j.Logger) enumeration.nextElement();
          categories.add(new CategoryModel(logger));
      }
      categories.add(new CategoryModel(LogManager.getRootLogger()));

      Collections.sort(categories, new Comparator<CategoryModel>() {
          public int compare(CategoryModel c1, CategoryModel c2) {
              org.apache.log4j.Logger l1 = c1.getLogger();
              org.apache.log4j.Logger l2 = c2.getLogger();
              return l1.getName().compareTo(l2.getName());
          }
      });
  }

  private void initAppenders() {
      appenders.clear();
      Set<Appender> allAppenders = Log4jUtils.getAllAppenders();
      for (Appender appender : allAppenders) {
          appenders.add(new AppenderModel(appender));
      }
  }

  public String downloadLogFile() throws IOException {
      AppenderModel appender = (AppenderModel) currentAppender.getValue();
      String fileName = appender.getFile();
      FileInputStream stream = new FileInputStream(fileName);
      FacesContext facesContext = FacesContext.getCurrentInstance();
      ServletResponse response = (ServletResponse) facesContext.getExternalContext().getResponse();
      response.setContentType("text/plain");
      IOUtils.copy(stream, response.getOutputStream());
      facesContext.responseComplete();
      return null;
  }

  public String updateCategories() {
      boolean update = false;
      for (CategoryModel category : categories) {
          if (category.isLevelUpdated()) {
              org.apache.log4j.Logger logger = getLogger(category.getName());
              logger.setLevel(Level.toLevel(category.getLevel()));
              update = true;
          }
      }
      if (update) {
          initCategories();
      }
      return null;
  }

  public String addCategory() {
      LOG.debug("debug");
      LOG.trace("trace");
      org.apache.log4j.Logger logger = getLogger(category);
      logger.setLevel(Level.toLevel(level));
      initCategories();
      return null;
  }

  public String selectCategory() {
      category = ((CategoryModel) currentCategory.getValue()).getName();
      categoryControl.setSubmittedValue(category);
      return null;
  }

  private org.apache.log4j.Logger getLogger(String category) {
      return ("root".equals(category))
              ? LogManager.getRootLogger()
              : LogManager.getLogger(category);
  }

  public List<AppenderModel> getAppenders() {
      return appenders;
  }

  public List<CategoryModel> getCategories() {
      return categories;
  }

  public List<SelectItem> getLevels() {
      return levels;
  }

  public String getCategory() {
      return category;
  }

  public void setCategory(String category) {
      this.category = category;
  }

  public UIInput getCategoryControl() {
      return categoryControl;
  }

  public void setCategoryControl(UIInput categoryControl) {
      this.categoryControl = categoryControl;
  }

  public String getLevel() {
      return level;
  }

  public void setLevel(String level) {
      this.level = level;
  }

  public UIParameter getCurrentCategory() {
      return currentCategory;
  }

  public void setCurrentCategory(UIParameter currentCategory) {
      this.currentCategory = currentCategory;
  }

  public int getCatergoriesFirst() {
      return catergoriesFirst;
  }

  public void setCatergoriesFirst(int catergoriesFirst) {
      this.catergoriesFirst = catergoriesFirst;
  }

  public int getCatergoriesRows() {
      return catergoriesRows;
  }

  public void setCatergoriesRows(int catergoriesRows) {
      this.catergoriesRows = catergoriesRows;
  }

  public UIParameter getCurrentAppender() {
      return currentAppender;
  }

  public void setCurrentAppender(UIParameter currentAppender) {
      this.currentAppender = currentAppender;
  }

}
