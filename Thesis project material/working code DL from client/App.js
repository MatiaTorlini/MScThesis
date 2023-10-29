import './App.css';
import { useState, useEffect } from 'react';

class IndObj {
  constructor(name){
    this.name = name;
  }
  static from(json) {
    return new IndObj(json.name);
  }
}


function App() {

  const [obj, setObj] = useState([]);

  async function Do() {
    const response = await fetch('http://localhost:8080');
    const body = await response.text();
    
    if(response.ok) {
      setObj(body);
    }
  }
  
  return (
      <>
      <h1 class="center-element">This is an HTTP/GET test on an ontology backed server</h1>
      <div class="center-element">
        <button  onClick={Do}>GetOntologyResource</button>
      </div>
      <div>
           {obj}
      </div>
   
      </>
  );
}

export default App;
