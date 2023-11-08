
async function loadComponents() {
    const response = await fetch('http://localhost:8080/components');
    const body = await response.json();
    if (response.ok) {
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
    const filename = 'modelinterface' + rdfinterface.modelName + '.rdf';
    return new Promise((resolve, reject) => {
        fetch('http://localhost:8080/newobject', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ component: rdfinterface.component, ports: rdfinterface.ports, modelName: rdfinterface.modelName }),

        }).then((response) => {
            if (response.ok) {
                response.blob().then((blob) => {
                    const blobURL = URL.createObjectURL(blob);
                    const a = document.createElement('a');
                    a.href = blobURL;
                    a.download = filename;
                    a.click();
                    URL.revokeObjectURL(blobURL);
                    resolve(null);
                })
            }
            else {
                response.json().then((obj) => { reject(obj); })
            }
        }).catch(err => { reject({ 'error': 'server error' }) });
    });
}

function createObjectNode(rdfinterfacefile) {
    return new Promise((resolve, reject) => {
        fetch("http://localhost:8080/addnode", {
            method: 'POST',
            body: rdfinterfacefile,
        }).then((response) => {
            if (response.ok) {
                //response is a json to render the node
                console.log("response ok");
                resolve(null);
            }
            else {
                response.json().then((obj) => reject(obj));
            }
        }).catch(err => {reject({'error' : 'server error'})})
    })
   
};

async function askConnections() {
    const response = await fetch('http://localhost:8080/reason');
    const body = await response.json();
    if (response.ok) {
        return body;
    }
    else
        throw body;
}


const API = { loadComponents, loadFlows, createInterface, createObjectNode, askConnections };
export default API;

