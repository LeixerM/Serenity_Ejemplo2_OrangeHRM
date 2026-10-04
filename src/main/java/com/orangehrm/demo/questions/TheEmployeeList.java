package com.orangehrm.demo.questions;

import com.orangehrm.demo.ui.EmployeeListPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import com.orangehrm.demo.interactions.WaitFor;

import java.util.Collection;
import java.util.List;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/** Columns of a result row: [checkbox, Id, First (& Middle) Name, Last Name, Job Title, ...]. */
public final class TheEmployeeList {

    private static final int FIRST_AND_MIDDLE_NAME = 2;
    private static final int LAST_NAME = 3;

    private TheEmployeeList() {
    }

    /** Employee ids of every result row, once the row of the expected employee is shown. */
    public static Question<Collection<String>> idsOnceShowing(String employeeId) {
        return Question.about("the employee ids in the results")
                .answeredBy(actor -> {
                    waitForTheRowOf(actor, employeeId);
                    return List.copyOf(Text.ofEach(EmployeeListPage.RESULT_IDS).answeredBy(actor)
                            .stream().map(String::trim).toList());
                });
    }

    public static Question<String> firstAndMiddleNameOf(String employeeId) {
        return cell("the first and middle name of employee " + employeeId, employeeId, FIRST_AND_MIDDLE_NAME);
    }

    public static Question<String> lastNameOf(String employeeId) {
        return cell("the last name of employee " + employeeId, employeeId, LAST_NAME);
    }

    public static Question<String> noRecordsMessage() {
        return Question.about("the empty results message")
                .answeredBy(actor -> {
                    actor.attemptsTo(WaitFor.the(EmployeeListPage.NO_RECORDS_FOUND, isVisible()));
                    return Text.of(EmployeeListPage.NO_RECORDS_FOUND).answeredBy(actor).trim();
                });
    }

    public static Question<String> notification() {
        return Question.about("the notification message")
                .answeredBy(actor -> {
                    actor.attemptsTo(WaitFor.the(EmployeeListPage.TOAST_MESSAGE, isVisible()));
                    return Text.of(EmployeeListPage.TOAST_MESSAGE).answeredBy(actor).trim();
                });
    }

    private static Question<String> cell(String description, String employeeId, int column) {
        return Question.about(description)
                .answeredBy(actor -> {
                    waitForTheRowOf(actor, employeeId);
                    List<String> cells = List.copyOf(Text.ofEach(EmployeeListPage.CELLS_OF_EMPLOYEE.of(employeeId))
                            .answeredBy(actor));
                    return cells.get(column).trim();
                });
    }

    private static void waitForTheRowOf(Actor actor, String employeeId) {
        actor.attemptsTo(WaitFor.the(EmployeeListPage.ROW_OF_EMPLOYEE.of(employeeId), isVisible()));
    }
}
