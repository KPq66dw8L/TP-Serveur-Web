import React, { useState, useEffect } from 'react';

import EasyHTTP from './elements/EasyHTTP';

const http = new EasyHTTP();

function createProfHandler(e, firstName, lastName, username, password, setAuth) {
    e.preventDefault();

    if (firstName !== "" && lastName !== "" && username !== "" && password !== "" ) {
        http.post('http://localhost:8081/register', {
            firstName: firstName,
            lastName: lastName,
            username: username,
            hashedPassword: password
        })
        .then(data => {
            console.log(data);
            setAuth(data);
            if (data.status === 200) {
                document.getElementById('register-form').reset();
            }
        })
        .catch(err => console.log(err));
    }
}

function deleteProfHandler(idToDel, currentId) {

    http.delete(`http://localhost:8081/protected/register/${currentId}/delete/${idToDel}`)
    .then(data => {
        console.log(data);
        window.location.reload(false); 
    })
    .catch(err => console.log(err));
}

export default function Register({setAuth, auth}) {

    const [profs, setProfs] = useState([]);
    const [firstName, setFirstName] = useState("");
    const [lastname, setLastName] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const [msg, setMsg] = useState("");

    useEffect(() => {
        http.get('http://localhost:8081/register')
        .then(data => {
            setProfs(data);
        })
        // Resolving promise for error
        .catch(err => console.log(err));
    }, [])

    useEffect(() => {
        if (auth !== null) {
            setMsg("Bienvenue. Vous êtes déjà connecté");
            document.getElementById('register-form').innerHTML = "";
        }
    },[auth]);

    return (
        <div>
            <h1>Register</h1>

            <form method='post' encType='multipart/form-data' id='register-form'>
                <input type='text' name='firstname' placeholder="firstname" onChange={(e) => setFirstName(e.target.value)}/>
                <input type='text' name='lastname' placeholder="lastname" onChange={(e) => setLastName(e.target.value)}/>
                <input type='text' name='username' placeholder="username" onChange={(e) => setUsername(e.target.value)}/>
                <input type='text' name='password' placeholder="password" onChange={(e) => setPassword(e.target.value)}/>
            <button onClick={(e) => createProfHandler(e, firstName, lastname, username, password, setAuth)} >Create prof</button>
            </form>

            <h1>{msg}</h1>

            <div>
                <h1>List of profs registered</h1>
                <ul>
                    {profs.map(prof => { 
                        return (
                            <li key={prof.id}>{prof.firstName} {prof.lastName} aka {prof.username}. Id: {prof.id} { (auth !== null && prof.id !== JSON.parse(auth).id) && <button onClick={() => deleteProfHandler(prof.id, JSON.parse(auth).id)}>Delete</button>}</li>
                        );
                    })} 
                </ul>
            </div>
        </div>
    );
}