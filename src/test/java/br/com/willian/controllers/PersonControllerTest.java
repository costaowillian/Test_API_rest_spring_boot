package br.com.willian.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import br.com.willian.dtos.PersonDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.willian.exceptions.ResourceNotFoundException;
import br.com.willian.model.Person;
import br.com.willian.services.PersonServices;

@WebMvcTest(PersonController.class)
class PersonControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private PersonServices services;
	
	private Person person0;
	
	@BeforeEach
	void setup(){
		//Given / Arrange
		person0 = new Person(1L, "Willian", "Costa", "Feira de Santana - BA", "Male", "willian@gmail.com");
	}
	
	@DisplayName("Test Given Person Object When Create Person Should Return Saved Person")
	@Test
	void testGivenPersonObject_WhenCreatePerson_ShouldReturnSavedPerson() throws JsonProcessingException, Exception {
		//Given / Arrange
		when(services.createPerson(any(PersonDTO.class))).thenAnswer((invocation) -> invocation.getArgument(0));
		
		//When / Act
		ResultActions response =  mockMvc.perform(post("/api/v1/person").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new PersonDTO(person0))));
		
		//Then /Assert
		response.andDo(print()).andExpect(status().isCreated())
				.andExpect(jsonPath("$.first_name", is(person0.getFirstName())))
				.andExpect(jsonPath("$.last_name", is(person0.getLastName())))
				.andExpect(jsonPath("$.email", is(person0.getEmail())));
	}
	
	@DisplayName("Test Given Person List Of Persons When Find All Should Return Persons List")
	@Test
	void testGivenListOfPersons_WhenFindAll_ShouldReturnPersonsList() throws JsonProcessingException, Exception {
		//Given / Arrange
		List<PersonDTO> persons = new ArrayList<>();
		persons.add(new PersonDTO(person0));
		Person person2 = new Person(2L, "Leonardo", "Costa", "Feira de Santana - BA", "Male", "leonardo@gmail.com");
		persons.add(new PersonDTO(person2));
		
		when(services.findAll()).thenReturn(persons);
		
		//When / Act
		ResultActions response =  mockMvc.perform(get("/api/v1/person"));
		
		//Then /Assert
		response.andExpect(status().isOk())
		.andDo(print())
				.andExpect(jsonPath("$.size()", is(persons.size())));
	}
	
	@DisplayName("Test Given Person Id When Find By Id Should Return Person Object")
	@Test
	void testGivenPersonId_WhenFindById_ShouldReturnPersonObject() throws JsonProcessingException, Exception {
		//Given / Arrange
		long personId = 1L;
		when(services.findById(personId)).thenReturn(new PersonDTO(person0));
		
		//When / Act
		ResultActions response =  mockMvc.perform(get("/api/v1/person/{id}", personId));
		
		//Then /Assert
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.first_name", is(person0.getFirstName())))
				.andExpect(jsonPath("$.last_name", is(person0.getLastName())))
				.andExpect(jsonPath("$.email", is(person0.getEmail())));
	}
	
	@DisplayName("Test Given Invalid Person Id When Find By Id Should Return Not Found")
	@Test
	void testGivenInvalidPersonId_WhenFindById_ShouldWSSdReturnNotFound() throws JsonProcessingException, Exception {
		//Given / Arrange
		long personId = 1L;
		when(services.findById(personId)).thenThrow(ResourceNotFoundException.class);
		
		//When / Act
		ResultActions response =  mockMvc.perform(get("/api/v1/person/{id}", personId));
		
		//Then /Assert
		response.andExpect(status().isNotFound())
				.andDo(print());
	}
	
	@DisplayName("Test Given Update Person When Update Should Return Updated Person Object")
	@Test
	void testGivenUpdatePerson_WhenUpdate_ShouldReturnUpdatedPersonObject() throws JsonProcessingException, Exception {
		//Given / Arrange
		long personId = 1L;
		when(services.findById(personId)).thenReturn(new PersonDTO(person0));
		when(services.updatePerson(any(PersonDTO.class))).thenAnswer((invocation) -> invocation.getArgument(0));
		
		PersonDTO updatedPerson = new PersonDTO(person0);
		
		//When / Act
		ResultActions response =  mockMvc.perform(put("/api/v1/person")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedPerson)));
		
		//Then /Assert
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.first_name", is(updatedPerson.getFirstName())))
				.andExpect(jsonPath("$.last_name", is(updatedPerson.getLastName())))
				.andExpect(jsonPath("$.email", is(updatedPerson.getEmail())));
	}
	
	@DisplayName("Test Given Nonexistent Person When Update Should Return Not Found")
	@Test
	void testGivenNonexistentPerson_WhenUpdate_ShouldReturnNotFound() throws JsonProcessingException, Exception {
		
		//Given / Arrange
		long personId = 1L;
		when(services.findById(personId)).thenThrow(ResourceNotFoundException.class);
		
		when(services.updatePerson(any(PersonDTO.class))).thenAnswer((invocation) -> invocation.getArgument(1));
		
		Person updatedPerson = new Person(1L, "Leonardo", "Costa", "Feira de Santana - BA", "Male", "leonardo@gmail.com");
		
		//When / Act
		ResultActions response =  mockMvc.perform(put("/api/v1/person")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedPerson)));
		
		//Then /Assert

		response.andExpect(status().isNotFound())
			.andDo(print());
	}
	
	@DisplayName("Test Given Person Id When Delete Should Return Null Content")
	@Test
	void testGivenPersonID_WhenDelete_ShouldReturnNullContent() throws JsonProcessingException, Exception {
		
		//Given / Arrange
		long personId = 1L;
		willDoNothing().given(services).deletePerson(personId);
		
		//When / Act
		ResultActions response =  mockMvc.perform(delete("/api/v1/person/{id}", personId));

		//Then /Assert
		response.andExpect(status().isNoContent())
			.andDo(print());
	}
	
}
