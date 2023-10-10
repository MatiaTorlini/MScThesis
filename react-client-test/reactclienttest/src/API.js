
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

const API = {loadComponents, loadFlows};
export default API;

/* /${param} */