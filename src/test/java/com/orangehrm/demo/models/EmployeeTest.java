package com.orangehrm.demo.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmployeeTest {

    @Test
    void generatesADifferentEmployeeEveryTime() {
        Employee first = Employee.unique();
        Employee second = Employee.unique();

        assertNotEquals(first.lastName(), second.lastName());
        assertNotEquals(first.employeeId(), second.employeeId());
    }

    @Test
    void employeeIdFitsTheOrangeHrmLimitOfTenCharacters() {
        assertTrue(Employee.unique().employeeId().length() <= 10);
    }

    @Test
    void formatsNamesLikeTheApplication() {
        Employee employee = new Employee("Qa", "Auto", "Employeeabc", "123");

        assertEquals("Qa Auto Employeeabc", employee.fullName());
        assertEquals("Qa Auto", employee.firstAndMiddleName());
    }
}
