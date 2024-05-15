package br.com.willian.controllers;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import br.com.willian.dtos.PersonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.willian.model.Person;
import br.com.willian.services.PersonServices;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/person")
public class PersonController {
	
	@Autowired
	private PersonServices service;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PersonDTO>> findAll() {
		try {
			List<Person> list = service.findAll();
			List<PersonDTO> listDto = list.stream().map(x -> new PersonDTO(x)).collect(Collectors.toList());
			return ResponseEntity.ok().body(listDto);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}	
	
	@GetMapping(value = "/{id}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonDTO> findById(@PathVariable(value = "id") Long id) throws Exception{
		try {
			Person obj = service.findById(id);
			return ResponseEntity.ok().body(new PersonDTO(obj));
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonDTO> create(@RequestBody PersonDTO person) throws Exception{
		person = service.createPerson(person);

		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(person.getId()).toUri();
		return ResponseEntity.created(uri).body(person);
	}
	
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonDTO> update(@RequestBody PersonDTO person) throws Exception{
		try {
			return ResponseEntity.ok(service.updatePerson(person));	
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) throws Exception{	
		service.deletePerson(id);
		return ResponseEntity.noContent().build();
	}	
}
