package br.com.willian.dtos;

import br.com.willian.model.Book;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Date;


@JsonPropertyOrder({
        "id", "title", "author", "price", "release_date"
})
public class BooksDTO extends RepresentationModel<BooksDTO> implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private Long key;
    private String author;

    @JsonProperty("release_date")
    private Date launchDate;
    private Double price;
    private String title;

    public BooksDTO() {}

    public BooksDTO(Book book) {
        this.key = book.getId();
        this.author = book.getAuthor();
        this.launchDate = book.getLaunchDate();
        this.price = book.getPrice();
        this.title = book.getTitle();
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Date launch_date) {
        this.launchDate = launch_date;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
