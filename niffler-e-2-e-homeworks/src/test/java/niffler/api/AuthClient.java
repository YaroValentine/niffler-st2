package niffler.api;

import niffler.api.context.CookieContext;
import niffler.api.context.SessionContext;
import niffler.api.interceptor.AddCookiesInterceptor;
import niffler.api.interceptor.ReceivedCodeInterceptor;
import niffler.api.interceptor.RecievedCookiesInterceptor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AuthClient extends BaseRestClient {

    private final AuthService authService = retrofit.create(AuthService.class);

    public AuthClient() {
        super(
                CFG.getAuthUrl(),
                true,
                new RecievedCookiesInterceptor(),
                new AddCookiesInterceptor(),
                new ReceivedCodeInterceptor()
        );
    }

    public void authorizedPreRequest() {
        try {
            authService.authorize(
                    "code",
                    "client",
                    "openid",
                    CFG.getFrontUrl() + "/authorized",
                    SessionContext.getInstance().getCodeChallenge(),
                    "S256"
            ).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void login(String username, String password) {
        final CookieContext cookieContext = CookieContext.getInstance();

        try {
            authService.login(
                    cookieContext.getFormattedCookie("JSESSIONID"),
                    cookieContext.getFormattedCookie("XSRF-TOKEN"),
                    cookieContext.getCookie("XSRF-TOKEN"),
                    username,
                    password
            ).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getToken() {
        final SessionContext sessionContext = SessionContext.getInstance();

        try {
            return authService.token(
                    "Basic " + Base64.getEncoder().encodeToString("client:secret".getBytes(StandardCharsets.UTF_8)),
                    "client",
                    CFG.getFrontUrl() + "/authorized",
                    "authorization_code",
                    sessionContext.getCode(),
                    sessionContext.getCodeVerifier()
            ).execute().body().get("id_token").asText();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void signUp(String username, String password) {
        final CookieContext cookieContext = CookieContext.getInstance();

        try {
            authService.register().execute();
            authService.signUp(
                    cookieContext.getFormattedCookie("XSRF-TOKEN"),
                    cookieContext.getCookie("XSRF-TOKEN"),
                    username,
                    password,
                    password
            ).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
