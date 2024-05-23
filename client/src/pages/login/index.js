import React from "react";
import './styles.css';
import logoimage from '../../assets/livro.png';
import imgBanner from '../../assets/audio-livro.png'

export default function Login() {
    return (
        <>
            <div className="login-container">
                <section className="form">
                    <img src={ logoimage } alt="logo"></img>
                    <form>
                        <h1>
                            Acces your Account
                        </h1>
                        <input type="text" placeholder="UserName" />
                        <input type="password" placeholder="Password" />
                        <button className="button" type="submit">Login</button>
                    </form>
                </section>
                <img src={ imgBanner } alt="login"></img>
            </div>
        </>
    )
}