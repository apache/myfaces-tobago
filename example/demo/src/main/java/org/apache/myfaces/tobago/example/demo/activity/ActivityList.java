package org.apache.myfaces.tobago.example.demo.activity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: lofwyr
 * Date: 30.09.2010
 * Time: 16:47:47
 * To change this template use File | Settings | File Templates.
 */
public class ActivityList {

  private static final Log LOG = LogFactory.getLog(ActivityList.class);

  public static final String NAME = "activities";

  private Map<String, Activity> data = new ConcurrentHashMap<String, Activity>();
  
  public void add(Activity activity) {
    LOG.info("Adding session id: " + activity.getSessionId());
    data.put(activity.getSessionId(),activity);
  }

  public void remove(String sessionId) {
    LOG.info("Removing session id: " + sessionId);
    final Activity activity = data.remove(sessionId);
  }

  public List<Activity> getValues() {
    final Collection<Activity> values = data.values();
    ArrayList<Activity> result = new ArrayList<Activity>();
    result.addAll(values);
    return result;
  }

  public void jsfRequest(String sessionId) {
    data.get(sessionId).jsfRequest();
  }

  public void ajaxRequest(String sessionId) {
    data.get(sessionId).ajaxRequest();
  }
}
