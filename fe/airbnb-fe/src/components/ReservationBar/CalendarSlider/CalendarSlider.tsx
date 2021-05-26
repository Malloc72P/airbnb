import styled from 'styled-components';
import React, { useState, useEffect, useRef, useMemo, ReactElement } from 'react';
import { useRecoilValue, useRecoilState } from 'recoil';

import { T_CheckInOut } from '../atoms';
import { CheckInOut } from '../atoms';
import Calendar, { CalendarProps } from './Calendar';

type CalendarSliderProps = {
  className?: string
}

function CalendarSlider({ className }: CalendarSliderProps): ReactElement {
  const ref = useRef<HTMLDivElement>(null);
  const daysRef = useRef<HTMLUListElement>(null);
  const [leftSlide, setLeftSlide] = useState<boolean>(false);
  const [rightSlide, setRightSlide] = useState<boolean>(false);
  const [betweenSpace, setBetweenSpace] = useState<number>(0);
  const [slideWidth, setSlideWidth] = useState<number>(0);
  const [calendarsData, setCalendarsData] = useState<CalendarProps[]>([]);
  const [todayDate] = useState<Date>(new Date());
  const checkInOut = useRecoilValue<T_CheckInOut>(CheckInOut);
  const calendarCnt = 2;

  useEffect(() => {
    const pivotDate: Date = checkInOut.in && checkInOut.out ? new Date(checkInOut.in) : todayDate;
    const newCalendarsData: CalendarProps[] = Array(calendarCnt + 2);
    const daysOffsetWidth: number = daysRef.current?.offsetWidth ?? 0;
    const newBetweenSpace: number = (ref.current?.offsetWidth ?? 0) - 2 * daysOffsetWidth;

    for (let i = 0; i < newCalendarsData.length; i++) {
      const currDate: Date = new Date(pivotDate);
      currDate.setMonth(pivotDate.getMonth() + i - 1);

      newCalendarsData[i] = {
        width: daysOffsetWidth,
        todayDate,
        year: currDate.getFullYear(),
        month: currDate.getMonth()
      };
    }

    setCalendarsData(newCalendarsData);
    setBetweenSpace(newBetweenSpace);
    setSlideWidth(daysOffsetWidth + newBetweenSpace);
  }, []);

  const handleClickLeft = (): void => {
    setLeftSlide(true);
  }

  const handleClickRight = (): void => {
    setRightSlide(true);
  }

  const handleTransitionEnd = (): void => {
    const { year: frontYear, month: frontMonth } = calendarsData[0];
    const newCalendarDate: Date = new Date(frontYear, frontMonth);

    if (rightSlide) {
      newCalendarDate.setMonth(newCalendarDate.getMonth() + calendarCnt + 2);
      setCalendarsData([
        ...calendarsData.slice(1),
        {
          width: slideWidth - betweenSpace,
          todayDate,
          year: newCalendarDate.getFullYear(),
          month: newCalendarDate.getMonth()
        }
      ]);
      setRightSlide(false);
    } else {
      newCalendarDate.setMonth(newCalendarDate.getMonth() - 1);
      setCalendarsData([
        {
          width: slideWidth - betweenSpace,
          todayDate,
          year: newCalendarDate.getFullYear(),
          month: newCalendarDate.getMonth()
        },
        ...calendarsData.slice(0, calendarCnt + 1)
      ]);
      setLeftSlide(false);
    }
  }

  const renderDays = (): ReactElement => {
    return (
      <>
        <li>일</li>
        <li>월</li>
        <li>화</li>
        <li>수</li>
        <li>목</li>
        <li>금</li>
        <li>토</li>
      </>
    );
  }

  const renderCalendars = (): ReactElement[] => {
    return calendarsData.map((calendarData) => 
      <Calendar key={`${calendarData.year}${calendarData.month}`} {...calendarData}/>
    );
  }

  const getPositionLeft = (): number => {
    if (rightSlide) return -2 * slideWidth;
    if (leftSlide) return 0;
    return -slideWidth;
  }

  return (
    <StyledCalendarSlider ref={ref}>
      <PositionRelativeDiv>
        <div className='days-cont'>
          <DayList ref={daysRef} className='left'>
            {renderDays()}
          </DayList>
          <DayList className='right'>
            {renderDays()}
          </DayList>
        </div>
        <CalendarContainer
          className={rightSlide || leftSlide ? 'on-slide' : ''}
          onTransitionEnd={handleTransitionEnd}
          positionLeft={getPositionLeft()}
          betweenSpace={betweenSpace}>
          {renderCalendars()}
        </CalendarContainer>
      </PositionRelativeDiv>
      <LeftBtn onClick={handleClickLeft}></LeftBtn>
      <RightBtn onClick={handleClickRight}></RightBtn>
    </StyledCalendarSlider>
  )
};

export default CalendarSlider;

const StyledCalendarSlider = styled.div`
  box-shadow: 0 0 0 1px blue inset;
  flex: 40%;
  width: 100%;
  position: relative;

  button {
    position: absolute;
  }
`;

const LeftBtn = styled.button`
  width: 3rem;
  height: 3rem;
  top: 0;
  left: 0;
  background-color: red;
`;

const RightBtn = styled.button`
  width: 3rem;
  height: 3rem;
  top: 0;
  right: 0;
  background-color: blue;
`;

const PositionRelativeDiv = styled.div`
  width: 100%;
  position: relative;

  .days-cont {
    width: 100%;
    display: flex;
    justify-content: space-between;
    position: absolute;
    top: 4rem;
  }
`;

type CalendarContainerProps = {
  positionLeft: number,
  betweenSpace: number
}

const CalendarContainer = styled.div<CalendarContainerProps>`
  width: ${props => `calc(200% + ${props.betweenSpace}px)`};
  display: flex;
  justify-content: space-between;
  position: relative;
  left: ${props => `${props.positionLeft}px`};

  &.on-slide {
    transition: left 200ms linear;
  }
`

const DayList = styled.ul`
  display: inline-block;
  list-style: none;
  padding: 0;
  color: #828282;
  font-weight: 800;

  li {
    width: 3.8rem;
    height: 3.8rem;
    display: inline-block;
    padding: 0.5rem 0;
    font-size: 1rem;
  }
`;
