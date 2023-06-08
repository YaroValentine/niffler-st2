package niffler.test.db;

import niffler.db.dao.NifflerUsersDAOJdbc;
import niffler.jupiter.annotation.GenerateUserWithJdbc;
import io.qameta.allure.AllureId;
import niffler.db.entity.UserEntity;
import niffler.test.BaseWebTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JdbcCrudTests extends BaseWebTest {

    @AllureId("201")
    @GenerateUserWithJdbc
    @Test
    void createUserTest(UserEntity user){
        System.out.println(user.getUsername());
        Assertions.assertNotNull(user.getUsername());
    }

    @AllureId("202")
    @GenerateUserWithJdbc
    @Test
    void readUserTest(UserEntity user){
        NifflerUsersDAOJdbc jdbc = new NifflerUsersDAOJdbc();
        Assertions.assertTrue(jdbc.readUser(user.getId()).getEnabled());
    }

    @AllureId("203")
    @GenerateUserWithJdbc
    @Test
    void updateUserTest(UserEntity user){
        Assertions.assertTrue(user.getEnabled());
        NifflerUsersDAOJdbc jdbc = new NifflerUsersDAOJdbc();
        user.setEnabled(false);
        jdbc.updateUser(user);
        Assertions.assertFalse(user.getEnabled());
    }

    @AllureId("204")
    @GenerateUserWithJdbc
    @Test
    void deleteUserTest(UserEntity user){
        System.out.println("User to delete: " + user.getUsername());
    }
}
