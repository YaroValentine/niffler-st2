package niffler.jupiter.extensions;

import com.github.javafaker.Faker;
import io.qameta.allure.AllureId;
import niffler.db.dao.NifflerUsersDAO;
import niffler.db.dao.NifflerUsersDAOHibernate;
import niffler.db.entity.Authority;
import niffler.db.entity.AuthorityEntity;
import niffler.db.entity.UserEntity;
import niffler.jupiter.annotation.GenerateUserWithHibernate;
import org.junit.jupiter.api.extension.*;

import java.util.Arrays;
import java.util.Objects;

public class GenerateUserHibernateExtension implements ParameterResolver, BeforeEachCallback, AfterTestExecutionCallback {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace
            .create(GenerateUserHibernateExtension.class);
    private Faker faker = new Faker();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        final String testId = getTestId(context);
        GenerateUserWithHibernate annotation = context.getRequiredTestMethod().getAnnotation(GenerateUserWithHibernate.class);
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
                                authorityEntity.setUser(userEntity);
                                return authorityEntity;
                            }
                    ).toList());
            NifflerUsersDAO db = new NifflerUsersDAOHibernate();
            db.createUser(userEntity);
            context.getStore(NAMESPACE).put(testId, userEntity);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserEntity.class)
                && extensionContext.getRequiredTestMethod().getAnnotation(GenerateUserWithHibernate.class) != null;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext,
                                   ExtensionContext extensionContext) throws ParameterResolutionException {
        GenerateUserWithHibernate annotation = extensionContext.getRequiredTestMethod().getAnnotation(GenerateUserWithHibernate.class);
        if (annotation != null) {
            final String testId = getTestId(extensionContext);
            return extensionContext.getStore(NAMESPACE).get(testId, UserEntity.class);
        }
        return null;
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        GenerateUserWithHibernate annotation = context.getRequiredTestMethod().getAnnotation(GenerateUserWithHibernate.class);
        if (annotation != null) {
            final String testId = getTestId(context);
            UserEntity user = (UserEntity) context.getStore(NAMESPACE).get(testId);
            NifflerUsersDAO db = new NifflerUsersDAOHibernate();
            db.removeUser(user);
        }
    }

    private String getTestId(ExtensionContext context) {
        return Objects
                .requireNonNull(context.getRequiredTestMethod().getAnnotation(AllureId.class))
                .value();
    }
}