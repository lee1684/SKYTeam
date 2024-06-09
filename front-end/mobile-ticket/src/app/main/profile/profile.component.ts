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

  public async onClickProfileUpdate() {
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
  }

  public onClickRemoveAccount() {
    this.isPopUpBottomDialog = true;
  }

  public async removeAccount() {
    await this._apiExecutorService.removeAccount();
    this._router.navigate(['/']);
  }
}
