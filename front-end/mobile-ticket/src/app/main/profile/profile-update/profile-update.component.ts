import { Component, ViewChild } from '@angular/core';
import { RegisterUserInfo } from '../../../onboarding/onboarding.component';
import { Router } from '@angular/router';
import { ButtonElementsService } from '../../../service/button-elements.service';
import { ApiExecutorService } from '../../../service/api-executor.service';
import { NgIf } from '@angular/common';
import { SimpleButtonComponent } from '../../../ssalon-component/simple-button/simple-button.component';
import { SimpleInputComponent } from '../../../ssalon-component/simple-input/simple-input.component';
import { ProfileImgComponent } from '../../../ssalon-component/profile-img/profile-img.component';
import { SimpleToggleGroupComponent } from '../../../ssalon-component/simple-toggle-group/simple-toggle-group.component';
import { SquareButtonComponent } from '../../../ssalon-component/square-button/square-button.component';

@Component({
  selector: 'app-profile-update',
  standalone: true,
  imports: [
    NgIf,
    SimpleButtonComponent,
    SimpleInputComponent,
    ProfileImgComponent,
    SimpleToggleGroupComponent,
    SquareButtonComponent,
  ],
  templateUrl: './profile-update.component.html',
  styleUrl: './profile-update.component.scss'
})
export class ProfileUpdateComponent {
  @ViewChild('profileImg', { static: false })
  profileImg: ProfileImgComponent | null = null;

  private _userInfo: RegisterUserInfo = {
    nickname: '',
    profilePictureUrl: '',
    gender: 'M',
    address: '',
    introduction: '',
    interests: [],
  };

  private genderMap = {
    'M': 0,
    'F': 1,
    'G': 2,
  }

  constructor(
    private _router: Router,
    private _apiExecutorService: ApiExecutorService,
    public buttonElementsService: ButtonElementsService
  ) {}

  public get userInfo(): RegisterUserInfo {
    return this._userInfo;
  }

  public async ngOnInit() {
    this._userInfo = await this._apiExecutorService.getProfile();
    const genderSelectionButton = this.buttonElementsService.genderSelectionButtons
      .find(genderSelectionButton => genderSelectionButton.value === this.genderMap[this._userInfo.gender]);
    genderSelectionButton!.selected = true;

    const locationSelectionButton = this.buttonElementsService.locationSelectionButtons
      .find(locationSelectionButton => locationSelectionButton.label === this._userInfo.address);
    locationSelectionButton!.selected = true;

    this.buttonElementsService.interestSelectionButtons.forEach(interestSelectionButton => {
      this._userInfo.interests.forEach(interest => {
        if (interestSelectionButton.label === interest) {
          interestSelectionButton.selected = true;
        }
      });
    });
  }

  public onChangeUserInfo(type: string, value: string): void {
    switch (type) {
      case 'nickname':
        this._userInfo!.nickname = value;
        break;
      case 'introduction':
        this._userInfo!.introduction = value;
        break;
      case 'gender':
        const index = this.buttonElementsService.genderSelectionButtons.find((button) => {
          return button.selected === true;
        })!.value;
        this._userInfo!.gender = this.getStringFromGenderEnum(index);
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

  public async onClickUpdate() {
    this._apiExecutorService.updateMyProfile(this._userInfo)
      .then(() => this._router.navigate(['/web/main']));
  }
}
