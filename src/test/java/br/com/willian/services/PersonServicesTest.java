package br.com.willian.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
