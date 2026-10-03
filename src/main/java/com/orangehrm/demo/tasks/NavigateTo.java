package com.orangehrm.demo.tasks;

import com.orangehrm.demo.ui.LoginOrangeHrmUi;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.waits.WaitUntil;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public class NavigateTo {

    public static Performable navigatePageOrangeHrm(){
        // Crear las variables de entorno
        EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();

        // Obtener la URL específica del entorno desde serenity.conf
        String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("webdriver.base.url");

        // Imprimir la URL utilizada
        System.out.println("Using base URL: " + baseUrl);

        // Retornar la tarea de navegar a la página de login
        return Task.where("{0} navigate to OrangeHRM page",
                Open.url(baseUrl),
                WaitUntil.the(LoginOrangeHrmUi.INPUT_USER, WebElementStateMatchers.isVisible()).forNoMoreThan(40).seconds()
        );
    }
}
