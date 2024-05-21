package br.com.willian.integrationtests.controller.withjson;

import br.com.willian.cnfigs.TestConfigs;
import br.com.willian.integrationtests.dto.BooksDTO;
import br.com.willian.integrationtests.dto.security.AccountCredentialsDTO;
import br.com.willian.integrationtests.dto.security.TokenDTO;
import br.com.willian.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
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

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerJsonTest extends AbstractIntegrationTest {

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
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SITE)
					.body(booksDTO).when().post()
				.then()
					.statusCode(201)
				.extract()
					.body()
						.asString();

		BooksDTO createdBook = objectMapper.readValue(content, BooksDTO.class);
		booksDTO = createdBook;
		assertNotNull(createdBook, () -> "Created book Should not null");

		assertNotNull(createdBook.getKey(), () -> "Created book Id Should not null");
		assertNotNull(createdBook.getAuthor(), () -> "Created book author Should not null");
		assertNotNull(createdBook.getTitle(), () -> "Created book author Should not null");

		assertTrue(createdBook.getKey() > 0, () ->  "The book Id should be bigger then 0");

		assertEquals("Adventure Time", createdBook.getTitle(), () -> "Created book title and book title Should be the same!");
		assertEquals("Willian Costa", createdBook.getAuthor(), () -> "Created book author and book author Should  be the same!");
		assertEquals(79.90, createdBook.getPrice(), () -> "Created book price and book price Should  be the same!");
	}

	@Test
	@Order(2)
	public void testCreateWithWrongOrigin() throws IOException {

		mockBook();

		String content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_FAIL)
				.body(booksDTO).when().post()
				.then()
				.statusCode(403)
				.extract()
				.body()
				.asString();


		assertNotNull(content, () -> "Should return error 403");

		assertEquals("Invalid CORS request", content, () -> "Should contains Invalid CORS request!");
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
		assertNotNull(createdBook, () -> "Created book Should not null");

		assertNotNull(createdBook.getKey(), () -> "Created book Id Should not null");
		assertNotNull(createdBook.getAuthor(), () -> "Created book author Should not null");
		assertNotNull(createdBook.getTitle(), () -> "Created book author Should not null");

		assertTrue(createdBook.getKey() > 0, () ->  "The book Id should be bigger then 0");

		assertEquals("Adventure Time", createdBook.getTitle(), () -> "Created book title and book title Should be the same!");
		assertEquals("Willian Costa", createdBook.getAuthor(), () -> "Created book author and book author Should  be the same!");
		assertEquals(79.90, createdBook.getPrice(), () -> "Created book price and book price Should  be the same!");
	}

	@Test
	@Order(4)
	public void testFindByIdWithWrongOrigin() throws IOException {

		mockBook();

		String content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_FAIL)
				.pathParams("id", booksDTO.getKey()).when().get("{id}")
				.then()
				.statusCode(403)
				.extract()
				.body()
				.asString();

		assertNotNull(content, () -> "Should return error 403");

		assertEquals("Invalid CORS request", content, () -> "Should contains Invalid CORS request!");
	}


	private void mockBook() {
		booksDTO.setAuthor("Willian Costa");
		booksDTO.setLaunchDate(new Date());
		booksDTO.setPrice(79.90);
		booksDTO.setTitle("Adventure Time");
	}

}
