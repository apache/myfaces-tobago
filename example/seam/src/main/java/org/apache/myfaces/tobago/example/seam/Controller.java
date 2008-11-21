package org.apache.myfaces.tobago.example.seam;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.bpm.CreateProcess;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Name("controller")
public class Controller {

  private static final Log LOG = LogFactory.getLog(Controller.class);

  private Issue issue;

  @CreateProcess(definition = "issue-process") 
  public String init() {
    LOG.info("init");
    return "new";
  }

  public String save() {
    LOG.info("save");
    return "todo";
  }

  public Issue getIssue() {
    return issue;
  }

  public void setIssue(Issue issue) {
    this.issue = issue;
  }
}
