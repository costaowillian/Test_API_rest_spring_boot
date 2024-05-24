import React from "react";
import { Link } from "react-router-dom";
import './styles.css';
import logoImg from '../../assets/livro.png';
import { FiPower } from "react-icons/fi";

export default function Header(props) {

    const { isNewBook } = props;

    const userName = localStorage.getItem('userName');

    return(
        <> 
        <header>
            <Link to="/books" ><img src={ logoImg } alt="logo"></img></Link>
            <span> Welcome, <strong>{userName}</strong>!</span>
            {!isNewBook && <Link className="button" to="/book/new">Add new Book</Link>}
            <button type="button">
                <FiPower size={18} color="#251fc5" />
            </button> 
        </header>
        </>
    )
}