package com.orangehrm.demo.tasks;

import com.orangehrm.demo.interactions.OpenTheLoginPage;
import com.orangehrm.demo.interactions.WaitFor;
import com.orangehrm.demo.models.Credentials;
import com.orangehrm.demo.ui.LoginPage;
import com.orangehrm.demo.ui.SideMenu;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public final class LogIn {

    private LogIn() {
    }

    public static Performable as(Credentials credentials) {
        return Task.where("{0} logs in to OrangeHRM as " + credentials.username(),
                OpenTheLoginPage.ofOrangeHrm(),
                Enter.theValue(credentials.username()).into(LoginPage.USERNAME),
                Enter.theValue(credentials.password()).into(LoginPage.PASSWORD),
                Click.on(LoginPage.LOG_IN),
                WaitFor.the(SideMenu.ITEM.of("PIM"), isVisible())
        );
    }
}
