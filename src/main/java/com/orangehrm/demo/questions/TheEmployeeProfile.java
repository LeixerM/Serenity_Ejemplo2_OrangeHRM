package com.orangehrm.demo.questions;

import com.orangehrm.demo.ui.PersonalDetailsPage;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.questions.Text;
import org.openqa.selenium.JavascriptExecutor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TheEmployeeProfile {

    private static final Pattern EMPLOYEE_NUMBER = Pattern.compile("/empNumber/(\\d+)");

    /** Downloads the stored photo with the browser session and returns its SHA-256 as hex. */
    private static final String PHOTO_SHA_256 = """
            const [employeeNumber, done] = arguments;
            fetch('/web/index.php/pim/viewPhoto/empNumber/' + employeeNumber)
              .then(response => response.arrayBuffer())
              .then(bytes => crypto.subtle.digest('SHA-256', bytes))
              .then(hash => done(Array.from(new Uint8Array(hash))
                  .map(b => b.toString(16).padStart(2, '0')).join('')))
              .catch(error => done('error: ' + error));
            """;

    private TheEmployeeProfile() {
    }

    public static Question<String> displayedName() {
        return Question.about("the name on the employee profile")
                .answeredBy(actor -> Text.of(PersonalDetailsPage.EMPLOYEE_NAME).answeredBy(actor).trim());
    }

    /** Internal OrangeHRM employee number, read from the profile URL (.../empNumber/123). */
    public static Question<String> employeeNumber() {
        return Question.about("the employee number")
                .answeredBy(actor -> {
                    String url = BrowseTheWeb.as(actor).getDriver().getCurrentUrl();
                    Matcher matcher = EMPLOYEE_NUMBER.matcher(url);
                    if (!matcher.find()) {
                        throw new IllegalStateException("Not on an employee profile page: " + url);
                    }
                    return matcher.group(1);
                });
    }

    public static Question<String> storedPhotoChecksum(String employeeNumber) {
        return Question.about("the SHA-256 of the stored profile photo")
                .answeredBy(actor -> String.valueOf(((JavascriptExecutor) BrowseTheWeb.as(actor).getDriver())
                        .executeAsyncScript(PHOTO_SHA_256, employeeNumber)));
    }
}
