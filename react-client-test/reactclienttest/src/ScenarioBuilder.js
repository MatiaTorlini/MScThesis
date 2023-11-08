import React, { useCallback, useRef } from 'react';
import { Button, Row, Col, Container } from 'react-bootstrap';
import ReactFlow, {
  MiniMap,
  Controls,
  Background,
  useNodesState,
  useEdgesState,
  addEdge,
  useReactFlow,
  ReactFlowProvider
} from 'reactflow';


import 'reactflow/dist/style.css';
import API from './API';


function ScenarioBuilder(props) {


  //const onConnect = useCallback((params) => setEdges((eds) => addEdge(params, eds)), [setEdges]);

  return <>
 
    <div style={{ width: '100vw', height: '90vh' }}>
      <ReactFlow
        nodes={props.nodes}
        edges={props.edges}
        // onNodesChange={onNodesChange}
        // onEdgesChange={onEdgesChange}
        // onConnect={onConnect}
        fitView
      >
        <Controls />
        <MiniMap />
        <Background variant="dots" gap={12} size={1} />
      </ReactFlow>
    </div>
  </>
}

function ScenarioBuilderProvider(props) {

  const askTopology = async () => {

    try {const res = await API.askConnections();}
    catch(e) {console.log("error asking topology")}

  }

  return (<>
    <div style={{display:"flex", alignItems:"center", justifyContent:"center" }}>
      <Button onClick={askTopology} style={{marginTop:"50px"}}>REASON</Button>

      </div>
      <ReactFlowProvider>
        <ScenarioBuilder addNode={props.addNode} nodes={props.nodes} models={props.models} />
      </ReactFlowProvider>

  </>
  );
}

export default ScenarioBuilderProvider;