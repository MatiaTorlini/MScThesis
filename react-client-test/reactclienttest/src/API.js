
async function loadComponents() {
    const response = await fetch('http://localhost:8080/components');
    const body = await response.json();
    console.log(body);
    if(response.ok) {
      return body;
    }
    else
        throw body;
}


async function loadFlows(param) {
    const response = await fetch(`http://localhost:8080/flows/${param}`);
    const body = await response.json();
    if (response.ok) {
        return body;
    }
    else
        throw body;
}

function createInterface(rdfinterface) {
    return new Promise((resolve, reject)=> {
        fetch('http://localhost:8080/newobject', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body : JSON.stringify({component:rdfinterface.component, ports:rdfinterface.ports, modelName:rdfinterface.modelName})
        }).then((response) => {
            if (response.ok) {
                resolve(null);
            }
            else {
                response.json().then((obj) => {reject(obj);})
            }
        }).catch(err => {reject({'error' : 'server error' }) });
    });
}

const API = {loadComponents, loadFlows, createInterface};
export default API;

/* /${param} */