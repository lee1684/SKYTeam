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
