import { Component, ViewChild } from '@angular/core';
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
import { ButtonElementsService } from '../service/button-elements.service';
import {
  MobileTicketViewMode,
  TicketComponent,
} from '../ticket/ticket.component';
import { SsalonConfigService } from '../service/ssalon-config.service';
import { QrCheckComponent } from './qr-check/qr-check.component';
import { StatusElement } from '../ssalon-component/circle-toggle-status-group/circle-toggle-status-group.component';
import { QrShowComponent } from './qr-show/qr-show.component';
import { MobileTicketViewerComponent } from '../ticket/mobile-ticket-viewer/mobile-ticket-viewer.component';
import { MoimReviewComponent } from './moim-review/moim-review.component';

export enum MeetingInfoTabEnum {
  TICKET,
  INFO,
  CHATTING,
  DIARY,
  QRCHECK,
  QRSHOW,
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
    QrCheckComponent,
    QrShowComponent,
    TicketComponent,
    MoimReviewComponent,
  ],
  templateUrl: './meeting-info.component.html',
  styleUrl: './meeting-info.component.scss',
})
export class MeetingInfoComponent {
  @ViewChild('ticket', { static: false }) ticket: TicketComponent | null = null;

  public moimId: string = '';
  public moimInfo: any = {};
  public ticketInfo: any = {};
  public joined: boolean = false;
  public meetingInfoTabEnum = MeetingInfoTabEnum;
  public tabs: NewButtonElement[] = [];
  public nowTab: MeetingInfoTabEnum = MeetingInfoTabEnum.INFO;
  public isCreator: boolean = false;
  public isParticipant: boolean = false;
  public joiningUsers: StatusElement[] = [];
  constructor(
    private _route: ActivatedRoute,
    private _router: Router,
    private _apiExecutorService: ApiExecutorService,
    public buttonElementsService: ButtonElementsService
  ) {
    this._route.queryParams.subscribe((params) => {
      this.moimId = params['moimId'];
    });
  }

  public async ngOnInit() {
    this.moimInfo = await this._apiExecutorService.getMoimInfo(this.moimId);
    this.ticketInfo = await this._apiExecutorService.getTicket(this.moimId);
    this.tabs = await this.getTabs();
    this.nowTab = this.tabs.find((tab) => tab.selected)!.value;
    if (this.isParticipant) {
      this.buttonElementsService.joinButtonElements[0].selected = false;
      this.buttonElementsService.joinButtonElements[0].label = this.isCreator
        ? '정보 및 증표 수정하기(개최자입니다)'
        : '참가자입니다.';
    } else {
      this.buttonElementsService.joinButtonElements[0].selected = true;
      this.buttonElementsService.joinButtonElements[0].label = '참가하기';
    }
    let userInfos = await this._apiExecutorService.getJoiningUsers(this.moimId);
    userInfos.forEach((userInfo: any, index: number) => {
      this.joiningUsers.push({
        imgSrc: userInfo.profilePictureUrl,
        label: userInfo.nickname,
        value: userInfo.userId,
        status: userInfo.attendance,
      });
    });
  }

  public onClickTab(tab: MeetingInfoTabEnum) {
    this.nowTab = tab;
  }

  public async getTabs(): Promise<NewButtonElement[]> {
    this.isCreator = await this._apiExecutorService.checkCreator(this.moimId);
    this.isParticipant = await this._apiExecutorService.checkParticipant(
      this.moimId
    );
    if (this.isCreator) {
      return [
        {
          selected: false,
          value: MeetingInfoTabEnum.QRCHECK,
          label: 'QR검증',
        },
        {
          selected: false,
          value: MeetingInfoTabEnum.TICKET,
          label: '증표 보기',
        },
        { selected: true, value: MeetingInfoTabEnum.INFO, label: '모임 정보' },
        {
          selected: false,
          value: MeetingInfoTabEnum.CHATTING,
          label: '채팅',
        },
        {
          selected: false,
          value: MeetingInfoTabEnum.DIARY,
          label: '모임 후기',
        },
      ];
    } else if (this.isParticipant) {
      return [
        {
          selected: false,
          value: MeetingInfoTabEnum.QRSHOW,
          label: 'QR',
        },
        {
          selected: false,
          value: MeetingInfoTabEnum.TICKET,
          label: '증표 보기',
        },
        { selected: true, value: MeetingInfoTabEnum.INFO, label: '모임 정보' },
        {
          selected: false,
          value: MeetingInfoTabEnum.CHATTING,
          label: '채팅',
        },
        {
          selected: false,
          value: MeetingInfoTabEnum.DIARY,
          label: '모임 후기',
        },
      ];
    } else {
      return [
        {
          selected: false,
          value: MeetingInfoTabEnum.TICKET,
          label: '증표 보기',
        },
        { selected: true, value: MeetingInfoTabEnum.INFO, label: '모임 정보' },
      ];
    }
  }

  public onClickTicket() {
    this._router.navigate(['/web/ticket'], {
      queryParams: { moimId: this.moimId, viewType: 'view' },
    });
  }

  public async onClickJoinButton() {
    if (this.buttonElementsService.joinButtonElements[0].selected) {
      await this._apiExecutorService.joinMoim(this.moimId);
      location.reload();
      //let currentUrl = this._router.url;
      //this._router.navigate([`/web/meeting-info`], {
      //  queryParams: {
      //    moimId: this.moimId,
      //  },
      //});
    }
  }

  public getThumbSrc(moimId: string): string {
    return (
      'https://test-bukkit-240415.s3.ap-northeast-2.amazonaws.com/Thumbnails/' +
      moimId +
      '/Thumb-' +
      moimId +
      '.png'
    );
  }
}
