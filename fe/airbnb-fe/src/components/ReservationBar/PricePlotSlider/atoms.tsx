import { atom, selector } from 'recoil';

export type T_SliderBtnPosition = {
  leftBtn: number|null,
  rightBtn: number|null,
};

export const SliderBtnPosition = atom<T_SliderBtnPosition>({
  key: 'SliderBtnPosition',
  default: {
    leftBtn: null,
    rightBtn: null
  }
});