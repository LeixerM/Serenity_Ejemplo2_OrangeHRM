package com.orangehrm.demo.tasks;

import com.orangehrm.demo.ui.EmployeeListPage;
import com.orangehrm.demo.ui.SideMenu;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import com.orangehrm.demo.interactions.WaitFor;
import net.serenitybdd.screenplay.targets.Target;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

public final class SearchEmployees {

    private SearchEmployees() {
    }

    /** Searches the PIM employee list by (part of) the employee name. */
    public static Performable byName(String name) {
        return searchUsing(EmployeeListPage.EMPLOYEE_NAME_FILTER, name, "name");
    }

    /** Searches the PIM employee list by employee id. */
    public static Performable byEmployeeId(String employeeId) {
        return searchUsing(EmployeeListPage.EMPLOYEE_ID_FILTER, employeeId, "employee id");
    }

    private static Performable searchUsing(Target filter, String value, String criterion) {
        return Task.where("{0} searches the employee list by " + criterion + " '" + value + "'",
                Click.on(SideMenu.ITEM.of("PIM")),
                Click.on(EmployeeListPage.EMPLOYEE_LIST_TAB),
                WaitFor.the(filter, isVisible()),
                Enter.theValue(value).into(filter),
                Click.on(EmployeeListPage.SEARCH)
        );
    }
}
