import React, {useState, useEffect} from "react";
import './styles.css';
import Header from "../../components/header/index";
import { FiEdit, FiTrash2 } from "react-icons/fi";
import { useNavigate } from "react-router-dom";
import api from '../../services/api';

export default function Book() {
    
    const navigate = useNavigate([]); 
    
    const [books, setBooks] = useState();

    const accessToken = localStorage.getItem('accessToken');
    
    useEffect(() => {
        api.get("/api/v1/books", {
            headers: {
                'authorization': `Bearer ${accessToken}`
            }, 
            params: {
                'page': 0,
                'size': 4,
                'direction': 'asc'
            }
        }).then(response => {
            setBooks(response.data._embedded.booksDTOList);
        })
    }, []);

    const editBook = async(id) => {
        try {
            navigate(`book/new/${id}`);
        } catch(error) {
            alert("Edit failed! Try again!");
        }
    }

    const deleteBook = async(id) => {
        try {
           await api.delete(`/api/v1/books/${id}`, {headers: {
            'authorization': `Bearer ${accessToken}`
        }});

        setBooks(books.filter(book => book.id !== id));
        }catch (err) {
            alert('Delete failed" Try again!');
        }
    }

    return(
        <>
            <div className="book-container"> 
                <Header />

                <h1>Registered Books</h1>

                <ul>
                    {books ? (
                        books.map(book => (
                            <li key={book.id}>
                                <strong>Title:</strong>
                                <p>{book.title}</p>
                                <strong>Author:</strong>
                                <p>{book.author}</p>
                                <strong>Price:</strong>
                                <p>{Intl.NumberFormat('pt-br', {style:'currency', currency: 'brl'}).format(book.price)}</p>
                                <strong>Release Date:</strong>
                                <p>{Intl.DateTimeFormat('pt-BR').format(new Date(book.release_date))}</p>

                                <button type="button" onClick={() => editBook(book.id)}>
                                    <FiEdit size={20} color="#251fc5" />
                                </button>
                                <button type="button" onClick={() => deleteBook(book.id)}>
                                    <FiTrash2 size={20} color="#251fc5" />
                                </button>
                            </li>))) 
                            : (
                            <li>No books available</li>
                        )
                    }
                </ul>
            </div>
        </>
    )
}