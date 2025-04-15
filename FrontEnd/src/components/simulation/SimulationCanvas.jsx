import React, { useCallback, useEffect } from "react";
import {
  addEdge,
  MiniMap,
  Controls,
  Background,
  ReactFlow,
  applyNodeChanges,
  applyEdgeChanges,
} from "@xyflow/react";
import "@xyflow/react/dist/style.css";
import { nodeTypes } from "./index";
import { edgeTypes } from "./index";
import "./Styling/Canvas.css";





function SimulationCanvas({ nodes, setNodes, edges, setEdges}) {

  const onNodesChange = useCallback(
    (changes) => setNodes((nds) => applyNodeChanges(changes, nds)),
    []
  );

  const onEdgesChange = useCallback(
    (changes) => setEdges((eds) => applyEdgeChanges(changes, eds)),
    []
  );

  const onConnect = useCallback(
    (connection) => setEdges((eds) => addEdge({ ...connection, type: 'custom' , markerEnd: {type: 'arrowclosed', color: 'blue',},}, eds)),
    []
  );

  const addNode = () => {
    const id = `${nodes.length}`;
    setNodes((nds) => [
      ...nds,
      {
        id,
        type: "Defult-Node",
        data: { label: nodes.length },
        position: { x: Math.random() * 400, y: Math.random() * 400 },
      },
    ]);
  };


  const deleteNode = (nodeId) => {
    setNodes((nds) => nds.filter((node) => node.id !== nodeId));
    setEdges((eds) => eds.filter((edge) => edge.source !== nodeId && edge.target !== nodeId));
  };

  // Save the current nodes and edges as a JSON file
  const saveToFile = () => {
    const data = JSON.stringify({ nodes, edges }, null, 2);
    const blob = new Blob([data], { type: "application/json" });
    const url = URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = "simulation-data.json";
    a.click();
    URL.revokeObjectURL(url);
  };

  // Load nodes and edges from a JSON file
  const loadFromFile = (event) => {
    const file = event.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = (e) => {
      try {
        const { nodes: loadedNodes, edges: loadedEdges } = JSON.parse(e.target.result);
        setNodes(loadedNodes || []);
        setEdges(loadedEdges || []);
      } catch (error) {
        console.error("Error parsing JSON file:", error);
      }
    };
    reader.readAsText(file);
  };


  return (
    <div style={{ height: "100vh" }}>

      <ReactFlow
        nodes={nodes}
        edges={edges}
        onNodesChange={onNodesChange}
        onEdgesChange={onEdgesChange}
        onConnect={onConnect}
        nodeTypes={nodeTypes}
        edgeTypes={edgeTypes}
        fitView
      >
        <MiniMap />
        <Controls />
        <Background />
      </ReactFlow>
      <div className="delete-nodes">
        <h3 className="header">Nodes</h3>
        <ul>
          {nodes.map((node) => (
            <li key={node.id}>
              {node.data.label}{" "}
              <button onClick={() => deleteNode(node.id)}>Delete</button>
            </li>
          ))}
        </ul>
      </div>
      <div className="buttons">
        <button className="queueButton" onClick={addNode}>Add Queue</button>
      </div>
      <div className="save-load">
        <button className="saveButton" onClick={saveToFile}>
          Save to File
        </button>
        <label className="loadButton">
          Load from file
          <input type="file" name="Load From File" accept=".json" onChange={loadFromFile} style={{ display: "none" }} />
        </label>


      </div>

    </div>
  );
}

export default SimulationCanvas;