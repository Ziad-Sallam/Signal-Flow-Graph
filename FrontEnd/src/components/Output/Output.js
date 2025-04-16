import { Handle } from '@xyflow/react';
import './Output.css';
import {motion} from 'framer-motion';

const sectionVariants = {
    hidden: { opacity: 0, y: 20 },
    visible: (i) => ({
      opacity: 1,
      y: 0,
      transition: {
        delay: i * 0.2,
        duration: 0.6,
        ease: 'easeOut',
      },
    }),
  };


export default function Output ({ result , setResult}) {
    const {
        forwardPaths,
        pathGains,
        cycles,
        cycleGains,
        nonTouchingLoops,
        delta,
        transferFunction,
        deltaOfPaths,
      } = result;

      function handelClose (){
        setResult(null);
      }

  return (
    <div className="output-container">
      <motion.h2
        className="text-2xl font-bold text-center"
        initial={{ opacity: 0, scale: 0.8 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ duration: 0.5 }}
      >
        Signal Flow Graph Result
      </motion.h2>

      {[ // Render animated sections
        {
          title: "Forward Paths",
          content: (
            <ul className="list-disc list-inside">
              {forwardPaths.map((path, idx) => (
                <li key={idx}>
                  Path {idx + 1}: {path.join(" → ")} | Gain: {pathGains[idx]}
                </li>
              ))}
            </ul>
          ),
        },
        {
          title: "Cycles (Loops)",
          content: (
            <ul className="list-disc list-inside">
              {cycles.map((cycle, idx) => (
                <li key={idx}>
                  Loop {idx + 1}: {cycle.join(" → ")} | Gain: {cycleGains[idx]}
                </li>
              ))}
            </ul>
          ),
        },
        {
          title: "Non-Touching Loops",
          content: nonTouchingLoops.length > 0 ? (
            <ul className="list-disc list-inside">
              {nonTouchingLoops.map((group, idx) => (
                  <li key={idx}>
                      {console.log("hiii")}
                      {console.log(group)}
                      Group {idx + 1}: {group.join(" > ")}
                  </li>
              ))}
            </ul>
          ) : (
            <p className="text-gray-500">No non-touching loops</p>
          ),
        },
        {
          title: "Global Values",
          content: (
            <>
              <p><strong>Δ (Delta):</strong> {delta}</p>
              <p><strong>Δ of Forward Paths:</strong> {deltaOfPaths.join(", ")}</p>
              <p className='transformationFunc'><strong>Transfer Function:</strong> {transferFunction}</p>
            </>
          ),
        }
      ].map((section, i) => (
        <motion.div
          key={i}
          custom={i}
          initial="hidden"
          animate="visible"
          variants={sectionVariants}
        >
          <h3 className="text-xl font-semibold mb-2">{section.title}</h3>
          {section.content}
        </motion.div>
      ))}

      <button
        className="closeButton"
        onClick={handelClose} >Close</button>
    </div>
  );
}