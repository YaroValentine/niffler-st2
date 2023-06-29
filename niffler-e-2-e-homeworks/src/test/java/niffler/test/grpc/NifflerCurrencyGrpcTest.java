package niffler.test.grpc;

import guru.qa.grpc.niffler.grpc.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static guru.qa.grpc.niffler.grpc.CurrencyValues.*;
import static org.junit.jupiter.params.provider.Arguments.*;

public class NifflerCurrencyGrpcTest extends BaseGrpcTest {

    @Test
    void getAllCurrenciesTest() {
        CurrencyResponse allCurrencies = currencyStub.getAllCurrencies(EMPTY);
        List<Currency> currenciesList = allCurrencies.getAllCurrenciesList();

        Assertions.assertEquals(RUB, currenciesList.get(0).getCurrency());
        Assertions.assertEquals(KZT, currenciesList.get(1).getCurrency());
        Assertions.assertEquals(EUR, currenciesList.get(2).getCurrency());
        Assertions.assertEquals(USD, currenciesList.get(3).getCurrency());
        Assertions.assertEquals(4, allCurrencies.getAllCurrenciesList().size());
    }

    @ParameterizedTest
    @MethodSource("ratesData")
    void calculateRateTest(double amount, CurrencyValues spendCurrency, CurrencyValues desiredCurrency, double expectedAmount) {
        final CalculateRequest cr = CalculateRequest.newBuilder()
                .setAmount(amount)
                .setSpendCurrency(spendCurrency)
                .setDesiredCurrency(desiredCurrency)
                .build();

        CalculateResponse response = currencyStub.calculateRate(cr);
        Assertions.assertEquals(expectedAmount, response.getCalculatedAmount());
    }



    static Stream<Arguments> ratesData() {
        return Stream.of(
                of(100.0, RUB, RUB, 100.0),
                of(100.0, RUB, KZT, 714.29),
                of(100.0, RUB, USD, 1.5),
                of(100.0, RUB, EUR, 1.39),
                of(-50.0, RUB, EUR, -0.69),

                of(100.0, KZT, RUB, 14.0),
                of(100.0, KZT, KZT, 100.0),
                of(100.0, KZT, USD, 0.21),
                of(100.0, KZT, EUR, 0.19),
                of(-50.0, KZT, EUR, -0.1),

                of(100.0, EUR, RUB, 7200.0),
                of(100.0, EUR, KZT, 51428.57),
                of(100.0, EUR, USD, 108.0),
                of(100.0, EUR, EUR, 100.0),
                of(-50.0, EUR, EUR, -50.0),

                of(100.0, USD, RUB, 6666.67),
                of(100.0, USD, KZT, 47619.05),
                of(100.0, USD, USD, 100.0),
                of(100.0, USD, EUR, 92.59),
                of(-50.0, USD, EUR, -46.3)
        );
    }

}
