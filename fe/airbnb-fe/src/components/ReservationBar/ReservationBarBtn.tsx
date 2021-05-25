import styled from 'styled-components';
import React, { ReactElement } from 'react';
import { useRecoilValue } from 'recoil';

import { ReservationBarBtnType } from './atoms';
import { SelectedBtn } from './atoms';

type ReservationBarBtnProps = {
  className?: string,
  dataBtnType?: ReservationBarBtnType,
  onClickCapture: (target: HTMLDivElement) => void,
  children?: ReactElement[]
}

function ReservationBarBtn({ className, dataBtnType, onClickCapture, children }: ReservationBarBtnProps): ReactElement {
  const selectedBtn = useRecoilValue<ReservationBarBtnType|null>(SelectedBtn);

  const handleClickCapture = ({ target, currentTarget }: React.MouseEvent<HTMLDivElement>) => {
    onClickCapture(currentTarget as HTMLDivElement);
  }

  return (
    <StyledReservationBarBtn
      className={(className ?? '') + (dataBtnType === selectedBtn ? ' selected' : '')}
      onClickCapture={handleClickCapture}
      data-btn-type={dataBtnType}>
      {children}
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

  .entered {
    color: #333333;
    font-weight: 800;
  }
`;
