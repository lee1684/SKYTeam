import { ButtonElementsService } from './../../service/button-elements.service';
import { NewButtonElement } from './../../ssalon-component/simple-toggle-group/simple-toggle-group.component';
import { Component, Input } from '@angular/core';
import { SimpleContentComponent } from '../../ssalon-component/simple-content/simple-content.component';
import {
  ApiExecutorService,
  Profile,
} from '../../service/api-executor.service';
import { ProfileImgComponent } from '../../ssalon-component/profile-img/profile-img.component';
import { TopNavigatorComponent } from '../../ssalon-component/top-navigator/top-navigator.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [ProfileImgComponent, SimpleContentComponent, TopNavigatorComponent],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
})
export class ProfileComponent {
  @Input() myProfile: Profile = undefined as unknown as Profile;

  public  interestImages: NewButtonElement[] = [];

  constructor(
    private _router: Router,
    private _apiExecutorService: ApiExecutorService,
    private buttonElementsService: ButtonElementsService,
  ) {}
  public ngOnInit() {
    this.myProfile = this._apiExecutorService.myProfile;
    this.interestImages = this.myProfile.interests.map(interest => {
      const categoryIndex = this.buttonElementsService.category.indexOf(interest);
      const categoryEnglish = this.buttonElementsService.categoryEnglish[categoryIndex];
      return {
        label: interest,
        value: categoryIndex,
        imgSrc: `assets/interest-icons/${categoryEnglish}.png`,
        selected: false,
      };
    })
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

  public onClickProfileUpdate(): void {
    this._router.navigate(['/web/profile-update']);
  }
}
