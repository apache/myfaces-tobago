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

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Provides the possibility to get information about various logging APIs available in the current setup.
 */
@Named
@ApplicationScoped
public class UniversalLoggingInfo {

  private static final PrintStream LOG = System.err;

  public static final String JUL = "JUL";
  public static final String SLF4J = "SLF4J";
  public static final String LOG4J = "LOG4J";
  public static final String LOG4J2 = "LOG4J2";
  public static final String JCL = "JCL";

  /**
   * should always be available, because it's part of Java since 1.4
   */
  private LoggingInfo jul;

  /**
   * should always be true, because it's used by Tobago
   */
  private LoggingInfo slf4j;

  private LoggingInfo log4j;

  private LoggingInfo log4j2;

  private LoggingInfo commonsLogging;

  private String testCategory = UniversalLoggingInfo.class.getName();

  public UniversalLoggingInfo() {
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

  public void setTestCategory(String testCategory) {
    if (testCategory != null) {
      this.testCategory = testCategory;
    } else {
      this.testCategory = "";
    }
  }

  public static class LoggingInfo {

    private String id;
    private Object logger;
    private String[] calls;
    private String activeLevels = "n/a";
    private String factoryClassName;
    private String factoryMethod;
    private boolean usesString; // is false it uses "Object" for logging

    public LoggingInfo(String id, String factoryClassName, String factoryMethod,
                       String category, boolean usesString, String... calls) {
      this.id = id;
      this.calls = calls;
      this.factoryClassName = factoryClassName;
      this.factoryMethod = factoryMethod;
      this.usesString = usesString;
      reset(category);
    }

    public void logDemo() {
      for (String call : calls) {
        try {
          invoke(id, logger, call);
        } catch (Exception e) {
          LOG.println("Ignoring: " + e);
        }
      }
    }

    private void invoke(String id, Object logger, String name) throws Exception {
      final Class clazz = usesString ? String.class : Object.class;
      final Method method = logger.getClass().getMethod(name, clazz);
      method.invoke(logger, "Hello " + id + ", this is the level: " + name);
    }

    public boolean isAvailable() {
      return logger != null;
    }

    public String getActiveLevels() {
      return activeLevels;
    }

    protected void reset(String category) {

      logger = null;

      try {
        final Method method = Class.forName(factoryClassName).getMethod(factoryMethod, String.class);
        logger = method.invoke(null, category);
      } catch (Exception e) {
        LOG.println("Ignoring: " + e);
      }

      if (logger != null) {
        activeLevels = "";
        for (String call : calls) {
          try {
            if (checkLevel(category, call)) {
              activeLevels += call + ":+ ";
            } else {
              activeLevels += call + ":- ";
            }
          } catch (Exception e) {
            LOG.println(e.getMessage());
            e.printStackTrace();
            activeLevels += call + ":? ";
          }
        }
      } else {
        activeLevels = "n/a";
      }
    }

    private boolean checkLevel(String category, String level)
        throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {

      final Method method = Class.forName(factoryClassName).getMethod(factoryMethod, String.class);
      final Object c = method.invoke(null, category);

      if (JUL.equals(id)) {
//        c.isLoggable(Level.parse(level.toUpperCase()))
        final Class<?> levelClass = Class.forName("java.util.logging.Level");
        final Method isLoggable = c.getClass().getMethod("isLoggable", levelClass);
        final Method parse = levelClass.getMethod("parse", String.class);
        final Object levelObject = parse.invoke(null, level.toUpperCase());
        final Object hasLevel = isLoggable.invoke(c, levelObject);
        return (Boolean) hasLevel;
      }

      if (SLF4J.equals(id)) {
        String methodName = "is" + level.substring(0, 1).toUpperCase() + level.substring(1) + "Enabled";
        final Object hasLevel = c.getClass().getMethod(methodName).invoke(c);
        return (Boolean) hasLevel;
      }

      if (LOG4J.equals(id)) {
//        org.apache.log4j.Logger.getLogger("").isEnabledFor(Priority.toPriority("debug"));

        final Class<?> levelClass = Class.forName("org.apache.log4j.Priority");
        final Method isLoggable = c.getClass().getMethod("isEnabledFor", levelClass);
        final Method parse = levelClass.getMethod("toPriority", String.class);
        final Object levelObject = parse.invoke(null, level.toUpperCase());
        final Object hasLevel = isLoggable.invoke(c, levelObject);
        return (Boolean) hasLevel;
      }

      if (LOG4J2.equals(id)) {
        // org.apache.logging.log4j.core.Logger l = null; l.isEnabled(Level.parse("DEBUG"))

        final Class<?> levelClass = Class.forName("org.apache.logging.log4j.Level");
        final Method isLoggable = c.getClass().getMethod("isEnabled", levelClass);
        final Method parse = levelClass.getMethod("getLevel", String.class);
        final Object levelObject = parse.invoke(null, level.toUpperCase());
        final Object hasLevel = isLoggable.invoke(c, levelObject);
        return (Boolean) hasLevel;
      }

      if (JCL.equals(id)) {
        //org.apache.commons.logging.Log l = null; l.isDebugEnabled();
        String methodName = "is" + level.substring(0, 1).toUpperCase() + level.substring(1) + "Enabled";
        final Object hasLevel = c.getClass().getMethod(methodName).invoke(c);
        return (Boolean) hasLevel;
      }

      throw new IllegalStateException();
    }
  }
}
