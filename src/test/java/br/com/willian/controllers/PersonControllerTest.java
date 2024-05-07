package br.com.willian.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
		when(services.createPerson(any(Person.class))).thenAnswer((invocation) -> invocation.getArgument(0));
		
		//When / Act
		ResultActions response =  mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(person0)));
		
		//Then /Assert
		response.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName", is(person0.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(person0.getLastName())))
				.andExpect(jsonPath("$.email", is(person0.getEmail())));
	}
	

}
