import './App.css';
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import { useEffect, useState } from 'react';
import AnnotationPage from './AnnotationPage';
import API from './API';
import HomePage from './HomePage';
import Navigation from './Navbar';


function App() {

  const [flows, setFlows] = useState([]);
  const [component, setComponent] = useState('');
  const [interfaceName, setInterfaceName] = useState('');
  const [componentsList, setComponentsList] = useState([]);
  const [ports, setPorts] = useState([]);

  const addPort = () => {
    const newPort = {
      name: interfaceName + "Port-" + ports.length ,
      causality: true,
      flow: flows[0],
     // valueReference: undefined
    };
    setPorts([...ports, newPort]);
  }

  const removePort = (idx) => {
    const tmp = [...ports];
    tmp.splice(idx, 1);
    setPorts(tmp);
  }

  const setClass = (c) => {
    setPorts([]);
    setComponent(c)
  }

  const addInterface = async () => {
    const RDFInterface = {
      modelName : interfaceName,
      component : component,
      ports : ports
    };
    await API.createInterface(RDFInterface);
    setComponent('');
    setPorts([]);
    setInterfaceName('');
  }

  const handleNameChange = (idx, newname) => {
    const tmp = [...ports];
    tmp[idx].name = newname;
    setPorts(tmp);
}

const handleCausalityChange = (idx, newcausality) => {
    const tmp = [...ports];
    tmp[idx].causality = newcausality;
    setPorts(tmp);
}

const changeInterfaceName = (target) => {
  setInterfaceName(target);
}

const handleFlowChange = (idx, newflow) => {
    const tmp = [...ports];
    tmp[idx].flow = newflow;
    setPorts(tmp);
}

const printPorts = () => {
  console.log(ports);
}


  useEffect(() => {
    async function loadComponents() {
      const loadedComp = await API.loadComponents().then((loadedComp) => { 
      setComponentsList(loadedComp);
       setComponent(loadedComp[0]);
    });
      }
      loadComponents();
  }, [])

  useEffect(() => {
    async function loadFlows(component) {
      const loadedFlows = await API.loadFlows(component);
      setFlows(loadedFlows);
    }
    loadFlows(component);
  }, [component]);

  const post = async () => {
    await API.createInterface();
  }

  return (
    <>

      <Router>
        <Navigation />
        <Routes>
          <Route path="/" element={<HomePage post={post}/>} />
          <Route path="/annotate" element={<AnnotationPage componentsList={componentsList} setClass={setClass}
            flows={flows} removePort={removePort} addPort={addPort} handleCausalityChange={handleCausalityChange}
            handleFlowChange={handleFlowChange} handleNameChange={handleNameChange} addInterface={addInterface}
            ports={ports}  changeInterfaceName={changeInterfaceName}/>} />
        </Routes>
      </Router>
    </>

  );
}

export default App;
