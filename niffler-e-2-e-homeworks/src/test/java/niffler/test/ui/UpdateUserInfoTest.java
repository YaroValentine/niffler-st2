package niffler.test.ui;

import io.qameta.allure.AllureId;
import niffler.api.UserService;
import niffler.cycles.BaseTest;
import niffler.jupiter.annotation.ClasspathUser;
import niffler.model.UserJson;
import niffler.page.LoginPage;
import niffler.page.MainPage;
import niffler.test.BaseWebTest;
import okhttp3.OkHttpClient;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateUserInfoTest extends BaseWebTest {
    private static final OkHttpClient httpClient = new OkHttpClient.Builder()
            .build();

    private static final Retrofit retrofit = new Retrofit.Builder()
            .client(httpClient)
            .baseUrl("http://127.0.0.1:8089")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final UserService userService = retrofit.create(UserService.class);

    @AllureId("80")
    @ValueSource(strings = {
            "testdata/user-yarotest.json",
            "testdata/user-dan.json"})
    @ParameterizedTest
    void updateUserInfoFromJson(@ClasspathUser UserJson user) throws IOException {
        boolean updatePosted = userService.updateUserInfo(user)
                .execute().isSuccessful();
        assertTrue(updatePosted);

        open(MainPage.URL, LoginPage.class)
                .doLogin(user.getUsername(), "secret")
                .getHeader().goToProfilePage()
                .verifyNameEquals(user.getFirstname())
                .verifySurnameEquals(user.getSurname())
                .verifyCurrencyEquals(user.getCurrency());

    }
}

