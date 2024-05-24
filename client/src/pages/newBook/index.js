import React, {useState} from "react";
import { useNavigate } from "react-router-dom";
import './styles.css';
import Header from "../../components/header/index";
import api from '../../services/api';

export default function NewBook() {

    const [author, setAuthor] = useState("");
    const [price, setPrice] = useState("");
    const [title, setTitle] = useState("");
    const [releaseDate, setReleaseDate] = useState("");

    const navigate = useNavigate();    

    const createNewBook = async(e) => {
        e.preventDefault();

        let data = JSON.stringify({
            "title": title,
            "author": author,
            "price": price,
            "release_date": releaseDate
        });

        const accessToken = localStorage.getItem('accessToken');

        try {
            const response = await api.post("/api/v1/books", data, {
                headers: {
                    'authorization': `Bearer ${accessToken}`
                }
            })

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
                    <h1>Add a new Book</h1>
                    <p>Enter the book information and click on "add"!</p>
                    <form onSubmit={createNewBook}>
                        <input type="text" placeholder="Titlte" value={title} onChange={e => setTitle(e.target.value)}></input>
                        <input type="text" placeholder="Author" value={author} onChange={e => setAuthor(e.target.value)}></input>

                        <div className="input-division">
                            <input type="number" placeholder="Price" value={price} onChange={e => setPrice(e.target.value)}></input>
                            <input type="date" value={releaseDate} onChange={e => setReleaseDate(e.target.value)}></input>
                        </div>

                        <button className="button" type="submit">add</button>
                    </form>
                </section>
            </div>
        </>
    );
}