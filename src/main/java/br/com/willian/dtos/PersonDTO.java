package br.com.willian.dtos;

import br.com.willian.model.Person;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@JsonPropertyOrder({
        "id", "first_name", "last_name", "email", "gender", "Address",
})
public class PersonDTO extends RepresentationModel<PersonDTO> implements Serializable  {

    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private Long key;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;
    private String address;

    //@JsonIgnore
    private String gender;
    private String email;

    public PersonDTO() {}

    public PersonDTO(Person person) {
        this.key = person.getId();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.address = person.getAddress();
        this.gender = person.getGender();
        this.email = person.getEmail();
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long id) {
        this.key = id;
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
