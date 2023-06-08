package niffler.api;


import niffler.config.Config;
import niffler.model.CategoryJson;
import niffler.model.SpendJson;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;

public class SpendRestClient extends BaseRestClient {
    private final SpendService spendService = retrofit.create(SpendService.class);

    public SpendRestClient() {
        super(Config.getConfig().getSpendUrl());
    }

    public SpendJson addSpend(SpendJson spend) {
        try {
            return spendService.addSpend(spend).execute().body();
        } catch (IOException e) {
            Assertions.fail("Can't execute api call to niffler-spend: " + e.getMessage());
            return null;
        }
    }
    public CategoryJson addCategory(CategoryJson category) {
        try {
            return spendService.addCategory(category).execute().body();
        } catch (IOException e) {
            Assertions.fail("Can't execute api call to niffler-spend: " + e.getMessage());
            return null;
        }
    }


}
