import "../styles/students.css";

import React, { useState, useEffect } from 'react';

import EasyHTTP from './elements/EasyHTTP';

const http = new EasyHTTP();

function addGommetteHandler(e) {
    e.preventDefault();
    return null;
}

function addStudentHandler(e, firstName, lastName, group, studentsLen, setStudentsLen) {
    e.preventDefault();
    if (firstName !== "" && lastName !== "" && group !== "") {
        http.post('http://localhost:8081/users', {
            firstName: firstName,
            lastName: lastName,
            group: group
        })
        .then(data => console.log(data))
        .then(setStudentsLen(studentsLen + 1))
        // Resolving promise for error
        .catch(err => console.log(err));
    }
}

function AddGommetteForm({user, loggedIn}) {
    if (!loggedIn) {
        return null;
    } else {
        return (
            <div>
                <a data-student-id={"http://localhost:8081/users/"+user.id+"/delete"} href="#!" id="delete-student">Delete student</a>

                <form action='http://localhost:8081/users' method='post' encType='multipart/form-data' id="add-gommette">

                    <select name="gommette" id="gommette">
                        <option value="white">White</option>
                        <option value="green">Green</option>
                        <option value="red">Red</option>
                    </select>
                    <input required type="text" name="description"/>
                    <input type="hidden" value={user.id} name="studentId"/>
                    <button onClick={(e) => addGommetteHandler(e)} type="submit" name="add-gommette">Add gommette</button>
                </form>
            </div>
    );
    }
}


export default function Students() {

    const [loggedIn, setLoggedIn] = useState(true);
    const [students, setStudents] = useState([]);
    const [studentsLen, setStudentsLen] = useState(students.length); // just so that the component re-renders when we create a student
    // add student form related hooks
    const [stuFirstname, setStuFirstname] = useState("");
    const [stuLastname, setStuLastname] = useState("");
    const [stuGroup, setStuGroup] = useState("");

    // le hook useEffect est appelé à chaque fois que le state du 2eme parametre est modifié, si aucun parametre n'est precisé, 
    // le hook est appelé a chaque changement d'etant d'un hook
    useEffect(() => {

        http.get('http://localhost:8081/users')
        .then(data => setStudents(data))
        // Resolving promise for error
        .catch(err => console.log(err));

    }, [studentsLen]);

    let addStudentForm = null;

    if (loggedIn) {
        addStudentForm = (
                <form method='post' encType='multipart/form-data' id='add-student-form'>
                    <input type='text' name='firstname' placeholder='firstname' onChange={e => setStuFirstname(e.target.value)}/>
                    <input type='text' name='lastname' placeholder='lastname' onChange={e => setStuLastname(e.target.value)}/>
                    <input type='text' name='group' placeholder='class/group' onChange={e => setStuGroup(e.target.value)}/>

                    <button type="submit" name="add-student" onClick={(e) => addStudentHandler(e, stuFirstname, stuLastname, stuGroup, studentsLen, setStudentsLen)}>Add student</button>
                </form>);
    }

    return (
        <div>
            <ul>
                {students.map((user) => (
                    <div key={user.id}>
                        
                        <li>{user.id} - <a href={"http://localhost:8081/users/" + user.id}>{user.firstName} {user.lastName}</a> in {user.group}, Gommettes: {user.white}, {user.green}, {user.red} </li>

                        <AddGommetteForm user={user} loggedIn={loggedIn}/>
                        
                    </div>
                ))}
            </ul>

            {addStudentForm}
        </div>
    );
}