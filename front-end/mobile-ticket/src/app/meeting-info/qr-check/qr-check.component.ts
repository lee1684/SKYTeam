import { Component, ElementRef, Input, ViewChild } from '@angular/core';
import { User } from '../../ticket/ticket.component';
import {
  CircleToggleStatusGroupComponent,
  StatusElement,
} from '../../ssalon-component/circle-toggle-status-group/circle-toggle-status-group.component';
import { NgIf } from '@angular/common';
import jsQR from 'jsqr';
import { ApiExecutorService } from '../../service/api-executor.service';

@Component({
  selector: 'app-qr-check',
  standalone: true,
  imports: [NgIf, CircleToggleStatusGroupComponent],
  templateUrl: './qr-check.component.html',
  styleUrl: './qr-check.component.scss',
})
export class QrCheckComponent {
  @ViewChild('barcodeContainer', { static: false })
  barcodeContainer: ElementRef | null = null;
  @ViewChild('qrVideo', { static: false })
  qrVideo: ElementRef<HTMLVideoElement> | null = null;
  @ViewChild('qrCanvas', { static: false })
  qrCanvas: ElementRef<HTMLCanvasElement> | null = null;
  @ViewChild('joiningUsersComponent', { static: false })
  joiningUsersComponent: CircleToggleStatusGroupComponent | null = null;

  @Input() moimId: string = '';
  public isDetectingQRCode: boolean = false;
  public isCameraLoaded: boolean = false;
  public qrStream: MediaStream | null = null;
  public qrCodeSrc: string = '';
  @Input() joiningUsers: StatusElement[] = [];
  public checkStatus: {
    checkStatus: boolean | null;
    checkingUser?: User;
    color: string;
    text: string;
  } = { checkStatus: null, color: '#006BFF', text: 'QR코드를 인식해주세요.' };
  constructor(private _apiExecutorService: ApiExecutorService) {}
  public ngOnInit() {}
  public async ngAfterViewChecked() {
    this.startQRCodeDetection();
  }
  public ngOnDestroy() {
    this.stopDetectQRCode();
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
                this.moimId,
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
}
