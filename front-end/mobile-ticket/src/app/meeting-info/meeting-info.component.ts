import { Component, Input, ViewChild } from '@angular/core';
import { TopNavigatorComponent } from '../ssalon-component/top-navigator/top-navigator.component';
import { ActivatedRoute, Router } from '@angular/router';
import { TabGroupComponent } from '../ssalon-component/tab-group/tab-group.component';
import {
  NewButtonElement,
  SimpleToggleGroupComponent,
} from '../ssalon-component/simple-toggle-group/simple-toggle-group.component';
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
import { trigger, state, style } from '@angular/animations';
import { ScenegraphService } from '../service/scenegraph.service';

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
    SimpleToggleGroupComponent,
  ],
  templateUrl: './meeting-info.component.html',
  styleUrl: './meeting-info.component.scss',
  animations: [
    /* 제어 패널 이동 애니메이션 */
    trigger('copiedAnim', [state('1', style({ top: '50px' }))]),
  ],
})
export class MeetingInfoComponent {
  @ViewChild('ticket', { static: false }) ticket: TicketComponent | null = null;

  @Input() nowTab: MeetingInfoTabEnum = MeetingInfoTabEnum.INFO;

  public moimId: string = '';
  public moimInfo: any = undefined as unknown as any;
  public participants: any = undefined as unknown as any;
  public ticketInfo: any = undefined as unknown as any;
  public joined: boolean = false;
  public meetingInfoTabEnum = MeetingInfoTabEnum;
  public tabs: NewButtonElement[] = [];
  public isCreator: boolean = false;
  public isParticipant: boolean = false;
  public joiningUsers: StatusElement[] = [];
  public isReviewCreated: boolean = false;

  public isCopyButtonClicked: boolean = false;
  constructor(
    private _route: ActivatedRoute,
    private _router: Router,
    private _apiExecutorService: ApiExecutorService,
    public buttonElementsService: ButtonElementsService,
    public sceneGraphService: ScenegraphService
  ) {
    this._route.queryParams.subscribe((params) => {
      this.moimId = params['moimId'];
    });
  }

  public async ngOnInit() {
    this.moimInfo = await this._apiExecutorService.getMoimInfo(this.moimId);
    this.participants = await this._apiExecutorService.getJoiningUsers(
      this.moimId
    );
    this.ticketInfo = await this._apiExecutorService.getTicket(this.moimId);
    await this.checkDiaryCreated();
    this.tabs = await this.getTabs();
    this.nowTab = this.tabs.find((tab) => tab.selected)!.value;
    this.changeButtonLabel(this.nowTab);
    let userInfos = await this._apiExecutorService.getJoiningUsers(this.moimId);
    userInfos.forEach((userInfo: any, index: number) => {
      this.joiningUsers.push({
        imgSrc: userInfo.profilePictureUrl,
        label: userInfo.nickname,
        value: userInfo.userId,
        status: userInfo.attendance,
      });
    });
    console.log(this.moimInfo);
  }

  public async onClickTab(tab: MeetingInfoTabEnum) {
    this.moimInfo = await this._apiExecutorService.getMoimInfo(this.moimId);
    this.ticketInfo = await this._apiExecutorService.getTicket(this.moimId);
    await this.checkDiaryCreated();
    let userInfos = await this._apiExecutorService.getJoiningUsers(this.moimId);
    userInfos.forEach((userInfo: any, index: number) => {
      this.joiningUsers.push({
        imgSrc: userInfo.profilePictureUrl,
        label: userInfo.nickname,
        value: userInfo.userId,
        status: userInfo.attendance,
      });
    });
    this.nowTab = tab;
    this.changeButtonLabel(this.nowTab);
  }

  public async checkDiaryCreated() {
    let result = await this._apiExecutorService.getMoimReview(this.moimId);
    if (result === false) {
      this.isReviewCreated = false;
    } else {
      this.isReviewCreated = true;
    }
  }

