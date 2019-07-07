package com.assignment.moneytransfer;

import com.assignment.moneytransfer.api.TransferRequest;
import com.assignment.moneytransfer.api.TransferResponse;
import io.dropwizard.jersey.errors.ErrorMessage;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(DropwizardExtensionsSupport.class)
public class IntegrationTest {

    private static final String TMP_FILE = createTempFile();
    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("test-example.yml");

    public static final DropwizardAppExtension<MoneyTransferConfiguration> RULE = new DropwizardAppExtension<>(
            MoneyTransferApplication.class, CONFIG_PATH,
            ConfigOverride.config("database.url", "jdbc:h2:" + TMP_FILE));

    @BeforeAll
    public static void migrateDb() throws Exception {
        RULE.getApplication().run("db", "migrate", CONFIG_PATH);
    }

    private static String createTempFile() {
        try {
            return File.createTempFile("test-example", null).getAbsolutePath();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void testPostTransaction_whenSuccess() throws Exception {
        final TransferRequest request = new TransferRequest(1L, 2L, BigDecimal.valueOf(50.0));
        final TransferResponse newPerson = postPerson(request);
        assertThat(newPerson.getReferenceId()).isNotNull();
    }

    @Test
    public void testPostTransaction_whenAccountNotFound() throws Exception {
        final TransferRequest request = new TransferRequest(100L, 2L, BigDecimal.valueOf(50.0));
        final ErrorMessage newPerson = postPersonExpectException(request);
        assertThat(newPerson.getCode()).isEqualTo(500);
    }

    @Test
    public void testPostTransaction_whenAccountStatusClosed() throws Exception {
        final TransferRequest request = new TransferRequest(1L, 3L, BigDecimal.valueOf(50.0));
        final ErrorMessage newPerson = postPersonExpectException(request);
        assertThat(newPerson.getCode()).isEqualTo(500);
    }

    @Test
    public void testPostTransaction_whenAccountStatusDormant() throws Exception {
        final TransferRequest request = new TransferRequest(1L, 3L, BigDecimal.valueOf(50.0));
        final ErrorMessage newPerson = postPersonExpectException(request);
        assertThat(newPerson.getCode()).isEqualTo(500);
    }

    @Test
    public void testPostTransaction_whenBalanceInsufficient() throws Exception {
        final TransferRequest request = new TransferRequest(1L, 2L, BigDecimal.valueOf(10000.0));
        final ErrorMessage newPerson = postPersonExpectException(request);
        assertThat(newPerson.getCode()).isEqualTo(500);
    }

    private TransferResponse postPerson(TransferRequest person) {
        return RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/transfer")
                .request()
                .post(Entity.entity(person, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(TransferResponse.class);
    }

    private ErrorMessage postPersonExpectException(TransferRequest person) {
        return RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/transfer")
                .request()
                .post(Entity.entity(person, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(ErrorMessage.class);
    }

    @Test
    public void testLogFileWritten() throws IOException {
        // The log file is using a size and time based policy, which used to silently
        // fail (and not write to a log file). This test ensures not only that the
        // log file exists, but also contains the log line that jetty prints on startup
        final Path log = Paths.get("./logs/application.log");
        assertThat(log).exists();
        final String actual = new String(Files.readAllBytes(log), UTF_8);
        assertThat(actual).contains("0.0.0.0:" + RULE.getLocalPort());
    }
}
