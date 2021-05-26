import styled from 'styled-components';
import React, { useRef, ReactElement } from 'react';
import { useRecoilState, useRecoilValue, useSetRecoilState } from 'recoil';

import { ReservationBarBtnType, T_CheckInOut, T_CheckInOutString, T_PriceRange, T_PriceRangeString } from './atoms';
import { SelectedBtn,  LocationSearchState, CheckInOut, CheckInOutString, PriceRange, PriceRangeString } from './atoms';

import ReservationBarBtn from './ReservationBarBtn';
import ReservationBarDropPopup from './ReservationBarDropPopup';
import LocationSearch from './LocationSearch/LocationSearch';
import CalendarSlider from './CalendarSlider/CalendarSlider';
import PricePlotSlider from './PricePlotSlider/PricePlotSlider';

type ReservationBarProps = {
  className?: string,
};

function ReservationBar({ className }: ReservationBarProps): ReactElement {
  const ref = useRef<HTMLDivElement>(null);
  const [selectedBtn, setSelectedBtn] = useRecoilState<ReservationBarBtnType|null>(SelectedBtn);
  const [location, setLocation] = useRecoilState<string|null>(LocationSearchState);
  const checkInOutString = useRecoilValue<T_CheckInOutString>(CheckInOutString);
  const setCheckInOut = useSetRecoilState<T_CheckInOut>(CheckInOut);
  const priceRangeString = useRecoilValue<T_PriceRangeString>(PriceRangeString);
  const setPriceRange = useSetRecoilState<T_PriceRange>(PriceRange);

  const handleChange = ({ target }: React.ChangeEvent<HTMLInputElement>): void => {
    setLocation(target.value);
  }

  const handleClickCaptureBtn = (currentTarget: HTMLDivElement): void => {
    setSelectedBtn((oldSelectedBtn: ReservationBarBtnType|null): ReservationBarBtnType|null => {
      const newSelectedBtn: ReservationBarBtnType|null = currentTarget.dataset.btnType as ReservationBarBtnType;

      if (oldSelectedBtn === newSelectedBtn)
        return null;

      return newSelectedBtn;
    });
  }

  const renderDropPopup = (): ReactElement => {
    switch (selectedBtn) {
      case ReservationBarBtnType.Location:
        return (
          <ReservationBarDropPopup outsideBlacklist={[ref.current as HTMLElement]}>
            <LocationSearch/>
          </ReservationBarDropPopup>
        );

      case ReservationBarBtnType.CheckIn:
      case ReservationBarBtnType.CheckOut:
        return (
          <ReservationBarDropPopup outsideBlacklist={[ref.current as HTMLElement]} width='100%'>
            <CalendarSlider/>
          </ReservationBarDropPopup>
        );

      case ReservationBarBtnType.PriceRange:
        return (
          <ReservationBarDropPopup outsideBlacklist={[ref.current as HTMLElement]} right='0'>
            <PricePlotSlider/>
          </ReservationBarDropPopup>
        );
      
      // TODO: Personnel

      default:
        throw new Error(`Not existing index ${selectedBtn}`);
    }
  }

  return (
    <StyledReservationBar className={className} ref={ref}>
      <ReservationBarBtn
        className={location ? 'entered' : ''}
        dataBtnType={ReservationBarBtnType.Location}
        onClickCapture={handleClickCaptureBtn}
        resetContent={() => setLocation(null)}>
        <div className='title'>위치</div>
        <input className={`content`} value={location ?? ''} onChange={handleChange} placeholder={'어디로 여행가세요?'}/>
      </ReservationBarBtn>
      <ReservationBarBtn
        className={checkInOutString.in !== null ? 'entered' : ''}
        dataBtnType={ReservationBarBtnType.CheckIn}
        onClickCapture={handleClickCaptureBtn}
        resetContent={() => setCheckInOut({ in: null, out: null })}>
        <div className='title'>체크인</div>
        <div className={`content`}>{checkInOutString.in ?? '날짜 입력'}</div>
      </ReservationBarBtn>        
      <ReservationBarBtn
        className={checkInOutString.out !== null ? 'entered' : ''}
        dataBtnType={ReservationBarBtnType.CheckOut}
        onClickCapture={handleClickCaptureBtn}
        resetContent={() => setCheckInOut({ in: null, out: null })}>
        <div className='title'>체크아웃</div>
        <div className={`content`}>{checkInOutString.out ?? '날짜 입력'}</div>
      </ReservationBarBtn>
      <ReservationBarBtn
        className={priceRangeString.from !== null ? 'entered' : ''}
        dataBtnType={ReservationBarBtnType.PriceRange}
        onClickCapture={handleClickCaptureBtn}
        resetContent={() => setPriceRange({ from: null, to: null })}>
        <div className='title'>요금</div>
        <div className='content'>{priceRangeString.from !== null ? `${priceRangeString.from} ~ ${priceRangeString.to}` : '금액대 설정'}</div>
      </ReservationBarBtn>
      <ReservationBarBtn /* TODO: dataBtnType={} */ className='with-btn'
        onClickCapture={handleClickCaptureBtn}
        resetContent={() => {/* TODO */}}>
        <div className='title'>인원</div>
        <div className='content'>tmp</div>
        <button className='search-btn'>
        </button>
      </ReservationBarBtn>
      {selectedBtn && renderDropPopup()}
    </StyledReservationBar>
  )
};

export default ReservationBar;

const StyledReservationBar = styled.div`
  width: 72rem;
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
