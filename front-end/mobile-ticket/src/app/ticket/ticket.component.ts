import { MobileTicketViewerComponent } from './mobile-ticket-viewer/mobile-ticket-viewer.component';
import {
  MobileTicketEditMode,
  MobileTicketEditorComponent,
} from './mobile-ticket-editor/mobile-ticket-editor.component';
import { MobileTicketEditViewerComponent } from './mobile-ticket-edit-viewer/mobile-ticket-edit-viewer.component';
import { NgIf } from '@angular/common';
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

export enum MobileTicketViewMode {
  APPVIEW,
  APPEDITVIEW,
  WEBVIEW,
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
  public people: string = '';

  constructor(
    private _apiExecutorService: ApiExecutorService,
    private _ssalonConfigService: SsalonConfigService
  ) {}
  public ngOnInit(): void {}
  public ngAfterViewInit(): void {
    this.setFirstPage();
  }
  public ngAfterViewChecked(): void {
    this.startQRCodeDetection();
  }

  public setFirstPage() {
    if (this._ssalonConfigService.VIEW_TYPE === 'edit') {
      this.changeViewMode(MobileTicketViewMode.APPEDITVIEW);
    } else if (this._ssalonConfigService.VIEW_TYPE === 'view') {
      this.onClickPreviewButton();
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
        console.log('1');
        this.qrVideo.nativeElement.srcObject = this.qrStream;
        console.log('2');
        this.qrVideo.nativeElement.play();
        console.log('3');
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
            let a = await this._apiExecutorService.checkQR(code.data);
            //let a = { data: true };
            this.people = a.data === true ? '참가자' : '마피아';
            //this.stopDetectQRCode();
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
    this.isDetectingQRCode = !this.isDetectingQRCode;
    if (!this.isDetectingQRCode) {
      this.stopDetectQRCode();
    }
  }
  public changeEditMode(mode: MobileTicketEditMode) {
    this.editMode = mode;
  }

  public changeViewMode(mode: MobileTicketViewMode): void {
    if (mode === MobileTicketViewMode.APPVIEW) {
      this.updateServer();
    }
    this.stopDetectQRCode();
    this.mode = mode;
  }

  public addFabricObject(object: any) {
    console.log(object);
  }

  public onClickBackButton() {
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
    let a = qrcode(0, 'L');
    a.addData(await this._apiExecutorService.getBarcode());
    //a.addData('https://www.naver.com');
    a.make();
    this.qrCodeSrc = a.createDataURL(2, 0);
    this.changeViewMode(MobileTicketViewMode.APPVIEW);
    this.generateQRCode();
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
      /* 서버에 저장 API 연결해야함. */
      await this._apiExecutorService.editTicket(body);
    }
  }

  public openTextEditor(IText: FabricText) {
    (this.mobileTicketEditor!.fabricObjects as FabricText[]).push(IText);
    this.mobileTicketEditor!.syncTextAttributeWithSelectedText();
    this.mobileTicketEditor!.onClickChangeEditMode(MobileTicketEditMode.TEXT);
  }

  public async generateQRCode() {}
}
