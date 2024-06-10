import { Component, Input, ViewChild } from '@angular/core';
import { SimpleContentComponent } from '../../ssalon-component/simple-content/simple-content.component';
import { ApiExecutorService } from '../../service/api-executor.service';
import { ImageRowContainerComponent } from '../../ssalon-component/image-row-container/image-row-container.component';
import { NgIf } from '@angular/common';
import { ChatContainerComponent } from '../../ssalon-component/chat-container/chat-container.component';
import { SimpleInputComponent } from '../../ssalon-component/simple-input/simple-input.component';
import { NewButtonElement } from '../../ssalon-component/simple-toggle-group/simple-toggle-group.component';
import { Router } from '@angular/router';
import { CircleToggleStatusGroupComponent } from '../../ssalon-component/circle-toggle-status-group/circle-toggle-status-group.component';

@Component({
  selector: 'app-moim-info',
  standalone: true,
  imports: [
    NgIf,
    SimpleContentComponent,
    ImageRowContainerComponent,
    ChatContainerComponent,
    SimpleInputComponent,
  ],
  templateUrl: './moim-info.component.html',
  styleUrl: './moim-info.component.scss',
})
export class MoimInfoComponent {
  @ViewChild('capacity', { static: true })
  capacity: SimpleInputComponent | null = null;
  @ViewChild('fee', { static: true }) fee: SimpleInputComponent | null = null;

  @Input() moimId: string = '';
  @Input() moimInfo: any = {};
  @Input() participants: any[] = [];
  @Input() isCreator: boolean = false;
  @Input() isParticipant: boolean = false;
  public photos: NewButtonElement[] = [
    {
      imgSrc: 'assets/add_photo.png',
      value: -1,
      label: '사진추가',
      selected: false,
    },
  ];

  constructor(
    private _apiExecutorService: ApiExecutorService,
    private _router: Router
  ) {}

  public async ngOnInit() {}

  public getDate() {
    const date = new Date(this.moimInfo.meetingDate);

    const year: number = date.getFullYear();
    const month: number = date.getMonth() + 1;
    const day: number = date.getDate();
    const hour: number = date.getHours();
    const minute: number = date.getMinutes();

    const formattedDate: string = `${year}년 ${month}월 ${day}일 ${hour}시 ${minute}분`;

    return `${formattedDate}`;
  }

  public getBill() {
    let bill: string =
      this.moimInfo.payment !== undefined ? this.moimInfo.payment : '1000';
    return `${Number(bill).toLocaleString('en-US')} 원`;
  }

  public getImageUrlsToNewButtonElements(imageUrls: string[]) {
    let newButtonElements = imageUrls.map((url: string, index: number) => {
      return {
        imgSrc: url,
        value: index,
        label: '',
        selected: false,
      };
    });

    return newButtonElements;
  }

  public getParticipantImages() {
    return this.getImageUrlsToNewButtonElements(
      this.participants.map((participant: any) => {
        return participant.profilePictureUrl;
      })
    );
  }

  public onClickParticipantButton() {
    this._router.navigate(['/web/meeting-participants'], {
      queryParams: { id: this.moimId },
    });
  }
}
