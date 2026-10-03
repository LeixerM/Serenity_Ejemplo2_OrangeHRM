package com.orangehrm.demo.tasks;

import com.orangehrm.demo.models.DataEmployee;
import com.orangehrm.demo.ui.EmployeeOrangeHrmUi;
import lombok.AllArgsConstructor;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.*;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.questions.Attribute;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.openqa.selenium.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isPresent;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

@AllArgsConstructor
public class CreateEmployee implements Task {
    public static CreateEmployee dataEmployee() {
        return Tasks.instrumented(CreateEmployee.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {

        DataEmployee data = actor.recall("employeeData");
        Path filePath = data.getPathFile();

        actor.attemptsTo(
                WaitUntil.the(EmployeeOrangeHrmUi.SECTION_PIM, isVisible()).forNoMoreThan(20).seconds(),
                Click.on(EmployeeOrangeHrmUi.SECTION_PIM),
                WaitUntil.the(EmployeeOrangeHrmUi.BUTTON_ADD_EMPLOYEE, isVisible()).forNoMoreThan(20).seconds(),
                Click.on(EmployeeOrangeHrmUi.BUTTON_ADD_EMPLOYEE),
                WaitUntil.the(EmployeeOrangeHrmUi.BUTTON_UPLOAD_AVATAR, isVisible()).forNoMoreThan(40).seconds()
        );

        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement input = driver.findElement(By.cssSelector("input.oxd-file-input"));
        if (input != null) {
            js.executeScript("arguments[0].style.display = 'block';", input);
        }

        actor.attemptsTo(
                Upload.theFile(filePath).to(EmployeeOrangeHrmUi.INPUT_UPLOAD_AVATAR),
                WaitUntil.the(EmployeeOrangeHrmUi.PLACEHOLDER_IMAGE_EXISTS, isVisible()).forNoMoreThan(30).seconds(),

                Scroll.to(EmployeeOrangeHrmUi.INPUT_FIRSTNAME),
                Enter.theValue(data.getFirstName()).into(EmployeeOrangeHrmUi.INPUT_FIRSTNAME),
                Enter.theValue(data.getMiddleName()).into(EmployeeOrangeHrmUi.INPUT_MIDDLENAME),
                Enter.theValue(data.getLastName()).into(EmployeeOrangeHrmUi.INPUT_LASTNAME),

                Scroll.to(EmployeeOrangeHrmUi.INPUT_EMPLOYEE_ID),
                Click.on(EmployeeOrangeHrmUi.INPUT_EMPLOYEE_ID),
                Enter.theValue(Keys.chord(Keys.CONTROL, "a")).into(EmployeeOrangeHrmUi.INPUT_EMPLOYEE_ID),
                Hit.the(Keys.DELETE).into(EmployeeOrangeHrmUi.INPUT_EMPLOYEE_ID),
                Enter.theValue(data.getIdEmployee()).into(EmployeeOrangeHrmUi.INPUT_EMPLOYEE_ID),

                Scroll.to(EmployeeOrangeHrmUi.BUTTON_SAVE_EMPLOYEE),
                Click.on(EmployeeOrangeHrmUi.BUTTON_SAVE_EMPLOYEE)


        );


       /* Path filePath = dataEmployee.getPathFile().toAbsolutePath();

        actor.attemptsTo(
                WaitUntil.the(EmployeeOrangeHrmUi.SECTION_PIM, isVisible()).forNoMoreThan(20).seconds(),
                Click.on(EmployeeOrangeHrmUi.SECTION_PIM),
                WaitUntil.the(EmployeeOrangeHrmUi.BUTTON_ADD_EMPLOYEE, isVisible()).forNoMoreThan(20).seconds(),
                Click.on(EmployeeOrangeHrmUi.BUTTON_ADD_EMPLOYEE),
                WaitUntil.the(EmployeeOrangeHrmUi.INPUT_UPLOAD_AVATAR, WebElementStateMatchers.isVisible()).forNoMoreThan(60).seconds()
                );
        actor.attemptsTo(
                WaitUntil.the(EmployeeOrangeHrmUi.BUTTON_UPLOAD_AVATAR, isVisible()).forNoMoreThan(40).seconds()
        );
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.querySelector('input.oxd-file-input').style.display = 'block';");

        actor.attemptsTo(
                WaitUntil.the(EmployeeOrangeHrmUi.INPUT_UPLOAD_AVATAR, WebElementStateMatchers.isVisible()).forNoMoreThan(30).seconds(),
                Upload.theFile(filePath).to(EmployeeOrangeHrmUi.INPUT_UPLOAD_AVATAR),
                WaitUntil.the(EmployeeOrangeHrmUi.PLACEHOLDER_IMAGE_EXISTS, isVisible()).forNoMoreThan(60).seconds(),

                WaitUntil.the(EmployeeOrangeHrmUi.INPUT_FIRSTNAME, WebElementStateMatchers.isVisible()).forNoMoreThan(30).seconds(),
                Scroll.to(EmployeeOrangeHrmUi.INPUT_FIRSTNAME),
                Enter.theValue(dataEmployee.getFirstName()).into(EmployeeOrangeHrmUi.INPUT_FIRSTNAME),

                WaitUntil.the(EmployeeOrangeHrmUi.INPUT_MIDDLENAME, WebElementStateMatchers.isVisible()).forNoMoreThan(10).seconds(),
                Enter.theValue(dataEmployee.getMiddleName()).into(EmployeeOrangeHrmUi.INPUT_MIDDLENAME),
                WaitUntil.the(EmployeeOrangeHrmUi.INPUT_LASTNAME, WebElementStateMatchers.isVisible()).forNoMoreThan(10).seconds(),
                Enter.theValue(dataEmployee.getLastName()).into(EmployeeOrangeHrmUi.INPUT_LASTNAME),

                WaitUntil.the(EmployeeOrangeHrmUi.INPUT_EMPLOYEE_ID, WebElementStateMatchers.isVisible()).forNoMoreThan(10).seconds(),
                Scroll.to(EmployeeOrangeHrmUi.INPUT_EMPLOYEE_ID),
                Click.on(EmployeeOrangeHrmUi.INPUT_EMPLOYEE_ID),
                Enter.theValue(Keys.chord(Keys.CONTROL, "a")).into(EmployeeOrangeHrmUi.INPUT_EMPLOYEE_ID),
                Hit.the(Keys.DELETE).into(EmployeeOrangeHrmUi.INPUT_EMPLOYEE_ID),
                Enter.theValue(dataEmployee.getIdEmployee()).into(EmployeeOrangeHrmUi.INPUT_EMPLOYEE_ID),

                WaitUntil.the(EmployeeOrangeHrmUi.BUTTON_SAVE_EMPLOYEE, WebElementStateMatchers.isVisible()).forNoMoreThan(10).seconds(),
                Scroll.to(EmployeeOrangeHrmUi.BUTTON_SAVE_EMPLOYEE),
                Click.on(EmployeeOrangeHrmUi.BUTTON_SAVE_EMPLOYEE),
                WaitUntil.the(EmployeeOrangeHrmUi.TEXT_PERSONAL_DETAILS, isVisible()).forNoMoreThan(60).seconds()
        );
    }

    public static CreateEmployee withDataEmployee(DataEmployee dataEmployee) {
        return Tasks.instrumented(CreateEmployee.class, dataEmployee);
    }*/
    }

    public static Performable attributeSrcImage() {
        return Task.where(
                "{0} obtiene el valor del atributo src de la imagen",
                actor -> {
                    // Esperar a que la imagen sea visible
                    actor.attemptsTo(
                            WaitUntil.the(EmployeeOrangeHrmUi.IMAGE_VALUE, isVisible())
                                    .forNoMoreThan(Duration.ofSeconds(30))
                    );

                    // Obtener el atributo src
                    String imageValue = Attribute.of(EmployeeOrangeHrmUi.IMAGE_VALUE)
                            .named("src")
                            .answeredBy(actor);

                    System.out.println("El valor del src es: " + imageValue);
                    actor.attemptsTo(
                            WaitUntil.the(EmployeeOrangeHrmUi.IMAGE_VALUE, isVisible())
                                    .forNoMoreThan(Duration.ofSeconds(30))
                    );
                    actor.remember("employeeImageSrc", imageValue);
                }
        );
    }

    public static Performable filterForName() {
        DataEmployee data = theActorInTheSpotlight().recall("employeeData");
        return Task.where(
                "{0} filter For Name Page OrangeHRM",
                actor -> {
                    // Esperar a que la imagen sea visible
                    actor.attemptsTo(
                            Scroll.to(EmployeeOrangeHrmUi.SECTION_DIRECTORY),
                            Click.on(EmployeeOrangeHrmUi.SECTION_DIRECTORY),
                            WaitUntil.the(EmployeeOrangeHrmUi.PLACEHOLDER_DIRECTORY_NAME, isVisible()).forNoMoreThan(20).seconds(),
                            Enter.theValue(data.getFirstName()).into(EmployeeOrangeHrmUi.PLACEHOLDER_DIRECTORY_NAME),
                            WaitUntil.the(EmployeeOrangeHrmUi.PLACEHOLDER_DIRECTORY_NAME, isPresent()).forNoMoreThan(20).seconds(),
                            Hit.the(Keys.ARROW_DOWN).into(EmployeeOrangeHrmUi.PLACEHOLDER_DIRECTORY_NAME),
                            Hit.the(Keys.ENTER).into(EmployeeOrangeHrmUi.PLACEHOLDER_DIRECTORY_NAME)
                    );
                }
        );
    }

    public static Performable visibleInformationEmployee() {
        return Task.where(
                "{0} scroll Information Employee Page OrangeHRM",
                actor -> {
                    // Esperar a que la imagen sea visible
                    actor.attemptsTo(
                            WaitUntil.the(EmployeeOrangeHrmUi.BUTTON_SEARCH, isPresent()).forNoMoreThan(10).seconds(),
                            Click.on(EmployeeOrangeHrmUi.BUTTON_SEARCH),
                            Scroll.to(EmployeeOrangeHrmUi.IMAGE_DIRECTORY)
                    );
                }
        );
    }
}


