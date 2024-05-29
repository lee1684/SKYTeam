import { NgIf } from '@angular/common';
import { Component, EventEmitter, input, Input, Output } from '@angular/core';
import { ApiExecutorService } from '../../service/api-executor.service';

@Component({
  selector: 'app-profile-img',
  standalone: true,
  imports: [NgIf],
  templateUrl: './profile-img.component.html',
  styleUrl: './profile-img.component.scss',
})
export class ProfileImgComponent {
  @Input() imgSize = 96;
  @Input() imgSrc = 'assets/default_profile.png';
  @Input() isEditable = true;

  @Output() public readonly onImgClickEvent = new EventEmitter();
  constructor(private _apiExecutorService: ApiExecutorService) {}
  public onClickEditButton(): void {
    const fileInput = document.createElement('input');
    fileInput.type = 'file';
    fileInput.accept = 'image/*';
    fileInput.multiple = true;
    let fileUrls: string[] = [];

    // 파일 선택 이벤트 처리
    fileInput.onchange = async function (this: ProfileImgComponent) {
      // 파일 URL 배열 초기화
      fileUrls = [];

      // 파일이 선택되었는지 확인
      if (fileInput.files && fileInput.files.length > 0) {
        const files = Array.from(fileInput.files);
        let loadedFiles = 0;

        files.forEach((file, index) => {
          const reader = new FileReader();

          reader.onload = async function (this: ProfileImgComponent, e: any) {
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

    this.onImgClickEvent.emit();
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
        this.imgSrc = result.mapURI[key];
      }
    } catch (e) {
      console.log(e);
    }
  }
}
