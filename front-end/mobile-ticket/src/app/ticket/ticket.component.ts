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
import {
  ApiExecutorService,
  ImageGeneration,
} from '../service/api-executor.service';
import { Component, ElementRef, Input, ViewChild } from '@angular/core';
import { FabricImage, FabricText, Path } from 'fabric';
import { ScenegraphService } from '../service/scenegraph.service';
import {
  CircleToggleStatusGroupComponent,
  StatusElement,
} from '../ssalon-component/circle-toggle-status-group/circle-toggle-status-group.component';
import { ActivatedRoute, Router } from '@angular/router';
import { TopNavigatorComponent } from '../ssalon-component/top-navigator/top-navigator.component';
import { NewButtonElement } from '../ssalon-component/simple-toggle-group/simple-toggle-group.component';
import { SimpleInputComponent } from '../ssalon-component/simple-input/simple-input.component';

export enum MobileTicketViewMode {
  APPVIEW,
  APPEDITVIEW,
  WEBVIEW,
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
    SimpleInputComponent,
    NgIf,
    TopNavigatorComponent,
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

  public topNavigatorButton: NewButtonElement = {
    selected: false,
    label: '저장 및 미리보기',
    imgSrc: 'assets/icons/go-back.png',
    value: 0,
  };

  public quitFeatureButton: NewButtonElement = {
    selected: false,
    label: '생성 완료',
    value: 0,
  };

  public mobileTicketViewMode = MobileTicketViewMode;
  public mode: MobileTicketViewMode = MobileTicketViewMode.APPEDITVIEW;

  public mobileTicketEditMode = MobileTicketEditMode;
  public editMode: MobileTicketEditMode = MobileTicketEditMode.NONE;
  public goBackButtonElement: ButtonElement = {
    imgSrc: 'assets/icons/go-back.png',
    label: '뒤로가기',
    value: 0,
  };

  public joinButtonElement: NewButtonElement = {
    selected: false,
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
  private prompt: string = '';
  private tempImg: any;
  public imageUrl: string = '';

  @Input() public moimId: string = undefined as unknown as string;
  @Input() public viewType: string = undefined as unknown as string;
  @Input() public createTemplate: string = undefined as unknown as string;
  @Input() public face: string = 'front';
  public isFromUrl: boolean = false;
  constructor(
    private _apiExecutorService: ApiExecutorService,
    private _ssalonConfigService: SsalonConfigService,
    private _sceneGraphService: ScenegraphService,
    private _route: ActivatedRoute,
    private _router: Router,
    private _location: Location
  ) {}
  public ngOnInit(): void {
    if (this.viewType === undefined) {
      this._route.queryParams.subscribe((params) => {
        this.moimId = params['moimId'];
        this.viewType = params['viewType'];
        this.createTemplate = params['createTemplate'];
        this.face = params['face'];
        this.isFromUrl = true;
        this.setFirstPage();
      });
    } else {
      this.isFromUrl = false;
      this.setFirstPage();
    }
  }

  public ngOnDestroy(): void {
    this._sceneGraphService.destroy();
  }

  public onChangeInput(prompt: string): void {
    this.prompt = prompt;
  }

  public setFirstPage() {
    if (this.viewType === 'edit') {
      this._sceneGraphService.mobileTicketAutoRotate = false;
      this.changeViewMode(MobileTicketViewMode.APPEDITVIEW);
    } else if (this.viewType === 'view') {
      this._sceneGraphService.mobileTicketAutoRotate = true;
      this.changeViewMode(MobileTicketViewMode.APPVIEW);
    } else {
      this._sceneGraphService.mobileTicketAutoRotate = true;
      this.changeViewMode(MobileTicketViewMode.WEBVIEW);
    }
  }

  public changeEditMode(mode: MobileTicketEditMode) {
    this.editMode = mode;
  }

  public async changeViewMode(mode: MobileTicketViewMode) {
    this.mode = mode;
  }

  public onClickBackButton() {
    /** 뷰 모드이고, 주소로 들어갔을 때 -> 수정모드에서 프리뷰 모드로 간 것. */
    if (this.mode === MobileTicketViewMode.APPVIEW && this.isFromUrl === true) {
      this.changeViewMode(MobileTicketViewMode.APPEDITVIEW);
    }
  }

  public getBackButtonVisibility(): boolean {
    if (!this.isFromUrl && this.mode === MobileTicketViewMode.APPVIEW) {
      return false;
    } else if (this.mode === MobileTicketViewMode.APPEDITVIEW) {
      return false;
    } else {
      return true;
    }
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
    this.createTemplate = 'edit';
    this.changeViewMode(MobileTicketViewMode.APPVIEW);
  }

  public async onClickQuitButton() {
    this._router.navigate(['/web/meeting-info'], {
      queryParams: { moimId: this.moimId },
    });
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
      if (this.face === 'front') {
        await this._apiExecutorService.editTicket(this.moimId, body);
      } else {
        await this._apiExecutorService.editDiary(this.moimId, body);
      }
    }
  }

  public openTextEditor(IText: FabricText) {
    (this.mobileTicketEditor!.fabricObjects as FabricText[]).push(IText);
    this.mobileTicketEditor!.syncTextAttributeWithSelectedText();
    this.mobileTicketEditor!.onClickChangeEditMode(MobileTicketEditMode.TEXT);
  }
}
