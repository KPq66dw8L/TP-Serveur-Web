import "../styles/students.css";

import React, { useState, useEffect } from 'react';
import Select from 'react-select';

import EasyHTTP from './elements/EasyHTTP';

const http = new EasyHTTP();

/*
* Handlers
*/
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
        .then(() => { document.getElementById('add-student-form').reset(); })
        // Resolving promise for error
        .catch(err => console.log(err));
    }
}

function deleteGommetteHandler(e, user, studentsLen, setStudentsLen) {
    e.preventDefault();
    let link = "http://localhost:8081/users/"+user.id+"/delete";
    http.delete(link)
    .then(data => console.log(data))
    .then(() => setStudentsLen(studentsLen - 1))
    .catch(err => console.log(err));
}

function addGommetteHandler(e, user, gommetteColour, gommetteDescription, studentsLen, setStudentsLen) {
    e.preventDefault();
    http.put('http://localhost:8081/users', {
        colour: gommetteColour,
        description: gommetteDescription,
        id: user.id
    })
    .then(data => console.log(data))
    .then(() => {setStudentsLen(studentsLen)}) // to re-render the page
    .catch(err => console.log(err));
}

/*
* Main component
*/
export default function Students() {

    const [loggedIn, setLoggedIn] = useState(true);
    const [students, setStudents] = useState([]);
    const [studentsLen, setStudentsLen] = useState(students.length); // just used to re-render when we create a student
    // 'add student form' related hooks:
    const [stuFirstname, setStuFirstname] = useState("");
    const [stuLastname, setStuLastname] = useState("");
    const [stuGroup, setStuGroup] = useState("");

    // le hook useEffect est appelé à chaque fois que le state du 2eme parametre est modifié, si aucun parametre n'est precisé, 
    // le hook est appelé a chaque changement d'un hook
    useEffect(() => {

        http.get('http://localhost:8081/users')
        .then(data => setStudents(data))
        // Resolving promise for error
        .catch(err => console.log(err));

    }, [studentsLen]);

    return (
        <div>
            {loggedIn && <AddStudentForm stuFirstname={stuFirstname} setStuFirstname={setStuFirstname} stuLastname={stuLastname} setStuLastname={setStuLastname} stuGroup={stuGroup} setStuGroup={setStuGroup} studentsLen={studentsLen} setStudentsLen={setStudentsLen} />}
            <ul>
                {students.map((user) => (
                    <div key={user.id} className={"user user" + user.id}>
                        
                        <li>{user.id} - <a href={"http://localhost:8081/users/" + user.id}>{user.firstName} {user.lastName}</a> in {user.group}, Gommettes: {user.white}, {user.green}, {user.red} </li>

                        {loggedIn && <DeleteStudent user={user} studentsLen={studentsLen} setStudentsLen={setStudentsLen}/>}

                        {loggedIn && <AddGommetteForm user={user} studentsLen={studentsLen} setStudentsLen={setStudentsLen} />}
                        
                    </div>
                ))}
            </ul>

        </div>
    );
}

/*
* Sub-components
*/
function AddStudentForm({stuFirstname, setStuFirstname, stuLastname, setStuLastname,  stuGroup, setStuGroup, studentsLen, setStudentsLen}) {
    return (
        <form method='post' encType='multipart/form-data' id='add-student-form'>
            <input type='text' name='firstname' placeholder='firstname' onChange={e => setStuFirstname(e.target.value)}/>
            <input type='text' name='lastname' placeholder='lastname' onChange={e => setStuLastname(e.target.value)}/>
            <input type='text' name='group' placeholder='class/group' onChange={e => setStuGroup(e.target.value)}/>

            <button type="submit" name="add-student" onClick={(e) => addStudentHandler(e, stuFirstname, stuLastname, stuGroup, studentsLen, setStudentsLen)}>Add student</button>
        </form>
    );
}

function DeleteStudent({user, studentsLen, setStudentsLen}){
    return (<a href="#!" id="delete-student" onClick={(e) => deleteGommetteHandler(e, user, studentsLen, setStudentsLen)}>Delete student</a>);
}

function AddGommetteForm({user, studentsLen, setStudentsLen}) {
    const [gommetteColour, setGommetteColour] = useState("");
    const [gommetteDescription, setGommetteDescription] = useState("");

    const options = [
        { value: 'white', label: 'White' },
        { value: 'green', label: 'Green' },
        { value: 'red', label: 'Red' }
      ]

    return (
        <div className="addOrDelete">

            <form action='http://localhost:8081/users' method='post' encType='multipart/form-data' id="add-gommette">

                <Select options={options} defaultValue={options[0]} onChange={(e) => setGommetteColour(e.value)}/>

                <input required type="text" name="description" placeholder="description" onChange={e => setGommetteDescription(e.target.value)}/>
                <button onClick={(e) => addGommetteHandler(e, user, gommetteColour, gommetteDescription, studentsLen, setStudentsLen)} type="submit" name="add-gommette">Add gommette</button>
            </form>
        </div>
    );
}