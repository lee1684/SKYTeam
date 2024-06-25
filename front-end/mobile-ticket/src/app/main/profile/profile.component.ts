import { Component, Input, ViewChild } from '@angular/core';
import { SimpleContentComponent } from '../../ssalon-component/simple-content/simple-content.component';
import {
  ApiExecutorService,
  Profile,
} from '../../service/api-executor.service';
import { ProfileImgComponent } from '../../ssalon-component/profile-img/profile-img.component';
import { TopNavigatorComponent } from '../../ssalon-component/top-navigator/top-navigator.component';
import { Router } from '@angular/router';
import { ProfileUpdateComponent } from './profile-update/profile-update.component';
import { NgIf } from '@angular/common';
import { SquareButtonComponent } from '../../ssalon-component/square-button/square-button.component';
import { ButtonElementsService } from '../../service/button-elements.service';
import {
  BottomDialogComponent,
  BottomDialogType,
} from '../../ssalon-component/bottom-dialog/bottom-dialog.component';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    ProfileImgComponent,
    SimpleContentComponent,
    TopNavigatorComponent,
    ProfileUpdateComponent,
    NgIf,
    SquareButtonComponent,
    BottomDialogComponent,
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
})
export class ProfileComponent {
  @ViewChild('profileUpdate', { static: false })
  profileUpdate: ProfileUpdateComponent | null = null;
  @Input() myProfile: Profile = undefined as unknown as Profile;

  public mode: 'edit' | 'info' = 'info';
  public bottomDialogType = BottomDialogType;
  public isPopUpBottomDialog = false;
  constructor(
    private _router: Router,
    private _apiExecutorService: ApiExecutorService,
    public buttonElementsService: ButtonElementsService
  ) {}
  public ngOnInit() {
    this.myProfile = this._apiExecutorService.myProfile;
  }
  public getGender(genderString: string) {
    if (genderString === 'M') {
      return '남자';
    } else if (genderString === 'F') {
      return '여자';
    } else {
      return '기타';
    }
  }

  public async onClickProfile(value: number) {
    if (value === 0) {
      if (this.mode === 'info') {
        this.buttonElementsService.editProfileButton[0].label = '수정완료';
        this.buttonElementsService.editProfileButton[0].selected = true;
        this.mode = 'edit';
      } else {
        await this.profileUpdate!.onClickUpdate();
        await this._apiExecutorService.getMyProfile();
        this.myProfile = this._apiExecutorService.myProfile;
        this.buttonElementsService.editProfileButton[0].label = '프로필 수정';
        this.buttonElementsService.editProfileButton[0].selected = false;
        this.mode = 'info';
      }
    } else {
      await this._apiExecutorService.logout();
      this.deleteCookie('access');
      this.deleteCookie('refresh');
      this.deleteCookie('JSESSIONID');
      this._router.navigate(['/']);
    }
  }

  public onClickRemoveAccount() {
    window.scrollTo(0, 0);
    this.isPopUpBottomDialog = true;
  }

  public onClickCSButton() {
    location.href = 'https://pf.kakao.com/_xjxatUG';
  }

  public async removeAccount(value: number) {
    if (value === 0) {
      this.isPopUpBottomDialog = false;
    } else {
      await this._apiExecutorService.removeAccount();
      this.deleteCookie('access');
      this.deleteCookie('refresh');
      this.deleteCookie('JSESSIONID');
      this._router.navigate(['/']);
    }
  }

  private deleteCookie(name: string) {
    document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
  }
}
import { Component, Input } from '@angular/core';
import { SimpleContentComponent } from '../../ssalon-component/simple-content/simple-content.component';
import {
  ApiExecutorService,
  Profile,
} from '../../service/api-executor.service';
import { ProfileImgComponent } from '../../ssalon-component/profile-img/profile-img.component';
import { TopNavigatorComponent } from '../../ssalon-component/top-navigator/top-navigator.component';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [ProfileImgComponent, SimpleContentComponent, TopNavigatorComponent],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
})
export class ProfileComponent {
  @Input() myProfile: Profile = undefined as unknown as Profile;
  constructor(private _apiExecutorService: ApiExecutorService) {}
  public ngOnInit() {
    this.myProfile = this._apiExecutorService.myProfile;
  }
  public getGender(genderString: string) {
    if (genderString === 'M') {
      return '남자';
    } else if (genderString === 'F') {
      return '여자';
    } else {
      return '기타';
    }
  }
}
