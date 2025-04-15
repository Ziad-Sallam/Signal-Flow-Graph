
import { useState } from "react";

export default function CharactristicEq() {
const [coeff, setCoeff] = useState([]);

const handleSubmit = (coeff) => {
    console.log(coeff);
}
return(
    <div>
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
          <button onClick={() => setCoeff([...coeff, 0])}>Add Coefficient</button>
          <button onClick={() => setCoeff(coeff.slice(0, coeff.length - 1))}>
            Remove Coefficient
          </button>

          <button onClick={() => {handleSubmit(coeff)}}>Submit</button>
    </div>
);
}
