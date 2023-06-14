package niffler.jupiter.extensions;

import niffler.api.SpendRestClient;
import niffler.jupiter.annotation.GenerateSpend;
import niffler.model.CategoryJson;
import niffler.model.SpendJson;
import org.junit.jupiter.api.extension.*;

import java.util.Date;


public class GenerateSpendAndCategoryExtensionTest implements ParameterResolver, BeforeEachCallback {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace
            .create(GenerateSpendAndCategoryExtensionTest.class);

    private final SpendRestClient spendRestClient = new SpendRestClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        GenerateSpend generateSpend = context.getRequiredTestMethod()
                .getAnnotation(GenerateSpend.class);

        if (generateSpend != null) {
            SpendJson spend = new SpendJson();
            spend.setUsername(generateSpend.username());
            spend.setAmount(generateSpend.amount());
            spend.setDescription(generateSpend.description());
            spend.setCategory(generateSpend.category());
            spend.setSpendDate(new Date());
            spend.setCurrency(generateSpend.currency());

            CategoryJson category = new CategoryJson();
            category.setUsername(generateSpend.username());
            category.setCategory(generateSpend.category());
            spendRestClient.addCategory(category);

            SpendJson created = spendRestClient.addSpend(spend);
            context.getStore(NAMESPACE).put("spend", created);

        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class);
    }

    @Override
    public SpendJson resolveParameter(ParameterContext parameterContext,
                                      ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get("spend", SpendJson.class);
    }
}
