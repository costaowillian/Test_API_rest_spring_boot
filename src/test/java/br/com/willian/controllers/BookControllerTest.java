package br.com.willian.controllers;


import br.com.willian.cnfigs.TestConfigs;
import br.com.willian.integrationtests.dto.BooksDTO;
import br.com.willian.integrationtests.dto.PersonDTO;
import br.com.willian.integrationtests.dto.security.AccountCredentialsDTO;
import br.com.willian.integrationtests.dto.security.TokenDTO;
import br.com.willian.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.willian.model.Book;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static BooksDTO booksDTO;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        booksDTO = new BooksDTO();
    }

    @Test
    @Order(0)
    public void authorization() throws IOException {
        AccountCredentialsDTO user = new AccountCredentialsDTO("leandro", "admin123");

        String accessToken = given()
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(user).when().post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class)
                .getAccessToken();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
                .setBasePath("/api/v1/books")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    public void testCreate() throws IOException {

        mockBook();

        String content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(booksDTO).when().post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        BooksDTO createdBook = objectMapper.readValue(content, BooksDTO.class);
        booksDTO = createdBook;
        assertNotNull(createdBook, () -> "created Book Should not null");

        assertNotNull(createdBook.getKey(), () -> "created Book Id Should not null");
        assertNotNull(createdBook.getPrice(), () -> "created Book price Should not null");
        assertNotNull(createdBook.getAuthor(), () -> "created Book author Should not null");

        assertTrue(createdBook.getKey() > 0, () ->  "The book Id should be bigger then 0");

        assertEquals("Willian Costa", createdBook.getAuthor(), () -> "created Book author and book author Should be the same!");
        assertEquals(79.90, createdBook.getPrice(), () -> "created Book email and Person Email Should  be the same!");
        assertEquals("Adventure Time", createdBook.getTitle(), () -> "created Book title and book title Should  be the same!");
    }

    @Test
    @Order(2)
    public void testUpdate() throws IOException {

        booksDTO.setAuthor("Jackson Five");

        String content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(booksDTO).when().put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        BooksDTO createdBook = objectMapper.readValue(content, BooksDTO.class);
        booksDTO = createdBook;
        assertNotNull(createdBook, () -> "Update Person Should not null");

        assertEquals(booksDTO.getKey(), createdBook.getKey(), () ->  "The Person Id should the same from created Person");

        assertNotNull(createdBook, () -> "created Book Should not null");

        assertNotNull(createdBook.getKey(), () -> "created Book Id Should not null");
        assertNotNull(createdBook.getPrice(), () -> "created Book price Should not null");
        assertNotNull(createdBook.getAuthor(), () -> "created Book author Should not null");

        assertTrue(createdBook.getKey() > 0, () ->  "The book Id should be bigger then 0");

        assertEquals("Jackson Five", createdBook.getAuthor(), () -> "created Book author and book author Should be the same!");
        assertEquals(79.90, createdBook.getPrice(), () -> "created Book email and Person Email Should  be the same!");
        assertEquals("Adventure Time", createdBook.getTitle(), () -> "created Book title and book title Should  be the same!");
    }

    @Test
    @Order(3)
    public void testFindById() throws IOException {

        mockBook();

        String content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SITE)
                .pathParams("id", booksDTO.getKey()).when().get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        BooksDTO createdBook = objectMapper.readValue(content, BooksDTO.class);
        booksDTO = createdBook;

        assertNotNull(createdBook, () -> "created Book Should not null");

        assertNotNull(createdBook.getKey(), () -> "created Book Id Should not null");
        assertNotNull(createdBook.getPrice(), () -> "created Book price Should not null");
        assertNotNull(createdBook.getAuthor(), () -> "created Book author Should not null");

        assertTrue(createdBook.getKey() > 0, () ->  "The book Id should be bigger then 0");

        assertEquals("Jackson Five", createdBook.getAuthor(), () -> "created Book author and book author Should be the same!");
        assertEquals(79.90, createdBook.getPrice(), () -> "created Book email and Person Email Should  be the same!");
        assertEquals("Adventure Time", createdBook.getTitle(), () -> "created Book title and book title Should  be the same!");
    }

    @Test
    @Order(4)
    public void testDelete() throws IOException {

        given().spec(specification)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SITE)
                .pathParams("id", booksDTO.getKey()).when().delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(5)
    public void testFindAll() throws IOException {

        List<BooksDTO> content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(booksDTO).when().get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(new TypeRef<List<BooksDTO>>() {});

        BooksDTO findFirstBook = content.getFirst();

        assertNotNull(findFirstBook.getKey(), () -> "Book Id Should not null");
        assertNotNull(findFirstBook.getAuthor(), () -> "Book Author Should not null");
        assertNotNull(findFirstBook.getPrice(), () -> "Book price Should not null");

        assertEquals(1, findFirstBook.getKey(), () ->  "The Person Id should be 1");

        assertEquals("Michael C. Feathers", findFirstBook.getAuthor(), () -> "created Book author and book author Should be the same!");
        assertEquals(49.00, findFirstBook.getPrice(), () -> "created Book email and Person Email Should  be the same!");
        assertEquals("Working effectively with legacy code", findFirstBook.getTitle(), () -> "created Book title and book title Should  be the same!");

        BooksDTO findBook = content.get(1);

        assertNotNull(findBook.getKey(), () -> "Book Id Should not null");
        assertNotNull(findBook.getAuthor(), () -> "Book Author Should not null");
        assertNotNull(findBook.getPrice(), () -> "Book price Should not null");

        assertEquals(2, findBook.getKey(), () ->  "The Person Id should be 1");

        assertEquals("Ralph Johnson, Erich Gamma, John Vlissides e Richard Helm", findBook.getAuthor(), () -> "created Book author and book author Should be the same!");
        assertEquals(45.00, findBook.getPrice(), () -> "created Book email and Person Email Should  be the same!");
        assertEquals("Design Patterns", findBook.getTitle(), () -> "created Book title and book title Should  be the same!");
    }

    private void mockBook() {
        booksDTO.setTitle("Adventure Time");
        booksDTO.setPrice(79.90);
        booksDTO.setLaunchDate(new Date());
        booksDTO.setAuthor("Willian Costa");
    }
}
