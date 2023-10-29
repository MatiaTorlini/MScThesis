import './App.css';
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import { useEffect, useState } from 'react';
import AnnotationPage from './AnnotationPage';
import API from './API';
import HomePage from './HomePage';
import Navigation from './Navbar';
import ScenarioBuilderProvider from './ScenarioBuilder';
import ModelAdder from './ModelAdder';
import ReactFlow, {
 
  useNodesState,
  useEdgesState,

} from 'reactflow';

function App() {

  const [flows, setFlows] = useState([]);
  const [component, setComponent] = useState('');
  const [interfaceName, setInterfaceName] = useState('');
  const [componentsList, setComponentsList] = useState([]);
  const [ports, setPorts] = useState([]);
  const [selectedFile, SetSelectedFile] = useState('');

  const models = [
    {
        modelName:'testmodel1',
        modelID:'testid1',
        ports: 
        [
            {
                name:'testport1',
                portId:'11',
                causality:'true',
            },
            {
                name:'testport2',
                portId:'12',
                causality:'false'
            }
        ]
    },
    {
        modelName:'testmodel2',
        modelID:'testid2',
        ports: 
        [
            {
                name:'testport21',
                portId:'21',
                causality:'true',
            }
        ]
    }
  ];
  var x_pos = 0;
var y_pos = 0;

const tmp = [];
 
 
const [nodes, setNodes, onNodesChange] = useNodesState([]);
const [edges, setEdges, onEdgesChange] = useEdgesState([]);


useEffect(() => {
    const createNode = (model) => {
    const node = {
    id: model.modelID,
    position: { x: x_pos, y: y_pos },
    data: { label: model.modelName }
  }
  x_pos = x_pos + 200;
  return node;
}
    models.forEach((m) => tmp.push(createNode(m)));
    console.log("Created tmp")
    for (let i = 0; i < tmp.length; i++) {
      console.log(tmp[i]);
      setNodes(old => [...old, tmp[i]]);
    }
  },[])

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

const addModel = (target) => {
  //SetSelectedFile(target).then(() => {
    const formData = new FormData();
    console.log(formData);
    formData.append('file', target);
    API.createObjectNode(formData);
 // })
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
            <Route path="/compose" element={<ScenarioBuilderProvider nodes={nodes} edges={edges} models={models}/>}/>
            <Route path="/add_model" element={<ModelAdder addModel={addModel}/>}/>
        </Routes>
      </Router>
    </>

  );
}

export default App;
