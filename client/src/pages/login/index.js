import React, {useState} from "react";
import { useNavigate } from "react-router-dom";
import './styles.css';
import logoimage from '../../assets/livro.png';
import imgBanner from '../../assets/audio-livro.png'

import api from '../../services/api';

export default function Login() {
    const [userName, setUserName] = useState("");
    const [password, setPassword] = useState("");

    const navigate = useNavigate();

    const login = async(e) => {
        e.preventDefault();
        let data = JSON.stringify({
            "userName": userName,
            "password": password
          });

        try {
            const response = await api.post('/auth/signin', data);
            localStorage.setItem('userName', userName);
            localStorage.setItem('accessToken', response.data.accessToken);
            console.log(response);
            navigate('/books');
        } catch (err) {
            console.log(err);
            alert("Login failed! Try again!")
        }
    }

    return (
        <>
            <div className="login-container">
                <section className="form">
                    <img src={ logoimage } alt="logo"></img>
                    <form onSubmit={login}>
                        <h1>
                            Acces your Account
                        </h1>
                        <input type="text" placeholder="UserName" value={userName} onChange={e => setUserName(e.target.value)} />
                        <input type="password" placeholder="Password" value={password} onChange={e => setPassword(e.target.value)}/>
                        <button className="button" type="submit">Login</button>
                    </form>
                </section>
                <img src={ imgBanner } alt="login"></img>
            </div>
        </>
    )
}