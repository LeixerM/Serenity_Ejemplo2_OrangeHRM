package com.orangehrm.demo.models;

import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.thucydides.model.environment.SystemEnvironmentVariables;

/**
 * Login credentials. Resolution order: -Dorangehrm.username / -Dorangehrm.password system properties,
 * then the ORANGEHRM_USERNAME / ORANGEHRM_PASSWORD environment variables, then the public demo
 * account defined in serenity.conf.
 */
public record Credentials(String username, String password) {

    public static Credentials fromConfiguration() {
        return new Credentials(setting("orangehrm.username", "ORANGEHRM_USERNAME"),
                setting("orangehrm.password", "ORANGEHRM_PASSWORD"));
    }

    private static String setting(String property, String environmentVariable) {
        String systemProperty = System.getProperty(property);
        if (systemProperty != null && !systemProperty.isBlank()) {
            return systemProperty;
        }
        String environmentValue = System.getenv(environmentVariable);
        if (environmentValue != null && !environmentValue.isBlank()) {
            return environmentValue;
        }
        return EnvironmentSpecificConfiguration.from(SystemEnvironmentVariables.currentEnvironmentVariables())
                .getProperty(property);
    }

    @Override
    public String toString() {
        return "Credentials[username=" + username + ", password=***]";
    }
}
