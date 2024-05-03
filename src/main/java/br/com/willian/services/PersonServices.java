package br.com.willian.services;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

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

		return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
	}
	
	public Person createPerson(Person person) {
		logger.info("Creating one person...");
				
		return repository.save(person);
	}
	
	public Person updatePerson(Person person) {
		logger.info("updating one person...");
		
		Person entity = repository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAdress(person.getAdress());
		entity.setGender(person.getGender());
		
		return repository.save(person);
	}
	
	public void deletePerson(Long id) {
		logger.info("deleting one person...");
		
		Person entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		
		repository.delete(entity);
	}
	
/*	private Person mockPerson(int i) {
		Person person =  new Person();
		person.setId(counter.incrementAndGet());
		person.setFirstName("Person name " + i);
		person.setLastName("Last name " + i);
		person.setAdress("Some Adress " + i);
		person.setGender("Gender " + i);
		return person;
	}*/
}
