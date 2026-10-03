package com.orangehrm.demo.ui;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public final class EmployeeListPage {

    public static final Target EMPLOYEE_LIST_TAB = Target.the("the 'Employee List' tab")
            .locatedBy("//nav//a[normalize-space()='Employee List']");
    public static final Target EMPLOYEE_NAME_FILTER = Target.the("the employee name filter")
            .locatedBy("//label[normalize-space()='Employee Name']/../following-sibling::div//input");
    public static final Target EMPLOYEE_ID_FILTER = Target.the("the employee id filter")
            .locatedBy("//label[normalize-space()='Employee Id']/../following-sibling::div/input");
    public static final Target SEARCH = Target.the("the 'Search' button").located(By.cssSelector("button[type='submit']"));

    public static final Target ROW_OF_EMPLOYEE = Target.the("the result row of employee {0}")
            .locatedBy("//div[contains(@class,'oxd-table-card')][.//div[contains(@class,'oxd-table-cell')][normalize-space()='{0}']]");
    public static final Target CELLS_OF_EMPLOYEE = Target.the("the cells of employee {0}")
            .locatedBy("//div[contains(@class,'oxd-table-card')][.//div[contains(@class,'oxd-table-cell')][normalize-space()='{0}']]//div[contains(@class,'oxd-table-cell')]");
    public static final Target RESULT_IDS = Target.the("the ids in the results")
            .located(By.cssSelector(".oxd-table-body .oxd-table-card .oxd-table-cell:nth-child(2)"));
    public static final Target DELETE_EMPLOYEE = Target.the("the delete button of employee {0}")
            .locatedBy("//div[contains(@class,'oxd-table-card')][.//div[contains(@class,'oxd-table-cell')][normalize-space()='{0}']]//button[.//i[contains(@class,'bi-trash')]]");
    public static final Target NO_RECORDS_FOUND = Target.the("the 'No Records Found' message")
            .locatedBy("//div[contains(@class,'orangehrm-horizontal-padding')]//span[normalize-space()='No Records Found']");

    public static final Target CONFIRM_DELETE = Target.the("the 'Yes, Delete' button")
            .locatedBy("//div[@role='document']//button[normalize-space()='Yes, Delete']");
    public static final Target TOAST_MESSAGE = Target.the("the notification message")
            .located(By.cssSelector(".oxd-toast .oxd-text--toast-message"));

    private EmployeeListPage() {
    }
}
