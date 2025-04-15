import { motion } from "framer-motion";
import "./result.css";
export default function RouthTableResult({ data }) {
  const { answer, stability, rhpPoles, roots } = data;

  return (
    <div className="routh-result-container">
      <motion.h2
        initial={{ opacity: 0, scale: 0.9 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ duration: 0.5 }}
      >
        Routh-Hurwitz Result
      </motion.h2>

      <div className="result-section">
        <h3>Stability: <span className={stability === "stable" ? "stable" : "unstable"}>{stability}</span></h3>
        <p>Right Half Plane Poles: <strong>{rhpPoles}</strong></p>
        <p>Roots: <code>{roots}</code></p>
      </div>

      <div className="routh-table">
        <h3>Routh Table:</h3>
        <table>
          <tbody>
            {answer.map((row, i) => (
              <tr key={i}>
                {row.map((val, j) => (
                  <td key={j}>{val}</td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}