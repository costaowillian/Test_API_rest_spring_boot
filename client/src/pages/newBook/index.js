import React, {useState, useEffect} from "react";
import { useNavigate, useParams } from "react-router-dom";
import './styles.css';
import Header from "../../components/header/index";
import api from '../../services/api';

export default function NewBook() {

    const [id, setId] = useState("")
    const [author, setAuthor] = useState("");
    const [price, setPrice] = useState("");
    const [title, setTitle] = useState("");
    const [releaseDate, setReleaseDate] = useState("");
    const { bookId } = useParams();
    const navigate = useNavigate();  
    const accessToken = localStorage.getItem('accessToken');

    useEffect(() => {
        if(bookId === '0') {
            return; 
        } else { 
            loadBook()
        }
    }, [bookId])

    const loadBook = async() => {
        try {
            const response = await api.get(`/api/v1/books/${bookId}`, {
                headers: {
                    'authorization': `Bearer ${accessToken}`
                }},);
                setId(response.data.id);
                setAuthor(response.data.author);
                setPrice(response.data.price);
                setTitle(response.data.title)
                setReleaseDate(response.data.release_date);
        } catch (err) {
            alert('Error recovering book! Try again!');
            navigate("/books");
        }
    }

    const saveOrUpdate = async(e) => {
        e.preventDefault();

        try {
            if(bookId == 0) {
                let data = JSON.stringify({
                    "title": title,
                    "author": author,
                    "price": price,
                    "release_date": releaseDate
                });
                await api.post("/api/v1/books", data, {
                    headers: {
                        'authorization': `Bearer ${accessToken}`
                    }
                })
            } else {
                let data = JSON.stringify({
                    'id': id,
                    "title": title,
                    "author": author,
                    "price": price,
                    "release_date": releaseDate
                });
                await api.put("/api/v1/books", data, {
                    headers: {
                        'authorization': `Bearer ${accessToken}`
                    }
                })
            }
            navigate('/books');
        } catch(err) {
            alert('Error while recording Book! Try again!');
        }
    }

    return (
        <>
            <div className="new-book-container">
            <Header isNewBook="true" />
                <section>
                    <h1>{ bookId === '0'? 'Add a new' : 'Update a' } book</h1>
                    <p>Enter the book information and click on { bookId === '0'? '"add"!' :'"update"'}!</p>
                    <form onSubmit={saveOrUpdate}>
                        <input type="text" placeholder="Titlte" value={title} onChange={e => setTitle(e.target.value)}></input>
                        <input type="text" placeholder="Author" value={author} onChange={e => setAuthor(e.target.value)}></input>

                        <div className="input-division">
                            <input type="number" placeholder="Price" value={price} onChange={e => setPrice(e.target.value)}></input>
                            <input type="date" value={releaseDate} onChange={e => setReleaseDate(e.target.value)}></input>
                        </div>
                        <button className="button" type="submit">{ bookId === '0'? 'add' : 'update' }</button>
                    </form>
                </section>
            </div>
        </>
    );
}