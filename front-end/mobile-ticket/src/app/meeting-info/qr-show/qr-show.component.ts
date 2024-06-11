import { Component, Input } from '@angular/core';
import { MobileTicketViewerComponent } from '../../ticket/mobile-ticket-viewer/mobile-ticket-viewer.component';
import { NgIf } from '@angular/common';
import { ScenegraphService } from '../../service/scenegraph.service';
import { ApiExecutorService } from '../../service/api-executor.service';
import qrcode from 'qrcode-generator';
import {
  DomSanitizer,
  SafeResourceUrl,
  SafeUrl,
} from '@angular/platform-browser';
import { TicketComponent } from '../../ticket/ticket.component';

@Component({
  selector: 'app-qr-show',
  standalone: true,
  imports: [NgIf, TicketComponent],
  templateUrl: './qr-show.component.html',
  styleUrl: './qr-show.component.scss',
})
export class QrShowComponent {
  @Input() moimId: string = '';
  @Input() participants: any = undefined as unknown as any;
  public qrCodeSrc: string = '';
  public ticketViewerSrc: SafeResourceUrl = '';

  constructor(
    private _sceneGraphService: ScenegraphService,
    private _apiExecutorService: ApiExecutorService,
    public sanitizer: DomSanitizer
  ) {}
  public async ngOnInit() {
    let url = `https://ssalon.co.kr/web/ticket?moimId=${this.moimId}&viewType=view`;
    this.ticketViewerSrc = this.sanitizer.bypassSecurityTrustResourceUrl(url);

    this.participants = await this._apiExecutorService.getJoiningUsers(
      this.moimId
    );

    await this.setQrCodeImgSrc();
    console.log(this.participants);
  }
  public async setQrCodeImgSrc() {
    let a = qrcode(0, 'L');
    a.addData(await this._apiExecutorService.getBarcode(this.moimId));
    //a.addData('https://www.naver.com');
    a.make();
    this.qrCodeSrc = a.createDataURL(5, 0);
  }

  public getAttendance() {
    if (
      this.participants.find((participant: any) => {
        if (participant.userId === this._apiExecutorService.myProfile.id) {
          return participant.attendance;
        }
      }).attendance
    ) {
      return '출석완료';
    } else {
      return '출석안함';
    }
  }
}
