import styled from 'styled-components';
import React, { ReactElement } from 'react';

import sliderBtnSvg from 'rsc/slider-btn.svg';

type SliderBtnProps = {
  className: string,
  onMouseDown: React.MouseEventHandler<HTMLDivElement>
}

function SliderBtn({ className, onMouseDown }: SliderBtnProps): ReactElement {
  return (
    <StyledSliderBtn className={className} onMouseDown={onMouseDown}>
      <img src={sliderBtnSvg} alt='slider-button'/>
    </StyledSliderBtn>
  )
}

export default SliderBtn;

type StyledSliderBtnProps = {
}

const StyledSliderBtn = styled.div<StyledSliderBtnProps>`
  position: absolute;
  bottom: -1rem;
  cursor: pointer;
  z-index: 1;

  &.left {
    left: -0.7rem;
  }

  &.right {
    right: -0.7rem;
  }
`;