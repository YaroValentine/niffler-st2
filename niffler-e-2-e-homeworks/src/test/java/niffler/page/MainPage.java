package niffler.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import niffler.page.component.Header;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage extends BasePage<MainPage> {
    public static final String URL = "main";
    private final Header header = new Header();
    private final ElementsCollection spendingTbl = $(".spendings-table tbody").$$("tr");
    private final SelenideElement deleteSelectedBtn = $(byText("Delete selected"));

    public Header getHeader() {
        return header;
    }

    @Override
    public MainPage checkThatPageLoaded() {
        getHeader().checkThatComponentDisplayed();
        return this;
    }

    @Step("Select Spending by description: {spendDescription}")
    public MainPage selectSpendingByDescription(String spendDescription) {
        spendingTbl.find(text(spendDescription))
                .$$("td").first().scrollTo()
                .click();
        return this;
    }

    @Step("Click Delete Selected")
    public MainPage clickDeleteSelected() {
        deleteSelectedBtn.click();
        return this;
    }

    @Step("Verify Spending table is empty")
    public void verifySpendingTableIsEmpty() {
        spendingTbl.shouldHave(size(0));
    }

    @Step("Delete Spending: {spendDescription}")
    public MainPage deleteSpending(String description) {
        selectSpendingByDescription(description)
                .clickDeleteSelected()
                .verifySpendingTableIsEmpty();
        return this;
    }

}
