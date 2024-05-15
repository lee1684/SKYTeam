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
import { SquareButtonComponent } from '../ssalon-component/square-button/square-button.component';
import { ButtonElementsService } from '../service/button-elements.service';

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
    SquareButtonComponent,
  ],
  templateUrl: './onboarding.component.html',
  styleUrl: './onboarding.component.scss',
})
export class OnboardingComponent {
  @ViewChild('progressBar', { static: true })
  progressBar: ElementRef | null = null;
  @ViewChild('nextButton', { static: true })
  nextButton: SquareButtonComponent | null = null;
  public onBoardingStep: OnboardingStep[] = [
    { label: '프로필을 작성해주세요', value: 0 },
    { label: '모임을 할 장소를 선택해주세요', value: 1 },
    { label: '관심 있는 카테고리를 선택해주세요', value: 2 },
  ];
  public nowOnboardingStep: OnboardingStep = this.onBoardingStep[0];

  constructor(
    private _router: Router,
    public buttonElementsService: ButtonElementsService
  ) {
    let value = '; ' + document.cookie;
    let parts = value.split('; ' + 'token' + '=');
    console.log(document.cookie, parts);
    console.log('ASDFASDFASFDSa');
  }
  public onClickNextButton(value: number): void {
    const nextStep = this.nowOnboardingStep.value + 1;
    if (nextStep >= this.onBoardingStep.length) {
      this._router.navigate(['/web/main']);
    } else {
      this.nowOnboardingStep = this.onBoardingStep[nextStep];
    }
  }
}
