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
import { ApiExecutorService } from '../service/api-executor.service';

export interface OnboardingStep {
  label: string;
  value: number;
}

export interface RegisterUserInfo {
  nickname: string;
  profilePictureUrl: string;
  gender: '' | 'M' | 'F' | 'G';
  address: string;
  introduction: string;
  interests: string[];
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
  @ViewChild('profileImg', { static: false })
  profileImg: ProfileImgComponent | null = null;
  public onBoardingStep: OnboardingStep[] = [
    { label: '프로필을 작성해주세요', value: 0 },
    { label: '모임을 할 장소를 선택해주세요', value: 1 },
    { label: '관심 있는 카테고리를 선택해주세요', value: 2 },
  ];
  public nowOnboardingStep: OnboardingStep = this.onBoardingStep[0];
  private _userInfo: RegisterUserInfo = {
    nickname: '',
    profilePictureUrl: '',
    gender: '',
    address: '',
    introduction: '',
    interests: [],
  };

  constructor(
    private _router: Router,
    private _apiExecutorService: ApiExecutorService,
    public buttonElementsService: ButtonElementsService
  ) {}
  public onChangeUserInfo(type: string, value: string): void {
    switch (type) {
      case 'nickname':
        this._userInfo!.nickname = value;
        break;
      case 'introduction':
        this._userInfo!.introduction = value;
        break;
      case 'gender':
        this._userInfo!.gender = this.getStringFromGenderEnum(
          this.buttonElementsService.genderSelectionButtons.find((button) => {
            return button.selected === true;
          })!.value
        );
        break;
      case 'address':
        this._userInfo!.address =
          this.buttonElementsService.locationSelectionButtons.find((button) => {
            return button.selected === true;
          })!.label;
        break;
      case 'interests':
        this._userInfo!.interests = [
          ...this.buttonElementsService.interestSelectionButtons,
        ]
          .filter((button) => {
            return button.selected == true;
          })
          .map((button) => {
            return button.label;
          });
    }
    if (this.nowOnboardingStep === this.onBoardingStep[0]) {
      if (this._userInfo!.nickname !== '' && this._userInfo!.gender !== '') {
        this.buttonElementsService.nextButtons[0]!.selected = true;
      }
    } else if (this.nowOnboardingStep === this.onBoardingStep[1]) {
      if (this._userInfo!.address !== '') {
        this.buttonElementsService.nextButtons[0]!.selected = true;
      }
    } else if (this.nowOnboardingStep === this.onBoardingStep[2]) {
      if (this._userInfo!.interests.length > 0) {
        this.buttonElementsService.nextButtons[0]!.selected = true;
      }
    }
  }

  public getStringFromGenderEnum(value: number) {
    if (value === 0) {
      return 'M';
    } else if (value === 1) {
      return 'F';
    } else {
      return 'G';
    }
  }
  public onClickEditProfileImageButton() {}

  public async onClickNextButton(value: number) {
    if (this.buttonElementsService.nextButtons[0]!.selected) {
      const nextStep = this.nowOnboardingStep.value + 1;
      if (nextStep >= this.onBoardingStep.length) {
        this.buttonElementsService.interestSelectionButtons.every((element) => {
          element.selected = false;
        });

        await this._apiExecutorService.registerUser(this._userInfo);
        await this._router.navigate(['/web/main']);
      } else {
        if (this.nowOnboardingStep === this.onBoardingStep[0]) {
          this._userInfo.profilePictureUrl = this.profileImg!.imgSrc;
        }
        this.nowOnboardingStep = this.onBoardingStep[nextStep];
      }
      this.buttonElementsService.nextButtons[0]!.selected = false;
    }
  }
}
