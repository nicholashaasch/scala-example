import React, {Component, useState} from "react";
import {Job} from "./List";

interface SubmitProps {

}

export function Submit(props:SubmitProps) {
    const [formData, setFormData] = useState<Job>({
        company: '',
        description: '',
    });

    const handleChange = (e:any) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = () => {
        fetch('http://localhost:8080/job', {
            method: 'POST',
            body: JSON.stringify({
                company: formData.company,
                description: formData.description
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
                    <input name={"company"} type={"text"} value={formData.company} onChange={handleChange} />
                </label>
                <label>
                    Description
                    <textarea name={"description"} value={formData.description} onChange={handleChange} />
                </label>
                <button type="submit">Submit</button>
            </form>
        </div>
    );
}