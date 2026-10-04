package com.orangehrm.demo.ui;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public final class LoginPage {

    public static final Target USERNAME = Target.the("the username field").located(By.cssSelector("input[name='username']"));
    public static final Target PASSWORD = Target.the("the password field").located(By.cssSelector("input[name='password']"));
    public static final Target LOG_IN = Target.the("the 'Login' button").located(By.cssSelector("button[type='submit']"));

    private LoginPage() {
    }
}
