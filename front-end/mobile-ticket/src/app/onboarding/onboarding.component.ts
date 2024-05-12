import { Component, ElementRef, ViewChild, viewChild } from '@angular/core';
import { SimpleInputComponent } from '../ssalon-component/simple-input/simple-input.component';
import { ProfileImgComponent } from '../ssalon-component/profile-img/profile-img.component';
import {
  NewButtonElement,
  SimpleToggleGroupComponent,
} from '../ssalon-component/simple-toggle-group/simple-toggle-group.component';
import { SimpleButtonComponent } from '../ssalon-component/simple-button/simple-button.component';
import { ButtonElement } from '../ssalon-component/circle-toggle-button-group/circle-toggle-button-group.component';
import { NgIf } from '@angular/common';
import { Router } from '@angular/router';

export interface OnboardingStep {
  label: string;
  value: number;
}
@Component({
  selector: 'app-onboarding',
  standalone: true,
  imports: [
    NgIf,
    SimpleButtonComponent,
    SimpleInputComponent,
    ProfileImgComponent,
    SimpleToggleGroupComponent,
  ],
  templateUrl: './onboarding.component.html',
  styleUrl: './onboarding.component.scss',
})
export class OnboardingComponent {
  @ViewChild('progressBar', { static: true })
  progressBar: ElementRef | null = null;
  public onBoardingStep: OnboardingStep[] = [
    { label: '프로필을 작성해주세요', value: 0 },
    { label: '모임을 할 장소를 선택해주세요', value: 1 },
    { label: '관심 있는 카테고리를 선택해주세요', value: 2 },
  ];
  public nowOnboardingStep: OnboardingStep = this.onBoardingStep[0];
  public nextButtonElement: ButtonElement = {
    label: '다음',
    value: 0,
    imgSrc: 'assets/next.png',
  };
  /** 첫번째 화면 */
  public genderSelectionButtons = [
    {
      selected: true,
      value: 0,
      label: '남자',
    },
    { selected: false, value: 1, label: '여자' },
    { selected: false, value: 2, label: '기타' },
  ];
  /** 두번째 화면 */
  public locationSelectionButtons = [
    { selected: true, value: 0, label: '서울특별시' },
    { selected: false, value: 1, label: '경기도' },
    { selected: false, value: 2, label: '강원도' },
    { selected: false, value: 3, label: '충청북도' },
    { selected: false, value: 4, label: '충청남도' },
    { selected: false, value: 5, label: '전라남도' },
    { selected: false, value: 6, label: '전라북도' },
    { selected: false, value: 7, label: '경상북도' },
    { selected: false, value: 8, label: '경상남도' },
    { selected: false, value: 9, label: '인천광역시' },
  ];
  /** 세번째 화면 */
  public interestSelectionButtons = [
    {
      selected: true,
      value: 0,
      label: '운동',
      imgSrc: 'assets/interest-icons/excersize.png',
    },
    {
      selected: false,
      value: 1,
      label: '게임',
      imgSrc: 'assets/interest-icons/game.png',
    },
    {
      selected: false,
      value: 2,
      label: '음악',
      imgSrc: 'assets/interest-icons/music.png',
    },
    {
      selected: false,
      value: 3,
      label: '음식',
      imgSrc: 'assets/interest-icons/food.png',
    },
    {
      selected: false,
      value: 4,
      label: '독서',
      imgSrc: 'assets/interest-icons/book.png',
    },
  ];
  constructor(private _router: Router) {}
  public onClickNextButton(): void {
    const nextStep = this.nowOnboardingStep.value + 1;
    if (nextStep >= this.onBoardingStep.length) {
      this._router.navigate(['/main']);
    } else {
      this.nowOnboardingStep = this.onBoardingStep[nextStep];
    }
  }
}
