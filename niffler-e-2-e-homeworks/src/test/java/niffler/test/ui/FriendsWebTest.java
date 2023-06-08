package niffler.test.ui;

import io.qameta.allure.AllureId;
import niffler.jupiter.annotation.User;
import niffler.jupiter.extensions.UsersQueueExtension;
import niffler.model.UserJson;
import niffler.page.LoginPage;
import niffler.page.MainPage;
import niffler.test.BaseWebTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;

@ExtendWith(UsersQueueExtension.class)
public class FriendsWebTest extends BaseWebTest {

    @AllureId("101")
    @Test
    void friendsShouldBeVisible0(@User(userType = User.UserType.WITH_FRIENDS) UserJson user) {
        open(MainPage.URL, LoginPage.class)
                .doLogin(user.getUsername(), user.getPassword())
                .checkThatPageLoaded()
                .getHeader().goToFriendsPage()
                .verifyFriendsTableNotEmpty();
    }

    @AllureId("101")
    @Test
    void friendsShouldBeVisible1(@User(userType = User.UserType.WITH_FRIENDS) UserJson user) {
        open(MainPage.URL, LoginPage.class)
                .doLogin(user.getUsername(), user.getPassword())
                .checkThatPageLoaded()
                .getHeader().goToFriendsPage()
                .verifyFriendsTableNotEmpty();
    }

    @AllureId("101")
    @Test
    void friendsShouldBeVisible2(@User(userType = User.UserType.WITH_FRIENDS) UserJson user) {
        open(MainPage.URL, LoginPage.class)
                .doLogin(user.getUsername(), user.getPassword())
                .checkThatPageLoaded()
                .getHeader().goToFriendsPage()
                .verifyFriendsTableNotEmpty();
    }

    @AllureId("101")
    @Test
    void friendsShouldBeVisible3(@User(userType = User.UserType.INVITATION_SENT) UserJson user) {
        open(MainPage.URL, LoginPage.class)
                .doLogin(user.getUsername(), user.getPassword())
                .checkThatPageLoaded()
                .getHeader().goToPeoplePage()
                .verifyPendingInvitationExists();
    }

    @AllureId("101")
    @Test
    void friendsShouldBeVisible4(@User(userType = User.UserType.INVITATION_SENT) UserJson user) {
        open(MainPage.URL, LoginPage.class)
                .doLogin(user.getUsername(), user.getPassword())
                .checkThatPageLoaded()
                .getHeader().goToPeoplePage()
                .verifyPendingInvitationExists();
    }

}
