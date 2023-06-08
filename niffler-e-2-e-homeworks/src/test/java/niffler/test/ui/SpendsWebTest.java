package niffler.test.ui;

import io.qameta.allure.AllureId;
import niffler.jupiter.annotation.GenerateSpend;
import niffler.jupiter.extensions.GenerateSpendExtension;
import niffler.model.CurrencyValues;
import niffler.model.SpendJson;
import niffler.page.LoginPage;
import niffler.page.MainPage;
import niffler.test.BaseWebTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;

@ExtendWith(GenerateSpendExtension.class)
public class SpendsWebTest extends BaseWebTest {

    @BeforeEach
    void doLogin() {
        open(MainPage.URL, LoginPage.class)
                .doLogin("yaro", "secret");
    }

    @GenerateSpend(
            username = "yaro",
            description = "QA GURU ADVANCED VOL 2",
            currency = CurrencyValues.USD,
            amount = 700.00,
            category = "learning"
    )
    @AllureId("101")
    @Test
    void spendShouldBeDeletedByActionInTable(SpendJson spend) {
        open(MainPage.URL, MainPage.class)
                .deleteSpending(spend.getDescription());
    }
}
