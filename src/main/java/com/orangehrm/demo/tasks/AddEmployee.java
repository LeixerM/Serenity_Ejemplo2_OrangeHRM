package com.orangehrm.demo.tasks;

import com.orangehrm.demo.interactions.UploadProfilePhoto;
import com.orangehrm.demo.models.Employee;
import com.orangehrm.demo.ui.AddEmployeePage;
import com.orangehrm.demo.ui.PersonalDetailsPage;
import com.orangehrm.demo.ui.SideMenu;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import com.orangehrm.demo.interactions.WaitFor;
import org.openqa.selenium.Keys;

import java.nio.file.Path;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public final class AddEmployee {

    private AddEmployee() {
    }

    /** Fills in the "Add Employee" form with a profile photo and saves it, landing on the personal details page. */
    public static Performable withProfilePhoto(Employee employee, Path photo) {
        return Task.where("{0} adds the employee " + employee.fullName() + " with a profile photo",
                Click.on(SideMenu.ITEM.of("PIM")),
                Click.on(AddEmployeePage.ADD_EMPLOYEE_TAB),
                UploadProfilePhoto.from(photo),
                Enter.theValue(employee.firstName()).into(AddEmployeePage.FIRST_NAME),
                Enter.theValue(employee.middleName()).into(AddEmployeePage.MIDDLE_NAME),
                Enter.theValue(employee.lastName()).into(AddEmployeePage.LAST_NAME),
                // The id field is pre-filled with a suggested value; replace it with ours.
                Enter.theValue(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE.toString(), employee.employeeId())
                        .into(AddEmployeePage.EMPLOYEE_ID),
                Click.on(AddEmployeePage.SAVE),
                WaitFor.the(PersonalDetailsPage.EMPLOYEE_NAME_SHOWING.of(employee.lastName()), isVisible())
        );
    }
}
