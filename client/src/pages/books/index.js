import React from "react";
import './styles.css';
import Header from "../../components/header/index";
import { FiEdit, FiTrash2 } from "react-icons/fi";

export default function Book() {
    return(
        <>
            <div className="book-container"> 
                <Header />

                <h1>Registered Books</h1>

                <ul>
                    <li>
                        <strong>Tittle:</strong>
                        <p>Dooker Deep Dive</p>
                        <strong>Author</strong>
                        <p>Nigel Poulton</p>
                        <strong>Price:</strong>
                        <p>R$ 47,00</p>
                        <strong>Release Date:</strong>
                        <p>12/07/2017</p>

                        <button type="button">
                            <FiEdit size={20} color="#251fc5" />
                        </button>
                        <button type="button">
                            <FiTrash2 size={20} color="#251fc5" />
                        </button>
                    </li>
                    <li>
                        <strong>Tittle:</strong>
                        <p>Dooker Deep Dive</p>
                        <strong>Author</strong>
                        <p>Nigel Poulton</p>
                        <strong>Price:</strong>
                        <p>R$ 47,00</p>
                        <strong>Release Date:</strong>
                        <p>12/07/2017</p>

                        <button type="button">
                            <FiEdit size={20} color="#251fc5" />
                        </button>
                        <button type="button">
                            <FiTrash2 size={20} color="#251fc5" />
                        </button>
                    </li>
                    <li>
                        <strong>Tittle:</strong>
                        <p>Dooker Deep Dive</p>
                        <strong>Author</strong>
                        <p>Nigel Poulton</p>
                        <strong>Price:</strong>
                        <p>R$ 47,00</p>
                        <strong>Release Date:</strong>
                        <p>12/07/2017</p>

                        <button type="button">
                            <FiEdit size={20} color="#251fc5" />
                        </button>
                        <button type="button">
                            <FiTrash2 size={20} color="#251fc5" />
                        </button>
                    </li>
                </ul>
            </div>
        </>
    )
}