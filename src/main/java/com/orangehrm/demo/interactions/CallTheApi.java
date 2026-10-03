package com.orangehrm.demo.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.JavascriptExecutor;

import java.util.Map;

/**
 * Calls the OrangeHRM REST API (/web/index.php/api/v2) from inside the logged-in browser, so the
 * request reuses the session cookie. Used for fast test setup and clean-up only.
 */
public final class CallTheApi {

    private static final String FETCH = """
            const [path, method, body, done] = arguments;
            fetch('/web/index.php/api/v2' + path, {
                method: method,
                headers: { 'Content-Type': 'application/json' },
                body: body
            })
              .then(response => response.json().catch(() => null)
                  .then(json => done({ status: response.status, body: json })))
              .catch(error => done({ status: -1, body: String(error) }));
            """;

    private CallTheApi() {
    }

    /** Sends the request and returns {@code {status, body}}, with the JSON body parsed by the browser. */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> send(Actor actor, String method, String path, String jsonBody) {
        JavascriptExecutor browser = (JavascriptExecutor) BrowseTheWeb.as(actor).getDriver();
        Object result = browser.executeAsyncScript(FETCH, path, method, jsonBody);
        if (!(result instanceof Map)) {
            throw new IllegalStateException("The browser could not call the OrangeHRM API: " + result);
        }
        return (Map<String, Object>) result;
    }
}
