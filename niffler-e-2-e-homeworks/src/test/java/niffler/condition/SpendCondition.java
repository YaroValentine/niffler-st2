package niffler.condition;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.impl.CollectionSource;
import niffler.api.util.DateHelper;
import niffler.model.CurrencyValues;
import niffler.model.SpendJson;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.openqa.selenium.By.*;

public class SpendCondition {

    public static CollectionCondition spends(SpendJson... expectedSpends) {
        return new CollectionCondition() {

            @Override
            public void fail(CollectionSource collection, @Nullable List<WebElement> elements,
                             @Nullable Exception lastError, long timeoutMs) {
                if (elements == null || elements.isEmpty()) {
                    throw new ElementNotFound(collection, toString(), timeoutMs, lastError);
                } else if (elements.size() != expectedSpends.length) {
                    throw new SpendsSizeMismatch(collection,
                            Arrays.asList(expectedSpends),
                            elements.stream().map(SpendCondition::convertWebElementToSpendJson).toList(),
                            explanation,
                            timeoutMs);
                } else {
                    throw new SpendsMismatch(
                            collection,
                            Arrays.asList(expectedSpends),
                            elements.stream().map(SpendCondition::convertWebElementToSpendJson).toList(),
                            explanation,
                            timeoutMs);
                }
            }

            @Override
            public boolean missingElementSatisfiesCondition() {
                return false;
            }

            @Override
            public boolean test(List<WebElement> webElements) {
                if (webElements.size() != expectedSpends.length) {
                    return false;
                }

                for (int i = 0; i < expectedSpends.length; i++) {
                    WebElement row = webElements.get(i);
                    SpendJson expectedSpend = expectedSpends[i];
                    SpendJson actualSpend = convertWebElementToSpendJson(row);
                    expectedSpend.setSpendDate(DateHelper.convertJavaDateToFrontDate(expectedSpend.getSpendDate()));
                    if (!expectedSpend.equals(actualSpend)) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    private static SpendJson convertWebElementToSpendJson(WebElement row) {
        Date dateFromTable = DateHelper.convertFrontDateToJavaDate(row.findElements(cssSelector("td")).get(1).getText());
        SpendJson sj = new SpendJson();
        sj.setSpendDate(dateFromTable);
        sj.setAmount(Double.valueOf(row.findElements(cssSelector("td")).get(2).getText()));
        sj.setCurrency(CurrencyValues.valueOf(row.findElements(cssSelector("td")).get(3).getText()));
        sj.setCategory(row.findElements(cssSelector("td")).get(4).getText());
        sj.setDescription(row.findElements(cssSelector("td")).get(5).getText());
        return sj;
    }

}
