package br.com.willian.repositories;

import java.util.Optional;

import br.com.willian.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.willian.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

	@Modifying
	@Query("UPDATE Person p SET p.enabled = false WHERE p.id =:id")
	void disablePerson(@Param("id") Long id);

	@Query("SELECT p FROM Person p WHERE p.firstName LIKE LOWER(CONCAT ('%', :firstName, '%'))")
	Page<Person> findPersonsByName(@Param("firstName") String firstName, Pageable pageable);
	
	Optional<Person> findByEmail(String email);
	
	//define custom query using JPQL with index parameters
	@Query("select p from Person p where p.firstName = ?1 and  p.lastName = ?2")
	Person findByJPQL(String firstName, String LastName);
	
	//define custom query using JPQL with name parameters
	@Query("select p from Person p where p.firstName =:firstName and  p.lastName =:LastName")
	Person findByJPQLNamedParameters(
			@Param("firstName") String firstName,
			@Param("LastName") String LastName);
	
	//define custom query using Native SQL with index parameters
	@Query(value = "select * from person p where p.first_name = ?1 and  p.last_name = ?2", nativeQuery = true)
	Person findByNativeSQL(String firstName, String LastName);
	
	//define custom query using Native SQL with name parameters
		@Query(value = "select * from person p where p.first_name =:firstName and  p.last_name =:LastName", nativeQuery = true)
		Person findByByNativeSQLNamedParameters(
				@Param("firstName") String firstName,
				@Param("LastName") String LastName);
}
