package br.com.willian.integrationtests.dto;

import br.com.willian.model.Person;

import java.io.Serializable;


public class PersonDTO implements Serializable  {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
    private String email;

    public PersonDTO() {}

    public PersonDTO(Person person) {
        this.id = person.getId();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.address = person.getAddress();
        this.gender = person.getGender();
        this.email = person.getEmail();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
}
