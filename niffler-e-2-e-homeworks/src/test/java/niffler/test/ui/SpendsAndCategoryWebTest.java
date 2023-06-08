package niffler.test.ui;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.AllureId;
import niffler.jupiter.annotation.GenerateCategory;
import niffler.jupiter.annotation.GenerateSpend;
import niffler.jupiter.extensions.GenerateSpendAndCategoryExtensionTest;
import niffler.model.CurrencyValues;
import niffler.model.SpendJson;
import niffler.page.LoginPage;
import niffler.page.MainPage;
import niffler.page.ProfilePage;
import niffler.test.BaseWebTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;

@ExtendWith(GenerateSpendAndCategoryExtensionTest.class)
public class SpendsAndCategoryWebTest extends BaseWebTest {

    @BeforeEach
    void doLogin() {
        Selenide.open(MainPage.URL, LoginPage.class)
                .doLogin("yaro", "secret");
    }

    @GenerateSpend(
            username = "yaro",
            description = "QA GURU ADVANCED VOL 2",
            currency = CurrencyValues.USD,
            amount = 700.00,
            category = "test"
    )
    @GenerateCategory(
            username = "yaro",
            category = "test"
    )
    @Test
    @AllureId("90")
    void spendShouldBeDeletedByActionInTable(SpendJson spend) {
        open(ProfilePage.URL, ProfilePage.class)
                .verifySpendingExistsInAllYourSpendingCategoriesTable(spend.getCategory());

        open(MainPage.URL, MainPage.class)
                .deleteSpending(spend.getDescription());
    }

}
