package com.orangehrm.demo.ui;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public final class AddEmployeePage {

    public static final Target ADD_EMPLOYEE_TAB = Target.the("the 'Add Employee' tab")
            .locatedBy("//nav//a[normalize-space()='Add Employee']");
    public static final Target UPLOAD_PHOTO_BUTTON = Target.the("the upload photo button")
            .locatedBy("//button[.//i[contains(@class,'bi-plus')]]");
    public static final Target PHOTO_FILE_INPUT = Target.the("the photo file input").located(By.cssSelector("input.oxd-file-input"));
    public static final Target PHOTO_PREVIEW = Target.the("the photo preview")
            .located(By.cssSelector("img.employee-image[src^='data:image']"));
    public static final Target FIRST_NAME = Target.the("the first name field").located(By.cssSelector("input[name='firstName']"));
    public static final Target MIDDLE_NAME = Target.the("the middle name field").located(By.cssSelector("input[name='middleName']"));
    public static final Target LAST_NAME = Target.the("the last name field").located(By.cssSelector("input[name='lastName']"));
    public static final Target EMPLOYEE_ID = Target.the("the employee id field")
            .locatedBy("//label[normalize-space()='Employee Id']/../following-sibling::div/input");
    public static final Target SAVE = Target.the("the 'Save' button").located(By.cssSelector("button[type='submit']"));

    private AddEmployeePage() {
    }
}
