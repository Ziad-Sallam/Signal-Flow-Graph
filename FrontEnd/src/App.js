import React, { useState } from "react";
import axios from "axios";
import SimulationCanvas from "./components/simulation/SimulationCanvas";

import { initialNodes } from "./components/simulation/index";
import "./App.css";
import CharactristicEq from "./components/phase2/CharactristicEq";
import Output from "./components/Output/Output";

function App() {
  const [nodes, setNodes] = useState(initialNodes);
  const [edges, setEdges] = useState([]);
  const [result, setResult] = useState(null);
  const [charactristicEq, setCharactristicEq] = useState(false);
  const [animationClass, setAnimationClass] = useState("");

  const toggleCharactristicEq = () => {
    if (charactristicEq) {
      setAnimationClass("slide-out");
      setTimeout(() => setCharactristicEq(false), 400); // match animation-duration
    } else {
      setCharactristicEq(true);
      setAnimationClass("slide-in");
    }
  };


  function createAdjacencyMatrix(nodes, edges) {
    const size = nodes.length;
    const nodeIdToIndex = {};

    // Map node id to its index in the matrix
    nodes.forEach((node, index) => {
      nodeIdToIndex[node.id] = index;
    });

    // Initialize matrix with zeros
    const matrix = Array(size)
      .fill(null)
      .map(() => Array(size).fill(0));

    // Fill matrix based on edges
    edges.forEach((edge) => {
      const from = nodeIdToIndex[edge.source];
      const to = nodeIdToIndex[edge.target];
      const weight = parseFloat(edge.data?.weight) || 0;

      matrix[from][to] = weight;
    });

    return matrix;
  }



  const handleStart = async () => {
    console.log(nodes);
    console.log(edges);

    const matrix = createAdjacencyMatrix(nodes, edges);
    console.log({ adjacencyMatrix: matrix });

    axios.post('http://localhost:8080/signal-flow-graph', {
      adjacencyMatrix: matrix,
    })
      .then((response) => {
        console.log('Response from backend:', response.data);
        setResult(response.data); // Assuming the response contains the result you want to displa
      })
      .catch((error) => {
        console.error('Error sending matrix:', error);
      });
  };


  return (
    <div>
      <SimulationCanvas
        nodes={nodes}
        setNodes={setNodes}
        edges={edges}
        setEdges={setEdges}
        onCalculate={handleStart}
      />
      <div className="result">
        {result && (<Output result={result} setResult={setResult} />)}
      </div>
      {charactristicEq && (
        <div className={`slide-container ${animationClass}`}>
          <CharactristicEq />
        </div>
      )}

      <button className="charactristicEqButton" onClick={toggleCharactristicEq}>
        {charactristicEq ? "Hide Characteristic Equation" : "Show Characteristic Equation"}
      </button>

    </div>
  );
}

export default App;
