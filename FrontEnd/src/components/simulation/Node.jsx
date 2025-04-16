import { Handle, Position } from '@xyflow/react';
import "./Styling/Queue.css";

 
function Node({data}) {
  
  return (
    <div className="queueNode">
      <Handle
        type="target"
        position={Position.Top}
        id="top1"
       
      />
      <Handle
        type="source"
        position={Position.Top}
        id="top2"
       
      />
      <div>
        {data.label && <p>{data.label}</p>}
      </div>
      <Handle
        type="target"
        position={Position.Bottom}
        id="bottom1"
        
      />
      <Handle
        type="source"
        position={Position.Bottom}
        id="bottom2"
        
      />
    </div>
  );
}

 
export default Node;