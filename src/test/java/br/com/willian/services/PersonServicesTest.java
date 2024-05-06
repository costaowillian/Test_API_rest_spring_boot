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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.willian.exceptions.DuplicateResourceException;
import br.com.willian.model.Person;
import br.com.willian.repositories.PersonRepository;


@ExtendWith(MockitoExtension.class)
public class PersonServicesTest {
	
	@Mock
	private PersonRepository repository;
	
	@InjectMocks
	private PersonServices services;
	
	private Person person0;

	@BeforeEach
	void setup(){
		//Given / Arrange
		person0 = new Person("Willian", "Costa", "Feira de Santana - BA", "Male", "willian@gmail.com");
	}
	
	@DisplayName("test Given Person Object When Save Person Shoud Return Person Object")
	@Test
	void testGivenPersonObject_WhenSavePerson_ShoudReturnPersonObject() {
		//Given / Arrange
		when(repository.findByEmail(anyString())).thenReturn(Optional.empty());	
		when(repository.save(person0)).thenReturn(person0);	
		
		//When / Act
		Person savedPerson = services.createPerson(person0);
		
		//Then /Assert
		assertNotNull(savedPerson, () -> "Should not return null");
		assertEquals("Willian", savedPerson.getFirstName(), () -> "Shoul return the same firstsNames");
	}
	
	@DisplayName("test Given ExistingEmail When Save Person Shoud Return Throw Exception")
	@Test
	void testGivenExistingEmail_WhenSavePerson_ShoudThrowException() {
		//Given / Arrange
		when(repository.findByEmail(anyString())).thenReturn(Optional.of(person0));		
		String expectedMessage = "Person already exist with given e-mail: " + person0.getEmail();
		
		//When / Act
		DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> {
			services.createPerson(person0);
		},
		() -> "Duplicate E-mail should have cause an DuplicateResourceException!"
		);	
		
		//Then /Assert
		verify(repository, never()).save(any(Person.class));
		assertEquals(expectedMessage, exception.getMessage(), "Exception message is incorrect");
	}
	
	@DisplayName("Test Given Person List When Find All Persons Shoud Return Persons List")
	@Test
	void testGivenPersonList_WhenFindAllPersons_ShoudReturnPersonsList() {
		//Given / Arrange
		Person person1 = new Person("Leonardo", "Silva", "Salvador - BA", "Male", "leonardo@gmail.com");
		
		when(repository.findAll()).thenReturn(List.of(person0, person1));		
		
		
		//When / Act
		List<Person> personsList = services.findAll();
		
		//Then /Assert
		assertNotNull(personsList, () -> "Should not return a empty list!");
		assertEquals(2, personsList.size(), () -> "Persons List should have 2 Persons object!");
	}
	
	@DisplayName("Test Given Empty Person List When Find All Persons Shoud Return Empty Persons List")
	@Test
	void testGivenEmptyPersonList_WhenFindAllPersons_ShoudReturnEmptyPersonsList() {
		//Given / Arrange	
		when(repository.findAll()).thenReturn(Collections.EMPTY_LIST);		
		
		
		//When / Act
		List<Person> personsList = services.findAll();
		
		//Then /Assert
		assertTrue(personsList.isEmpty(), () -> "Person List should have empty");
		assertEquals(0, personsList.size(), () -> "Persons List should have 0 Persons object!");
	}
	
	@DisplayName("test Given Person Id When Find By Id Shoud Return Person Object")
	@Test
	void testGivenPersonId_WhenFindById_ShoudReturnPersonObject() {
		//Given / Arrange
		when(repository.findById(anyLong())).thenReturn(Optional.of(person0));
		
		//When / Act
		Person savedPerson = services.findById(1L);
		
		//Then /Assert
		assertNotNull(savedPerson, () -> "Should not return null");
		assertEquals("Willian", savedPerson.getFirstName(), () -> "Shoul return the same firstsNames");
	}
	
	@DisplayName("test Given Person Object When Update Person Shoud Return Updated Person Object")
	@Test
	void testGivenPersonObject_WhenUpdatePerson_ShoudReturnUpdatedPersonObject() {
		//Given / Arrange
		person0.setId(1L);
		when(repository.findById(anyLong())).thenReturn(Optional.of(person0));
		
		person0.setEmail("leonardo@gmail.com");
		person0.setFirstName("leonardo");		
		
		when(repository.save(person0)).thenReturn(person0);
		
		//When / Act
		Person updatedPerson = services.updatePerson(person0);
		
		//Then /Assert
		assertNotNull(updatedPerson, () -> "Should not return null");
		assertEquals("leonardo", updatedPerson.getFirstName(), () -> "Shoul return the same firstsNames");
		assertEquals("leonardo@gmail.com", updatedPerson.getEmail(), () -> "Shoul return the same email");
	}
	
	@DisplayName("test Given Person Id When delete Person Shoud Do Nothing")
	@Test
	void testGivenPersonOId_WhendeletePerson_ShoudDoNothing() {
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