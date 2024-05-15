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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.willian.exceptions.ResourceNotFoundException;
import br.com.willian.model.Person;
import br.com.willian.services.PersonServices;

@WebMvcTest
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
	
	@DisplayName("Test Given Person Object When Create Person Shoud Return Saved Person")
	@Test
	void testGivenPersonObject_WhenCreatePerson_ShoudReturnSavedPerson() throws JsonProcessingException, Exception {
		//Given / Arrange
		when(services.createPerson(any(PersonDTO.class))).thenAnswer((invocation) -> invocation.getArgument(0));
		
		//When / Act
		ResultActions response =  mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(person0)));
		
		//Then /Assert
		response.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName", is(person0.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(person0.getLastName())))
				.andExpect(jsonPath("$.email", is(person0.getEmail())));
	}
	
	@DisplayName("Test Given Person List Of Persons When Find All Shoud Return Persons List")
	@Test
	void testGivenListOfPersons_WhenFindAll_ShoudReturnPersonsList() throws JsonProcessingException, Exception {
		//Given / Arrange
		List<Person> persons = new ArrayList<>();
		persons.add(person0);
		persons.add(new Person(2L, "Leonardo", "Costa", "Feira de Santana - BA", "Male", "leonardo@gmail.com"));
		
		when(services.findAll()).thenReturn(persons);
		
		//When / Act
		ResultActions response =  mockMvc.perform(get("/person"));
		
		//Then /Assert
		response.andExpect(status().isOk())
		.andDo(print())
				.andExpect(jsonPath("$.size()", is(persons.size())));
	}
	
	@DisplayName("Test Given Person Id When Find By Id Shoud Return Person Object")
	@Test
	void testGivenPersonId_WhenFindById_ShoudReturnPersonObject() throws JsonProcessingException, Exception {
		//Given / Arrange
		long personId = 1L;
		when(services.findById(personId)).thenReturn(person0);
		
		//When / Act
		ResultActions response =  mockMvc.perform(get("/person/{id}", personId));
		
		//Then /Assert
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.firstName", is(person0.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(person0.getLastName())))
				.andExpect(jsonPath("$.email", is(person0.getEmail())));
	}
	
	@DisplayName("Test Given Invalid Person Id When Find By Id Shoud Return Not Found")
	@Test
	void testGivenInvalidPersonId_WhenFindById_ShoudReturnNotFound() throws JsonProcessingException, Exception {
		//Given / Arrange
		long personId = 1L;
		when(services.findById(personId)).thenThrow(ResourceNotFoundException.class);
		
		//When / Act
		ResultActions response =  mockMvc.perform(get("/person/{id}", personId));
		
		//Then /Assert
		response.andExpect(status().isNotFound())
				.andDo(print());
	}
	
	@DisplayName("Test Given Update Person When Update Shoud Return Updated Person Object")
	@Test
	void testGivenUpdatePerson_WhenUpdate_ShoudReturnUpdatedPersonObject() throws JsonProcessingException, Exception {
		//Given / Arrange
		long personId = 1L;
		when(services.findById(personId)).thenReturn(person0);
		when(services.updatePerson(any(PersonDTO.class))).thenAnswer((invocation) -> invocation.getArgument(0));
		
		Person updatedPerson = new Person(1L, "Leonardo", "Costa", "Feira de Santana - BA", "Male", "leonardo@gmail.com");
		
		//When / Act
		ResultActions response =  mockMvc.perform(put("/person")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedPerson)));
		
		//Then /Assert
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.firstName", is(updatedPerson.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(updatedPerson.getLastName())))
				.andExpect(jsonPath("$.email", is(updatedPerson.getEmail())));
	}
	
	@DisplayName("Test Given Unexistent Person When Update Shoud Return Not Found")
	@Test
	void testGivenUnexistentPerson_WhenUpdate_ShoudReturnNotFound() throws JsonProcessingException, Exception {
		
		//Given / Arrange
		long personId = 1L;
		when(services.findById(personId)).thenThrow(ResourceNotFoundException.class);
		
		when(services.updatePerson(any(PersonDTO.class))).thenAnswer((invocation) -> invocation.getArgument(1));
		
		Person updatedPerson = new Person(1L, "Leonardo", "Costa", "Feira de Santana - BA", "Male", "leonardo@gmail.com");
		
		//When / Act
		ResultActions response =  mockMvc.perform(put("/person")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedPerson)));
		
		//Then /Assert
		response.andExpect(status().isNotFound())
			.andDo(print());
	}
	
	@DisplayName("Test Given Person Id When Delete Shoud Return Null Content")
	@Test
	void testGivenPersonID_WhenDelete_ShoudReturnNullContent() throws JsonProcessingException, Exception {
		
		//Given / Arrange
		long personId = 1L;
		willDoNothing().given(services).deletePerson(personId);
		
		//When / Act
		ResultActions response =  mockMvc.perform(delete("/person/{id}", personId));
		
		//Then /Assert
		response.andExpect(status().isNoContent())
			.andDo(print());
	}
	
}
