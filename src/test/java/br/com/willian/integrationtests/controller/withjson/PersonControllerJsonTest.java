package br.com.willian.integrationtests.controller.withjson;

import br.com.willian.cnfigs.TestConfigs;

import br.com.willian.dtos.PersonDTO;
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

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;

	private static PersonDTO personDTO;

	@BeforeAll
	public static void setup() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		personDTO = new PersonDTO();
	}

	@Test
	@Order(1)
	public void testCreate() throws IOException {

		mockPerson();

		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, "http://localhost:3000")
				.setBasePath("/api/v1/person")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

		String content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.body(personDTO).when().post()
				.then()
					.statusCode(201)
				.extract()
					.body()
						.asString();

		PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
		personDTO = createdPerson;
		assertNotNull(createdPerson, () -> "Created Person Should not null");

		assertNotNull(createdPerson.getKey(), () -> "Created Person Id Should not null");
		assertNotNull(createdPerson.getEmail(), () -> "Created Person email Should not null");
		assertNotNull(createdPerson.getFirstName(), () -> "Created Person first name Should not null");

		assertTrue(createdPerson.getKey() > 0, () ->  "The Person Id should be bigger then 0");

		assertEquals("Stallman", createdPerson.getLastName(), () -> "Created Person last name and Person last name Should be the same!");
		assertEquals("richard@gmail.com", createdPerson.getEmail(), () -> "Created Person email and Person Email Should  be the same!");
		assertEquals("Richard", createdPerson.getFirstName(), () -> "Created Person first name and Person first name Should  be the same!");
	}

	private void mockPerson() {
		personDTO.setFirstName("Richard");
		personDTO.setLastName("Stallman");
		personDTO.setAddress("New York City - USA");
		personDTO.setGender("Male");
		personDTO.setEmail("richard@gmail.com");
	}

}
