package br.com.willian.services;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import br.com.willian.dtos.PersonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.willian.exceptions.DuplicateResourceException;
import br.com.willian.exceptions.ResourceNotFoundException;
import br.com.willian.model.Person;
import br.com.willian.repositories.PersonRepository;

@Service
public class PersonServices {

	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired
	PersonRepository repository;
	
	public List<Person> findAll() {
		logger.info("Finding all persons...");
		
		return repository.findAll();
	}
	
	public Person findById(Long id) {
		logger.info("Finding one person...");
		Optional<Person> obj = repository.findById(id);

		return obj.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
	}
	
	public PersonDTO createPerson(PersonDTO person) {
		logger.info("Creating one person...");
		
		Optional<Person> savedPerson = repository.findByEmail(person.getEmail());
		
		if(savedPerson.isPresent()) {
			throw new DuplicateResourceException("Person already exist with given e-mail: " + person.getEmail());
		}

		return new PersonDTO(repository.save(fromDto(person)));
	}
	
	public PersonDTO updatePerson(PersonDTO person) {
		logger.info("updating one person...");
		
		Person entity = repository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		entity.setEmail(person.getEmail());

		return new PersonDTO(repository.save(fromDto(person)));
	}
	
	public void deletePerson(Long id) {
		logger.info("deleting one person...");
		
		Person entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		
		repository.delete(entity);
	}

	public Person fromDto(PersonDTO personDto) {
		return new Person(personDto.getId(), personDto.getFirstName(), personDto.getLastName(), personDto.getAddress(), personDto.getGender(), personDto.getEmail());
	}
}
