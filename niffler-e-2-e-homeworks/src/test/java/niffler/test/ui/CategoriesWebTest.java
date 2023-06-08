package niffler.test.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import niffler.jupiter.annotation.GenerateCategory;
import niffler.jupiter.extensions.GenerateCategoryExtension;
import niffler.model.CategoryJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

@Disabled
@ExtendWith(GenerateCategoryExtension.class)
public class CategoriesWebTest {

    @BeforeEach
    void doLogin() {
        Configuration.baseUrl = "http://127.0.0.1:3000";

        Selenide.open("/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue("yaro");
        $("input[name='password']").setValue("secret");
        $("button[type='submit']").click();
    }

    @GenerateCategory(
            username = "yaro",
            category = "test")
    @Test
    void categoryShouldBeCreated(CategoryJson category) {
        open("/profile");

        $(".main-content__section-categories ul")
                .shouldHave(text(category.getCategory()));
    }
}
