import styled from 'styled-components';
import React, { ReactElement } from 'react';

type CurvedPlotProps = {
  width: number,
  height: number,
}

function CurvedPlot({ width, height }: CurvedPlotProps): ReactElement {
  return (
    <StyledCurvedPlot width={width} height={height}>
      
    </StyledCurvedPlot>
  )
}

export default CurvedPlot;

type StyledCurvedPlotProps = {
  width: number,
  height: number
}

const StyledCurvedPlot = styled.canvas`
  width: ${props => props.width};
  height: ${props => props.height};
  position: absolute;
  top: 0;
  left: 0;
`;
