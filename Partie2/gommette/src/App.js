import './App.css';
import React from 'react';
import {
  BrowserRouter as Router,
  Routes,
  Route,
} from "react-router-dom";

// my modules
import NavBar from './pages/elements/Navbar';
import Students from './pages/Students';
import Login from './pages/Login';
import Register from './pages/Register';


function App() {
  return (
    <div className="App">
      
      <Router>
        <NavBar />
        <Routes>
          <Route path="/" element={<Students/>}/>
          <Route path="/students" element={<Students/>}/>
          
          <Route path="/login" element={<Login/>}/>
          <Route path="/register" element={<Register/>}/>
        </Routes>
      </Router>
    </div>
  );
}

export default App;
