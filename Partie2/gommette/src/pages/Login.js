import React, { useState, useEffect } from 'react';

import EasyHTTP from './elements/EasyHTTP';

const http = new EasyHTTP();

function loginHandler(e, setAuth, username, password, setMsg) {
    e.preventDefault();
    // && localStorage.getItem('user') === null
    if (username !== "" && password !== "" ) {
        http.post('http://localhost:8081/login', {
            username: username,
            hashedPassword: password // TODO: encode password in base64 probly, decode it in the server, before hashing it for real
        })
        .then(data => {
            data.text().then(text => {
                console.log(text);
                text = JSON.parse(text);
                setMsg("Bienvenue " + text.firstName + " " + text.lastName);
                localStorage.setItem('user', `${text.username};${text.hashedPassword}`);
            });
            // setAuth(data);
            document.getElementById('login-form').reset();
        })
        .catch(err => {
            console.log(err); 
            setMsg("Erreur d'authentification");
            localStorage.removeItem('user');
        });
    }
}

export default function Login({setAuth}) {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [msg, setMsg] = useState("");

    useEffect(() => {
        if (localStorage.getItem('user') !== null) {
            setAuth(true);
            setMsg("Bienvenue " + localStorage.getItem('user').split(';')[0] + ". Vous êtes déjà connecté");
            document.getElementById('login-form').innerHTML = ""; // ne sera fait qu'au reload, donc BOF
        }
    },[]);

    return (
        <div>

            <h1>Login</h1>

            <form method='post' encType='multipart/form-data' id='login-form'>
                <input type='text' name='username' placeholder="username" onChange={(e) => setUsername(e.target.value)}/>
                <input type='text' name='password' placeholder="password" onChange={(e) => setPassword(e.target.value)}/>

                <button onClick={(e) => loginHandler(e, setAuth, username, password, setMsg)}>Login</button>
            </form>

            <h1>{msg}</h1>
        </div>
    );
}