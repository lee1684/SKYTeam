import { MobileTicketViewerComponent } from './mobile-ticket-viewer/mobile-ticket-viewer.component';
import {
  MobileTicketEditMode,
  MobileTicketEditorComponent,
} from './mobile-ticket-editor/mobile-ticket-editor.component';
import { MobileTicketEditViewerComponent } from './mobile-ticket-edit-viewer/mobile-ticket-edit-viewer.component';
import { NgIf, Location } from '@angular/common';
import { SimpleToggleButtonGroupComponent } from '../ssalon-component/simple-toggle-button-group/simple-toggle-button-group.component';
import { SimpleButtonComponent } from '../ssalon-component/simple-button/simple-button.component';
import {
  ButtonElement,
  CircleToggleButtonGroupComponent,
} from '../ssalon-component/circle-toggle-button-group/circle-toggle-button-group.component';
import {
  DecorationInfo,
  SsalonConfigService,
} from '../service/ssalon-config.service';
import { ApiExecutorService } from '../service/api-executor.service';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { FabricImage, FabricText, Path } from 'fabric';
import jsQR from 'jsqr';
import qrcode from 'qrcode-generator';
import { ScenegraphService } from '../service/scenegraph.service';
import {
  CircleToggleStatusGroupComponent,
  StatusElement,
} from '../ssalon-component/circle-toggle-status-group/circle-toggle-status-group.component';
import { ActivatedRoute } from '@angular/router';

export enum MobileTicketViewMode {
  APPVIEW,
  APPEDITVIEW,
  WEBVIEW,
  QRCHECKVIEW,
  QRSHOWVIEW,
}

export interface User {
  profilePictureUrl: string;
  nickname: string;
  attendance: boolean;
  userId: number;
}

@Component({
  selector: 'app-ticket',
  standalone: true,
  imports: [
    MobileTicketViewerComponent,
    MobileTicketEditorComponent,
    MobileTicketEditViewerComponent,
    SimpleToggleButtonGroupComponent,
    CircleToggleButtonGroupComponent,
    CircleToggleStatusGroupComponent,
    SimpleButtonComponent,
    NgIf,
  ],
  templateUrl: './ticket.component.html',
  styleUrl: './ticket.component.scss',
})
export class TicketComponent {
  @ViewChild('mobileTicketEditViewer', { static: false })
  mobileTicketEditViewer: MobileTicketEditViewerComponent | null = null;
  @ViewChild('mobileTicketEditor', { static: false })
  mobileTicketEditor: MobileTicketEditorComponent | null = null;
  @ViewChild('barcodeContainer', { static: false })
  barcodeContainer: ElementRef | null = null;
  @ViewChild('qrVideo', { static: false })
  qrVideo: ElementRef<HTMLVideoElement> | null = null;
  @ViewChild('qrCanvas', { static: false })
  qrCanvas: ElementRef<HTMLCanvasElement> | null = null;
  @ViewChild('joiningUsersComponent', { static: false })
  joiningUsersComponent: CircleToggleStatusGroupComponent | null = null;

  public mobileTicketViewMode = MobileTicketViewMode;
  public mode: MobileTicketViewMode = MobileTicketViewMode.APPEDITVIEW;

  public mobileTicketEditMode = MobileTicketEditMode;
  public editMode: MobileTicketEditMode = MobileTicketEditMode.NONE;
  public goBackButtonElement: ButtonElement = {
    imgSrc: 'assets/icons/go-back.png',
    label: '뒤로가기',
    value: 0,
  };

  public joinButtonElement: ButtonElement = {
    imgSrc: 'assets/icons/view.png',
    label: '참여하기',
    value: 0,
  };

  public isDetectingQRCode: boolean = false;
  public isCameraLoaded: boolean = false;
  public qrStream: MediaStream | null = null;
  public qrCodeSrc: string = '';
  public joiningUsers: StatusElement[] = [];
  public checkStatus: {
    checkStatus: boolean | null;
    checkingUser?: User;
    color: string;
    text: string;
  } = { checkStatus: null, color: '#006BFF', text: 'QR코드를 인식해주세요.' };

