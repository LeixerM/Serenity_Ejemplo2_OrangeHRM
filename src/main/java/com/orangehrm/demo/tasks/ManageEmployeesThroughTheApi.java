package com.orangehrm.demo.tasks;

import com.orangehrm.demo.interactions.CallTheApi;
import com.orangehrm.demo.models.Employee;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;

import java.util.Map;

/**
 * Test setup and clean-up through the OrangeHRM REST API, which is much faster than the UI
 * and keeps each scenario focused on the behaviour it is about.
 */
public final class ManageEmployeesThroughTheApi {

    /** Actor memory key holding the internal employee number of the employee created by the scenario. */
    public static final String EMPLOYEE_NUMBER = "employeeNumber";

    private ManageEmployeesThroughTheApi() {
    }

    public static Performable register(Employee employee) {
        return Task.where("{0} registers the employee " + employee.fullName() + " through the API", actor -> {
            String body = """
                    {"firstName": "%s", "middleName": "%s", "lastName": "%s", "employeeId": "%s"}"""
                    .formatted(employee.firstName(), employee.middleName(), employee.lastName(), employee.employeeId());
            Map<String, Object> response = CallTheApi.send(actor, "POST", "/pim/employees", body);
            if (!Long.valueOf(200).equals(response.get("status"))) {
                throw new IllegalStateException("Could not create the employee through the API: " + response);
            }
            Map<?, ?> data = (Map<?, ?>) ((Map<?, ?>) response.get("body")).get("data");
            actor.remember(EMPLOYEE_NUMBER, String.valueOf(data.get("empNumber")));
        });
    }

    /** Deletes the employee by its internal number; an employee that is already gone is not an error. */
    public static Performable remove(String employeeNumber) {
        return Task.where("{0} removes the employee #" + employeeNumber + " through the API", actor ->
                CallTheApi.send(actor, "DELETE", "/pim/employees", "{\"ids\": [" + employeeNumber + "]}"));
    }
}
