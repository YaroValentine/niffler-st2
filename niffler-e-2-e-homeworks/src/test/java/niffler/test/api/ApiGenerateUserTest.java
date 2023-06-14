package niffler.test.api;

import io.qameta.allure.AllureId;
import niffler.jupiter.annotation.GenerateUserWithApi;
import niffler.model.SpendJson;
import niffler.model.UserJson;
import niffler.page.LoginPage;
import niffler.page.MainPage;
import niffler.test.BaseWebTest;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

public class ApiGenerateUserTest extends BaseWebTest {

    @GenerateUserWithApi(username = "", password = "")
    @AllureId("501")
    @Test
    void apiRegistrationTest(UserJson user) {
        open(MainPage.URL, LoginPage.class)
                .doLogin(user.getUsername(), user.getPassword())
                .checkThatPageLoaded();
    }

}


