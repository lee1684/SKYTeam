import {
  Component,
  EventEmitter,
  Input,
  Output,
  ViewChild,
} from '@angular/core';
import { SimpleInputComponent } from '../../ssalon-component/simple-input/simple-input.component';
import { ButtonElementsService } from '../../service/button-elements.service';
import {
  BottomDialogComponent,
  BottomDialogType,
} from '../../ssalon-component/bottom-dialog/bottom-dialog.component';
import { SquareButtonComponent } from '../../ssalon-component/square-button/square-button.component';
import { NgIf } from '@angular/common';
import { ImageRowContainerComponent } from '../../ssalon-component/image-row-container/image-row-container.component';
import { NewButtonElement } from '../../ssalon-component/simple-toggle-group/simple-toggle-group.component';
import { ApiExecutorService } from '../../service/api-executor.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-create-meeting-info',
  standalone: true,
  imports: [
    NgIf,
    SimpleInputComponent,
    SquareButtonComponent,
    BottomDialogComponent,
    ImageRowContainerComponent,
  ],
  templateUrl: './create-meeting-info.component.html',
  styleUrl: './create-meeting-info.component.scss',
})
export class CreateMeetingInfoComponent {
  @ViewChild('capacity', { static: true })
  capacity: SimpleInputComponent | null = null;
  @ViewChild('fee', { static: true }) fee: SimpleInputComponent | null = null;
  public bottomDialogType = BottomDialogType;
  public isPopUpBottomNavigator = false;
  @Input() public meetingInfo: any = {
    title: '',
    description: '',
    category: '',
    capacity: 0,
    location: '',
    meetingDate: '',
    payment: -1,
    meetingPictureUrls: [],
    isSharable: true,
  };
  @Input() photos: NewButtonElement[] = [
    {
      imgSrc: 'assets/add_photo.png',
      value: -1,
      label: '사진추가',
      selected: false,
    },
  ];

  @Output() public readonly onCreateButtonReadyEvent = new EventEmitter();

  constructor(
    public buttonElementsService: ButtonElementsService,
    private _apiExecutorService: ApiExecutorService,
    private _route: ActivatedRoute
  ) {}

  public ngAfterViewChecked() {
    this.onCreateButtonReadyEvent.emit(this.isAllTyped());
  }

  public ngOnDestroy() {
    this.buttonElementsService.interestSelectionButtons.forEach(
      (interestSelectionButton) => {
        interestSelectionButton.selected = false;
      }
    );
  }

  public popUpBottomNavigator() {
    this.isPopUpBottomNavigator = true;
  }
  public onCategorySelectedEvent(value: any) {
    this.meetingInfo.category = this.buttonElementsService.getLabelByValue(
      this.buttonElementsService.interestSelectionButtons,
      value.value
    );
    this.isPopUpBottomNavigator = false;
    this.buttonElementsService.interestSelectionButtons.forEach(
      (interestSelectionButton) => {
        interestSelectionButton.selected = false;
      }
    );
  }
  public onClickNoLimitCapacity(checkNoLimit: boolean) {
    this.capacity!.innerText = checkNoLimit ? 99999 : '';
    this.meetingInfo.capacity = checkNoLimit ? 99999 : 0;
  }
  public onClickFree(checkFreePayment: boolean) {
    this.fee!.innerText = checkFreePayment ? 0 : '';
    this.meetingInfo.payment = checkFreePayment ? 0 : -1;
  }
  public onInput(type: string, value: any) {
    this.meetingInfo[type] = value;
  }
  public isAllTyped() {
    return (
      this.meetingInfo.title !== '' &&
      this.meetingInfo.description !== '' &&
      this.meetingInfo.category !== '' &&
      this.meetingInfo.capacity !== 0 &&
      this.meetingInfo.location !== '' &&
      this.meetingInfo.meetingDate !== '' &&
      this.meetingInfo.payment !== -1
    );
  }
  public onClickImage(value: number) {
    if (value === -1) {
      const fileInput = document.createElement('input');
      fileInput.type = 'file';
      fileInput.accept = 'image/*';
      fileInput.multiple = true;
      let fileUrls: string[] = [];

      // 파일 선택 이벤트 처리
      fileInput.onchange = async function (this: CreateMeetingInfoComponent) {
        // 파일 URL 배열 초기화
        fileUrls = [];

        // 파일이 선택되었는지 확인
        if (fileInput.files && fileInput.files.length > 0) {
          const files = Array.from(fileInput.files);
          let loadedFiles = 0;

          files.forEach((file, index) => {
            const reader = new FileReader();

            reader.onload = async function (
              this: CreateMeetingInfoComponent,
              e: any
            ) {
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
        this.meetingInfo.meetingPictureUrls.unshift(result.mapURI[key]);
      }
      console.log(this.meetingInfo);
    } catch (e) {
      console.log(e);
    }
  }
}
