package niffler.test.grpc;

import guru.qa.grpc.niffler.grpc.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class NifflerCurrencyGrpcTest extends BaseGrpcTest {

    @Test
    void getAllCurrenciesTest() {
        CurrencyResponse allCurrencies = currencyStub.getAllCurrencies(EMPTY);
        List<Currency> currenciesList = allCurrencies.getAllCurrenciesList();
        Assertions.assertEquals(CurrencyValues.RUB, currenciesList.get(0).getCurrency());
        Assertions.assertEquals(4, allCurrencies.getAllCurrenciesList().size());
    }

    @Test
    void calculateRateTest() {
        final CalculateRequest cr = CalculateRequest.newBuilder()
                .setAmount(100.0)
                .setSpendCurrency(CurrencyValues.RUB)
                .setDesiredCurrency(CurrencyValues.KZT)
                .build();

        CalculateResponse response = currencyStub.calculateRate(cr);
        Assertions.assertEquals(714.29, response.getCalculatedAmount());
    }

}
