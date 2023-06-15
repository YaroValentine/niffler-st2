package niffler.jupiter.extensions;

import com.github.javafaker.Faker;
import io.qameta.allure.AllureId;
import niffler.api.AuthClient;
import niffler.api.UserdataClient;
import niffler.jupiter.annotation.GenerateUserWithApi;
import niffler.model.UserJson;
import org.junit.jupiter.api.extension.*;

import java.util.Objects;

public class GenerateUserApiExtension implements BeforeEachCallback, ParameterResolver {
    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace
            .create(GenerateUserApiExtension.class);
    private final AuthClient authClient = new AuthClient();
    private final UserdataClient userClient = new UserdataClient();
    private Faker faker = new Faker();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        GenerateUserWithApi annotation = context.getRequiredTestMethod().getAnnotation(GenerateUserWithApi.class);
        if (annotation != null) {
            UserJson user = new UserJson();
            if (annotation.username().equals("") && annotation.password().equals("")) {
                user.setUsername(faker.name().username());
                user.setPassword(faker.internet().password(5, 8, false, false));
            } else {
                user.setUsername(annotation.username());
                user.setPassword(annotation.password());
            }

            authClient.signUp(user.getUsername(), user.getPassword());
            UserJson createdUser = userClient.getUser(user.getUsername());
            createdUser.setPassword(user.getPassword());
            context.getStore(NAMESPACE).put(getTestId(context), createdUser);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(getTestId(extensionContext), UserJson.class);
    }

    private String getTestId(ExtensionContext context) {
        return Objects
                .requireNonNull(context.getRequiredTestMethod().getAnnotation(AllureId.class))
                .value();
    }

}
