import React from 'react';
import "./Styling/Connection.css";
import {
  getBezierPath,
  useStore,
  BaseEdge,
  type EdgeProps,
  type ReactFlowState,
  useReactFlow,
} from '@xyflow/react';
 
export type GetSpecialPathParams = {
  sourceX: number;
  sourceY: number;
  targetX: number;
  targetY: number;
};
 
export const getSpecialPath = (
  { sourceX, sourceY, targetX, targetY }: GetSpecialPathParams,
  offset: number,
) => {
  const centerX = (sourceX + targetX) / 2;
  const centerY = (sourceY + targetY) / 2;
 
  return `M ${sourceX} ${sourceY} Q ${centerX} ${
    centerY + offset
  } ${targetX} ${targetY}`;
};
 
export default function CustomEdge({
  id,
  data,
  source,
  target,
  sourceX,
  sourceY,
  targetX,
  targetY,
  sourcePosition,
  targetPosition,
  markerEnd,
}: EdgeProps) {
  const isBiDirectionEdge = useStore((s: ReactFlowState) => {
    const edgeExists = s.edges.some(
      (e) =>
        (e.source === target && e.target === source) ||
        (e.target === source && e.source === target),
    );
 
    return edgeExists;
  });

  const {setEdges} = useReactFlow(); 
 
  const edgePathParams = {
    sourceX,
    sourceY,
    sourcePosition,
    targetX,
    targetY,
    targetPosition,
  };
 
  let path = '';
  let pointOnPath ;
 
  if (isBiDirectionEdge) {
    const offset = sourceX < targetX ? 25 : -25;
    path = getSpecialPath({ sourceX, sourceY, targetX, targetY }, offset);
    pointOnPath = getPointOnQuadraticCurve(sourceX, sourceY, targetX, targetY, offset);
  } else {
    [path] = getBezierPath(edgePathParams);
    pointOnPath = getPointOnBezierCurve(sourceX, sourceY, targetX, targetY, sourcePosition, targetPosition);
  }

  function getPointOnBezierCurve(sourceX, sourceY, targetX, targetY, sourcePosition, targetPosition, t = 0.5) {
    const [edgePath] = getBezierPath({
      sourceX,
      sourceY,
      targetX,
      targetY,
      sourcePosition,
      targetPosition,
    });
  
    const match = edgePath.match(/M\s(-?\d+\.?\d*)\s(-?\d+\.?\d*)\sC\s(-?\d+\.?\d*),(-?\d+\.?\d*)\s(-?\d+\.?\d*),(-?\d+\.?\d*)\s(-?\d+\.?\d*),(-?\d+\.?\d*)/);
    if (!match) return { x: (sourceX + targetX) / 2, y: (sourceY + targetY) / 2 };
  
    const [, x1, y1, cx1, cy1, cx2, cy2, x2, y2] = match.map(Number);
  
    const x = Math.pow(1 - t, 3) * x1 +
              3 * Math.pow(1 - t, 2) * t * cx1 +
              3 * (1 - t) * Math.pow(t, 2) * cx2 +
              Math.pow(t, 3) * x2;
  
    const y = Math.pow(1 - t, 3) * y1 +
              3 * Math.pow(1 - t, 2) * t * cy1 +
              3 * (1 - t) * Math.pow(t, 2) * cy2 +
              Math.pow(t, 3) * y2;
  
    return { x, y };
  }

  
  function getPointOnQuadraticCurve(sourceX, sourceY, targetX, targetY, offset, t = 0.5) {
    const cx = (sourceX + targetX) / 2;
    const cy = (sourceY + targetY) / 2 + offset;
  
    const x = Math.pow(1 - t, 2) * sourceX +
              2 * (1 - t) * t * cx +
              Math.pow(t, 2) * targetX;
  
    const y = Math.pow(1 - t, 2) * sourceY +
              2 * (1 - t) * t * cy +
              Math.pow(t, 2) * targetY;
  
    return { x, y };
  }
  

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
    <path
  id={id}
  className="edge-path"
  d={path}
  markerEnd={markerEnd}
  fill="none"
/>


    <foreignObject
      width={100}
      height={100}
      x={pointOnPath.x - 30}
      y={pointOnPath.y - 20}
      style={{ overflow: 'visible' }}
    >
      <input
      type="number"
      value={data?.weight || 1 }
      onChange={handleWeightChange}
      placeholder="W"
      style={{
        width: '50px',
        height: '25px',
        fontSize: '14px',
        textAlign: 'center',
        backgroundColor: 'white',
        border: '1px solid gray',
        borderRadius: '4px',
      }}
      />
    </foreignObject>
    </>
  );
}