import { Component } from '@angular/core';
import { TopNavigatorComponent } from '../ssalon-component/top-navigator/top-navigator.component';
import { ActivatedRoute, Router } from '@angular/router';
import { TabGroupComponent } from '../ssalon-component/tab-group/tab-group.component';
import { NewButtonElement } from '../ssalon-component/simple-toggle-group/simple-toggle-group.component';
import { NgIf } from '@angular/common';
import { SimpleContentComponent } from '../ssalon-component/simple-content/simple-content.component';
import { MoimInfoComponent } from './moim-info/moim-info.component';
import { ChattingComponent } from './chatting/chatting.component';
import { ApiExecutorService } from '../service/api-executor.service';
import { SquareButtonComponent } from '../ssalon-component/square-button/square-button.component';

export enum MeetingInfoTabEnum {
  TICKET,
  INFO,
  CHATTING,
  REVIEW,
}
@Component({
  selector: 'app-meeting-info',
  standalone: true,
  imports: [
    NgIf,
    TopNavigatorComponent,
    TabGroupComponent,
    SimpleContentComponent,
    MoimInfoComponent,
    ChattingComponent,
    SquareButtonComponent,
  ],
  templateUrl: './meeting-info.component.html',
  styleUrl: './meeting-info.component.scss',
})
export class MeetingInfoComponent {
  public moimId: string = '';
  public moimInfo: any = {};
  public joined: boolean = false;
  public meetingInfoTabEnum = MeetingInfoTabEnum;
  public tabs: NewButtonElement[] = this.getTabs();
  public nowTab: number = this.tabs.find((tab) => tab.selected)!.value;
  public joinButtonElements: NewButtonElement[] = [
    {
      selected: true,
      value: 0,
      label: '참가하기',
    },
  ];
  constructor(
    private _route: ActivatedRoute,
    private _router: Router,
    private _apiExecutorService: ApiExecutorService
  ) {
    this._route.queryParams.subscribe((params) => {
      this.moimId = params['moimId'];
    });
  }

  public async ngOnInit() {
    this.moimInfo = await this._apiExecutorService.getMoimInfo(this.moimId);
    if (await this._apiExecutorService.checkParticipant(this.moimId)) {
      this.joinButtonElements[0].selected = false;
      this.joinButtonElements[0].label = '참가자입니다.';
    }
  }

  public getTabs(): NewButtonElement[] {
    return [
      { selected: true, value: 0, label: '증표 보기' },
      { selected: false, value: 1, label: '모임 정보' },
    ];
  }

  public onClickTicket() {
    this._router.navigate(['/web/ticket'], {
      queryParams: { moimId: this.moimId, viewType: 'view' },
    });
  }
}
