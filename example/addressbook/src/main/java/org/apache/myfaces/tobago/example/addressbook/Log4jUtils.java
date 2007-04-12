package org.apache.myfaces.tobago.example.addressbook;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerRepository;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * User: idus
 * Date: 12.04.2007
 * Time: 22:41:11
 */
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
