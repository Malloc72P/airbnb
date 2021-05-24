import styled from 'styled-components';
import React, { useState, useEffect, useMemo, useRef, ReactElement } from 'react';
import { useRecoilState } from 'recoil';
import useFetch from 'util/hooks/useFetch';

import { T_PriceRange } from '../atoms';
import { PriceRange } from '../atoms';
import { T_SliderBtnPosition } from './atoms';
import { SliderBtnPosition } from './atoms';
import CurvedPlot from './CurvedPlot';
import SliderBtn from './SliderBtn';

const MOCK_DATA = {
  start: 10000,
  unit: 1000,
  data: [
    3, 5, 0, 1, 2, 3, 4, 5, 4, 10, 20, 5, 4, 5, 2, 6, 0, 0, 0, 0
  ]
}

type T_BtnPositions = {
  left: number,
  right: number
}

type PricePlotSliderProps = {
  plotWidth?: number,
  plotHeight?: number,
}

function PricePlotSlider({ plotWidth = 400, plotHeight= 200 }: PricePlotSliderProps): ReactElement {
  const ref = useRef<HTMLDivElement>(null);
  const [priceRange, setPriceRange] = useRecoilState<T_PriceRange>(PriceRange);
  const [btnPositions, setBtnPositions] = useState<T_BtnPositions>({ left: 0, right: plotWidth });
  const response = MOCK_DATA;

  const getCurrPlotWidth = (): number => btnPositions.right - btnPositions.left;

  useEffect(() => {
    document.addEventListener('mouseup', mouseEventHandlers.up);

    return () => {
      document.removeEventListener('mouseup', mouseEventHandlers.up);
    };
  }, []);

  const mouseEventHandlers = useMemo(() => {
    let isLeftBtnDown: boolean = false;
    let isRightBtnDown: boolean = false;
    let startClientX: number|null = null;

    const initStartClientX = (): void => {
      if (startClientX !== null || !ref.current) return;
      startClientX = ref.current.getClientRects()[0].x;
    }

    const handleMouseMoveBtn = (evt: MouseEvent): void => {
      console.log('test mouse move');

      if (startClientX === null)
        return;

      if (evt.clientX < startClientX) {
        if (isLeftBtnDown)
          setBtnPositions(positions => ({ ...positions, left: 0 }));

        return;
      }
      
      if (evt.clientX > startClientX + plotWidth) {
        if (isRightBtnDown)
          setBtnPositions(positions => ({ ...positions, right: plotWidth }));
        
        return;
      }

      setBtnPositions(positions => {
        if (startClientX === null)
          throw new Error('Invalid startClientX.');

        if (isLeftBtnDown && evt.clientX - startClientX > 0 && evt.clientX - startClientX < positions.right)
          return { ...positions, left: evt.clientX - startClientX };
          
        if (isRightBtnDown && evt.clientX - startClientX < plotWidth && evt.clientX - startClientX > positions.left)
          return { ...positions, right: evt.clientX - startClientX };
          
        return positions;
      });
    }

    return {
      up: (): void => {
        document.removeEventListener('mousemove', handleMouseMoveBtn);
        isLeftBtnDown = false;
        isRightBtnDown = false;
      },
      downLeft: (evt: React.MouseEvent<HTMLDivElement>): void => {
        initStartClientX()
        evt.preventDefault();
        document.addEventListener('mousemove', handleMouseMoveBtn);
        isLeftBtnDown = true;
      },
      downRight: (evt: React.MouseEvent<HTMLDivElement>): void => {
        initStartClientX()
        evt.preventDefault();
        document.addEventListener('mousemove', handleMouseMoveBtn);
        isRightBtnDown = true;
      }
    };
  }, []);

  return (
    <StyledPricePlotSlider ref={ref} plotWidth={plotWidth} plotHeight={plotHeight}>
      <div>가격 범위</div>
      <div>{priceRange.from}~{priceRange.to}</div>
      <div className='plot-cont'>
        <BackLight width={getCurrPlotWidth()} height={plotHeight} left={btnPositions.left}>
          <SliderBtn className='left' onMouseDown={mouseEventHandlers.downLeft}/>
          <SliderBtn className='right' onMouseDown={mouseEventHandlers.downRight}/>
        </BackLight>
        <CurvedPlot width={plotWidth} height={plotHeight}/>
      </div>
    </StyledPricePlotSlider>
  );
}

export default PricePlotSlider;

type StyledPricePlotSliderProps = {
  plotWidth: number,
  plotHeight: number
}

const StyledPricePlotSlider = styled.div<StyledPricePlotSliderProps>`
  box-shadow: 0 0 0 1px red inset;

  .plot-cont {
    width: ${props => `${props.plotWidth}px`};
    height: ${props => `${props.plotHeight}px`};
    margin-top: 2rem;
    position: relative;
  }
`;

type BackLightProps = {
  width: number,
  height: number,
  left: number
}

const BackLight = styled.div<BackLightProps>`
  box-shadow: 0 0 0 1px blue inset;
  width: ${props => `${props.width}px`};
  height: ${props => `${props.height}px`};
  position: relative;
  top: 0;
  left: ${props => `${props.left}px`};
  background-color: #333333;
`;