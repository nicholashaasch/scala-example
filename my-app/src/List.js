import React, {Component, useState} from "react";

export function List(props) {
    const [formData, setFormData] = useState({
        name: '',
        desc: '',
    });

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = () => {
        fetch('http://localhost:8080/job', {
            method: 'POST',
            body: JSON.stringify({
                company: formData.name,
                description: formData.desc
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
        </div>
    );
}