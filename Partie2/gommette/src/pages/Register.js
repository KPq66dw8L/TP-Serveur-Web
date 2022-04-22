import React, { useState, useEffect } from 'react';

import EasyHTTP from './elements/EasyHTTP';

const http = new EasyHTTP();

function createProfHandler(e, firstName, lastName, username, password) {
    e.preventDefault();
    // && localStorage.getItem('user') === null
    if (firstName !== "" && lastName !== "" && username !== "" && password !== "" ) {
        http.post('http://localhost:8081/register', {
            firstName: firstName,
            lastName: lastName,
            username: username,
            hashedPassword: password
        })
        .then(data => {
            // setAuth(data);
            console.log(data);
            // localStorage.setItem('user', data.token);
            if (data.status === 200) {
                document.getElementById('register-form').reset();
            }
        })
        .catch(err => console.log(err));
    }
}

export default function Register({setAuth}) {

    const [firstName, setFirstName] = useState("");
    const [lastname, setLastName] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const [msg, setMsg] = useState("");

    useEffect(() => {
        if (localStorage.getItem('user') !== null) {
            setAuth(true);
            setMsg("Bienvenue " + localStorage.getItem('user').split(';')[0] + ". Vous êtes déjà connecté");
            document.getElementById('register-form').innerHTML = "";
        }
    },[]);

    return (
        <div>
            <h1>Register</h1>
            <ul>
                {/* <#list users as user>
                    <li>{user.id} - {user.firstName} {user.lastName} aka {user.username} shhhh: {user.salt} {user.hashedPassword} </li>
                </#list> */}
            </ul>

            <form method='post' encType='multipart/form-data' id='register-form'>
                <input type='text' name='firstname' placeholder="firstname" onChange={(e) => setFirstName(e.target.value)}/>
                <input type='text' name='lastname' placeholder="lastname" onChange={(e) => setLastName(e.target.value)}/>
                <input type='text' name='username' placeholder="username" onChange={(e) => setUsername(e.target.value)}/>
                <input type='text' name='password' placeholder="password" onChange={(e) => setPassword(e.target.value)}/>
            <button onClick={(e) => createProfHandler(e, firstName, lastname, username, password)} >Create prof</button>
            </form>

            <h1>{msg}</h1>
        </div>
    );
}