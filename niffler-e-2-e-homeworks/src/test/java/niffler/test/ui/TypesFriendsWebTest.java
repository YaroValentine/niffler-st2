package niffler.test.ui;

import niffler.jupiter.annotation.User;
import niffler.jupiter.extensions.UsersQueueExtension;
import io.qameta.allure.AllureId;
import niffler.model.UserJson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(UsersQueueExtension.class)
public class TypesFriendsWebTest {

    @AllureId("101")
    @Test
    void correctUserTypesShouldBePulled(@User(userType = User.UserType.WITH_FRIENDS) UserJson userWithFriends,
                                        @User(userType = User.UserType.INVITATION_SENT) UserJson userInviteSent) {

        boolean userWithFriendsLoaded =
                userWithFriends.getUsername().equals("yaro") || userWithFriends.getUsername().equals("dan");
        boolean userWithSentInviteLoaded =
                userInviteSent.getUsername().equals("fantana") || userInviteSent.getUsername().equals("void");

        assertTrue(userWithFriendsLoaded);
        assertTrue(userWithSentInviteLoaded);
    }

}
