import { Handle, Position } from '@xyflow/react';
import "./Styling/Queue.css";

 
function Node({data}) {
  
  return (
    <div className="queueNode">
      <Handle
        type="target"
        position={Position.Center}
       
      />
      <Handle
        type="source"
        position={Position.Center}
       
      />
      <div>
        {data.label && <p>{data.label}</p>}
      </div>
      {/* <Handle
        type="target"
        position={Position.Left}
        
      />
      <Handle
        type="source"
        position={Position.Left}
        
      /> */}
    </div>
  );
}

 
export default Node;