package com.orangehrm.demo.interactions;

import com.orangehrm.demo.ui.AddEmployeePage;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.WebElement;

import java.nio.file.Path;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/**
 * Uploads a profile photo on the "Add Employee" form.
 * OrangeHRM hides the native file input behind a "+" button, and WebDriver can only type a file
 * path into a displayed input, so the input is made visible first, then the preview is awaited.
 */
public class UploadProfilePhoto implements Interaction {

    private static final String SHOW_ELEMENT = "arguments[0].style.display = 'block';";

    private final Path photo;

    public UploadProfilePhoto(Path photo) {
        this.photo = photo.toAbsolutePath();
    }

    public static UploadProfilePhoto from(Path photo) {
        return new UploadProfilePhoto(photo);
    }

    @Override
    @Step("{0} uploads the profile photo #photo")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(WaitFor.the(AddEmployeePage.UPLOAD_PHOTO_BUTTON, isVisible()));

        BrowseTheWeb browser = BrowseTheWeb.as(actor);
        WebElement fileInput = AddEmployeePage.PHOTO_FILE_INPUT.resolveFor(actor);
        browser.evaluateJavascript(SHOW_ELEMENT, fileInput);
        fileInput.sendKeys(photo.toString());

        actor.attemptsTo(WaitFor.the(AddEmployeePage.PHOTO_PREVIEW, isVisible()));
    }
}
