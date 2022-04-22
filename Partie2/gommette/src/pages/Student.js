import React, {useEffect, useState} from "react";
import {
  useParams
} from "react-router-dom";

import EasyHTTP from './elements/EasyHTTP';

const http = new EasyHTTP();

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

    console.log(user);

    return (
        <div>
            <h1>{user.id} - {user.firstName} {user.lastName} in {user.group}</h1>
            <h1>Whites</h1>
            <ul>
                {user.gommettes.white.map(gommette => {
                    return (
                        <GommettesPerColour gommette={gommette} key={gommette.id} />
                    );
                })}
            </ul>
            <h1>Greens</h1>
            <ul>
                {user.gommettes.green.map(gommette => {
                    return (
                        <GommettesPerColour gommette={gommette} key={gommette.id + 1} />
                    );
                })}
            </ul>
            <h1>Reds</h1>
            <ul>
                {user.gommettes.red.map(gommette => {
                    return (
                        <GommettesPerColour gommette={gommette} key={gommette.id + 2} />
                    );
                })}
            </ul>


        </div>
    );
}

function GommettesPerColour({gommette}) {

    return (
        <li>
            Colour: {gommette.colour}.
            Description: {gommette.description}.
            Date: {gommette.date}.
            Prof id: {gommette.prof}.
            {/* <a data-gommette-id="http://localhost:8081/gommette/${gommette.id}/delete" href="#" id="delete-gommette">Delete gommette</a> */}
        </li>
    );
}