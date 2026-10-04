package com.orangehrm.demo.interactions;

import net.serenitybdd.core.pages.WebElementState;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.targets.Target;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.hamcrest.Matcher;

import java.time.Duration;

/** One explicit, documented wait budget instead of ad-hoc timeouts scattered across tasks. */
public final class WaitFor {

    /**
     * The public demo is shared by many users and is sometimes slow, so elements get up to 20 seconds.
     * Waits poll and return as soon as the condition holds; this is only the upper bound.
     */
    public static final Duration DEMO_SITE_TIMEOUT = Duration.ofSeconds(20);

    private WaitFor() {
    }

    public static Interaction the(Target target, Matcher<WebElementState> expectedState) {
        return WaitUntil.the(target, expectedState).forNoMoreThan(DEMO_SITE_TIMEOUT);
    }
}
