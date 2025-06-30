import React, {Component, useEffect, useState} from "react";

export type Job = {
 company:string,
 description:string
}

interface ListProps{

}

function List(props:ListProps) {
    const [data, setData] = useState<Job[] | undefined>(undefined);
    const [search, setSearch] = useState<string>("");

    const handleChange = (e:any) => {
        setSearch(e.target.value);
    };

    useEffect(() => {
        fetch('http://localhost:8080/job')
            .then(response => response.json())
            .then(json => {
                const jobs = json as Job[];
                setData(jobs)
            })
            .catch(error => console.error(error));
    }, []);

    const dataToUse = data && data.filter((d) => {
        return !search || d.company.includes(search)
    })

    const jobs = <div>
        {dataToUse && dataToUse.map( d => {
            return <div style={{display: "flex", paddingBottom: "20px"}}>
                <div style={{paddingRight:"20px"}}>{d.company}</div>
                <div style={{whiteSpace: "pre-wrap"}}>{d.description}</div>
            </div>
            }
        )}
    </div>

    return (
        <div>
            <input name={"search"} type={"text"} value={search} onChange={handleChange} />
            {jobs}
        </div>
    )
}


export default List;