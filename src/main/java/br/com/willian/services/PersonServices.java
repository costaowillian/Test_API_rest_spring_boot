package br.com.willian.services;

import java.util.Optional;
import java.util.logging.Logger;

import br.com.willian.controllers.PersonController;
import br.com.willian.dtos.PersonDTO;
import br.com.willian.exceptions.RequiredObjectIsNullException;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import br.com.willian.exceptions.DuplicateResourceException;
import br.com.willian.exceptions.ResourceNotFoundException;
import br.com.willian.model.Person;
import br.com.willian.repositories.PersonRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonServices {

	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired
	private PersonRepository repository;

	@Autowired
	private PagedResourcesAssembler<PersonDTO> assembler;

	public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable) throws Exception {
		logger.info("Finding all persons...");

		Page<Person> personPage = repository.findAll(pageable);
		Page<PersonDTO> personDtoPage = personPage.map(x -> new PersonDTO(x));
		personDtoPage.forEach(p -> {
            try {
                p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

		Link link = linkTo(methodOn(PersonController.class).findAll(pageable.getPageNumber(),
				pageable.getPageSize(), "asc")).withSelfRel();

		return assembler.toModel(personDtoPage, link);
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

	@Transactional
	public PersonDTO disablePerson(Long id) throws Exception {
		logger.info("Disabling one person...");

		repository.disablePerson(id);

		Optional<Person> obj = Optional.ofNullable(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID")));

		assert obj.orElse(null) != null;
		PersonDTO personDto = new PersonDTO(obj.orElse(null));
		personDto.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return personDto;
	}

	public PagedModel<EntityModel<PersonDTO>> findPersonByName(String firstName, Pageable pageable) throws Exception {
		logger.info("Finding person by name...");

		Page<Person> personPage = repository.findPersonsByName(firstName, pageable);
		Page<PersonDTO> personDtoPage = personPage.map(x -> new PersonDTO(x));
		personDtoPage.forEach(p -> {
			try {
				p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});

		Link link = linkTo(methodOn(PersonController.class).findAll(pageable.getPageNumber(),
				pageable.getPageSize(), "asc")).withSelfRel();

		return assembler.toModel(personDtoPage, link);
	}
	
	public void deletePerson(Long id) {
		logger.info("deleting one person...");
		
		Person entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		
		repository.delete(entity);
	}

	public Person fromDto(PersonDTO personDto) {
		return new Person(personDto.getKey(), personDto.getFirstName(), personDto.getLastName(), personDto.getAddress(), personDto.getGender(), personDto.getEmail(), personDto.isEnabled());
	}
}
