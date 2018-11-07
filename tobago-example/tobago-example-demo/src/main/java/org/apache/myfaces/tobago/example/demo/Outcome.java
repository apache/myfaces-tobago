package org.apache.myfaces.tobago.example.demo;

/**
 * Class to collect all the JSF outcomes.
 */
public enum Outcome {

  CONCEPT_SECURITY_ROLES_XLOGIN(
      "/content/30-concept/80-security/20-roles/x-login.xhtml?faces-redirect=true"),
  COMPONENT_COMPONENT(
      "/content/20-component/component.xhtml?faces-redirect=true"),
  TEST_BUTTONLINK_XACTION(
      "/content/40-test/4000-button+link/x-action.xhtml?faces-redirect=true"),
  TEST_BUTTONLINK_XTARGETACTION(
      "/content/40-test/4000-button+link/x-targetAction.xhtml");

  private final String outcome;

  Outcome(final String outcome) {
    this.outcome = outcome;
  }

  @Override
  public String toString() {
    return outcome;
  }
}
