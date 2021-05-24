import { atom, selector } from 'recoil';
import { ReactElement } from 'react';

import LocationSearch from './LocationSearch/LocationSearch';
import CalendarSlider from './CalendarSlider/CalendarSlider';
import PricePlotSlider from './PricePlotSlider/PricePlotSlider';

export enum ReservationBarBtnType {
  Location = 'location',
  CheckIn = 'check-in',
  CheckOut = 'check-out',
  PriceRange = 'price-range'
};

export const SelectedBtn = atom<ReservationBarBtnType|null>({
  key: 'SelectedBtn',
  default: null
});

export const DropPopupContent = selector<ReactElement|null>({
  key: 'DropPopupContent',
  get: ({ get }): (ReactElement|null) => {
    const selectedBtnType: ReservationBarBtnType|null = get(SelectedBtn);

    switch(selectedBtnType) {
      case null:
        return null;

      case ReservationBarBtnType.Location:
        return <LocationSearch/>;

      case ReservationBarBtnType.CheckIn:
      case ReservationBarBtnType.CheckOut:
        return <CalendarSlider/>;

      case ReservationBarBtnType.PriceRange:
        return <PricePlotSlider/>
      // TODO: PriceRange, Personnel

      default:
        throw new Error(`Not existing index ${selectedBtnType}`);
    }
  }
});

export const LocationSearchState = atom<string>({
  key: 'LocationSearchState',
  default: ''
});

export type T_CheckInOut = {
  in: number | null,
  out: number | null
};

export const CheckInOut = atom<T_CheckInOut>({
  key: 'CheckInOutState',
  default: {
    in: null,
    out: null
  }
});

export type T_CheckInOutString = {
  in: string|null,
  out: string|null
};

export const CheckInOutString = selector<T_CheckInOutString>({
  key: 'CheckInOutStringState',
  get: ({ get }) => {
    const checkInOut: T_CheckInOut = get(CheckInOut);
    const checkInDate: Date|null = checkInOut.in === null ? null : new Date(checkInOut.in);
    const checkOutDate: Date|null = checkInOut.out === null ? null : new Date(checkInOut.out);

    return {
      in: checkInDate === null ? null : `${checkInDate.getMonth() + 1}월 ${checkInDate.getDate()}일`,
      out: checkOutDate === null ? null : `${checkOutDate.getMonth() + 1}월 ${checkOutDate.getDate()}일`
    }
  },
});

export type T_PriceRange = {
  from: number|null,
  to: number|null
};

export const PriceRange = atom<T_PriceRange>({
  key: 'PriceRange',
  default: {
    from: null,
    to: null
  }
});

