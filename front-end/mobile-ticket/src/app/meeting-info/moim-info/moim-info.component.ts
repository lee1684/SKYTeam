import { Component, Input, ViewChild } from '@angular/core';
import { SimpleContentComponent } from '../../ssalon-component/simple-content/simple-content.component';
import { ApiExecutorService } from '../../service/api-executor.service';
import { ImageRowContainerComponent } from '../../ssalon-component/image-row-container/image-row-container.component';
import { NgIf } from '@angular/common';
import { ChatContainerComponent } from '../../ssalon-component/chat-container/chat-container.component';
import { SimpleInputComponent } from '../../ssalon-component/simple-input/simple-input.component';
import { NewButtonElement } from '../../ssalon-component/simple-toggle-group/simple-toggle-group.component';
<<<<<<< HEAD
import { Router } from '@angular/router';
import { CircleToggleStatusGroupComponent } from '../../ssalon-component/circle-toggle-status-group/circle-toggle-status-group.component';
=======
>>>>>>> develop

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
<<<<<<< HEAD
=======
  @ViewChild('date', { static: true })
  date: SimpleInputComponent | null = null;
  @ViewChild('location', { static: true })
  location: SimpleInputComponent | null = null;
  @ViewChild('description', { static: true })
  description: SimpleInputComponent | null = null;
  @ViewChild('photo', { static: true })
  photo: ImageRowContainerComponent | null = null;
>>>>>>> develop
  @ViewChild('capacity', { static: true })
  capacity: SimpleInputComponent | null = null;
  @ViewChild('fee', { static: true }) fee: SimpleInputComponent | null = null;

  @Input() moimId: string = '';
  @Input() moimInfo: any = {};
<<<<<<< HEAD
  @Input() participants: any[] = [];
  @Input() isCreator: boolean = false;
  @Input() isParticipant: boolean = false;
=======
  @Input() mode: 'show' | 'edit' = 'show';
>>>>>>> develop
  public photos: NewButtonElement[] = [
    {
      imgSrc: 'assets/add_photo.png',
      value: -1,
      label: '사진추가',
      selected: false,
    },
  ];

<<<<<<< HEAD
  constructor(
    private _apiExecutorService: ApiExecutorService,
    private _router: Router
  ) {}

  public async ngOnInit() {}
=======
  constructor(private _apiExecutorService: ApiExecutorService) {}

  public async ngOnInit() {
    this.date!.innerText = this.moimInfo.date;
    this.location!.innerText = this.moimInfo.location;
    this.description!.innerText = this.moimInfo.description;
    this.capacity!.innerText = this.moimInfo.capacity;
    this.fee!.innerText = this.moimInfo.payment;
    this.photos = this.getImageUrlsToNewButtonElements(
      this.moimInfo.meetingPictureUrls
    );
  }
>>>>>>> develop

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

<<<<<<< HEAD
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
=======
  public onInput(type: string, value: any) {
    this.moimInfo[type] = value;
  }

  public onClickImage(value: number) {
    if (value === -1) {
      const fileInput = document.createElement('input');
      fileInput.type = 'file';
      fileInput.accept = 'image/*';
      fileInput.multiple = true;
      let fileUrls: string[] = [];

      // 파일 선택 이벤트 처리
      fileInput.onchange = async function (this: MoimInfoComponent) {
        // 파일 URL 배열 초기화
        fileUrls = [];

        // 파일이 선택되었는지 확인
        if (fileInput.files && fileInput.files.length > 0) {
          const files = Array.from(fileInput.files);
          let loadedFiles = 0;

          files.forEach((file, index) => {
            const reader = new FileReader();

            reader.onload = async function (this: MoimInfoComponent, e: any) {
              // 파일 URL을 배열에 추가
              fileUrls.push(e.target.result);

              // 모든 파일을 다 읽었을 때
              loadedFiles++;
              if (loadedFiles === files.length) {
                await this.getImageUrl(fileUrls);
              }
            }.bind(this);

            // 파일을 읽어들임
            reader.readAsDataURL(file);
          });
        }
      }.bind(this);

      // 파일 선택 대화 상자 열기
      fileInput.click();
    }
  }

  public async getImageUrl(urls: string[]) {
    let body = new FormData();
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
    urls.forEach((url, index) => {
      body.append('files', dataURItoBlob(url), `${index}.png`);
    });

    try {
      let result = await this._apiExecutorService.uploadGeneralImage(body);
      let keys = Object.keys(result.mapURI);
      console.log(keys);
      for (var i = 0; i < keys.length; i++) {
        var key = keys[i];
        let body = {
          imgSrc: result.mapURI[key],
          value: i,
          label: `사진_${i}`,
          selected: false,
        };
        this.photos.unshift(body);
        this.moimInfo.meetingPictureUrls.unshift(result.mapURI[key]);
      }
      console.log(this.moimInfo);
    } catch (e) {
      console.log(e);
    }
  }
  public onClickNoLimitCapacity(checkNoLimit: boolean) {
    this.capacity!.innerText = checkNoLimit ? 99999 : '';
    this.moimInfo.capacity = checkNoLimit ? 99999 : 0;
  }
  public onClickFree(checkFreePayment: boolean) {
    this.fee!.innerText = checkFreePayment ? 0 : '';
    this.moimInfo.payment = checkFreePayment ? 0 : -1;
>>>>>>> develop
  }
}
