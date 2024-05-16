package br.com.willian.controllers;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import br.com.willian.dtos.PersonDTO;
import br.com.willian.util.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.willian.model.Person;
import br.com.willian.services.PersonServices;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/person")
public class PersonController {
	
	@Autowired
	private PersonServices service;

	@GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public ResponseEntity<List<PersonDTO>> findAll() throws Exception{
		try {
			return ResponseEntity.ok().body(service.findAll());
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}	
	
	@GetMapping(value = "/{id}",
			produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public ResponseEntity<PersonDTO> findById(@PathVariable(value = "id") Long id) throws Exception {
			PersonDTO obj = service.findById(id);
			return ResponseEntity.ok().body(obj);
	}
	
	@PostMapping(consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML},
			produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public ResponseEntity<PersonDTO> create(@RequestBody PersonDTO person) throws Exception{
		person = service.createPerson(person);

		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(person.getKey()).toUri();
		return ResponseEntity.created(uri).body(person);
	}
	
	@PutMapping(consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML},
			produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
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
