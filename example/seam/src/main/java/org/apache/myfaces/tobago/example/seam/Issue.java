package org.apache.myfaces.tobago.example.seam;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.bpm.CreateProcess;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Name("issue")
public class Issue {

  private static final Log LOG = LogFactory.getLog(Issue.class);

  private String title;
  private String description;

  @CreateProcess(definition = "issue-process")
  public void init() {
    LOG.info("init");
  }

  public void create() {
    LOG.info("create");
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
