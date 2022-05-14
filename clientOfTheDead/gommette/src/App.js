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

// custom hook, to synchronize localStorage and state
const useLocalStorage = (storageKey, fallbackState) => {
  const [value, setValue] = React.useState(
    JSON.parse(localStorage.getItem(storageKey)) ?? fallbackState
  );
  console.log(value);
  React.useEffect(() => {
    localStorage.setItem(storageKey, JSON.stringify(value));
  }, [value, storageKey]);

  return [value, setValue];
};

function App() {

  const [auth, setAuth] = useLocalStorage('user', null);

  return (
    <div className="App">
      
      <Router>
        <NavBar />
        <Routes>
          <Route path="/" element={<Students auth={auth}/>}/>
          <Route path="/students" element={<Students auth={auth}/>}/>

          <Route path="/students/:id" element={<Student auth={auth}/>}/>
          
          <Route path="/login" element={<Login setAuth={setAuth} auth={auth} />}/>
          <Route path="/register" element={<Register setAuth={setAuth} auth={auth}/>}/>

          <Route path="*" element={<PageNotFound/>}/>
        </Routes>
      </Router>
    </div>
  );
}

export default App;
