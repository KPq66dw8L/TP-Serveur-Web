import './App.css';
import React, {useState} from 'react';
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
import PageNotFound from './pages/PageNotFound';


function App() {

  const [auth, setAuth] = useState({});


  return (
    <div className="App">
      
      <Router>
        <NavBar />
        <Routes>
          <Route path="/" element={<Students/>}/>
          <Route path="/students" element={<Students/>}/>
          
          <Route path="/login" element={<Login setAuth={setAuth} />}/>
          <Route path="/register" element={<Register/>}/>

          <Route path="*" element={<PageNotFound/>}/>
        </Routes>
      </Router>
    </div>
  );
}

export default App;
