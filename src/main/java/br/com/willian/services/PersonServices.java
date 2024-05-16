package br.com.willian.services;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import br.com.willian.controllers.PersonController;
import br.com.willian.dtos.PersonDTO;
import br.com.willian.exceptions.RequiredObjectIsNullException;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Service;

import br.com.willian.exceptions.DuplicateResourceException;
import br.com.willian.exceptions.ResourceNotFoundException;
import br.com.willian.model.Person;
import br.com.willian.repositories.PersonRepository;

@Service
public class PersonServices {

	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired
	private PersonRepository repository;

	public List<PersonDTO> findAll() throws Exception {
		logger.info("Finding all persons...");
		List<Person> list = repository.findAll();
		List<PersonDTO> listDto = list.stream().map(x -> new PersonDTO(x)).collect(Collectors.toList());
		listDto.forEach(p -> {
            try {
                p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
		return listDto;
	}
	
	public PersonDTO findById(Long id) throws Exception {
		logger.info("Finding one person...");
		Optional<Person> obj = Optional.ofNullable(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID")));

        assert obj.orElse(null) != null;
        PersonDTO personDto = new PersonDTO(obj.orElse(null));
		personDto.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return personDto;
	}
	
	public PersonDTO createPerson(PersonDTO person) throws Exception {

		if(person == null) throw new RequiredObjectIsNullException();

		logger.info("Creating one person...");
		
		Optional<Person> savedPerson = repository.findByEmail(person.getEmail());
		
		if(savedPerson.isPresent()) {
			throw new DuplicateResourceException("Person already exist with given e-mail: " + person.getEmail());
		}

		PersonDTO personDTO = new PersonDTO(repository.save(fromDto(person)));
		personDTO.add(linkTo(methodOn(PersonController.class).findById(personDTO.getKey())).withSelfRel());
		return personDTO;
	}
	
	public PersonDTO updatePerson(PersonDTO person) throws Exception {

		if(person == null) throw new RequiredObjectIsNullException();

		logger.info("updating one person...");
		
		Person entity = repository.findById(person.getKey()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		entity.setEmail(person.getEmail());

		PersonDTO personDTO = new PersonDTO(repository.save(entity));
		personDTO.add(linkTo(methodOn(PersonController.class).findById(personDTO.getKey())).withSelfRel());
		return personDTO;
	}
	
	public void deletePerson(Long id) {
		logger.info("deleting one person...");
		
		Person entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		
		repository.delete(entity);
	}

	public Person fromDto(PersonDTO personDto) {
		return new Person(personDto.getKey(), personDto.getFirstName(), personDto.getLastName(), personDto.getAddress(), personDto.getGender(), personDto.getEmail());
	}
}
