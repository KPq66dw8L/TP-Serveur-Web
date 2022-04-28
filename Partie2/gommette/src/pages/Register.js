import React, { useState, useEffect } from 'react';

import EasyHTTP from './elements/EasyHTTP';

const http = new EasyHTTP();

function createProfHandler(e, firstName, lastName, username, password) {
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
            // localStorage.setItem('user', data);
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

export default function Register({setAuth}) {

    const [profs, setProfs] = useState([]);
    const [firstName, setFirstName] = useState("");
    const [lastname, setLastName] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const [msg, setMsg] = useState("");

    const [count, setCount] = useState(0);
    const [current_id, setCurrent_id] = useState();

    useEffect(() => {
        http.get('http://localhost:8081/register')
        .then(data => {
            setProfs(data);
        })
        // Resolving promise for error
        .catch(err => console.log(err));
    }, [])

    useEffect(() => {
        if (localStorage.getItem('user') !== null) {
            setMsg("Bienvenue. Vous êtes déjà connecté");
            document.getElementById('register-form').innerHTML = "";
            let tmp_user = JSON.parse(localStorage.getItem('user'));
            setCurrent_id(tmp_user.id);
            setCount(count + 1);
        }
    },[count]);

    return (
        <div>
            <h1>Register</h1>

            <form method='post' encType='multipart/form-data' id='register-form'>
                <input type='text' name='firstname' placeholder="firstname" onChange={(e) => setFirstName(e.target.value)}/>
                <input type='text' name='lastname' placeholder="lastname" onChange={(e) => setLastName(e.target.value)}/>
                <input type='text' name='username' placeholder="username" onChange={(e) => setUsername(e.target.value)}/>
                <input type='text' name='password' placeholder="password" onChange={(e) => setPassword(e.target.value)}/>
            <button onClick={(e) => createProfHandler(e, firstName, lastname, username, password)} >Create prof</button>
            </form>

            <h1>{msg}</h1>

            <div>
                <h1>List of profs registered</h1>
                <ul>
                    {profs.map(prof => { 
                        return (
                            <li key={prof.id}>{prof.firstName} {prof.lastName} aka {prof.username}. Id: {prof.id} { ((localStorage.getItem('user') !== null) && prof.id !== current_id) && <button onClick={() => deleteProfHandler(prof.id, current_id)}>Delete</button>}</li>
                        );
                    })} 
                </ul>
            </div>
        </div>
    );
}