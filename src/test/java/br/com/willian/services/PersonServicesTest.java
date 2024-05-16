package br.com.willian.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import br.com.willian.dtos.PersonDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.willian.exceptions.DuplicateResourceException;
import br.com.willian.model.Person;
import br.com.willian.repositories.PersonRepository;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonServicesTest {
	
	@Mock
	private PersonRepository repository;
	
	@InjectMocks
	private PersonServices services;
	
	private Person person0;

	@BeforeEach
	void setup(){
		//Given / Arrange
		person0 = new Person(1L,"Willian", "Costa", "Feira de Santana - BA", "Male", "willian@gmail.com");
	}
	
	@DisplayName("test Given Person Object When Save Person Should Return Person Object")
	@Test
	void testGivenPersonObject_WhenSavePerson_ShouldReturnPersonObject() throws Exception {
		//Given / Arrange
		when(repository.findByEmail(anyString())).thenReturn(Optional.empty());	
		when(repository.save(person0)).thenReturn(person0);
		
		//When / Act
		PersonDTO savedPerson = services.createPerson(new PersonDTO(person0));
		
		//Then /Assert
		assertNotNull(savedPerson, () -> "Should not return null");
		assertTrue(savedPerson.toString().contains("</api/v1/person/1>;rel=\"self\""));
		assertEquals("Willian", savedPerson.getFirstName(), () -> "Should return the same firstsNames");
	}
	
	@DisplayName("test Given ExistingEmail When Save Person Should Return Throw Exception")
	@Test
	void testGivenExistingEmail_WhenSavePerson_ShouldThrowException() {
		//Given / Arrange
		when(repository.findByEmail(anyString())).thenReturn(Optional.of(person0));		
		String expectedMessage = "Person already exist with given e-mail: " + person0.getEmail();
		
		//When / Act
		DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> {
			services.createPerson(new PersonDTO(person0));
		},
		() -> "Duplicate E-mail should have cause an DuplicateResourceException!"
		);	
		
		//Then /Assert
		verify(repository, never()).save(any(Person.class));
		assertEquals(expectedMessage, exception.getMessage(), "Exception message is incorrect");
	}
	
	@DisplayName("Test Given Person List When Find All Persons Should Return Persons List")
	@Test
	void testGivenPersonList_WhenFindAllPersons_ShouldReturnPersonsList() throws Exception {
		//Given / Arrange
		Person person1 = new Person(2L,"Leonardo", "Silva", "Salvador - BA", "Male", "leonardo@gmail.com");
		
		when(repository.findAll()).thenReturn(List.of(person0, person1));		
		
		
		//When / Act
		List<PersonDTO> personsList = services.findAll();

		PersonDTO personOne = personsList.get(1);

		//Then /Assert
		assertNotNull(personOne, () -> "Should not return null");
		assertNotNull(personOne.getKey(), () -> "ID Should not return null");
		assertNotNull(personOne.getLinks(), () -> "Links Should not return null");
		assertTrue(personOne.toString().contains("</api/v1/person/2>;rel=\"self\""), () -> "Links should Contains the string </api/v1/person/2>;rel=\"self\"");
		assertNotNull(personsList, () -> "Should not return a empty list!");
		assertEquals(2, personsList.size(), () -> "Persons List should have 2 Persons object!");
	}
	
	@DisplayName("Test Given Empty Person List When Find All Persons Should Return Empty Persons List")
	@Test
	void testGivenEmptyPersonList_WhenFindAllPersons_ShouldReturnEmptyPersonsList() throws Exception {
		//Given / Arrange	
		when(repository.findAll()).thenReturn(Collections.EMPTY_LIST);		
		
		
		//When / Act
		List<PersonDTO> personsList = services.findAll();
		
		//Then /Assert
		assertTrue(personsList.isEmpty(), () -> "Person List should have empty");
		assertEquals(0, personsList.size(), () -> "Persons List should have 0 Persons object!");
	}
	
	@DisplayName("test Given Person Id When Find By Id Should Return Person Object")
	@Test
	void testGivenPersonId_WhenFindById_ShouldReturnPersonObject() throws Exception {
		//Given / Arrange
		when(repository.findById(anyLong())).thenReturn(Optional.of(person0));
		
		//When / Act
		PersonDTO savedPerson = services.findById(1L);
		
		//Then /Assert
		assertNotNull(savedPerson, () -> "Should not return null");
		assertNotNull(savedPerson.getKey(), () -> "ID Should not return null");
		assertNotNull(savedPerson.getLinks(), () -> "Links Should not return null");
		assertTrue(savedPerson.toString().contains("</api/v1/person/1>;rel=\"self\""));
		assertEquals("Willian", savedPerson.getFirstName(), () -> "Should return the same firstsNames");
	}
	
	@DisplayName("test Given Person Object When Update Person Should Return Updated Person Object")
	@Test
	void testGivenPersonObject_WhenUpdatePerson_ShouldReturnUpdatedPersonObject() throws Exception {
		//Given / Arrange
		person0.setId(1L);
		when(repository.findById(anyLong())).thenReturn(Optional.of(person0));
		
		person0.setEmail("leonardo@gmail.com");
		person0.setFirstName("leonardo");		
		
		when(repository.save(person0)).thenReturn(person0);
		
		//When / Act
		PersonDTO updatedPerson = services.updatePerson(new PersonDTO(person0));
		
		//Then /Assert
		assertNotNull(updatedPerson, () -> "Should not return null");
		assertTrue(updatedPerson.toString().contains("</api/v1/person/1>;rel=\"self\""));
		assertEquals("leonardo", updatedPerson.getFirstName(), () -> "Should return the same firstsNames");
		assertEquals("leonardo@gmail.com", updatedPerson.getEmail(), () -> "Should return the same email");
	}
	
	@DisplayName("test Given Person Id When delete Person Should Do Nothing")
	@Test
	void testGivenPersonOId_WhenDeletePerson_ShouldDoNothing() {
		//Given / Arrange
		person0.setId(1L);
		when(repository.findById(anyLong())).thenReturn(Optional.of(person0));
		willDoNothing().given(repository).delete(person0);	
		
		//When / Act
		services.deletePerson(person0.getId());
		
		//Then /Assert
		verify(repository, times(1)).delete(person0);
	}
}
