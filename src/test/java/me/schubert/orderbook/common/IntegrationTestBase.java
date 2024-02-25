package me.schubert.orderbook.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class IntegrationTestBase {

    @Autowired
    ServletWebServerApplicationContext servletWebServerApplicationContext;

    @BeforeAll
    public static void beforeAllBase() {
        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory(
                (cls, charset) -> {
                    ObjectMapper objectMapper = new JsonMapper().registerModule(new JavaTimeModule());
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    return objectMapper;
                }
        ));
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    protected RestAssuredClient client(TestIds ids) {
        return new RestAssuredClient(ids.userId(), servletWebServerApplicationContext);
    }

    protected static TestIds generateRandomIds() {
        return new TestIds(
                randomId(),
                randomId(),
                randomId()
        );
    }

    public static String randomId() {
        return UUID.randomUUID().toString();
    }
}
