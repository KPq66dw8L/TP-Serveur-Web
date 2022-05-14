import React from "react";
import {
  NavLink
} from "react-router-dom";

//style sheets
import "../../styles/navbar.css";

export default function Navbar() {
    
    

    return (
        <section className="navbar">
            <ul>
                <li><NavLink to={`/students`}><p>Students</p></NavLink></li>
                <li><NavLink to={`/login`}><p>Login</p></NavLink></li>
                <li><NavLink to={`/register`}><p>Register</p></NavLink></li>
            </ul>
        </section>
    );
}