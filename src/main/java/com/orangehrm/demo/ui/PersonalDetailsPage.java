package com.orangehrm.demo.ui;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public final class PersonalDetailsPage {

    public static final Target EMPLOYEE_NAME = Target.the("the employee name heading")
            .located(By.cssSelector(".orangehrm-edit-employee-name h6"));
    /** Only matches once the profile of that employee has loaded, so waits never read a half-rendered page. */
    public static final Target EMPLOYEE_NAME_SHOWING = Target.the("the employee name heading showing '{0}'")
            .locatedBy("//div[contains(@class,'orangehrm-edit-employee-name')]/h6[contains(normalize-space(),'{0}')]");

    private PersonalDetailsPage() {
    }
}
