package br.com.willian.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="person")
public class Person implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "first_name", nullable = false, length = 80)
	private String firstName;
	
	@Column(name = "last_name", nullable = false, length = 80)
	private String lastName;
	
	@Column(nullable = false, length = 100)
	private String address;
	
	@Column(nullable = false, length = 6)
	private String gender;
	
	@Column(nullable = false, length = 100)
	private String email;

	@Column(nullable = false)
	private boolean enabled;
	
	public Person() {
	}
		
	public Person(Long id, String firstName, String lastName, String address, String gender, String email, boolean enabled) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.gender = gender;
		this.email = email;
		this.enabled = enabled;
	}

	public Person(String firstName, String lastName, String address, String gender, String email, boolean enabled) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.gender = gender;
		this.email = email;
		this.enabled = enabled;
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lasttName) {
		this.lastName = lasttName;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String adress) {
		this.address = adress;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Person person = (Person) o;
		return enabled == person.enabled && Objects.equals(id, person.id) && Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.equals(address, person.address) && Objects.equals(gender, person.gender) && Objects.equals(email, person.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, firstName, lastName, address, gender, email, enabled);
	}
}
