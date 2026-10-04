package com.orangehrm.demo.interactions;

import com.orangehrm.demo.ui.LoginPage;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/**
 * Opens the login page ({@code webdriver.base.url} in serenity.conf).
 * The shared public demo occasionally leaves its single-page app blank on first load; in that case
 * the page is reloaded once. Any later problem still fails the test through the normal wait.
 */
public class OpenTheLoginPage implements Interaction {

    private static final By USERNAME_FIELD = By.cssSelector("input[name='username']");

    public static OpenTheLoginPage ofOrangeHrm() {
        return new OpenTheLoginPage();
    }

    @Override
    @Step("{0} opens the OrangeHRM login page")
    public <T extends Actor> void performAs(T actor) {
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        driver.get(loginPageUrl());
        try {
            new WebDriverWait(driver, WaitFor.DEMO_SITE_TIMEOUT)
                    .until(ExpectedConditions.visibilityOfElementLocated(USERNAME_FIELD));
        } catch (TimeoutException blankFirstLoad) {
            driver.navigate().refresh();
        }
        actor.attemptsTo(WaitFor.the(LoginPage.USERNAME, isVisible()));
    }

    private static String loginPageUrl() {
        return EnvironmentSpecificConfiguration.from(SystemEnvironmentVariables.currentEnvironmentVariables())
                .getProperty("webdriver.base.url");
    }
}
