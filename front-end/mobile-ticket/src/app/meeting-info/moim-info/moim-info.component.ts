import { Component, Input } from '@angular/core';
import { SimpleContentComponent } from '../../ssalon-component/simple-content/simple-content.component';
import { ApiExecutorService } from '../../service/api-executor.service';
import { ImageRowContainerComponent } from '../../ssalon-component/image-row-container/image-row-container.component';
import { NgIf } from '@angular/common';
import { ChatContainerComponent } from '../../ssalon-component/chat-container/chat-container.component';

@Component({
  selector: 'app-moim-info',
  standalone: true,
  imports: [
    NgIf,
    SimpleContentComponent,
    ImageRowContainerComponent,
    ChatContainerComponent,
  ],
  templateUrl: './moim-info.component.html',
  styleUrl: './moim-info.component.scss',
})
export class MoimInfoComponent {
  @Input() moimId: string = '';
  @Input() moimInfo: any = {};
  constructor(private _apiExecutorService: ApiExecutorService) {}

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
}