  public changeButtonLabel(tabEnum: MeetingInfoTabEnum) {
    if (tabEnum === MeetingInfoTabEnum.INFO) {
      if (this.isParticipant) {
        this.buttonElementsService.joinButtonElements[0].selected = false;
        this.buttonElementsService.joinButtonElements[0].label = this.isCreator
          ? '모임 정보 수정하기'
          : this.excededMaxParticipants()
          ? '참가 불가'
          : '참가 취소';
      } else {
        this.buttonElementsService.joinButtonElements[0].selected = true;
        this.buttonElementsService.joinButtonElements[0].label = '참가 하기';
      }
    } else if (tabEnum === MeetingInfoTabEnum.DIARY) {
      if (this.isReviewCreated) {
        this.buttonElementsService.joinButtonElements[0].selected = false;
        this.buttonElementsService.joinButtonElements[0].label =
          '모임 후기 수정하기';
      }
    }
  }

  public excededMaxParticipants(): boolean {
    return this.moimInfo.capacity <= this.participants.length;
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
  public onClickEditTicketButton($event: any) {
    if (this.nowTab === this.meetingInfoTabEnum.TICKET) {
      if ($event === 0) {
        this._router.navigate(['/web/ticket'], {
          queryParams: {
            moimId: this.moimId,
            viewType: 'edit',
            createTemplate: 'edit',
            face: 'front',
          },
        });
      } else {
        this._router.navigate(['/web/ticket'], {
          queryParams: {
            moimId: this.moimId,
            viewType: 'edit',
            createTemplate: 'edit',
            face: 'back',
          },
        });
      }
    }
  }
  public async onClickJoinButton() {
    if (
      this.buttonElementsService.joinButtonElements[0].selected &&
      !this.isParticipant &&
      !this.excededMaxParticipants()
    ) {
      if (this.moimInfo.payment) {
        let result = await this._apiExecutorService.payFee(this.moimId);
        function isMobile(): boolean {
          const userAgent = navigator.userAgent;
          return /android|avantgo|blackberry|bada|bb|meego|palm|ipad|playbook|plucker|series60|symbian|webos|windows phone|windows ce|ipod|iphone/i.test(
            userAgent
          );
        }
        if (isMobile()) {
          location.href = result.next_redirect_mobile_url;
        } else {
          location.href = result.next_redirect_pc_url;
        }
      } else {
        await this._apiExecutorService.joinMoim(this.moimId);
        location.reload();
      }
    } else {
      if (this.nowTab === this.meetingInfoTabEnum.INFO && this.isCreator) {
        this._router.navigate(['/web/meeting-edit'], {
          queryParams: {
            editType: 'moimInfo',
            moimId: this.moimId,
          },
        });
      } else if (
        this.nowTab === this.meetingInfoTabEnum.INFO &&
        this.isParticipant
      ) {
        let paymentInfo = await this._apiExecutorService.getPaymentinfo(
          this.moimId
        );
        if (paymentInfo) {
          if (
            await this._apiExecutorService.getRefund(
              this.moimId,
              paymentInfo.id
            )
          ) {
            await this._apiExecutorService.kickParticipant(
              this.moimId,
              this._apiExecutorService.myProfile.id,
              '참가 취소'
            );
            alert('환불이 완료되었습니다.');
          }
        } else {
          await this._apiExecutorService.kickParticipant(
            this.moimId,
            this._apiExecutorService.myProfile.id,
            '참가 취소'
          );
        }
        location.reload();
      } else if (this.nowTab === this.meetingInfoTabEnum.DIARY) {
        this._router.navigate(['/web/meeting-edit'], {
          queryParams: {
            editType: 'reviewInfo',
            moimId: this.moimId,
          },
        });
      }
    }
  }

  public async onClickShareButton() {
    await navigator.clipboard.writeText(
      `ssalon.co.kr/web/share?id=${this.moimId}&title=${this.moimInfo.title}`
    );
    console.log('Text copied to clipboard');
    this.isCopyButtonClicked = true;
    setTimeout(() => {
      this.isCopyButtonClicked = false;
    }, 2000);
  }

  public async onClickBackButton() {
    this._router.navigate(['/web/main']);
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

  public showButton(): boolean {
    return !(
      this.nowTab === this.meetingInfoTabEnum.QRCHECK ||
      this.nowTab === this.meetingInfoTabEnum.QRSHOW ||
      this.nowTab === this.meetingInfoTabEnum.CHATTING ||
      (this.nowTab === this.meetingInfoTabEnum.DIARY && !this.isReviewCreated)
    );
  }

  public isLoadedMoimInfo(): boolean {
    if (this.moimInfo === undefined) {
      return false;
    } else {
      return true;
    }
  }
}
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
  public moimInfo: any = undefined as unknown as any;
  public ticketInfo: any = undefined as unknown as any;
  public joined: boolean = false;
  public meetingInfoTabEnum = MeetingInfoTabEnum;
  public tabs: NewButtonElement[] = [];
  public nowTab: MeetingInfoTabEnum = MeetingInfoTabEnum.INFO;
  public isCreator: boolean = false;
  public isParticipant: boolean = false;
  public joiningUsers: StatusElement[] = [];
  public isReviewCreated: boolean = false;
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
    await this.checkDiaryCreated();
    this.tabs = await this.getTabs();
    this.nowTab = this.tabs.find((tab) => tab.selected)!.value;
    this.changeButtonLabel(this.nowTab);
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
    this.changeButtonLabel(this.nowTab);
  }

