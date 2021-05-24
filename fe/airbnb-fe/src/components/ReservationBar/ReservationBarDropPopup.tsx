import styled from 'styled-components';
import React, { useRef, useEffect, ReactElement } from 'react';
import { useSetRecoilState } from 'recoil';

import { ReservationBarBtnType } from './atoms';
import { SelectedBtn } from './atoms';

type ReservationBarDropPopupProps = {
  width?: string,
  left?: string,
  right?: string,
  outsideBlacklist?: HTMLElement[],
  children: ReactElement
};

function ReservationBarDropPopup({ width, left, right, outsideBlacklist = [], children }: ReservationBarDropPopupProps): ReactElement {
  const ref = useRef<HTMLDivElement>(null);
  const setSelectedBtn = useSetRecoilState<ReservationBarBtnType|null>(SelectedBtn);

  useEffect(() => {
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const handleClickOutside = ({ target }: MouseEvent): void => {
    if (target === ref.current || ref.current?.contains(target as Node))
      return;

    for (const el of outsideBlacklist) {
      if (target === el || el.contains(target as Node))
        return;
    }

    setSelectedBtn(null);
  };

  return (
    <StyledReservationBarDropPopup ref={ref} width={width} left={left} right={right}>
      {children}
    </StyledReservationBarDropPopup>
  );
};

export default ReservationBarDropPopup;

type StyledReservationBarDropPopupProps = {
  width?: string,
  left?: string,
  right?: string
};

const StyledReservationBarDropPopup = styled.div<StyledReservationBarDropPopupProps>`
  width: ${props => props.width ?? 'auto'};
  display: flex;
  align-items: center;
  justify-content: center;
  box-sizing: border-box;
  padding: 2.5rem;
  position: absolute;
  top: 6rem;
  ${props => props.left ? `left: ${props.left}` : ''};
  ${props => props.right ? `right: ${props.right}` : ''};
  background-color: #ffffff;
  border-radius: 3rem;
`;
