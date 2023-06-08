package niffler.page;

import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PeoplePage extends BasePage<PeoplePage> {

    ElementsCollection table = $$(".table tbody tr");

    @Override
    public PeoplePage checkThatPageLoaded() {
        $("people-content").shouldBe(visible);
        return this;
    }

    @Step("Verify Pending invitation exists")
    public PeoplePage verifyPendingInvitationExists() {
        table.find(text("Pending invitation"))
                .shouldBe(visible);
        return this;
    }
}