  public async checkDiaryCreated() {
    let result = await this._apiExecutorService.getMoimReview(this.moimId);
    if (result === false) {
      this.isReviewCreated = false;
    } else {
      this.isReviewCreated = true;
    }
  }

  public changeButtonLabel(tabEnum: MeetingInfoTabEnum) {
    if (tabEnum === MeetingInfoTabEnum.TICKET) {
      if (this.isParticipant && this.isCreator) {
        this.buttonElementsService.joinButtonElements[0].selected = false;
        this.buttonElementsService.joinButtonElements[0].label =
          '증표 앞면 수정하기';
      }
    } else if (tabEnum === MeetingInfoTabEnum.INFO) {
      if (this.isParticipant) {
        this.buttonElementsService.joinButtonElements[0].selected = false;
        this.buttonElementsService.joinButtonElements[0].label = this.isCreator
          ? '모임 정보 수정하기(개최자입니다)'
          : '참가 취소하기(참가자입니다)';
      } else {
        this.buttonElementsService.joinButtonElements[0].selected = true;
        this.buttonElementsService.joinButtonElements[0].label = '참가하기';
      }
    } else if (tabEnum === MeetingInfoTabEnum.DIARY) {
      if (this.isReviewCreated) {
        this.buttonElementsService.joinButtonElements[0].selected = false;
        this.buttonElementsService.joinButtonElements[0].label =
          '모임 후기 수정하기';
      }
    }
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
    } else {
      if (this.nowTab === this.meetingInfoTabEnum.TICKET && this.isCreator) {
        this._router.navigate(['/web/ticket'], {
          queryParams: {
            moimId: this.moimId,
            viewType: 'edit',
            createTemplate: 'edit',
            face: 'front',
          },
        });
      } else if (
        this.nowTab === this.meetingInfoTabEnum.INFO &&
        this.isCreator
      ) {
        this._router.navigate(['/web/meeting-create'], {
          queryParams: {
            moimId: this.moimId,
          },
        });
      }
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

  public showButton(): boolean {
    return !(
      this.nowTab === this.meetingInfoTabEnum.QRCHECK ||
      this.nowTab === this.meetingInfoTabEnum.QRSHOW ||
      this.nowTab === this.meetingInfoTabEnum.CHATTING ||
      (this.nowTab === this.meetingInfoTabEnum.DIARY && !this.isReviewCreated)
    );
  }

  public isLoadedMoimInfo(): boolean {
    if (this.moimInfo === undefined) {
      return false;
    } else {
      return true;
    }
  }
}
