import React, {Component, useEffect, useState} from "react";

function Contact(props) {
    const [data, setData] = useState(null);

    useEffect(() => {
        fetch('http://localhost:8080/member')
            .then(response => response.json())
            .then(json => setData(json))
            .catch(error => console.error(error));
    }, []);



    return (
        <div>
            <h3>SPA App - Contact</h3>
            <p>Please feel free to contact us with any questions or inquiries you may have. We are always happy to help!</p>
            <h4>Contact Details:</h4>
            {data && data.map((d)=>d.name)}
            <ul>
                <li><strong>Email:</strong> info@example.com</li>
                <li><strong>Phone:</strong> 1-800-555-1234</li>
                <li><strong>Address:</strong> 123 Main St, Anytown USA</li>
            </ul>
        </div>
    )
}



export default Contact;