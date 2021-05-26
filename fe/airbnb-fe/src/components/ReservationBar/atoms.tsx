import { atom, selector } from 'recoil';

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

export const LocationSearchState = atom<string|null>({
  key: 'LocationSearchState',
  default: null
});

export type T_CheckInOut = {
  in: number|null,
  out: number|null
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

export type T_PriceRangeString = {
  from: string|null,
  to: string|null
};

export const PriceRangeString = selector<T_PriceRangeString>({
  key: 'PriceRangeString',
  get: ({ get }): T_PriceRangeString => {
    const priceRange: T_PriceRange = get(PriceRange);

    return {
      from: priceRange.from?.toLocaleString('ko-KR', { style: 'currency', currency: 'KRW' }) ?? null,
      to: priceRange.to?.toLocaleString('ko-KR', { style: 'currency', currency: 'KRW' }) ?? null
    };
  }
})

