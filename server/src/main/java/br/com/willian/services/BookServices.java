package br.com.willian.services;

import br.com.willian.controllers.BookController;
import br.com.willian.dtos.BooksDTO;
import br.com.willian.exceptions.RequiredObjectIsNullException;
import br.com.willian.exceptions.ResourceNotFoundException;
import br.com.willian.model.Book;
import br.com.willian.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookServices {

    @Autowired
    private BookRepository repository;

    @Autowired
    private PagedResourcesAssembler<BooksDTO> assembler;

    public PagedModel<EntityModel<BooksDTO>> findAll(Pageable pageable) throws Exception{
        Page<Book> bookPage = repository.findAll(pageable);
        Page<BooksDTO> booksDTOPage = bookPage.map(x -> new BooksDTO(x));
        booksDTOPage.forEach(p -> {
            try {
                p.add(linkTo(methodOn(BookController.class).findById(p.getKey())).withSelfRel());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Link link = linkTo(methodOn(BookController.class).findAll(pageable.getPageNumber(),
                pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(booksDTOPage, link);
    }

    public BooksDTO findById(Long id) throws Exception{
        Optional<Book> book = Optional.ofNullable(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID")));

        assert book.orElse(null) != null;
        BooksDTO bookDto = new BooksDTO(book.orElse(null));
        bookDto.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return  bookDto;
    }

    public BooksDTO create(BooksDTO book) throws Exception{
        if(book == null) throw new RequiredObjectIsNullException();

        BooksDTO bookDto = new BooksDTO(repository.save(fromDto(book)));
        bookDto.add(linkTo(methodOn(BookController.class).findById(bookDto.getKey())).withSelfRel());
        return bookDto;
    }

    public BooksDTO update(BooksDTO bookDto) throws Exception{
        if(bookDto == null) throw new RequiredObjectIsNullException();

        Book entity = repository.findById(bookDto.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        entity = updateEntity(entity, bookDto);

        BooksDTO result = new BooksDTO(repository.save(entity));
        result.add(linkTo(methodOn(BookController.class).findById(result.getKey())).withSelfRel());
        return result;
    }

    public void delete(Long id) {
        Book entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        repository.delete(entity);
    }

    public Book updateEntity(Book entity, BooksDTO booksDTO) {
        entity.setAuthor(booksDTO.getAuthor());
        entity.setPrice(booksDTO.getPrice());
        entity.setTitle(booksDTO.getTitle());
        return entity;
    }

    public Book fromDto(BooksDTO bookDto) {
        return new Book(bookDto.getKey(), bookDto.getAuthor(), bookDto.getLaunchDate(),
                bookDto.getPrice(), bookDto.getTitle());
    }
}
