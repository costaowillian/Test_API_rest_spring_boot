import React from "react";
import { Link } from "react-router-dom";
import './styles.css';
import logoImg from '../../assets/livro.png';
import { FiPower } from "react-icons/fi";

export default function Header(props) {
    return(
        <> 
        <header>
            <img src={ logoImg } alt="logo"></img>
            <span> Welcome, <strong>Willian</strong>!</span>
            <Link className="button" to="/new-book">Add new Book</Link>
            <button type="button">
                <FiPower size={18} color="#251fc5" />
            </button> 
        </header>
        </>
    )
}