package com.orangehrm.demo.tasks;

import com.orangehrm.demo.models.Employee;
import com.orangehrm.demo.ui.EmployeeListPage;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import com.orangehrm.demo.interactions.WaitFor;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public final class DeleteEmployee {

    private DeleteEmployee() {
    }

    /** Finds the employee in the PIM list and deletes it with the row's trash button. */
    public static Performable fromTheEmployeeList(Employee employee) {
        return Task.where("{0} deletes the employee " + employee.employeeId() + " from the employee list",
                SearchEmployees.byEmployeeId(employee.employeeId()),
                WaitFor.the(EmployeeListPage.DELETE_EMPLOYEE.of(employee.employeeId()), isVisible()),
                Click.on(EmployeeListPage.DELETE_EMPLOYEE.of(employee.employeeId())),
                WaitFor.the(EmployeeListPage.CONFIRM_DELETE, isVisible()),
                Click.on(EmployeeListPage.CONFIRM_DELETE)
        );
    }
}
