# Regression baseline

Before any change, the original suite on `main` (`b0eb509`) was run unchanged. It was then upgraded one step at a
time, with a run after each step, so that any regression points to a single bump.

- Date: 2026-10-03
- Machine: Windows 11, JDK 21.0.9 (Temurin), Google Chrome 154 (stable)
- Command for every step: `./gradlew clean test` (project's own wrapper)

## 1. Baseline: original suite, unchanged

Stack: Serenity BDD 3.9.8, Cucumber 7.15.0, Selenium 4.19.1, JUnit 4 runner (`CucumberWithSerenity`), Gradle 8.13. Runs on Java 21 without changes.

| Scenario | Result | Reason |
|---|---|---|
| Register employee / Successful register employee | **FAIL** | `Expected: an element that is visible but: no matching element found ... (//a[@class='oxd-main-menu-item'])[2]`. Login worked, but headless Chrome opened with a ~780px window (`start-maximized` has no effect in headless mode). OrangeHRM then collapsed its side menu, so the PIM link was never visible. This is also why CI failed on every run since Nov 2025. |

## 2. Step-by-step upgrade

| Step | Commit | Change | Result of the original scenario |
|---|---|---|---|
| 0 | `b0eb509` (main) | none | FAIL: side menu collapsed (see above) |
| 1 | `4d3531e` | `serenity.conf`: `headless=new`, `window-size=1920,1080` (config only, Serenity 3.9.8) | FAIL, later in the flow. Login, PIM, add employee with avatar, save and directory search all pass. The final check fails: `Expected: "data:image/png;base64,..." but: was ".../pim/viewPhoto/empNumber/128"`. The test compares the preview `src` (a data URI) with the directory photo URL, which can never be equal. On top of that, the directory search picks the first suggestion for a random first name only, so it can land on another employee (empNumber 128 on two consecutive runs). |
| 2 | `3bae875` | Serenity 3.9.8 → 4.3.4, Cucumber 7.15.0 → 7.31.0, Serenity Gradle plugin 4.3.2. Explicit Selenium 4.19.1 and WebDriverManager removed (Serenity manages Selenium; Selenium Manager resolves the driver). Three `net.thucydides.core`/`net.serenitybdd.core` imports moved to `*.model.*`. | Same as step 1. All flow steps pass and the same final image check fails. No regression. |
| 3 | `0990303` | Serenity 5.3.11, Cucumber 7.34.2, JUnit 6.0.3 Platform Suite runner, Gradle 8.14.5, Java 21 toolchain | First run: **regression**. `WaitUntil(INPUT_UPLOAD_AVATAR, isPresent())` failed because Serenity 5 no longer treats the hidden file input as present. Fixed in the same commit by waiting for the visible "+" upload button instead. Re-run: same as step 1 (no regression left). |
| 4 | `85f4909` | Rebuilt suite (see mapping below) | PASS |

Other Serenity 5 behaviours found while rebuilding, all handled in step 4:

- Plain string locators without a leading `#`, `.`, `//` or `(` are treated as XPath. `input[name='username']` and `input.oxd-file-input` matched nothing, so all CSS locators now use `By.cssSelector`.
- `WaitUntil` without `forNoMoreThan` waits only the implicit timeout (2 s by default), so all waits go through `WaitFor` with an explicit 20 s budget.
- `WebElementStateMatchers.containsText` failed on a heading that was still rendering. The wait now targets a locator that only matches once the expected text is present.

## 3. Version changes

| Dependency | Old | New |
|---|---|---|
| Java | 17 in CI (local toolchain not pinned) | 21 (Gradle toolchain + CI) |
| Gradle wrapper | 8.13 | 8.14.5 |
| Serenity BDD (core, cucumber, screenplay, screenplay-webdriver) | 3.9.8 | 5.3.11 (via 4.3.4) |
| Serenity Gradle plugin | 3.9.8 | 5.3.9 (via 4.3.2) |
| Cucumber | 7.15.0 (`cucumber-junit`, JUnit 4) | 7.34.2 (`cucumber-junit-platform-engine`) |
| Test runner | JUnit 4 `CucumberWithSerenity` | JUnit 6.0.3 Platform `@Suite` + `SerenityReporterParallel` |
| Selenium | 4.19.1 (explicit) | managed by Serenity 5.3.11 (4.46.0) |
| `serenity-junit`, WebDriverManager 5.4.1, AssertJ 3.23.1, Lombok 1.18.30, JavaFaker 1.0.2 | present | removed (unused or replaced by records, `Ensure` and UUID/SecureRandom data) |
| `serenity-ensure`, Logback | absent | 5.3.11, 1.5.38 |

## 4. Scenario mapping (old → new)

| Old scenario | New scenario | Baseline | Upgraded (2026-10-03) |
|---|---|---|---|
| Successful register employee: create an employee with an avatar, find it in the directory, compare name and image | `@create` Add an employee with a profile photo | FAIL (collapsed menu; even with a desktop window, an image check that can never pass) | PASS. Checks the profile name and that the stored photo is byte-identical (SHA-256) to the uploaded file. |
| (same scenario, search part) | `@search` Find an employee by name | FAIL (see above) | PASS. The full unique name returns exactly one row with the right id, first/middle and last name. |
| none | `@delete` Delete an employee | n/a | PASS |

Every scenario now creates its own uniquely named employee and removes it in an `@After` hook.

No baseline-passing behaviour existed to regress: the only original scenario failed on baseline. Its intent
(create an employee with an avatar and find them) is covered by `@create` and `@search`, both passing.

Upgraded run: `./gradlew clean test aggregate` passed 6 tests with 0 failures (3 Cucumber scenarios and 3 unit
tests), on 3 of 4 consecutive local runs. One run failed in the `@search` Background because the shared demo
served a blank login page for over 20 s. `OpenTheLoginPage` now reloads once in that case, and the two runs after
that change both passed.
