package br.com.willian.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.willian.model.Person;

@DataJpaTest
class PersonRepositoryTest {
	
	@Autowired
	private PersonRepository repository;
	
	Person person0;
	
	@BeforeEach
	void setup(){
		person0 = new Person("Willian", "Costa", "Feira de Santana - BA", "Male", "willian@gmail.com");
		repository.save(person0);
	}
		
	@DisplayName("Given Person Object When Save Then Return Saved Person")
	@Test
	void testGivenPersonObject_WhenSave_ThenReturnSavedPerson() {
		//Given / Arrange
		
		//When / Act
		Person savedPerson = person0;
		
		//Then / Assert
		assertNotNull(savedPerson, () -> "Shoud not return null.");
		assertTrue(savedPerson.getId() > 0, () -> "Should not return Id less than 0.");
		assertEquals(person0.getFirstName(), savedPerson.getFirstName(), () -> "FirstName should be the same.");
		assertEquals(person0.getLastName(), savedPerson.getLastName(), () -> "LastName should be the same.");
		assertEquals(person0.getEmail(), savedPerson.getEmail(), () -> "Email should be the same.");
		assertEquals(person0.getAdress(), savedPerson.getAdress(), () -> "Adress should be the same.");
		assertEquals(person0.getGender(), savedPerson.getGender(), () -> "Gender should be the same.");
	}
	
	@DisplayName("Given Person List When Find All Then Return Person List")
	@Test
	void testGivenPersonList_WhenFindAll_ThenReturnPersonList() {
		//Given / Arrange
		Person person1 = new Person("Leonardo", "Silva", "Salvador - BA", "Male", "leonardo@gmail.com");
		
		repository.save(person1);
		
		//When / Act
		List<Person> personList = repository.findAll();
		
		//Then / Assert
		assertNotNull(personList, () -> "Shoud not return null.");
		assertEquals(2, personList.size(), () -> "Should contain 2 persons on list.");
	}
	
	@DisplayName("Given Person Object When Find By Id Then Return Person Object")
	@Test
	void testGivenPersonObject_WhenFindById_ThenReturnPersonObject() {
		//Given / Arrange
		
		//When / Act
		Person savedPerson = repository.findById(person0.getId()).get();
		
		//Then / Assert
		assertNotNull(savedPerson, () -> "Shoud not return null.");
		assertTrue(savedPerson.getId() > 0, () -> "Should not return Id less than 0.");
		assertEquals(person0.getId(), savedPerson.getId(), () -> "Ids should be the same.");
		assertEquals(person0.getFirstName(), savedPerson.getFirstName(), () -> "FirstName should be the same.");
		assertEquals(person0.getLastName(), savedPerson.getLastName(), () -> "LastName should be the same.");
		assertEquals(person0.getEmail(), savedPerson.getEmail(), () -> "Email should be the same.");
		assertEquals(person0.getAdress(), savedPerson.getAdress(), () -> "Adress should be the same.");
		assertEquals(person0.getGender(), savedPerson.getGender(), () -> "Gender should be the same.");
	}
	
	@DisplayName("Given Person Object When Find By Email Then Return Person Object")
	@Test
	void testGivenPersonObject_WhenFindByEmail_ThenReturnPersonObject() {
		//Given / Arrange
		
		//When / Act
		Person savedPerson = repository.findByEmail(person0.getEmail()).get();
		
		//Then / Assert
		assertNotNull(savedPerson, () -> "Shoud not return null.");
		assertEquals(person0.getId(), savedPerson.getId(), () -> "Ids should be the same.");
		assertEquals(person0.getFirstName(), savedPerson.getFirstName(), () -> "FirstName should be the same.");
		assertEquals(person0.getLastName(), savedPerson.getLastName(), () -> "LastName should be the same.");
		assertEquals(person0.getEmail(), savedPerson.getEmail(), () -> "Email should be the same.");
		assertEquals(person0.getAdress(), savedPerson.getAdress(), () -> "Adress should be the same.");
		assertEquals(person0.getGender(), savedPerson.getGender(), () -> "Gender should be the same.");
	}
	
	@DisplayName("Given Person Object When Update Person Then Return Updated Person Object")
	@Test
	void testGivenPersonObject_WhenUpdatePerson_ThenReturnUpdatedPersonObject() {
		//Given / Arrange
		
		//When / Act
		Person savedPerson = repository.findById(person0.getId()).get();
		savedPerson.setFirstName("Leonardo");
		savedPerson.setEmail("leonardo@gmail.com");
		
		Person updatedPerosn = repository.save(savedPerson);
		
		//Then / Assert
		assertNotNull(updatedPerosn, () -> "Shoud not return null.");
		assertEquals(person0.getId(), savedPerson.getId(), () -> "Ids should be the same.");
		assertEquals("Leonardo", updatedPerosn.getFirstName(), () -> "FirstName should be the same.");
		assertEquals("leonardo@gmail.com", updatedPerosn.getEmail(), () -> "Email should be the same.");
	}
	
	@DisplayName("Given Person Object When Delete Then Remove Person")
	@Test
	void testGivenPersonObject_WhenDelete_ThenRemovePerson() {
		//Given / Arrange
		
		//When / Act
		repository.deleteById(person0.getId());
		
		Optional<Person> personOptional = repository.findById(person0.getId());
		
		//Then / Assert
		assertTrue(personOptional.isEmpty());
	}
	
	@DisplayName("Given First Name And Last Name When Find By JPQL Then Return Person Object")
	@Test
	void testGivenFirstNameAndLastName_WhenFindByJPQL_ThenReturnPersonObject() {
		//Given / Arrange
		
		//When / Act
		Person savedPerson = repository.findByJPQL("Willian","Costa");
		
		//Then / Assert
		assertNotNull(savedPerson, () -> "Shoud not return null.");
		assertEquals(person0.getFirstName(), savedPerson.getFirstName(), () -> "FirstName should be the same.");
		assertEquals(person0.getLastName(), savedPerson.getLastName(), () -> "LastName should be the same.");
		assertEquals(person0.getEmail(), savedPerson.getEmail(), () -> "Email should be the same.");
		assertEquals(person0.getAdress(), savedPerson.getAdress(), () -> "Adress should be the same.");
		assertEquals(person0.getGender(), savedPerson.getGender(), () -> "Gender should be the same.");
	}
	
	
}
