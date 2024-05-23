package br.com.willian.integrationtests.wrappers;

import br.com.willian.integrationtests.dto.BooksDTO;
import br.com.willian.integrationtests.dto.PersonDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class BookEmbeddedDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("booksDTOList")
    private List<BooksDTO> books;

    public BookEmbeddedDTO() {
    }

    public List<BooksDTO> getBooks() {
        return books;
    }

    public void setBooks(List<BooksDTO> books) {
        this.books = books;
    }
}
