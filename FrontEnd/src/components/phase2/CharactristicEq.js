
import axios from "axios";
import { useState } from "react";
import "./CharactristicEq.css";
import RouthTableResult from "./result.js";
export default function CharactristicEq() {
const [coeff, setCoeff] = useState([]);
const [result, setResult] = useState(null);

const handleSubmit = (coeff) => {

  
    console.log(coeff);
    axios.post("http://localhost:8080/routh", 
        coeff,
    )
    .then((response) => {
        console.log("Response from backend:", response.data);
        setResult(response.data); 
    })
    .catch((error) => {
        console.error("Error sending coefficients:", error);
    });
}
return(
  <div className="charactristicEqContainer">
    <div className="charactristicEq">
          <h2>Characteristic Equation</h2>
        {coeff.map((value, index) => (
            <>
            <input
              key={index}
              type="number"
              value={value}
              onChange={(e) => {
                const newCoeff = [...coeff];
                newCoeff[index] = Number(e.target.value);
                setCoeff(newCoeff);
              }}
            />
            <span>x^{coeff.length - index - 1}</span>
            </>
            
          ))}
          <div className="buttonContainer">
          <button onClick={() => setCoeff([...coeff, 0])}>Add Coefficient</button>
          <button onClick={() => setCoeff(coeff.slice(0, coeff.length - 1))}>
            Remove Coefficient
          </button>

          <button onClick={() => {handleSubmit(coeff)}}>Submit</button>
          </div>
          {result && (
            <div className="result">
              <RouthTableResult data={result} />
            </div>
          )}
          
    </div>
    <button onClick={() => setResult(null)} className="clearButton">
      Clear Result
      </button>
    </div>
);
}
