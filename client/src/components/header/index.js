import React from "react";
import { Link } from "react-router-dom";
import './styles.css';
import logoImg from '../../assets/livro.png';
import { FiPower } from "react-icons/fi";
import { useNavigate } from "react-router-dom";

export default function Header(props) {

    const navigate = useNavigate([]); 

    const { isNewBook } = props;

    const userName = localStorage.getItem('userName');

    const logout = () => {
        const accessToken = localStorage.getItem('accessToken');
        localStorage.clear();
        navigate('/');
    }

    return(
        <> 
        <header>
            <Link to="/books" ><img src={ logoImg } alt="logo"></img></Link>
            <span> Welcome, <strong>{userName}</strong>!</span>
            {!isNewBook && <Link className="button" to="/book/new/0">Add new Book</Link>}
            <button type="button" onClick={logout}>
                <FiPower size={18} color="#251fc5" />
            </button> 
        </header>
        </>
    )
}