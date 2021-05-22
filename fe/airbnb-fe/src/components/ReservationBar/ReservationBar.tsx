import styled from 'styled-components';
import React, { useRef, ReactElement } from 'react';
import { useRecoilState, useRecoilValue, useSetRecoilState } from 'recoil';

import { ReservationBarBtnType, T_CheckInOut, T_CheckInOutString } from './atoms';
import { SelectedBtn, DropPopupContent, LocationSearchState, CheckInOutString } from './atoms';

import ReservationBarBtn from './ReservationBarBtn';
import ReservationBarDropPopup from './ReservationBarDropPopup';

type ReservationBarProps = {
  className?: string,
};

function ReservationBar({ className }: ReservationBarProps): ReactElement {
  const ref = useRef<HTMLDivElement>(null);
  const [selectedBtn, setSelectedBtn] = useRecoilState<ReservationBarBtnType|null>(SelectedBtn);
  const dropPopupContent = useRecoilValue<ReactElement|null>(DropPopupContent);
  const [location, setLocation] = useRecoilState<string>(LocationSearchState);
  const checkInOutString = useRecoilValue<T_CheckInOutString>(CheckInOutString);

  const handleClickCaptureBtn = (currentTarget: HTMLDivElement): void => {
    setSelectedBtn((oldSelectedBtn: ReservationBarBtnType|null): ReservationBarBtnType|null => {
      const newSelectedBtn: ReservationBarBtnType|null = currentTarget.dataset.btnType as ReservationBarBtnType;

      if (oldSelectedBtn === newSelectedBtn)
        return null;

      return newSelectedBtn;
    });
  }

  const handleChange = ({ target }: React.ChangeEvent<HTMLInputElement>) => {
    setLocation(target.value);
  }

  return (
    <StyledReservationBar className={className} ref={ref}>
      <ReservationBarBtn dataBtnType={ReservationBarBtnType.Location} onClickCapture={handleClickCaptureBtn}>
        <div className='title'>위치</div>
        <input className={`content ${location.length ? 'entered' : ''}`} value={location} onChange={handleChange} placeholder={'어디로 여행가세요?'}/>
      </ReservationBarBtn>
      <ReservationBarBtn dataBtnType={ReservationBarBtnType.CheckIn} onClickCapture={handleClickCaptureBtn}>
        <div className='title'>체크인</div>
        <div className={`content ${checkInOutString.in !== null ? 'entered' : ''}`}>{checkInOutString.in ?? '날짜 입력'}</div>
      </ReservationBarBtn>        
      <ReservationBarBtn dataBtnType={ReservationBarBtnType.CheckOut} onClickCapture={handleClickCaptureBtn}>
        <div className='title'>체크아웃</div>
        <div className={`content ${checkInOutString.out !== null ? 'entered' : ''}`}>{checkInOutString.out ?? '날짜 입력'}</div>
      </ReservationBarBtn>
      <ReservationBarBtn /* TODO: dataBtnType={} */onClickCapture={handleClickCaptureBtn}>
        <div className='title'>요금</div>
        <div className='content'>tmp</div>
      </ReservationBarBtn>
      <ReservationBarBtn /* TODO: dataBtnType={} */ className='with-btn' onClickCapture={handleClickCaptureBtn}>
        <div className='title'>인원</div>
        <div className='content'>tmp</div>
        <button className='search-btn'>
        </button>
      </ReservationBarBtn>
      {dropPopupContent && <ReservationBarDropPopup outsideBlacklist={[ref.current as HTMLElement]}>{dropPopupContent}</ReservationBarDropPopup>}
    </StyledReservationBar>
  )
};

export default ReservationBar;

const StyledReservationBar = styled.div`
  width: 64rem;
  height: 4.5rem;
  display: flex;
  margin: 0 auto;
  margin-top: 6rem;
  align-items: center;
  border-radius: 9999px;
  background-color: #ffffff;
  color: #010101;
  font-size: 14px;
  position: relative;

  input {
    border: none;
    outline: none;
    background-color: transparent;
  }

  .title {
    font-weight: 800;
    font-size: 1em;
  }

  .content {
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

  .search-btn {
    width: 3.5rem;
    height: 3.5rem;
    background-color: #E84C60;
    border: none;
    border-radius: 9999px;
    outline: none;
    position: absolute;
    top: 0.5rem;
    right: 0.5rem;
  }
`;
