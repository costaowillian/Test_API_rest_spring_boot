package br.com.willian.services;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import br.com.willian.dtos.BooksDTO;
import br.com.willian.exceptions.RequiredObjectIsNullException;
import br.com.willian.exceptions.ResourceNotFoundException;
import br.com.willian.model.Book;
import br.com.willian.repositories.BookRepository;
import br.com.willian.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookServiceTest {

    @Mock
    private BookRepository repository;

    @InjectMocks
    private BookService service;

    private Book book;

    @BeforeEach
    void setup() {
        //Given / Arrange
        Date specificDate = new Date(2023 - 1900, 0, 1);
        book = new Book(1L, "Willian Costa", specificDate, 49.90, "The Midnight Gospel");
    }

    @DisplayName("test Given Person Object When Save Person Should Return a Person Object")
    @Test
    void testGivenPersonObject_WhenSavePerson_ShouldReturnPersonObject() throws Exception {
        //Given / Arrange
        when(repository.save(book)).thenReturn(book);

        //When / Act
        BooksDTO savedBook = service.create((new BooksDTO(book)));

        //Then /Assert
        assertNotNull(savedBook, () -> "Should not return null");
        assertNotNull(savedBook.getKey(), () -> "ID Should not return null");
        assertTrue(savedBook.toString().contains("</api/v1/books/1>;rel=\"self\""));
        assertEquals(book.getAuthor(), savedBook.getAuthor(), () -> "Author Should be the same ");
        assertEquals(book.getTitle(), savedBook.getTitle(), () -> "Title should be the same");
    }

    @DisplayName("test Given Books List When Find All Books Should Return a Books List")
    @Test
    void testGivenGivenBooksList_WhenFindAllBooks_ShouldReturnBooksList() throws Exception {
        //Given / Arrange
        Date specificDate = new Date(2023 - 1900, 0, 1);
        Book book2 = new Book(1L, "Kayo Santana", specificDate, 49.90, "Space Odyssey");

        when(repository.findAll()).thenReturn(List.of(book, book2));
        //When / Act

        List<BooksDTO> booksList = service.findAll();
        BooksDTO book1 = booksList.get(1);

        //Then /Assert
        assertNotNull(book1, () -> "Should not return null");
        assertNotNull(book1.getKey(), () -> "ID Should not return null");
        assertNotNull(book1.getLinks(), () -> "Links Should not return null");
        assertTrue(book1.toString().contains("</api/v1/books/1>;rel=\"self\""), () -> "Links should Contains the string </api/v1/person/2>;rel=\"self\"");
        assertNotNull(booksList, () -> "Should not return a empty list!");
        assertEquals(2, booksList.size(), () -> "Persons List should have 2 Persons object!");
    }

    @DisplayName("test Given Empty Books List When Find All Books Should Return A Empty Books List")
    @Test
    void testGiven_When_Should() throws Exception {
        //Given / Arrange
        when(repository.findAll()).thenReturn(Collections.EMPTY_LIST);

        //When / Act
        List<BooksDTO> booksList = service.findAll();

        //Then /Assert
        assertTrue(booksList.isEmpty(), () -> "Books List Shoud have empty");
        assertEquals(0, booksList.size(), () -> "Books List Should have 0- Books Object");
    }

    @DisplayName("test Given Book Id When Find By Id Should Return A Book Object")
    @Test
    void testGivenBookId_WhenFindById_ShouldReturnABookObject() throws Exception {
        //Given / Arrange
        when(repository.findById(anyLong())).thenReturn(Optional.of(book));

        //When / Act
        BooksDTO savedBook = service.findById(1L);

        //Then /Assert
        assertNotNull(savedBook, () -> "Should not return null");
        assertNotNull(savedBook.getKey(), () -> "Should not return null ID");
        assertNotNull(savedBook.getLinks(), () -> "Should not return null link");
        assertTrue(savedBook.toString().contains("</api/v1/books/1>;rel=\"self\""));
        assertEquals(book.getAuthor(), savedBook.getAuthor(), () -> "Author Should be the same ");
        assertEquals(book.getTitle(), savedBook.getTitle(), () -> "Title should be the same");
    }

    @DisplayName("test Given Person Object When Update Should Return A Book Object")
    @Test
    void testGivenBookObject_WhenUpdate_ShouldReturnABookObject() throws Exception {
        //Given / Arrange
        when(repository.findById(anyLong())).thenReturn(Optional.of(book));

        book.setTitle("Space Odyssey");
        book.setAuthor("Kayo Santana");

        when(repository.save(book)).thenReturn(book);

        //When / Act
        BooksDTO updateBook = service.update(new BooksDTO(book));

        //Then /Assert
        assertNotNull(updateBook, () -> "Should not return null");
        assertNotNull(updateBook.getKey(), () -> "Should not return null ID");
        assertNotNull(updateBook.getLinks(), () -> "Should not return null link");
        assertTrue(updateBook.toString().contains("</api/v1/books/1>;rel=\"self\""));
        assertEquals("Kayo Santana", updateBook.getAuthor(), () -> "Author Should be the same ");
        assertEquals("Space Odyssey", updateBook.getTitle(), () -> "Title should be the same");
    }

    @DisplayName("test Given Book Id When delete Book Should Do Nothing")
    @Test
    void testGivenBookId_WhenDeleteBook_ShouldDoNothing() {
        //Given / Arrange
        when(repository.findById(anyLong())).thenReturn(Optional.of(book));
        willDoNothing().given(repository).delete(book);

        //When / Act
        service.delete(book.getId());

        //Then /Assert
        verify(repository, times(1)).delete(book);
    }

    @DisplayName("test Create Given Null Book Object Should Throw Exception")
    @Test
    void testCreate_GivenNullBookObject_ShouldThrowException() {
        //Given / Arrange
        String expectedMessage = "It is not allowed to persist a null object!";

        //When / Act
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.create(null);
        });

        String actualMessage = exception.getMessage();

        //Then /Assert
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @DisplayName("test Update Given Null Book Object Should Throw Exception")
    @Test
    void testUpdate_GivenNullBookObject_ShouldThrowException() {
        //Given / Arrange
        String expectedMessage = "It is not allowed to persist a null object!";

        //When / Act
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.update(null);
        });

        String actualMessage = exception.getMessage();

        //Then /Assert
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @DisplayName("test Given A Nonexistent ID When Find ById Should Throw Exception")
    @Test
    void testGivenANonexistentID_WhenFindById_ShouldThrowException() {
        //Given / Arrange
        String expectedMessage = "No records found for this ID";

        //When / Act
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(5L);
        });

        String actualMessage = exception.getMessage();

        //Then /Assert
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
