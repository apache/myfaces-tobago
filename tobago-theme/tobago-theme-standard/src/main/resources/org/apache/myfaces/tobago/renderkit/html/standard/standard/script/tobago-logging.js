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

/**
 * Logging helper class.
 * All calls with level higher than LOG.level (with default LOG.INFO) are logged into the console object (if any).
 * To set the level you may put this tag into the tc:page
 *       <tc:dataAttribute name="tobago-log-level" value="DEBUG"/>
 * This methods are only used in development mode, not in production mode.
 */
var LOG = {

  DEBUG: 1,
  INFO:  2,
  WARN:  3,
  ERROR: 4,

  hasConsole: typeof console != "undefined",
  level: 2, // INFO
  maximumSeverity: 0, // lower than DEBUG

  init: function(elements) {
    var page = Tobago.Utils.selectWidthJQuery(elements, "[data-tobago-log-level]");
    var levelString = page.data("tobago-log-level");
    switch (levelString) {
      case "DEBUG":
        this.level = this.DEBUG;
        break;
      case "INFO":
        this.level = this.INFO;
        break;
      case "WARN":
        this.level = this.WARN;
        break;
      case "ERROR":
        this.level = this.ERROR;
        break;
    }
  },

  getMaximumSeverity: function() {
    return this.maximumSeverity;
  },

  adjustMaximumSeverity: function (currentSeverity) {
    if (currentSeverity >= this.maximumSeverity) {
      this.maximumSeverity = currentSeverity;
    }
  },

  debug: function(text) {
    this.adjustMaximumSeverity(this.DEBUG);
    if (this.hasConsole && this.DEBUG >= this.level) {
      console.log(text);
    }
  },

  info  : function(text) {
    this.adjustMaximumSeverity(this.INFO);
    if (this.hasConsole && this.INFO >= this.level) {
      console.info(text);
    }
  },

  warn: function(text) {
    this.adjustMaximumSeverity(this.WARN);
    if (this.hasConsole && this.WARN >= this.level) {
      console.warn(text);
    }
  },

  error: function(text) {
    this.adjustMaximumSeverity(this.ERROR);
    if (this.hasConsole && this.ERROR >= this.level) {
      console.error(text);
    }
  },

  /** @deprecated */
  show: function() {
    this.warn("Menthod LOG.show() is deprecated!");
  },

  /** @deprecated */
  getMessages: function(severity) {
    this.warn("Menthod LOG.getMessages() is deprecated!");
  },

  /** @deprecated */
  getSeverityName: function(severity) {
    this.warn("Menthod LOG.getSeverityName() is deprecated!");
  },

  /** @deprecated */
  addAppender: function(appender) {
    this.warn("Menthod LOG.addAppender() is deprecated!");
  },

  /** @deprecated */
  addMessage: function(msg) {
    this.warn("Menthod LOG.addMessage() is deprecated!");
  },

  /** @deprecated */
  bindOnWindow: function() {
    this.warn("Menthod LOG.bindOnWindow() is deprecated!");
  },

  /** @deprecated */
  listScriptFiles: function() {
    this.warn("Menthod LOG.listScriptFiles() is deprecated!");
  },

  /** @deprecated */
  listThemeConfig : function() {
    this.warn("Menthod LOG.listThemeConfig() is deprecated!");
  },

  /** @deprecated */
  debugAjaxComponents: function() {
    this.warn("Menthod LOG.debugAjaxComponents() is deprecated!");
  }
};
