package niffler.test.db;

import io.qameta.allure.AllureId;
import niffler.db.dao.NifflerUsersDAOJdbc;
import niffler.db.entity.UserEntity;
import niffler.jupiter.annotation.GenerateUserWithSpringJdbc;
import niffler.jupiter.extensions.GenerateUserJdbcSpringExtension;
import niffler.test.BaseWebTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GenerateUserJdbcSpringExtension.class)
public class SpringJdbcCrudTests extends BaseWebTest {

    @AllureId("201")
    @GenerateUserWithSpringJdbc
    @Test
    void createUserTest(UserEntity user) {
        System.out.println(user.getUsername());
        Assertions.assertNotNull(user.getUsername());
    }

    @AllureId("202")
    @GenerateUserWithSpringJdbc
    @Test
    void readUserTest(UserEntity user) {
        NifflerUsersDAOJdbc jdbc = new NifflerUsersDAOJdbc();

        Assertions.assertEquals(jdbc.readUser(user.getId()).getAuthorities().get(0).getAuthority().toString(), "read");
        Assertions.assertEquals(jdbc.readUser(user.getId()).getAuthorities().get(1).getAuthority().toString(), "write");

        Assertions.assertTrue(jdbc.readUser(user.getId()).getEnabled());
    }

    @AllureId("203")
    @GenerateUserWithSpringJdbc
    @Test
    void updateUserTest(UserEntity user) {
        Assertions.assertTrue(user.getEnabled());
        NifflerUsersDAOJdbc jdbc = new NifflerUsersDAOJdbc();
        user.setEnabled(false);
        jdbc.updateUser(user);

        Assertions.assertFalse(user.getEnabled());
    }

    @AllureId("204")
    @GenerateUserWithSpringJdbc
    @Test
    void deleteUserTest(UserEntity user) {
        System.out.println("User to delete: " + user.getUsername());
    }
}
