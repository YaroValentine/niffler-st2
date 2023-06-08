package niffler.test.ui;

import io.qameta.allure.AllureId;
import niffler.jupiter.annotation.ClasspathUser;
import niffler.model.UserJson;
import niffler.page.LoginPage;
import niffler.page.MainPage;
import niffler.test.BaseWebTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.codeborne.selenide.Selenide.open;

public class LoginTest extends BaseWebTest {

    @ValueSource(strings = {
            "testdata/jakiro.json",
            "testdata/yaro.json"
    })
    @AllureId("104")
    @ParameterizedTest
    void loginTest(@ClasspathUser UserJson user) {
        open(MainPage.URL, LoginPage.class)
                .doLogin(user.getUsername(), user.getPassword())
                .checkThatPageLoaded();
    }

}
