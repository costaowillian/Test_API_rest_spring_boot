package br.com.willian.controllers;

import java.net.URI;

import br.com.willian.dtos.PersonDTO;
import br.com.willian.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.willian.services.PersonServices;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

//@CrossOrigin
@RestController
@RequestMapping("/api/v1/person")
@Tag(name = "People", description = "Endpoints For Managing People")
public class PersonController {
	
	@Autowired
	private PersonServices service;

	//@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Operation(
			summary = "Finds All People",
			description = "Finds All People",
			tags = {"People"},
			responses = {
					@ApiResponse(description = "Success", responseCode = "200",
							content = {
							@Content(
									mediaType = MediaType.APPLICATION_JSON,
									array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))
							)
					}),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
			})
	public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findAll(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "15") Integer size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction
	) throws Exception{
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));
		return ResponseEntity.ok().body(service.findAll(pageable));
	}

	@GetMapping(value = "/{id}",
			produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Operation(
			summary = "Finds a Person",
			description = "Finds a Person",
			tags = {"People"},
			responses = {
					@ApiResponse(description = "Success", responseCode = "200",
							content = { @Content(schema = @Schema(implementation = PersonDTO.class)) }),
					@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
			})
	public ResponseEntity<PersonDTO> findById(@PathVariable(value = "id") Long id) throws Exception {
			PersonDTO obj = service.findById(id);
			return ResponseEntity.ok().body(obj);
	}

	//@CrossOrigin(origins = { "http://localhost:8080", "https://willian.com.br" })
	@PostMapping(consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML},
			produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Operation(
			summary = "Add a new Person",
			description = "Add a new Person by passing a JSON or XML representation of the person!",
			tags = {"People"},
			responses = {
					@ApiResponse(description = "Created", responseCode = "201",
							content = { @Content(schema = @Schema(implementation = PersonDTO.class)) }),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
			})
	public ResponseEntity<PersonDTO> create(@RequestBody PersonDTO person) throws Exception{
		person = service.createPerson(person);

		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(person.getKey()).toUri();
		return ResponseEntity.created(uri).body(person);
	}
	
	@PutMapping(consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML},
			produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Operation(
			summary = "Update one Person",
			description = "Update one Person by passing a JSON or XML representation of the person!",
			tags = {"People"},
			responses = {
					@ApiResponse(description = "Updated", responseCode = "200",
							content = { @Content(schema = @Schema(implementation = PersonDTO.class)) }),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
			})
	public ResponseEntity<PersonDTO> update(@RequestBody PersonDTO person) throws Exception{
			return ResponseEntity.ok(service.updatePerson(person));
	}

	@PatchMapping(value = "/{id}",
			produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Operation(
			summary = "Disable Person",
			description = "Disable a specific Person by your id",
			tags = {"People"},
			responses = {
					@ApiResponse(description = "Success", responseCode = "200",
							content = { @Content(schema = @Schema(implementation = PersonDTO.class)) }),
					@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
			})
	public ResponseEntity<PersonDTO> disablePerson(@PathVariable(value = "id") Long id) throws Exception {
		PersonDTO obj = service.disablePerson(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@DeleteMapping("/{id}")
	@Operation(
			summary = "Deletes a Person",
			description = "Deletes a Person",
			tags = {"People"},
			responses = {
					@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
			})
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) throws Exception{	
		service.deletePerson(id);
		return ResponseEntity.noContent().build();
	}	
}
