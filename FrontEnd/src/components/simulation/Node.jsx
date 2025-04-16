import { Handle, Position } from '@xyflow/react';
import "./Styling/Node.css";

 
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
      <div className='text'>
        {data.label }
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