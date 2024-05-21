package br.com.willian.integrationtests.controller.withxml;

import br.com.willian.cnfigs.TestConfigs;
import br.com.willian.integrationtests.dto.security.AccountCredentialsDTO;
import br.com.willian.integrationtests.dto.security.TokenDTO;
import br.com.willian.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerXmlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static TokenDTO tokenDTO;

    @Test
    @Order(1)
    public void testSignin() throws IOException {
        AccountCredentialsDTO user = new AccountCredentialsDTO("leandro", "admin123");

        tokenDTO = given()
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .body(user).when().post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class);

        assertNotNull(tokenDTO.getAccessToken(), ()-> "Access Token should not null!");
        assertNotNull(tokenDTO.getRefreshToken(), ()-> "Refresh Token should not null!");
    }

    @Test
    @Order(2)
    public void testRefresh() throws IOException {
        AccountCredentialsDTO user = new AccountCredentialsDTO("leandro", "admin123");

        TokenDTO newtokenDTO = given()
                .basePath("/auth/refresh")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .pathParams("username", tokenDTO.getUserName())
                .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getRefreshToken()).when().put("{username}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class);

        assertNotNull(newtokenDTO.getAccessToken(), ()-> "Access Token should not null!");
        assertNotNull(newtokenDTO.getRefreshToken(), ()-> "Refresh Token should not null!");
    }

}
