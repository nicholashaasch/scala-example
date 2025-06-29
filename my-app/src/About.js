import React, {Component, useState} from "react";

export function About(props) {
    const [formData, setFormData] = useState({
        name: '',
        desc: '',
    });

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = () => {
        fetch('http://localhost:8080/member', {
            method: 'POST',
            // headers: {
            //     'Accept': 'application/json',
            //     'Content-Type': 'application/json',
            // }//,
            body: JSON.stringify({
                name: formData.name
            })
        })
            .then(response => response.json())
            .then(json => {
                return json;
            })
            .catch(error => {
                console.error(error);
            });
    }

    return (
        <div>
            <form onSubmit={handleSubmit}>
                <label>
                    Name
                    <input name={"name"} type={"text"} value={formData.name} onChange={handleChange} />

                </label>
                <label>
                    Description
                    <textarea name={"desc"} value={formData.desc} onChange={handleChange} />
                </label>
                <button type="submit">Submit</button>
                />
            </form>
            <h3>SPA App - About</h3>
            <p>This is a paragraph on the About of the SPA App.</p>
            <p>The Team of SPA App.</p>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>1</td>
                    <td>John Doe</td>
                    <td>johndoe@example.com</td>
                </tr>
                <tr>
                    <td>2</td>
                    <td>Jane Doe</td>
                    <td>janedoe@example.com</td>
                </tr>
                <tr>
                    <td>3</td>
                    <td>Bob Smith</td>
                    <td>bobsmith@example.com</td>
                </tr>
                </tbody>
            </table>
        </div>
    );
}