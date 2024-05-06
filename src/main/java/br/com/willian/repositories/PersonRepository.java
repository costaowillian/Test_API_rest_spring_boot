package br.com.willian.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.willian.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
	
	Optional<Person> findByEmail(String email);
	
	//define custom query using JPQL with index parameters
	@Query("select p from Person p where p.firstName = ?1 and  p.lastName = ?2")
	Person findByJPQL(String firstName, String LastName);
}
