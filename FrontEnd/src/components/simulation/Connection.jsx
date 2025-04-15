import { BaseEdge, getBezierPath, useReactFlow } from '@xyflow/react';


export default function CustomEdge({ id, sourceX, sourceY, targetX, targetY, markerEnd, data }) {
  const { setEdges } = useReactFlow(); 
  const [edgePath, labelX, labelY] = getBezierPath({
    sourceX,
    sourceY,
    targetX,
    targetY,
  });

  const handleWeightChange = (e) => {
    const newWeight = e.target.value;
    setEdges((edges) =>
      edges.map((edge) =>
        edge.id === id ? { ...edge, data: { ...edge.data, weight: newWeight } } : edge
      )
    );
  };

  return (
    <>
      <BaseEdge id={id} path={edgePath} markerEnd={markerEnd} />

      <foreignObject
        width={50}
        height={30}
        x={labelX - 25}
        y={labelY - 15}
        style={{ overflow: 'visible' }}
      >
        <input
          type="text"
          value={data?.weight || ''} 
          onChange={handleWeightChange} 
          placeholder="W"
          style={{
            width: '50px',
            height: '30px',
            fontSize: '14px',
            textAlign: 'center',
            border: '1px solid #555',
            borderRadius: '5px',
          }}
        />
      </foreignObject>
    </>
  );
}
