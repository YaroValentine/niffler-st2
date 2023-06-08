package niffler.jupiter.extensions;

import com.github.javafaker.Faker;
import io.qameta.allure.AllureId;
import niffler.db.dao.NifflerUsersDAO;
import niffler.db.dao.NifflerUsersDAOSpringJdbc;
import niffler.db.entity.Authority;
import niffler.db.entity.AuthorityEntity;
import niffler.db.entity.UserEntity;
import niffler.jupiter.annotation.GenerateUserWithHibernate;
import niffler.jupiter.annotation.GenerateUserWithSpringJdbc;
import org.junit.jupiter.api.extension.*;

import java.util.Arrays;
import java.util.Objects;

public class GenerateUserJdbcSpringExtension implements ParameterResolver, BeforeEachCallback, AfterTestExecutionCallback {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace
            .create(GenerateUserJdbcSpringExtension.class);

    private NifflerUsersDAO usersDAO = new NifflerUsersDAOSpringJdbc();
    private Faker faker = new Faker();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        final String testId = getTestId(context);
        GenerateUserWithSpringJdbc annotation = context.getRequiredTestMethod().getAnnotation(GenerateUserWithSpringJdbc.class);
        if (annotation != null) {
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(faker.name().username());
            userEntity.setPassword("secret");
            userEntity.setEnabled(true);
            userEntity.setAccountNonExpired(true);
            userEntity.setAccountNonLocked(true);
            userEntity.setCredentialsNonExpired(true);

            userEntity.setAuthorities(Arrays.stream(Authority.values())
                    .map(authority -> {
                                AuthorityEntity authorityEntity = new AuthorityEntity();
                                authorityEntity.setAuthority(authority);
                                return authorityEntity;
                            }
                    ).toList());

            usersDAO.createUser(userEntity);
            context.getStore(NAMESPACE).put(testId, userEntity);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserEntity.class)
                && extensionContext.getRequiredTestMethod().getAnnotation(GenerateUserWithSpringJdbc.class) != null;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext,
                                   ExtensionContext extensionContext) throws ParameterResolutionException {
        GenerateUserWithSpringJdbc annotation = extensionContext.getRequiredTestMethod().getAnnotation(GenerateUserWithSpringJdbc.class);
        if (annotation != null) {
            final String testId = getTestId(extensionContext);
            return extensionContext.getStore(NAMESPACE).get(testId, UserEntity.class);
        }
        return null;
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        GenerateUserWithSpringJdbc annotation = context.getRequiredTestMethod().getAnnotation(GenerateUserWithSpringJdbc.class);
        if (annotation != null) {
            final String testId = getTestId(context);
            UserEntity user = (UserEntity) context.getStore(NAMESPACE).get(testId);
            usersDAO.removeUser(user);
        }
    }

    private String getTestId(ExtensionContext context) {
        return Objects
                .requireNonNull(context.getRequiredTestMethod().getAnnotation(AllureId.class))
                .value();
    }
}