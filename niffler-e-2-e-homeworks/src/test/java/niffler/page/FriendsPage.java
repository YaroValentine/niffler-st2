package niffler.page;

import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;
import niffler.page.component.Header;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selenide.$$;

public class FriendsPage extends BasePage<FriendsPage> {

    private final Header header = new Header();
    private final ElementsCollection firendsTable = $$(".table tbody tr");

    public Header getHeader() {
        return header;
    }

    @Override
    public FriendsPage checkThatPageLoaded() {
        return null;
    }

    @Step("Verify Friends table not empty")
    public FriendsPage verifyFriendsTableNotEmpty() {
        firendsTable.shouldHave(sizeGreaterThan(0));
        return this;
    }
}
