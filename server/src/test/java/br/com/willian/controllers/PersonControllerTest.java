package br.com.willian.controllers;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.willian.cnfigs.TestConfigs;
import br.com.willian.integrationtests.dto.PersonDTO;
import br.com.willian.integrationtests.dto.security.AccountCredentialsDTO;
import br.com.willian.integrationtests.dto.security.TokenDTO;
import br.com.willian.integrationtests.wrappers.WrapperPersonDTO;
import com.fasterxml.jackson.databind.DeserializationFeature;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

import java.io.IOException;
import java.util.List;

import br.com.willian.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerTest extends AbstractIntegrationTest {

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
				.setBasePath("/api/v1/person")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}

	@Test
	@Order(1)
	public void testCreate() throws IOException {

		mockPerson();

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
		assertTrue(createdPerson.isEnabled(), () -> "Person enable Should be true");

		assertTrue(createdPerson.getKey() > 0, () ->  "The Person Id should be bigger then 0");

		assertEquals("Stallman", createdPerson.getLastName(), () -> "Created Person last name and Person last name Should be the same!");
		assertEquals("richard@gmail.com", createdPerson.getEmail(), () -> "Created Person email and Person Email Should  be the same!");
		assertEquals("Richard", createdPerson.getFirstName(), () -> "Created Person first name and Person first name Should  be the same!");
	}

	@Test
	@Order(2)
	public void testUpdate() throws IOException {

		personDTO.setLastName("Jackson");

		String content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(personDTO).when().put()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
		personDTO = createdPerson;
		assertNotNull(createdPerson, () -> "Update Person Should not null");

		assertEquals(personDTO.getKey(), createdPerson.getKey(), () ->  "The Person Id should the same from created Person");

		assertNotNull(createdPerson.getKey(), () -> "Update Person Id Should not null");
		assertNotNull(createdPerson.getEmail(), () -> "Update Person email Should not null");
		assertNotNull(createdPerson.getFirstName(), () -> "Update Person first name Should not null");
		assertTrue(createdPerson.isEnabled(), () -> "Person enable Should be true");

		assertEquals("Jackson", createdPerson.getLastName(), () -> "Update Person last name and Person last name Should be the same!");
		assertEquals("richard@gmail.com", createdPerson.getEmail(), () -> "Update Person email and Person Email Should  be the same!");
		assertEquals("Richard", createdPerson.getFirstName(), () -> "Update Person first name and Person first name Should  be the same!");
	}

	@Test
	@Order(3)
	public void testFindById() throws IOException {

		mockPerson();

		String content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SITE)
				.pathParams("id", personDTO.getKey()).when().get("{id}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
		personDTO = createdPerson;

		assertNotNull(createdPerson, () -> "Person Should not null");

		assertNotNull(createdPerson.getKey(), () -> "Person Id Should not null");
		assertNotNull(createdPerson.getEmail(), () -> "Person email Should not null");
		assertNotNull(createdPerson.getFirstName(), () -> "Person first name Should not null");
		assertTrue(createdPerson.isEnabled(), () -> "Person enable Should be true");

		assertTrue(createdPerson.getKey() > 0, () ->  "The Person Id should be bigger then 0");

		assertEquals("Jackson", createdPerson.getLastName(), () -> "Person last name and Person last name Should be the same!");
		assertEquals("richard@gmail.com", createdPerson.getEmail(), () -> "Person email and Person Email Should  be the same!");
		assertEquals("Richard", createdPerson.getFirstName(), () -> "Person first name and Person first name Should  be the same!");
	}

	@Test
	@Order(4)
	public void testDisablePersonById() throws IOException {

		String content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SITE)
				.pathParams("id", personDTO.getKey()).when().patch("{id}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
		personDTO = createdPerson;

		assertNotNull(createdPerson, () -> "Person Should not null");

		assertNotNull(createdPerson.getKey(), () -> "Person Id Should not null");
		assertNotNull(createdPerson.getEmail(), () -> "Person email Should not null");
		assertNotNull(createdPerson.getFirstName(), () -> "Person first name Should not null");
		assertFalse(createdPerson.isEnabled(), () -> "Person enable Should be false");

		assertTrue(createdPerson.getKey() > 0, () ->  "The Person Id should be bigger then 0");

		assertEquals("Jackson", createdPerson.getLastName(), () -> "Person last name and Person last name Should be the same!");
		assertEquals("richard@gmail.com", createdPerson.getEmail(), () -> "Person email and Person Email Should  be the same!");
		assertEquals("Richard", createdPerson.getFirstName(), () -> "Person first name and Person first name Should  be the same!");
	}

	@Test
	@Order(5)
	public void testDelete() throws IOException {

		given().spec(specification)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SITE)
				.pathParams("id", personDTO.getKey()).when().delete("{id}")
				.then()
				.statusCode(204);
	}

	@Test
	@Order(6)
	public void testFindAll() throws IOException {

		String content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.queryParams("page",1, "size", 10, "direction", "asc")
				.body(personDTO).when().get()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		WrapperPersonDTO wrapper = objectMapper.readValue(content, WrapperPersonDTO.class);

		var people = wrapper.getEmbeddedDTO().getPersons();

		PersonDTO findFirstPerson = people.getFirst();
		assertEquals(154, findFirstPerson.getKey(), ()-> "Id should be 154");
		assertNotNull(findFirstPerson.getKey(), () -> "Person Id Should not null");
		assertNotNull(findFirstPerson.getEmail(), () -> "Person email Should not null");
		assertNotNull(findFirstPerson.getFirstName(), () -> "Person first name Should not null");
		assertFalse(findFirstPerson.isEnabled(), () -> "Person enable Should be false");


		assertEquals("Hann", findFirstPerson.getLastName(), () -> " find First Person last name and Person last name Should be the same!");
		assertEquals("ahann3z@ihg.com", findFirstPerson.getEmail(), () -> " find First Person email and Person Email Should  be the same!");
		assertEquals("Alasdair", findFirstPerson.getFirstName(), () -> " find First Person first name and Person first name Should  be the same!");

		PersonDTO findPerson = people.get(1);

		assertEquals(283, findPerson.getKey(), ()-> "Id should be 283");
		assertNotNull(findPerson.getKey(), () -> "Person Id Should not null");
		assertNotNull(findPerson.getEmail(), () -> "Person email Should not null");
		assertNotNull(findPerson.getFirstName(), () -> "Person first name Should not null");
		assertTrue(findPerson.isEnabled(), () -> "Person enable Should be true");

		assertEquals("Bloxholm", findPerson.getLastName(), () -> " find First Person last name and Person last name Should be the same!");
		assertEquals("abloxholm7k@networksolutions.com", findPerson.getEmail(), () -> " find First Person email and Person Email Should  be the same!");
		assertEquals("Aldric", findPerson.getFirstName(), () -> " find First Person first name and Person first name Should  be the same!");
	}

	@Test
	@Order(7)
	public void testFindByName() throws IOException {

		String content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.pathParam("firstName", "ayr")
				.queryParams("page",0, "size", 6, "direction", "asc")
				.body(personDTO).when().get("/findPerson/{firstName}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		WrapperPersonDTO wrapper = objectMapper.readValue(content, WrapperPersonDTO.class);

		var people = wrapper.getEmbeddedDTO().getPersons();

		PersonDTO findFirstPerson = people.getFirst();
		assertEquals(1, findFirstPerson.getKey(), ()-> "Id should be 1");
		assertNotNull(findFirstPerson.getKey(), () -> "Person Id Should not null");
		assertNotNull(findFirstPerson.getEmail(), () -> "Person email Should not null");
		assertNotNull(findFirstPerson.getFirstName(), () -> "Person first name Should not null");
		assertTrue(findFirstPerson.isEnabled(), () -> "Person enable Should be true");


		assertEquals("Senna", findFirstPerson.getLastName(), () -> " find First Person last name and Person last name Should be the same!");
		assertEquals("ayrton@gmail.com", findFirstPerson.getEmail(), () -> " find First Person email and Person Email Should  be the same!");
		assertEquals("Ayrton", findFirstPerson.getFirstName(), () -> " find First Person first name and Person first name Should  be the same!");
	}

	/*
	@Test
	@Order(8)
	public void testHATEOAS() throws IOException {

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.accept(TestConfigs.CONTENT_TYPE_JSON)
				.queryParams("page", 3, "size", 10, "direction", "asc")
				.when()
				.get()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/677\"}}}"));
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/846\"}}}"));
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/714\"}}}"));

		assertTrue(content.contains("{\"first\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=0&size=10&sort=firstName,asc\"}"));
		assertTrue(content.contains("\"prev\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=2&size=10&sort=firstName,asc\"}"));
		assertTrue(content.contains("\"self\":{\"href\":\"http://localhost:8888/api/person/v1?page=3&size=10&direction=asc\"}"));
		assertTrue(content.contains("\"next\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=4&size=10&sort=firstName,asc\"}"));
		assertTrue(content.contains("\"last\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=100&size=10&sort=firstName,asc\"}}"));

		assertTrue(content.contains("\"page\":{\"size\":10,\"totalElements\":1007,\"totalPages\":101,\"number\":3}}"));
	} */

	private void mockPerson() {
		personDTO.setFirstName("Richard");
		personDTO.setLastName("Stallman");
		personDTO.setAddress("New York City - USA");
		personDTO.setGender("Male");
		personDTO.setEmail("richard@gmail.com");
		personDTO.setEnabled(true);
	}
}
