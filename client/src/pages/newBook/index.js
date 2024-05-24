import React from "react";
import './styles.css';
import Header from "../../components/header/index";

export default function NewBook() {
    return (
        <>
            
            <div className="new-book-container">
            <Header isNewBook="true" />
                <section>
                    <h1>Add a new Book</h1>
                    <p>Enter the book information and click on "add"!</p>
                    <form>
                        <input type="text" placeholder="Titlte"></input>
                        <input type="text" placeholder="Author"></input>

                        <div className="input-division">
                            <input type="number" placeholder="Price"></input>
                            <input type="date" placeholder="Titlte"></input>
                        </div>

                        <button className="button" type="submit">add</button>
                    </form>
                </section>
            </div>
        </>
    );
}