  public moimId: string = '';
  public viewType: string = '';
  constructor(
    private _apiExecutorService: ApiExecutorService,
    private _ssalonConfigService: SsalonConfigService,
    private _sceneGraphService: ScenegraphService,
    private _route: ActivatedRoute,
    private _location: Location
  ) {
    this._route.queryParams.subscribe((params) => {
      this.moimId = params['moimId'];
      this.viewType = params['viewType'];
    });
  }
  public ngOnInit(): void {}
  public async ngAfterViewInit() {
    this.setFirstPage();
  }
  public async ngAfterViewChecked() {
    this.startQRCodeDetection();
  }

  public setFirstPage() {
    if (this._ssalonConfigService.VIEW_TYPE === 'edit') {
      this.changeViewMode(MobileTicketViewMode.APPEDITVIEW);
    } else if (this._ssalonConfigService.VIEW_TYPE === 'view') {
      this.changeViewMode(MobileTicketViewMode.APPVIEW);
    } else if (this._ssalonConfigService.VIEW_TYPE === 'qrcheck') {
      // 만약에 본인이 개최자라면
      this.changeViewMode(MobileTicketViewMode.QRCHECKVIEW);
    } else if (this._ssalonConfigService.VIEW_TYPE === 'qrshow') {
      // 만약에 본인이 참가자라면
      this.changeViewMode(MobileTicketViewMode.QRSHOWVIEW);
    } else {
      this.changeViewMode(MobileTicketViewMode.WEBVIEW);
    }
  }

  public async startQRCodeDetection() {
    if (this.qrVideo && this.qrCanvas && !this.isCameraLoaded) {
      try {
        this.isCameraLoaded = true;
        this.qrCanvas.nativeElement.setAttribute('willReadFrequently', 'true');
        this.qrStream = await navigator.mediaDevices.getUserMedia({
          video: { facingMode: 'environment' },
        });
        this.qrVideo.nativeElement.srcObject = this.qrStream;
        this.qrVideo.nativeElement.play();
        this.detectQRCode();
      } catch (error) {
        console.error(error);
      }
    }
  }
  public async detectQRCode() {
    if (this.qrVideo && this.qrCanvas) {
      while (true) {
        if (
          this.qrVideo.nativeElement.readyState ===
          this.qrVideo.nativeElement.HAVE_ENOUGH_DATA
        ) {
          const canvasContext = this.qrCanvas.nativeElement.getContext('2d');
          this.qrCanvas.nativeElement.height =
            this.qrVideo.nativeElement.videoHeight;
          this.qrCanvas.nativeElement.width =
            this.qrVideo.nativeElement.videoWidth;
          canvasContext?.drawImage(
            this.qrVideo?.nativeElement,
            0,
            0,
            this.qrCanvas.nativeElement?.width!,
            this.qrCanvas.nativeElement?.height!
          );
          const imageData = canvasContext?.getImageData(
            0,
            0,
            this.qrCanvas.nativeElement?.width!,
            this.qrCanvas.nativeElement?.height!
          );
          const code = jsQR(
            imageData!.data,
            imageData!.width,
            imageData!.height
          );
          if (code) {
            try {
              let joinUserInfo: User = await this._apiExecutorService.checkQR(
                this._ssalonConfigService.MOIM_ID,
                code.data
              );
              this.checkStatus = {
                checkStatus: joinUserInfo.attendance,
                checkingUser: {
                  nickname: joinUserInfo.nickname,
                  profilePictureUrl: joinUserInfo.profilePictureUrl,
                  attendance: joinUserInfo.attendance,
                  userId: joinUserInfo.userId,
                },
                color: '#4DAF50',
                text: '환영합니다.',
              };
              //현재 QR코드 찍은 참가자가 출석체크가 되어있지 않다면 출석체크로 변경
              if (this.checkStatus.checkingUser!.attendance) {
                this.joiningUsersComponent?.changeStatus(
                  this.checkStatus.checkingUser!.userId,
                  true
                );
              }
            } catch (e) {
              this.checkStatus = {
                checkStatus: false,
                color: '#F44336',
                text: '참가자가 아닙니다.',
              };
            }
            await new Promise((resolve) => setTimeout(resolve, 1000)); // 100ms 대기
            this.checkStatus = {
              checkStatus: null,
              color: '#006BFF',
              text: 'QR코드를 인식해주세요.',
            };
            //let a = { data: true };
          }
        }
        await new Promise((resolve) => setTimeout(resolve, 100)); // 100ms 대기
      }
    }
  }
  public stopDetectQRCode() {
    if (this.qrStream) {
      this.qrStream.getTracks().forEach((track) => {
        track.stop();
      });
      this.isDetectingQRCode = false;
      this.isCameraLoaded = false;
    }
  }

