import React, {useEffect, useState} from "react";
import {
  useParams
} from "react-router-dom";
import Select from 'react-select';

import EasyHTTP from './elements/EasyHTTP';

const http = new EasyHTTP();

function deleteGommetteHandler(id) {
    let link = "http://localhost:8081/protected/gommette/"+id+"/delete";
    http.delete(link)
    .then(data => {
        console.log(data);
        window.location.reload(false); // TODO: find a way without too many props to refresh the main component 
    })
    .catch(err => console.log(err));
}

function modifyGommetteHandler(e, idGommette, gommetteColour, gommetteDescription, idStudent) {
    e.preventDefault();
    console.log("Modifying gommette");
    http.put(`http://localhost:8081/protected/users/${idStudent}/gommette`, {
        colour: gommetteColour,
        description: gommetteDescription,
        id: idGommette
    })
    .then(data => {
        console.log(data); 
        window.location.reload(false); 
    })
    .catch(err => console.log(err));
}

export default function Student() {

    let { id } = useParams(); // get the id from the url parameter
    let [user, setUser] = useState({});
    let [loaded, setLoaded] = useState(false);

    useEffect(() => {
        http.get('http://localhost:8081/users/'+ id)
        .then(data =>{ 
            setUser(data);
            setLoaded(true);
        })
        // Resolving promise for error
        .catch(err => console.log(err));
    }, [id, user.id]); // !!!!!! avoid using objects as dependencies, here object.prop = GOOD
    // https://dmitripavlutin.com/react-useeffect-infinite-loop/#21-avoid-objects-as-dependencies

    return (
        <div>
            {loaded ? <InfosStudent user={user} /> : <Waiting />}
        </div>);
}

function Waiting() {
    return (
        <div>
            <h1>Loading...</h1>
        </div>
    );
}

function InfosStudent({user}) {

    return (
        <div>
            <h1>{user.id} - {user.firstName} {user.lastName} in {user.group}</h1>
            <h1>Whites</h1>
            <ul>
                {user.gommettes.white.map(gommette => {
                    return (
                        <GommettesPerColour gommette={gommette} key={gommette.id} idStudent={user.id}/>
                    );
                })}
            </ul>
            <h1>Greens</h1>
            <ul>
                {user.gommettes.green.map(gommette => {
                    return (
                        <GommettesPerColour gommette={gommette} key={gommette.id} idStudent={user.id}/>
                    );
                })}
            </ul>
            <h1>Reds</h1>
            <ul>
                {user.gommettes.red.map(gommette => {
                    return (
                        <GommettesPerColour gommette={gommette} key={gommette.id} idStudent={user.id}  />
                    );
                })}
            </ul>


        </div>
    );
}

function GommettesPerColour({gommette, idStudent}) {

    let [modifyGommette, setModifyGommette] = useState(false);
    let [mot, setMot] = useState("Start");

    const [gommetteColour, setGommetteColour] = useState(gommette.colour);
    const [gommetteDescription, setGommetteDescription] = useState(gommette.description);

    let prof = localStorage.getItem('user');

    const options = [
        { value: 'white', label: 'White' },
        { value: 'green', label: 'Green' },
        { value: 'red', label: 'Red' }
    ];

    return (
        <li>
            Colour: {gommette.colour}.
            Description: {gommette.description}.
            Date: {gommette.date}.
            Prof id: {gommette.prof}.

            {prof !== null && <button onClick={() => deleteGommetteHandler(gommette.id)}>Delete</button>}
            {prof !== null && <button onClick={() => { modifyGommette ? setMot("Start") : setMot("End");setModifyGommette(!modifyGommette);}}>{mot} modifying</button>}

            { modifyGommette && <form action="">
                <Select options={options} onChange={(e) => setGommetteColour(e.value)}/>
                <input required type="text" name="description" placeholder="description" defaultValue={gommette.description} onChange={e => setGommetteDescription(e.target.value)}/>
                <button type="submit" onClick={(e) => modifyGommetteHandler(e, gommette.id, gommetteColour, gommetteDescription, idStudent)}>Submit modifications</button>
            </form> }
        </li>
    );
}