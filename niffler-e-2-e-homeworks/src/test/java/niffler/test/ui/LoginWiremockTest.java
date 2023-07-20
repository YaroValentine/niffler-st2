package niffler.test.ui;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.qameta.allure.AllureId;
import niffler.jupiter.annotation.ClasspathUser;
import niffler.model.UserJson;
import niffler.page.LoginPage;
import niffler.page.MainPage;
import niffler.test.BaseWebTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.codeborne.selenide.Selenide.open;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

@WireMockTest(httpPort = 8089)
public class LoginWiremockTest extends BaseWebTest {

/*
    @RegisterExtension
    WireMockExtension wm = WireMockExtension.newInstance()
            .options(WireMockConfiguration.options().port(8089))
            .configureStaticDsl(true)
            .build();
*/

    @ValueSource(strings = {"testdata/yaro.json"})
    @AllureId("104")
    @ParameterizedTest
    void loginTest(@ClasspathUser UserJson user) {

        stubFor(get("/currentUser?username=yaro")
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("Content-type", "Application/json")
                                .withBody("""
                                        {
                                          "id": "7a2e2e46-49e1-4db0-97ed-00a388ecc7d0",
                                          "username": "yaro",
                                          "firstname": "Yaroslav",
                                          "surname": "V",
                                          "currency": "KZT",
                                          "photo": null
                                        }"""
                                )

                ));

        open(MainPage.URL, LoginPage.class)
                .doLogin(user.getUsername(), user.getPassword())
                .checkThatPageLoaded();

    }

}
