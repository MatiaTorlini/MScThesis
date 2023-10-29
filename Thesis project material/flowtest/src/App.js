import React, { useCallback, useEffect, useMemo } from 'react';
import ReactFlow, {
  MiniMap,
  Controls,
  Background,
  useNodesState,
  useEdgesState,
  addEdge,
} from 'reactflow';

import 'reactflow/dist/style.css';
import './nodestyle.css';

const initialNodes = [];
const initialEdges = [];

const models = [
  {
    modelName: 'testmodel1',
    modelID: 'testid1',
    ports:
      [
        {
          name: 'testport1',
          portId: '11',
          causality: 'true',
        },
        {
          name: 'testport2',
          portId: '12',
          causality: 'false'
        }
      ]
  },
  {
    modelName: 'testmodel2',
    modelID: 'testid2',
    ports:
      [
        {
          name: 'testport21',
          portId: '21',
          causality: 'true',
        }
      ]
  }
];
var x_pos = 0;
var y_pos = 0;

const tmp = [];

export default function App() {

  useEffect(() => {
    const createNode = (model) => {
    const node = {
    id: model.modelID,
    position: { x: x_pos, y: y_pos },
    data: { label: model.modelName }
  }
  x_pos = x_pos + 100;
  return node;
}
    models.forEach((m) => tmp.push(createNode(m)));
    console.log("Created tmp")
    for (let i = 0; i < tmp.length; i++) {
      console.log(tmp[i]);
      setNodes(old => [...old, tmp[i]]);
    }
  },[])

  const [nodes, setNodes, onNodesChange] = useNodesState(initialNodes);
  const [edges, setEdges, onEdgesChange] = useEdgesState(initialEdges);

  const onConnect = useCallback((params) => setEdges((eds) => addEdge(params, eds)), [setEdges]);

  return (
    <div style={{ width: '100vw', height: '100vh' }}>
      <ReactFlow
        nodes={nodes}
        edges={edges}
        onNodesChange={onNodesChange}
        onEdgesChange={onEdgesChange}
        onConnect={onConnect}
        fitView
      >
        <Controls />
        <MiniMap />
        <Background variant="dots" gap={12} size={1} />
      </ReactFlow>
    </div>
  );
}
