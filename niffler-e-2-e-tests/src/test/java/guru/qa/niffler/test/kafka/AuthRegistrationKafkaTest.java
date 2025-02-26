package guru.qa.niffler.test.kafka;

import guru.qa.niffler.api.AuthRestClient;
import guru.qa.niffler.kafka.KafkaConsumerService;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.utils.DataUtils;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthRegistrationKafkaTest extends BaseKafkaTest {

    private final AuthRestClient authClient = new AuthRestClient();

    @Test
    @AllureId("600001")
    @DisplayName("KAFKA: Сообщение с пользователем публикуется в Kafka после успешной регистрации")
    @Tag("KAFKA")
    void messageShouldBeProducedToKafkaAfterSuccessfulRegistration() throws Exception {
        final String username = DataUtils.generateRandomUsername();
        final String password = DataUtils.generateRandomPassword();

        authClient.register(username, password);

        final UserJson messageFromKafka = KafkaConsumerService.getMessage(username);

        step("Check that message from kafka exist", () ->
                assertNotNull(messageFromKafka)
        );

        step("Check message content", () ->
                assertEquals(username, messageFromKafka.getUsername())
        );
    }
}
