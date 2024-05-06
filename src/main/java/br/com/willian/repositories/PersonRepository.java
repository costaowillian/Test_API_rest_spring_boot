package br.com.willian.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.willian.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
	
	Optional<Person> findByEmail(String email);
}