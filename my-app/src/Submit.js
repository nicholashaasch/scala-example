import React, {Component, useEffect, useState} from "react";

function Submit(props) {
    const [data, setData] = useState(null);

    useEffect(() => {
        fetch('http://localhost:8080/job')
            .then(response => response.json())
            .then(json => setData(json))
            .catch(error => console.error(error));
    }, []);

    const jobs = <div>
        {data && data.map( d => {
            return <div style={{display: "flex", paddingBottom: "20px"}}>
                <div style={{paddingRight:"20px"}}>{d.company}</div>
                <div>{d.description}</div>
            </div>
            }
        )}
    </div>

    return (
        <div>
            {jobs}
        </div>
    )
}


export default Submit;