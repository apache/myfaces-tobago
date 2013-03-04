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

package org.apache.myfaces.tobago.example.addressbook;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerRepository;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class Log4jUtils {

  public static Set<Appender> getAllAppenders() {
    return getAllAppenders(LogManager.getLoggerRepository());
  }

  /**
   * @return all appenders currently in use
   */
  public static Set<Appender> getAllAppenders(LoggerRepository repository) {
      Enumeration loggers = repository.getCurrentLoggers();
      Set<Appender> allAppenders = getAllAppenders(loggers);
      addAppenders(repository.getRootLogger(), allAppenders);
      return allAppenders;
  }

  public static Set<Appender> getAllAppenders(Enumeration loggers) {
      Set<Appender> allAppenders = new HashSet<Appender>();
      while (loggers.hasMoreElements()) {
          Logger logger = (Logger) loggers.nextElement();
          addAppenders(logger, allAppenders);
      }
      return allAppenders;
  }

  private static void addAppenders(Logger logger, Set<Appender> allAppenders) {
      Enumeration appenders = logger.getAllAppenders();
      while (appenders.hasMoreElements()) {
          Appender appender = (Appender) appenders.nextElement();
          allAppenders.add(appender);
      }
  }

  public static FileAppender getFileAppender(String name, LoggerRepository repository) {
      Set allAppenders = getAllAppenders(repository);
    for (Object allAppender : allAppenders) {
      Appender appender = (Appender) allAppender;
      if (appender instanceof FileAppender) {
        FileAppender fileAppender = (FileAppender) appender;
        if (fileAppender.getName() != null
            && fileAppender.getName().equals(name)) {
          return fileAppender;
        }
      }
    }
    return null;
  }

}