  public onClickQRCodeButton() {
    if (!this.isDetectingQRCode) {
      this.changeViewMode(MobileTicketViewMode.QRCHECKVIEW);
    } else {
      this.stopDetectQRCode();
    }
    this.isDetectingQRCode = !this.isDetectingQRCode;
  }

  public changeEditMode(mode: MobileTicketEditMode) {
    this.editMode = mode;
  }

  public async changeViewMode(mode: MobileTicketViewMode) {
    if (mode === MobileTicketViewMode.APPVIEW) {
      this.updateServer();
    } else if (mode === MobileTicketViewMode.QRSHOWVIEW) {
      await this.setQrCodeImgSrc();
      this._sceneGraphService.mobileTicketAutoRotate = true;
    } else if (mode === MobileTicketViewMode.QRCHECKVIEW) {
      let userInfos = await this._apiExecutorService.getJoiningUsers(
        this._ssalonConfigService.MOIM_ID
      );
      userInfos.forEach((userInfo: any, index: number) => {
        this.joiningUsers.push({
          imgSrc: userInfo.profilePictureUrl,
          label: userInfo.nickname,
          value: userInfo.userId,
          status: userInfo.attendance,
        });
      });
    }

    this.stopDetectQRCode();
    this.mode = mode;
  }

  public addFabricObject(object: any) {}

  public onClickBackButton() {
    if (this.mode === MobileTicketViewMode.APPVIEW) {
      this._location.back();
    }
    this.changeViewMode(MobileTicketViewMode.APPEDITVIEW);
  }

  public applyEdit(
    fabricObjects: FabricImage[] | FabricText[] | Path[] | null
  ) {
    this.mobileTicketEditViewer?.updateCanvas(fabricObjects);
  }

  public applyBackgroundColorEdit(color: string) {
    this.mobileTicketEditViewer?.updateBackgroundColor(color);
  }

  public async onClickPreviewButton() {
    await this.updateServer();
    this.changeViewMode(MobileTicketViewMode.APPVIEW);
  }

  public async setQrCodeImgSrc() {
    let a = qrcode(0, 'L');
    a.addData(await this._apiExecutorService.getBarcode());
    //a.addData('https://www.naver.com');
    a.make();
    this.qrCodeSrc = a.createDataURL(4, 0);
  }

  public async updateServer() {
    if (
      this.mobileTicketEditViewer !== null &&
      this.mobileTicketEditor !== null
    ) {
      let body: FormData = new FormData();

      /** png로 변환해서 서버에 올려야함. */
      let imageDataURL = this.mobileTicketEditViewer.getCanvasCapture();
      function dataURItoBlob(dataURI: string) {
        var byteString = atob(dataURI.split(',')[1]);
        var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];
        var ab = new ArrayBuffer(byteString.length);
        var ia = new Uint8Array(ab);
        for (var i = 0; i < byteString.length; i++) {
          ia[i] = byteString.charCodeAt(i);
        }
        return new Blob([ab], { type: mimeString });
      }
      let json: DecorationInfo = {
        thumbnailUrl: 'image.png',
        backgroundColor: this.mobileTicketEditor!.backgroundColor.color,
        fabric: this.mobileTicketEditViewer!.canvas?.toJSON(),
      };
      body.append('json', JSON.stringify(json));
      body.append('files', dataURItoBlob(imageDataURL), 'image.png');
      console.log(dataURItoBlob(imageDataURL));
      /* 서버에 저장 API 연결해야함. */
      await this._apiExecutorService.editTicket(body);
    }
  }

  public openTextEditor(IText: FabricText) {
    (this.mobileTicketEditor!.fabricObjects as FabricText[]).push(IText);
    this.mobileTicketEditor!.syncTextAttributeWithSelectedText();
    this.mobileTicketEditor!.onClickChangeEditMode(MobileTicketEditMode.TEXT);
  }
}
