package niffler.test.db;

import com.codeborne.selenide.Selenide;
import niffler.db.dao.NifflerUsersDAO;
import niffler.db.dao.NifflerUsersDAOHibernate;
import niffler.db.dao.NifflerUsersDAOSpringJdbc;
import niffler.db.entity.Authority;
import niffler.db.entity.AuthorityEntity;
import niffler.db.entity.UserEntity;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import niffler.page.LoginPage;
import niffler.page.MainPage;
import niffler.test.BaseWebTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginNewUserTest extends BaseWebTest {

    private NifflerUsersDAO usersDAO = new NifflerUsersDAOSpringJdbc();
    private UserEntity userEntity;
    private final static String TEST_PWD = "secret";

    @BeforeEach
    void createUserForTest() {
        userEntity = new UserEntity();
        userEntity.setUsername("valentin9");
        userEntity.setPassword(TEST_PWD);
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

        usersDAO.createUser(userEntity);
    }


    @AfterEach
    void cleanUp() {
        usersDAO.removeUser(userEntity);
    }

    @AllureId("200")
    @Test
    void loginTest() {
        Allure.step("open page", () -> open(MainPage.URL, LoginPage.class)
                .doLogin(userEntity.getUsername(), TEST_PWD)
                .checkThatPageLoaded());
    }
}
