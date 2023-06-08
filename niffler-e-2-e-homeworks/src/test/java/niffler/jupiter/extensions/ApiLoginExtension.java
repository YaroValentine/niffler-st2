package niffler.jupiter.extensions;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import niffler.api.AuthClient;
import niffler.api.context.CookieContext;
import niffler.api.context.SessionContext;
import niffler.api.util.OauthUtils;
import niffler.jupiter.annotation.ApiLogin;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Cookie;

public class ApiLoginExtension implements BeforeEachCallback, AfterTestExecutionCallback {

    private final AuthClient authClient = new AuthClient();
    private static final String JSESSIONID = "JSESSIONID";

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        ApiLogin apiLogin = context.getRequiredTestMethod().getAnnotation(ApiLogin.class);
        if (apiLogin != null) {
            doLogin(apiLogin.username(), apiLogin.password());
        }
    }

    private void doLogin(String username, String password) {
        SessionContext sessionContext = SessionContext.getInstance();
        CookieContext cookieContext  = CookieContext.getInstance();
        final String codeVerifier = OauthUtils.generateCodeVerifier();
        final String codeChallenge = OauthUtils.generateCodeChallenge(codeVerifier);

        sessionContext.setCodeVerifier(codeVerifier);
        sessionContext.setCodeChallenge(codeChallenge);

        authClient.authorizedPreRequest();
        authClient.login(username, password);
        final String token = authClient.getToken();
        Selenide.sessionStorage().setItem("id_token", token);
        Selenide.sessionStorage().setItem("codeChallenge", sessionContext.getCodeChallenge());
        Selenide.sessionStorage().setItem("codeVerifier", sessionContext.getCodeVerifier());
        Cookie jsessionIdCookie = new Cookie(JSESSIONID, cookieContext.getCookie(JSESSIONID));
        WebDriverRunner.getWebDriver().manage().addCookie(jsessionIdCookie);
    }


    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        SessionContext.getInstance().release();
        CookieContext.getInstance().release();
    }
}
