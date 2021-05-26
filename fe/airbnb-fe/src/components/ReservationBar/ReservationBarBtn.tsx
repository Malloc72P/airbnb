import styled from 'styled-components';
import React, { ReactElement } from 'react';
import { useRecoilValue } from 'recoil';

import { ReservationBarBtnType } from './atoms';
import { SelectedBtn } from './atoms';

import XBtnSvg from 'rsc/x-btn.svg';

type ReservationBarBtnProps = {
  className?: string,
  dataBtnType?: ReservationBarBtnType,
  onClickCapture: (target: HTMLDivElement) => void,
  resetContent: () => void,
  children: ReactElement[]
}

function ReservationBarBtn({ className, dataBtnType, onClickCapture, resetContent, children }: ReservationBarBtnProps): ReactElement {
  const selectedBtn = useRecoilValue<ReservationBarBtnType|null>(SelectedBtn);

  const handleClickCapture = ({ target, currentTarget }: React.MouseEvent<HTMLDivElement>): void => {
    if ((target as HTMLElement).classList.contains('x-btn')) {
      resetContent();
      return;
    }

    onClickCapture(currentTarget as HTMLDivElement);
  }

  const isSelected = (): boolean => {
    return dataBtnType === selectedBtn;
  }

  const isEntered = (): boolean => {
    return className?.split(' ').includes('entered') ?? false;
  }

  return (
    <StyledReservationBarBtn
      className={(className ?? '') + (isSelected() ? ' selected' : '')}
      onClickCapture={handleClickCapture}
      data-btn-type={dataBtnType}>
      {children}
      {isSelected() && isEntered() &&
        <button className='x-btn'>
          <img src={XBtnSvg} alt='cancel button'/>
        </button>}
    </StyledReservationBarBtn>
  );
};

export default ReservationBarBtn;

const StyledReservationBarBtn = styled.div`
  height: 100%;
  flex: 19%;
  text-align: left;
  box-sizing: border-box;
  padding-top: 0.8rem;
  padding-left: 1.7rem;
  border-radius: 9999px;
  position: relative;
  cursor: pointer;

  input {
    width: 75%;
    border: none;
    outline: none;
    background-color: transparent;
  }

  .title {
    height: 1.5em;
    font-weight: 800;
    font-size: 1em;
  }

  .content {
    line-height: 1.5rem;
    color: #4F4F4F;
    font-size: 1.1em;

    &::placeholder {
      color: #4F4F4F;
      font-size: 1em;
    }

    &.entered {
      color: #010101;
      font-weight: 800;
    }
  }

  &.entered > .content {
    color: #333333;
    font-weight: 800;
  }

  .x-btn {
    padding: 0;
    position: absolute;
    top: calc(50% - 12px);
    right: 1rem;
    border: none;
    outline: none;
    background-color: transparent;
    cursor: pointer;

    img {
      border-radius: 9999px;
      background-color: #ffffff;
      pointer-events: none;
    }

    &:hover > img {
      background-color: #e0e0e0;
    }
  }

  &.price-range {
    flex: 30%;
  }

  &.with-btn {
    flex: 20%;
  }

  &.selected {
    background-color: lightgray; // FIXME
  }

  &:not(.selected):hover {
    background-color: #e0e0e0;
  }

  &:not(:first-child)::before {
    content: '';
    width: 1px;
    height: 70%;
    background-color: #e0e0e0;
    position: absolute;
    left: 0;
  }

  &:hover::before, &:hover + &::before,
  &.selected::before, &.selected + &::before {
    background-color: transparent;
  }
`;
