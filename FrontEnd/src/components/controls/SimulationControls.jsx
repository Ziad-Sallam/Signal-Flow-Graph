import React from "react";
import"./Styling/SimulationControls.css";

function SimulationControls({onStart}) {
  return (
    <div className="controls">
      <button className="start" onClick={onStart}>Start</button>
    </div>
  );
}

export default SimulationControls;