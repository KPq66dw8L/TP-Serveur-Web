import React, { useState } from 'react';

import EasyHTTP from './elements/EasyHTTP';

const http = new EasyHTTP();

function loginHandler(e, setAuth, username, password, setMsg) {
    e.preventDefault();
    // && localStorage.getItem('user') === null
    if (username !== "" && password !== "" ) {
        http.post('http://localhost:8081/login', {
            username: username,
            hashedPassword: password
        })
        .then(data => {
            data.text().then(text => {
                text = JSON.parse(text);
                setMsg("Bienvenue " + text.firstName + " " + text.lastName);
            });
            // setAuth(data);
            // localStorage.setItem('user', data.token);
            document.getElementById('login-form').reset();
        })
        .catch(err => console.log(err));
    }
}

export default function Login({setAuth}) {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [msg, setMsg] = useState("");

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