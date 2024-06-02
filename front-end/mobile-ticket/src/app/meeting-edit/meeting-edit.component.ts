import {
  Component,
  EventEmitter,
  Input,
  Output,
  ViewChild,
} from '@angular/core';
import { ApiExecutorService } from '../service/api-executor.service';
import { ActivatedRoute, Router } from '@angular/router';
import {
  BottomDialogComponent,
  BottomNavigatorType,
} from '../ssalon-component/bottom-dialog/bottom-dialog.component';
import { SimpleInputComponent } from '../ssalon-component/simple-input/simple-input.component';
import { NewButtonElement } from '../ssalon-component/simple-toggle-group/simple-toggle-group.component';
import { ButtonElementsService } from '../service/button-elements.service';
import { NgIf } from '@angular/common';
import { ImageRowContainerComponent } from '../ssalon-component/image-row-container/image-row-container.component';
import { SquareButtonComponent } from '../ssalon-component/square-button/square-button.component';
import { TopNavigatorComponent } from '../ssalon-component/top-navigator/top-navigator.component';

@Component({
  selector: 'app-meeting-edit',
  standalone: true,
  imports: [
    NgIf,
    SimpleInputComponent,
    ImageRowContainerComponent,
    BottomDialogComponent,
    SquareButtonComponent,
    TopNavigatorComponent,
  ],
  templateUrl: './meeting-edit.component.html',
  styleUrl: './meeting-edit.component.scss',
})
export class MeetingEditComponent {
  @ViewChild('date', { static: false }) date: SimpleInputComponent | null =
    null;
  @ViewChild('location', { static: false })
  location: SimpleInputComponent | null = null;
  @ViewChild('description', { static: false })
  description: SimpleInputComponent | null = null;
  @ViewChild('photo', { static: false })
  photo: ImageRowContainerComponent | null = null;
  @ViewChild('capacity', { static: false })
  capacity: SimpleInputComponent | null = null;
  @ViewChild('fee', { static: false }) fee: SimpleInputComponent | null = null;
  public bottomNavigatorType = BottomNavigatorType;
  public isPopUpBottomNavigator = false;
  @Input() photos: NewButtonElement[] = [
    {
      imgSrc: 'assets/add_photo.png',
      value: -1,
      label: '사진추가',
      selected: false,
    },
  ];
  @Input() public editType: 'moimInfo' | 'reviewInfo' = 'moimInfo';
  @Input() public moimId: string = '';

  @Output() public readonly onCreateButtonReadyEvent = new EventEmitter();

  public moimInfo: any = undefined as unknown as any;
  public reviewInfo: any = undefined as unknown as any;
  public title: string = '모임 정보 수정하기';
  constructor(
    private _apiExecutorService: ApiExecutorService,
    private _route: ActivatedRoute,
    public buttonElementsService: ButtonElementsService,
    private _router: Router
  ) {
    this._route.queryParams.subscribe((params) => {
      this.editType = params['editType'];
      this.editType === 'moimInfo'
        ? '모임 정보 수정하기'
        : '모임 후기 수정하기';
      this.moimId = params['moimId'];
    });
  }
  public async ngOnInit() {
    if (this.editType === 'moimInfo') {
      this.moimInfo = await this._apiExecutorService.getMoimInfo(this.moimId);
      /**
      this.moimInfo = {
        title: tempMoimInfo.title,
        description: tempMoimInfo.description,
        category: tempMoimInfo.category,
        capacity: tempMoimInfo.capacity,
        location: tempMoimInfo.location,
        meetingDate: tempMoimInfo.meetingDate,
        payment: tempMoimInfo.payment,
        meetingPictureUrls: tempMoimInfo.meetingPictureUrls,
        isSharable: true,
      }; */
      this.moimInfo.meetingPictureUrls.forEach((url: string, index: number) => {
        let body = {
          imgSrc: url,
          value: index,
          label: `사진_${index}`,
          selected: false,
        };
        this.photos.unshift(body);
      });
    } else {
      this.reviewInfo = await this._apiExecutorService.getMoimReview(
        this.moimId
      );
      this.reviewInfo.diaryPictureUrls.forEach((url: string, index: number) => {
        let body = {
          imgSrc: url,
          value: index,
          label: `사진_${index}`,
          selected: false,
        };
        this.photos.unshift(body);
      });
    }
  }

  public ngAfterViewInit() {}

  public isMoimInfoLoaded() {
    return this.moimInfo !== undefined;
  }

  public isReviewInfoLoaded() {
    return this.reviewInfo !== undefined;
  }

  public ngAfterViewChecked() {}
  public popUpBottomNavigator() {
    this.isPopUpBottomNavigator = true;
  }
  public onCategorySelectedEvent(value: any) {
    this.moimInfo.category = this.buttonElementsService.getLabelByValue(
      this.buttonElementsService.interestSelectionButtons,
      value.value
    );
    this.isPopUpBottomNavigator = false;
  }
  public onClickNoLimitCapacity(checkNoLimit: boolean) {
    this.capacity!.innerText = checkNoLimit ? 99999 : '';
    this.moimInfo.capacity = checkNoLimit ? 99999 : 0;
  }
  public onClickFree(checkFreePayment: boolean) {
    this.fee!.innerText = checkFreePayment ? 0 : '';
    this.moimInfo.payment = checkFreePayment ? 0 : -1;
  }
  public onInput(type: string, value: any) {
    if (this.editType === 'moimInfo') {
      this.moimInfo[type] = value;
    } else {
      this.reviewInfo[type] = value;
    }
  }

  public onClickImage(value: number) {
    if (value === -1) {
      const fileInput = document.createElement('input');
      fileInput.type = 'file';
      fileInput.accept = 'image/*';
      fileInput.multiple = true;
      let fileUrls: string[] = [];

      // 파일 선택 이벤트 처리
      fileInput.onchange = async function (this: MeetingEditComponent) {
        // 파일 URL 배열 초기화
        fileUrls = [];

        // 파일이 선택되었는지 확인
        if (fileInput.files && fileInput.files.length > 0) {
          const files = Array.from(fileInput.files);
          let loadedFiles = 0;

          files.forEach((file, index) => {
            const reader = new FileReader();

            reader.onload = async function (
              this: MeetingEditComponent,
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
        if (this.editType === 'moimInfo') {
          this.moimInfo.meetingPictureUrls.unshift(result.mapURI[key]);
        } else {
          this.reviewInfo.diaryPictureUrls.unshift(result.mapURI[key]);
        }
      }
      console.log(this.moimInfo);
    } catch (e) {
      console.log(e);
    }
  }

  public async editInfo() {
    if (this.editType === 'moimInfo') {
      await this._apiExecutorService.editMoimInfo(this.moimId, this.moimInfo);
    } else {
      await this._apiExecutorService.editMoimReview(this.moimId, this.moimInfo);
    }

    this._router.navigate(['/web/meeting-info'], {
      queryParams: { moimId: this.moimId },
    });
  }
}
