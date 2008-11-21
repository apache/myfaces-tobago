package org.apache.myfaces.tobago.example.seam;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.bpm.CreateProcess;
import org.jboss.seam.annotations.bpm.EndTask;
import org.jboss.seam.annotations.bpm.StartTask;

@Name("orderStock")
public class OrderStock {

  @Out(scope = ScopeType.BUSINESS_PROCESS, required = false)
  Long processQuantity;

  private int quantity;

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @CreateProcess(definition = "simple")
  public void startProcess() {
    processQuantity = new Long(getQuantity());
  }

  @StartTask
  @EndTask(transition = "next")
  public void done() {
  }

  @StartTask
  @EndTask(transition = "cancel")
  public void cancel() {
  }

}


