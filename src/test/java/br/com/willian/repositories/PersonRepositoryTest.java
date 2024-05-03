package br.com.willian.repositories;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.willian.model.Person;

@DataJpaTest
class PersonRepositoryTest {
	
	@Autowired
	private PersonRepository repository;
	
	@DisplayName("Given Person Object When Save Then Return Saved Person")
	@Test
	void testGivenPersonObject_WhenSave_ThenReturnSavedPerson() {
		//Given / Arrange
		Person person0 = new Person("Willian", "Costa", "Feira de Santana - BA", "Male", "willian@gmail.com");
		
		//When / Act
		Person savedPerson = repository.save(person0);
		
		//Then / Assert
		assertNotNull(savedPerson, () -> "Shoud not return null.");
		assertTrue(savedPerson.getId() > 0, () -> "Shoud not return Id less than 0.");
		assertEquals(person0.getFirstName(), savedPerson.getFirstName(), () -> "FirstName should be the sabe.");
		assertEquals(person0.getLastName(), savedPerson.getLastName(), () -> "LastName should be the sabe.");
		assertEquals(person0.getEmail(), savedPerson.getEmail(), () -> "Email should be the sabe.");
		assertEquals(person0.getAdress(), savedPerson.getAdress(), () -> "Adress should be the sabe.");
		assertEquals(person0.getGender(), savedPerson.getGender(), () -> "Gender should be the sabe.");
	}
}
