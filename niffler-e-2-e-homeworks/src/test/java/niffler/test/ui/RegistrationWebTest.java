package niffler.test.ui;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.AllureId;
import niffler.page.RegistrationPage;
import niffler.test.BaseWebTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

public class RegistrationWebTest extends BaseWebTest {

    private RegistrationPage page = new RegistrationPage();

    @AllureId("10")
    @Test
    @ResourceLock("TEST_LOCK")
    public void errorMessageShouldBeVisibleInCaseThatPasswordsAreDifferent() {
        Selenide.open(RegistrationPage.URL);

        page.checkThatPageLoaded()
                .fillRegistrationForm("wdfsdasfs", "123", "12345")
                .checkErrorMessage("Passwords should be equal");
    }

    @AllureId("11")
    @Test
    @ResourceLock("TEST_LOCK")
    public void errorMessageShouldBeVisibleInCaseThatUsernameNotUniq() {
        final String username = "yaro";

        Selenide.open(RegistrationPage.URL);
        page.checkThatPageLoaded()
                .fillRegistrationForm(username, "secret", "secret")
                .checkErrorMessage("Username `" + username + "` already exists");
    }

    @AllureId("12")
    @Test
    public void errorMessageShouldBeVisibleInCaseThatPasswordsLessThan3Symbols() {
        Selenide.open(RegistrationPage.URL);
        page.checkThatPageLoaded()
                .fillRegistrationForm("wdfsdadfdaasfs", "1", "1")
                .checkErrorMessage("Allowed password length should be from 3 to 12 characters");
    }

    @AllureId("13")
    @Test
    public void errorMessageShouldBeVisibleInCaseThatUsernameLessThan3Symbols() {
        Selenide.open(RegistrationPage.URL);
        page.checkThatPageLoaded()
                .fillRegistrationForm("g", "12345", "12345")
                .checkErrorMessage("Allowed username length should be from 3 to 50 characters");
    }
}
