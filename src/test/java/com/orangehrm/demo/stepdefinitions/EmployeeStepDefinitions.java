package com.orangehrm.demo.stepdefinitions;

import com.orangehrm.demo.models.Credentials;
import com.orangehrm.demo.models.Employee;
import com.orangehrm.demo.questions.TheEmployeeList;
import com.orangehrm.demo.questions.TheEmployeeProfile;
import com.orangehrm.demo.tasks.AddEmployee;
import com.orangehrm.demo.tasks.DeleteEmployee;
import com.orangehrm.demo.tasks.LogIn;
import com.orangehrm.demo.tasks.ManageEmployeesThroughTheApi;
import com.orangehrm.demo.tasks.SearchEmployees;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.ensure.Ensure;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

import static com.orangehrm.demo.tasks.ManageEmployeesThroughTheApi.EMPLOYEE_NUMBER;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

/** Thin glue: maps Gherkin to Screenplay tasks and Ensure assertions. */
public class EmployeeStepDefinitions {

    private static final Path PROFILE_PHOTO = Path.of("src/test/resources/images/avatar.png");
    private static final String EMPLOYEE = "employee";

    @Before
    public void setTheStage() {
        OnStage.setTheStage(new OnlineCast());
    }

    /** Removes the employee the scenario created, so the shared demo does not fill up with test data. */
    @After
    public void removeTheEmployeeCreatedByTheScenario() {
        Actor actor = theActorInTheSpotlight();
        String employeeNumber = actor.recall(EMPLOYEE_NUMBER);
        if (employeeNumber != null) {
            actor.attemptsTo(ManageEmployeesThroughTheApi.remove(employeeNumber));
        }
    }

    @Given("{word} is logged in to OrangeHRM as an administrator")
    public void isLoggedIn(String actorName) {
        theActorCalled(actorName).wasAbleTo(LogIn.as(Credentials.fromConfiguration()));
    }

    @Given("a new employee exists")
    public void aNewEmployeeExists() {
        Employee employee = Employee.unique();
        theActorInTheSpotlight().remember(EMPLOYEE, employee);
        theActorInTheSpotlight().wasAbleTo(ManageEmployeesThroughTheApi.register(employee));
    }

    @When("she adds a new employee with a profile photo")
    public void addsANewEmployeeWithAProfilePhoto() {
        Employee employee = Employee.unique();
        Actor actor = theActorInTheSpotlight();
        actor.remember(EMPLOYEE, employee);
        actor.attemptsTo(AddEmployee.withProfilePhoto(employee, PROFILE_PHOTO));
        actor.remember(EMPLOYEE_NUMBER, actor.asksFor(TheEmployeeProfile.employeeNumber()));
    }

    @When("she searches the employee list by the employee's full name")
    public void searchesByFullName() {
        Employee employee = theActorInTheSpotlight().recall(EMPLOYEE);
        theActorInTheSpotlight().attemptsTo(SearchEmployees.byName(employee.fullName()));
    }

    @When("she deletes the employee from the employee list")
    public void deletesTheEmployee() {
        Employee employee = theActorInTheSpotlight().recall(EMPLOYEE);
        theActorInTheSpotlight().attemptsTo(DeleteEmployee.fromTheEmployeeList(employee));
    }

    @Then("the employee profile shows the full name and the uploaded photo")
    public void theProfileShowsNameAndPhoto() {
        Employee employee = theActorInTheSpotlight().recall(EMPLOYEE);
        String employeeNumber = theActorInTheSpotlight().recall(EMPLOYEE_NUMBER);
        theActorInTheSpotlight().attemptsTo(
                Ensure.that(TheEmployeeProfile.displayedName()).isEqualTo(employee.firstName() + " " + employee.lastName()),
                Ensure.that(TheEmployeeProfile.storedPhotoChecksum(employeeNumber)).isEqualTo(sha256Of(PROFILE_PHOTO))
        );
    }

    @Then("the employee is the only result, with the right id and names")
    public void theEmployeeIsTheOnlyResult() {
        Employee employee = theActorInTheSpotlight().recall(EMPLOYEE);
        theActorInTheSpotlight().attemptsTo(
                Ensure.that(TheEmployeeList.idsOnceShowing(employee.employeeId())).containsExactly(employee.employeeId()),
                Ensure.that(TheEmployeeList.firstAndMiddleNameOf(employee.employeeId())).isEqualTo(employee.firstAndMiddleName()),
                Ensure.that(TheEmployeeList.lastNameOf(employee.employeeId())).isEqualTo(employee.lastName())
        );
    }

    @Then("she is told {string}")
    public void sheIsTold(String message) {
        theActorInTheSpotlight().attemptsTo(
                Ensure.that(TheEmployeeList.notification()).isEqualTo(message)
        );
    }

    @Then("searching by the employee id finds no records")
    public void searchingByIdFindsNoRecords() {
        Employee employee = theActorInTheSpotlight().recall(EMPLOYEE);
        theActorInTheSpotlight().attemptsTo(
                SearchEmployees.byEmployeeId(employee.employeeId()),
                Ensure.that(TheEmployeeList.noRecordsMessage()).isEqualTo("No Records Found")
        );
    }

    private static String sha256Of(Path file) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(Files.readAllBytes(file)));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
