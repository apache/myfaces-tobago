package org.apache.myfaces.tobago.model;

public enum WizardBackwardNavigationStrategy {

    DELETE(Wizard.BACKWARD_NAVIGATION_STRATEGY_DELETE), REPLACE(Wizard.BACKWARD_NAVIGATION_STRATEGY_REPLACE), NOT_ALLOWED(
            Wizard.BACKWARD_NAVIGATION_STRATEGY_NOTALLOWED);

    private String name;

    private WizardBackwardNavigationStrategy(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static WizardBackwardNavigationStrategy getStrategy(String strategy) throws IllegalArgumentException {
        for (WizardBackwardNavigationStrategy ws : WizardBackwardNavigationStrategy.values()) {
            if (ws.name.equalsIgnoreCase(strategy)) {
                return ws;
            }
        }
        throw new IllegalArgumentException("WizardBackwardNavigationStrategy '" + strategy + "' unknown!");
    }

}
