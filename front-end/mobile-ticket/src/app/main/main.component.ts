import { Component, ElementRef, ViewChild } from '@angular/core';
import {
  NewButtonElement,
  SimpleToggleGroupComponent,
} from '../ssalon-component/simple-toggle-group/simple-toggle-group.component';
import { NgClass, NgFor, NgIf } from '@angular/common';
import { ImageRowContainerComponent } from '../ssalon-component/image-row-container/image-row-container.component';
import { Router } from '@angular/router';
import { ThumbnailMakerService } from '../service/thumbnail-maker.service';
import { BottomNavigatorComponent } from '../ssalon-component/bottom-navigator/bottom-navigator.component';
import { MoimListComponent } from './moim-list/moim-list.component';
import { MoimSearchComponent } from './moim-search/moim-search.component';
import { MyTicketListComponent } from './my-ticket-list/my-ticket-list.component';
import { ProfileComponent } from './profile/profile.component';
import { ApiExecutorService } from '../service/api-executor.service';

export enum MainTabEnum {
  MAIN,
  SEARCH,
  CREATE, // 누르는 즉시 navigate함.
  MINE,
  PROFILE,
}

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [
    NgClass,
    NgFor,
    NgIf,
    SimpleToggleGroupComponent,
    ImageRowContainerComponent,
    BottomNavigatorComponent,
    MoimListComponent,
    MoimSearchComponent,
    MyTicketListComponent,
    ProfileComponent,
  ],
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss',
})
export class MainComponent {
  public navigatorSelectionButtons = [
    { selected: true, value: MainTabEnum.MAIN, label: '메인' },
    { selected: false, value: MainTabEnum.SEARCH, label: '검색' },
    { selected: false, value: MainTabEnum.CREATE, label: '개최' },
    { selected: false, value: MainTabEnum.MINE, label: '증표' },
    { selected: false, value: MainTabEnum.PROFILE, label: '프로필' },
  ];
  public mainTabEnum = MainTabEnum;
  public nowTab: MainTabEnum = MainTabEnum.MAIN;
  constructor(
    private _router: Router,
    public thumbnailMakerService: ThumbnailMakerService,
    private _apiExecutorService: ApiExecutorService
  ) {}
  public async ngOnInit() {
    await this._apiExecutorService.getMyProfile();
  }

  public onClickNavigatorButton(value: MainTabEnum): void {
    this.nowTab = value;
    console.log(value);
    if (value === MainTabEnum.CREATE) {
      this._router.navigate(['/web/meeting-create']);
    }
  }
  public getNavigatorButtonClass(selected: boolean) {
    return selected ? 'navigator-button-selected' : 'navigator-button';
  }
}
