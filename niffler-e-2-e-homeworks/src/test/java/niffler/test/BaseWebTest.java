package niffler.test;

import com.codeborne.selenide.Configuration;
import niffler.config.Config;
import niffler.jupiter.annotation.WebTest;

@WebTest
public abstract class BaseWebTest {
    protected static final Config CFG = Config.getConfig();

    static {
        Configuration.browserSize = "1920x1080";
        Configuration.baseUrl = CFG.getFrontUrl();
    }

}