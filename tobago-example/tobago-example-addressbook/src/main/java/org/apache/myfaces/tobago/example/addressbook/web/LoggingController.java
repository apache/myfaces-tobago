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
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.myfaces.tobago.component.UIIn;
import org.apache.myfaces.tobago.example.addressbook.Log4jUtils;
import org.apache.myfaces.tobago.model.SelectItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.servlet.ServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component("logging")
@Scope("session")
public class LoggingController {

  private static final Logger LOG = LoggerFactory.getLogger(LoggingController.class);

  private List<AppenderModel> appenders = new ArrayList<AppenderModel>();
  private List<CategoryModel> categories = new ArrayList<CategoryModel>();
  private List<SelectItem> levels = new ArrayList<SelectItem>();
  private String category;
  private UIIn categoryControl;
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
    Configuration configuration = ((LoggerContext) LogManager.getContext(false)).getConfiguration();
    Map<String, LoggerConfig> loggers = configuration.getLoggers();
    for (LoggerConfig loggerConfig : loggers.values()) {
      categories.add(new CategoryModel(loggerConfig));
    }
    categories.add(new CategoryModel(configuration.getRootLogger()));

    Collections.sort(categories, new Comparator<CategoryModel>() {
      public int compare(final CategoryModel c1, final CategoryModel c2) {
        final LoggerConfig l1 = c1.getLogger();
        final LoggerConfig l2 = c2.getLogger();
        return l1.getName().compareTo(l2.getName());
      }
    });
  }

  private void initAppenders() {
      appenders.clear();
      final Set<Appender> allAppenders = Log4jUtils.getAllAppenders();
      for (final Appender appender : allAppenders) {
          appenders.add(new AppenderModel(appender));
      }
  }

  public String downloadLogFile() throws IOException {
      final AppenderModel appender = (AppenderModel) currentAppender.getValue();
      final String fileName = appender.getFile();
      final FileInputStream stream = new FileInputStream(fileName);
      final FacesContext facesContext = FacesContext.getCurrentInstance();
      final ServletResponse response = (ServletResponse) facesContext.getExternalContext().getResponse();
      response.setContentType("text/plain");
      IOUtils.copy(stream, response.getOutputStream());
      facesContext.responseComplete();
      return null;
  }

  public String updateCategories() {
      boolean update = false;
      for (final CategoryModel categoryModel : categories) {
          if (categoryModel.isLevelUpdated()) {
              final LoggerConfig logger = getLogger(categoryModel.getName());
              logger.setLevel(Level.toLevel(categoryModel.getLevel()));
              update = true;
          }
      }
      if (update) {
        ((LoggerContext) LogManager.getContext(false)).updateLoggers();
        initCategories();
      }
      return null;
  }

  public String addCategory() {
    LOG.debug("debug");
    LOG.trace("trace");
    Configuration configuration = ((LoggerContext) LogManager.getContext(false)).getConfiguration();
    final LoggerConfig loggerConfig = getLogger(category);
    LoggerConfig specificConfig = loggerConfig;
    if (!loggerConfig.getName().equals(category)) {
      specificConfig = new LoggerConfig(category, Level.toLevel(level, (Level) null), true);
      specificConfig.setParent(loggerConfig);
      configuration.addLogger(category, specificConfig);
    }
    ((LoggerContext) LogManager.getContext(false)).updateLoggers();
    initCategories();
    return null;
  }

  public String selectCategory() {
      category = ((CategoryModel) currentCategory.getValue()).getName();
      categoryControl.setSubmittedValue(category);
      return null;
  }

  private LoggerConfig getLogger(final String categoryParameter) {
    final Configuration configuration = ((LoggerContext) LogManager.getContext(false)).getConfiguration();
    return "root".equals(categoryParameter)
              ? configuration.getRootLogger()
              : configuration.getLoggerConfig(categoryParameter);
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

  public void setCategory(final String category) {
      this.category = category;
  }

  public UIIn getCategoryControl() {
      return categoryControl;
  }

  public void setCategoryControl(final UIIn categoryControl) {
      this.categoryControl = categoryControl;
  }

  public String getLevel() {
      return level;
  }

  public void setLevel(final String level) {
      this.level = level;
  }

  public UIParameter getCurrentCategory() {
      return currentCategory;
  }

  public void setCurrentCategory(final UIParameter currentCategory) {
      this.currentCategory = currentCategory;
  }

  public int getCatergoriesFirst() {
      return catergoriesFirst;
  }

  public void setCatergoriesFirst(final int catergoriesFirst) {
      this.catergoriesFirst = catergoriesFirst;
  }

  public int getCatergoriesRows() {
      return catergoriesRows;
  }

  public void setCatergoriesRows(final int catergoriesRows) {
      this.catergoriesRows = catergoriesRows;
  }

  public UIParameter getCurrentAppender() {
      return currentAppender;
  }

  public void setCurrentAppender(final UIParameter currentAppender) {
      this.currentAppender = currentAppender;
  }

}
