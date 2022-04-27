import './App.css';
import React, {useState, useEffect} from 'react';
import {
  BrowserRouter as Router,
  Routes,
  Route
} from "react-router-dom";

// my modules
import NavBar from './pages/elements/Navbar';
import Students from './pages/Students';
import Student from './pages/Student';
import Login from './pages/Login';
import Register from './pages/Register';
import PageNotFound from './pages/PageNotFound';


function App() {

  const [auth, setAuth] = useState({});

  useEffect(() => {
    setAuth(JSON.parse(localStorage.getItem('user')));
    console.log("AUTH: " + JSON.stringify(auth));
    
  }, []);


  return (
    <div className="App">
      
      <Router>
        <NavBar />
        <Routes>
          <Route path="/" element={<Students auth={auth}/>}/>
          <Route path="/students" element={<Students auth={auth}/>}/>

          <Route path="/students/:id" element={<Student auth={auth}/>}/>
          
          <Route path="/login" element={<Login setAuth={setAuth} auth={auth} />}/>
          <Route path="/register" element={<Register setAuth={setAuth}/>}/>

          <Route path="*" element={<PageNotFound/>}/>
        </Routes>
      </Router>
    </div>
  );
}

export default App;
