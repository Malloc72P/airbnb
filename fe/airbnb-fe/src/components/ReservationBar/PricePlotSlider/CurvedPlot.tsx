import styled from 'styled-components';
import React, { useRef, useEffect, ReactElement } from 'react';

export type T_PlotData = {
  from: number,
  to: number,
  unit: number,
  scale: number,
  data: number[]
};

type CurvedPlotProps = {
  width: number,
  height: number,
  data: T_PlotData,
}

function CurvedPlot({ width, height, data }: CurvedPlotProps): ReactElement {
  const ref = useRef<HTMLCanvasElement>(null);
  const scaledUnitWidth = width * data.unit * data.scale / (data.to - data.from);

  useEffect(() => {
    if (ref.current === null)
      return;

    const canvas: HTMLCanvasElement = ref.current;
    const ctx: CanvasRenderingContext2D|null = canvas.getContext('2d');

    if (ctx === null)
      throw new Error('Canvas context is null.');

    ctx.fillStyle = "#ffffff";
    ctx.beginPath();
    ctx.moveTo(0, 0);
    ctx.lineTo(0, height);
    let currX: number = 0;
    let currY: number = height;
    let nextX: number = -1;
    let nextY: number = -1;
    const heightScale: number = height / Math.max(...data.data);
    
    for (let i: number = 1; i < data.data.length - 1; i++) {
      nextX += scaledUnitWidth;
      nextY = height - heightScale * data.data[i];
      const midX = (currX + nextX) / 2;
      const midY = (currY + nextY) / 2;
      ctx.quadraticCurveTo(currX, currY, midX, midY);
      currX = nextX;
      currY = nextY;
    }

    ctx.lineTo(width, height);
    ctx.lineTo(width, 0);
    ctx.fill();
  }, []);

  return (
    <StyledCurvedPlot ref={ref} width={width} height={height}/>
  )
}

export default CurvedPlot;

type StyledCurvedPlotProps = {
  width: number,
  height: number
}

const StyledCurvedPlot = styled.canvas<StyledCurvedPlotProps>`
  width: ${props => props.width};
  height: ${props => props.height};
  position: absolute;
  top: 0;
  left: 0;
  background-color: rgba(200, 200, 200, 0.5);
`;
