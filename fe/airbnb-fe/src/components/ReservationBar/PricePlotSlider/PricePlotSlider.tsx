import styled from 'styled-components';
import React, { useState, useEffect, useMemo, useRef, ReactElement } from 'react';
import { useSetRecoilState, useRecoilValue } from 'recoil';
import useFetch from 'util/hooks/useFetch';

import { T_PriceRange, T_PriceRangeString } from '../atoms';
import { PriceRange, PriceRangeString } from '../atoms';
import CurvedPlot, { T_PlotData } from './CurvedPlot';
import SliderBtn from './SliderBtn';

const MOCK_DATA: T_PlotData = {
  from: 10000,
  to: 1000000,
  unit: 1000,
  scale: 20,
  data: [
    3, 5, 0, 1, 2, 3, 4, 5, 4, 10,
    20, 5, 4, 5, 2, 6, 0, 0, 0, 0,
    20, 5, 4, 5, 2, 6, 0, 0, 0, 0,
    20, 5, 4, 5, 2, 6, 0, 0, 0, 0,
    20, 5, 4, 5, 2, 6, 0, 0, 0, 0
  ]
}

type T_BtnPositions = {
  left: number,
  right: number
};

type PricePlotSliderProps = {
  plotWidth?: number,
  plotHeight?: number,
}

function PricePlotSlider({ plotWidth = 500, plotHeight= 200 }: PricePlotSliderProps): ReactElement {
  const ref = useRef<HTMLDivElement>(null);
  const setPriceRange = useSetRecoilState<T_PriceRange>(PriceRange);
  const priceRangeString = useRecoilValue<T_PriceRangeString>(PriceRangeString);
  const [btnPositions, setBtnPositions] = useState<T_BtnPositions>({ left: 0, right: plotWidth });
  const [unitWidth, setUnitWidth] = useState<number|null>(null);
  const response: T_PlotData = MOCK_DATA; // FIXME: apply data fetching

  const getCurrPlotWidth = (): number => btnPositions.right - btnPositions.left;

  useEffect(() => {
    document.addEventListener('mouseup', mouseEventHandlers.up);

    return () => {
      document.removeEventListener('mouseup', mouseEventHandlers.up);
    };
  }, []);

  useEffect(() => {
    if (priceRangeString.from !== null || response === null)
      return;

    setPriceRange({ from: response.from, to: response.to });
    setBtnPositions({ left: 0, right: plotWidth });
    setUnitWidth(plotWidth * response.unit / (response.to - response.from));
  }, [response, priceRangeString]);

  useEffect(() => {
    if (unitWidth === null)
      return;

    setPriceRange({
      from: response.from + response.unit * Math.floor(btnPositions.left / unitWidth),
      to: response.from + response.unit * Math.ceil(btnPositions.right / unitWidth)
    });
  }, [btnPositions]);

  const mouseEventHandlers = useMemo(() => {
    let isLeftBtnDown: boolean = false;
    let isRightBtnDown: boolean = false;
    let startClientX: number|null = null;

    const initStartClientX = (): void => {
      if (startClientX !== null || !ref.current) return;
      startClientX = ref.current.getClientRects()[0].x;
    }

    const handleMouseMoveBtn = (evt: MouseEvent): void => {
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
      <div>{priceRangeString.from} ~ {priceRangeString.to}</div>
      <div className='plot-cont'>
        <BackLight width={getCurrPlotWidth()} height={plotHeight} left={btnPositions.left}>
          <SliderBtn className='left' onMouseDown={mouseEventHandlers.downLeft}/>
          <SliderBtn className='right' onMouseDown={mouseEventHandlers.downRight}/>
        </BackLight>
        <CurvedPlot width={plotWidth} height={plotHeight} data={response}/>
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
  background-color: #000000;
`;