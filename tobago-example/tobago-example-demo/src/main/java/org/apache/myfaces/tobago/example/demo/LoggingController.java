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

package org.apache.myfaces.tobago.example.demo;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.status.Status;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * Provides the possibility to get information about various logging APIs available in the current setup.
 */
@Named
@ApplicationScoped
public class LoggingController {

  private static final PrintStream LOG = System.err;

  public static final String JUL = "JUL";
  public static final String SLF4J = "SLF4J";
  public static final String LOG4J = "LOG4J";
  public static final String LOG4J2 = "LOG4J2";
  public static final String JCL = "JCL";

  /**
   * should always be available, because it's part of Java since 1.4
   */
  private final LoggingInfo jul;

  /**
   * should always be true, because it's used by Tobago
   */
  private final LoggingInfo slf4j;

  private final LoggingInfo log4j;

  private final LoggingInfo log4j2;

  private final LoggingInfo commonsLogging;

  private String testCategory = LoggingController.class.getName();

  public LoggingController() {

/*
    LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
    JoranConfigurator configurator = new JoranConfigurator();
    configurator.setContext(context);
    XXX ... to be continued ...
*/

    jul = new LoggingInfo(JUL, "java.util.logging.Logger", "getLogger", testCategory, true,
        "finest",
        "finer",
        "fine",
        "config",
        "info",
        "warning",
        "severe");
    slf4j = new LoggingInfo(SLF4J, "org.slf4j.LoggerFactory", "getLogger", testCategory, true,
        "trace",
        "debug",
        "info",
        "warn",
        "error");
    log4j = new LoggingInfo(LOG4J, "org.apache.log4j.Logger", "getLogger", testCategory, false,
        "trace",
        "debug",
        "info",
        "warn",
        "error",
        "fatal");
    log4j2 = new LoggingInfo(LOG4J2, "org.apache.logging.log4j.LogManager", "getLogger", testCategory, false,
        "trace",
        "debug",
        "info",
        "warn",
        "error",
        "fatal");
    commonsLogging = new LoggingInfo(JCL, "org.apache.commons.logging.LogFactory", "getLog", testCategory, false,
        "trace",
        "debug",
        "info",
        "warn",
        "error",
        "fatal");
    update();
  }

  public String update() {
    jul.reset(testCategory);
    slf4j.reset(testCategory);
    log4j.reset(testCategory);
    log4j2.reset(testCategory);
    commonsLogging.reset(testCategory);
    return null;
  }

  public LoggingInfo getJul() {
    return jul;
  }

  public LoggingInfo getSlf4j() {
    return slf4j;
  }

  public LoggingInfo getLog4j() {
    return log4j;
  }

  public LoggingInfo getLog4j2() {
    return log4j2;
  }

  public LoggingInfo getCommonsLogging() {
    return commonsLogging;
  }

  public String getTestCategory() {
    return testCategory;
  }

  public void setTestCategory(final String testCategory) {
    if (testCategory != null) {
      this.testCategory = testCategory;
    } else {
      this.testCategory = "";
    }
  }

  public List<Status> getStatusList() {
    return ((LoggerContext) LoggerFactory.getILoggerFactory()).getStatusManager().getCopyOfStatusList();
  }

  public static String logbackLevel(final Integer level) {
    switch (level) {
      case 0:
        return "info";
      case 1:
        return "warn";
      case 2:
        return "error";
      default:
        return "unknown";
    }
  }

  public static Date logbackDate(final Long millis) {
    return new Date(millis);
  }

  public static class LoggingInfo {

    private String id;
    private Object logger;
    private String[] calls;
    private String activeLevels = "n/a";
    private String factoryClassName;
    private String factoryMethod;
    private boolean usesString; // is false it uses "Object" for logging

    public LoggingInfo(
        final String id, final String factoryClassName, final String factoryMethod,
        final String category, final boolean usesString, final String... calls) {
      this.id = id;
      this.calls = calls;
      this.factoryClassName = factoryClassName;
      this.factoryMethod = factoryMethod;
      this.usesString = usesString;
      reset(category);
    }

    public void logDemo() {
      for (final String call : calls) {
        try {
          invoke(id, logger, call);
        } catch (final Exception e) {
          LOG.println("Ignoring: " + e);
        }
      }
    }

    private void invoke(final String idString, final Object loggerObject, final String nameString) throws Exception {
      final Class clazz = usesString ? String.class : Object.class;
      final Method method = loggerObject.getClass().getMethod(nameString, clazz);
      method.invoke(loggerObject, "Hello " + idString + ", this is the level: " + nameString);
    }

    public boolean isAvailable() {
      return logger != null;
    }

    public String getActiveLevels() {
      return activeLevels;
    }

    protected void reset(final String category) {

      logger = null;

      try {
        final Method method = Class.forName(factoryClassName).getMethod(factoryMethod, String.class);
        logger = method.invoke(null, category);
      } catch (final Exception e) {
        LOG.println("Ignoring: " + e);
      }

      if (logger != null) {
        StringBuilder builder = new StringBuilder();
        for (final String call : calls) {
          try {
            if (checkLevel(category, call)) {
              builder.append(call);
              builder.append(":+ ");
            } else {
              builder.append(call);
              builder.append(":- ");
            }
          } catch (final Exception e) {
            LOG.println(e.getMessage());
            e.printStackTrace();
            builder.append(call);
            builder.append(":? ");
          }
        }
        activeLevels = builder.toString();
      } else {
        activeLevels = "n/a";
      }
    }

    private boolean checkLevel(final String category, final String level)
        throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {

      final Method method = Class.forName(factoryClassName).getMethod(factoryMethod, String.class);
      final Object c = method.invoke(null, category);

      if (JUL.equals(id)) {
        return checkLevelGeneric(level, c, "java.util.logging.Level", "isLoggable", "parse");
      }

      if (LOG4J.equals(id)) {
        return checkLevelGeneric(level, c, "org.apache.log4j.Priority", "isEnabledFor", "toPriority");
      }

      if (LOG4J2.equals(id)) {
        return checkLevelGeneric(level, c, "org.apache.logging.log4j.Level", "isEnabled", "getLevel");
      }

      if (SLF4J.equals(id) || JCL.equals(id)) {
        final String methodName = "is" + level.substring(0, 1).toUpperCase() + level.substring(1) + "Enabled";
        final Object hasLevel = c.getClass().getMethod(methodName).invoke(c);
        return (Boolean) hasLevel;
      }

      throw new IllegalStateException();
    }

    private boolean checkLevelGeneric(
        final String level, final Object c, final String clazz, final String enabledMethod, final String levelMethod)
        throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      final Class<?> levelClass = Class.forName(clazz);
      final Method isLoggable = c.getClass().getMethod(enabledMethod, levelClass);
      final Method parse = levelClass.getMethod(levelMethod, String.class);
      final Object levelObject = parse.invoke(null, level.toUpperCase());
      final Object hasLevel = isLoggable.invoke(c, levelObject);
      return (Boolean) hasLevel;
    }
  }
}
