import React, { Component } from "react";
import { Route, NavLink, Routes, HashRouter } from "react-router-dom";



import Submit from "./Submit";
import {List} from "./List";
import {Home} from "./Home";

class App extends Component {
    render() {
        return (
            <HashRouter>
                <div className="App">
                    <ul className="header">
                        <li><NavLink to="/">Home</NavLink></li>
                        <li><NavLink to="/submit">Submit</NavLink></li>
                        <li><NavLink to="/list">List</NavLink></li>
                    </ul>
                    <div className="content">
                        <Routes>
                            <Route path="/" element={<Home />}></Route>
                            <Route path="/submit" element={<List />}></Route>
                            <Route path="/list" element={<Submit />}></Route>
                        </Routes>
                    </div>
                </div>
            </HashRouter>
        );
    }
}
export default App;