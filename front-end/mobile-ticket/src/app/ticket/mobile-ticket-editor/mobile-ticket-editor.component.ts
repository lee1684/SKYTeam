import { SimpleButtonComponent } from '../../ssalon-component/simple-button/simple-button.component';
import { NgFor, NgIf } from '@angular/common';
import { ScenegraphService } from '../../service/scenegraph.service';
import {
  ButtonElement,
  CircleToggleButtonGroupComponent,
} from '../../ssalon-component/circle-toggle-button-group/circle-toggle-button-group.component';
import {
  ColorBoardComponent,
  SsalonColor,
  SsalonColorElement,
} from '../../ssalon-component/color-board/color-board.component';
import { SimpleToggleButtonGroupComponent } from '../../ssalon-component/simple-toggle-button-group/simple-toggle-button-group.component';
import { Vector2 } from 'three';
import {
  ApiExecutorService,
  ImageGeneration,
} from '../../service/api-executor.service';
import {
  ViewChild,
  ElementRef,
  Output,
  EventEmitter,
  Component,
  Input,
} from '@angular/core';
import { Canvas, FabricImage, FabricText, Path } from 'fabric';
import { NewButtonElement } from '../../ssalon-component/simple-toggle-group/simple-toggle-group.component';
import { ButtonElementsService } from '../../service/button-elements.service';
import { Router } from '@angular/router';
import { SquareButtonComponent } from '../../ssalon-component/square-button/square-button.component';

export enum MobileTicketEditMode {
  AI_GENERATE,
  BACKGROUND_COLOR_CHANGE,
  PHOTO,
  STICKER,
  TEXT,
  DRAW,
  NONE,
}

export enum TextAlign {
  LEFT,
  CENTER,
  RIGHT,
}

export interface SsalonTextAttribute {
  text: string;
  fontFamily: string;
  color: string;
  textAlign: string;
}

export interface SsalonImageAttribute {
  src: string[];
}

export interface SsalonPathAttribute {
  color: string;
  strokeWidth: number;
}

@Component({
  selector: 'app-mobile-ticket-editor',
  standalone: true,
  imports: [
    SimpleButtonComponent,
    SimpleToggleButtonGroupComponent,
    CircleToggleButtonGroupComponent,
    ColorBoardComponent,
    NgFor,
    NgIf,
    SquareButtonComponent,
  ],
  templateUrl: './mobile-ticket-editor.component.html',
  styleUrl: './mobile-ticket-editor.component.scss',
})
export class MobileTicketEditorComponent {
  @ViewChild('editFeatureButtons', { static: false })
  editFeatureButtons: CircleToggleButtonGroupComponent | null = null;
  @ViewChild('backgroundPath', { static: false })
  backgroundPath: ElementRef | null = null;
  @ViewChild('textEditInput', { static: false })
  textEditInput: ElementRef | null = null;
  @ViewChild('drawCanvas', { static: false })
  drawCanvas: ElementRef | null = null;
  @ViewChild('selectedPhotoContainer', { static: false })
  selectedPhotoContainer: ElementRef | null = null;
  @ViewChild('generatedImageContainer', { static: false })
  generatedImageContainer: ElementRef | null = null;
  @ViewChild('prompt', { static: false })
  prompt: ElementRef | null = null;
  @ViewChild('generatedImage', { static: false })
  generatedImage: ElementRef | null = null;

  @Input() moimId: string = '';
  @Input() face: string = 'front';

  @Output() public readonly onChangeViewer = new EventEmitter();
  @Output() public readonly onObjectEditEnded = new EventEmitter();
  @Output() public readonly onBackgroundColorEditEnded = new EventEmitter();
  @Output() public readonly onClickPreview = new EventEmitter();
  public editMode: MobileTicketEditMode = MobileTicketEditMode.NONE;
  public ssalonColor: SsalonColor = new SsalonColor();

  public mobileTicketEditMode = MobileTicketEditMode;
  public goBackButtonElement: NewButtonElement = {
    selected: false,
    imgSrc: 'assets/icons/go-back.png',
    label: '뒤로가기',
    value: 0,
  };
  public previewButtonElement: NewButtonElement = {
    selected: false,
    imgSrc: 'assets/icons/view.png',
    label: '저장 및 미리보기',
    value: 0,
  };
  public completeButtonElement: NewButtonElement = {
    selected: false,
    imgSrc: '',
    label: '완료',
    value: 0,
  };

  public editFeatures: ButtonElement[] = [
    {
      imgSrc: 'assets/icons/ai_generate.png',
      label: 'ai',
      value: MobileTicketEditMode.AI_GENERATE,
    },
    {
      imgSrc: 'assets/icons/color-board.png',
      label: '배경색 변경',
      value: MobileTicketEditMode.BACKGROUND_COLOR_CHANGE,
    },
    {
      imgSrc: 'assets/icons/photo.png',
      label: '사진',
      value: MobileTicketEditMode.PHOTO,
    },
    {
      imgSrc: 'assets/icons/sticker.png',
      label: '스티커',
      value: MobileTicketEditMode.STICKER,
    },
    {
      imgSrc: 'assets/icons/text.png',
      label: '텍스트',
      value: MobileTicketEditMode.TEXT,
    },
    {
      imgSrc: 'assets/icons/draw.png',
      label: '그리기',
      value: MobileTicketEditMode.DRAW,
    },
    {
      imgSrc: 'assets/icons/view.png',
      label: '그리기',
      value: MobileTicketEditMode.NONE,
    },
  ];

  public generatingImage: boolean = false;

  private _backgroundColorViewLoaded: boolean = false;

  public uploadPhotoNum: number = 0;
  public stickers: NewButtonElement[] = [];
  public editingSticker: FabricImage | null = null;
  public editingStickerSrc: string = '';

  public textFocused: boolean = false;
  public textAlign: 'left' | 'center' | 'right' = 'left';
  public textAlignButtons: ButtonElement[] = [
    {
      imgSrc: 'assets/text-aligns/textalign-left.png',
      label: '좌',
      value: TextAlign.LEFT,
    },
    {
      imgSrc: 'assets/text-aligns/textalign-center.png',
      label: '중',
      value: TextAlign.CENTER,
    },
    {
      imgSrc: 'assets/text-aligns/textalign-right.png',
      label: '우',
      value: TextAlign.RIGHT,
    },
  ];
  public fonts: ButtonElement[] = [
    { imgSrc: 'Ariel', label: 'Aa', value: 0 },
    { imgSrc: 'Josefin Sans', label: 'Aa', value: 1 },
    { imgSrc: 'Jersey 15', label: 'Aa', value: 2 },
    { imgSrc: 'Roboto', label: 'Aa', value: 3 },
    { imgSrc: 'Jacquarda Bastarda 9 Charted', label: 'Aa', value: 4 },
  ];
  public fontFamily: string = 'Josefin Sans';
  public isTextAddMode: boolean = true;
  public textColor: SsalonColorElement = this.ssalonColor.WHITE;
  public editingText: Text | null = null;

  public colorBoard: ButtonElement[] = [];

  public drawingFabricCanvas: Canvas | null = null;
  private _isDrawing = false;
  private _drawingPoints: Vector2[] = [];
  private _isDrawingFabricCanvasLoaded: boolean = false;
  public drawingFabricPaths: Path[] = [];

  /** ssalon editor 설정 값 관련
   * @param backgroundColor: SsalonColorElement 배경색
   * @param sslonPhotoAttribute: SsalonImageAttribute 이미지
   * @param sslonStickerAttribute: SsalonImageAttribute 스티커
   * @param sslonTextAttribute: SsalonTextAttribute 텍스트
   * @param sslonPathAttribute: SsalonPathAttribute 그림
   */
  public ssalonGenAIImageAttribute: SsalonImageAttribute = {
    src: [],
  };
  public backgroundColor: SsalonColorElement = this.ssalonColor.LIGHT_GRAY;
  public ssalonTextAttribute: SsalonTextAttribute = {
    text: '',
    fontFamily: 'Josefin Sans',
    color: '#FFFFFF',
    textAlign: 'left',
  };
  public ssalonPhotoAttribute: SsalonImageAttribute = {
    src: [],
  };
  public ssalonStickerAttribute: SsalonImageAttribute = {
    src: [],
  };
  public ssalonPathAttribute: SsalonPathAttribute = {
    color: '#FFFFFF',
    strokeWidth: 1,
  };
  /** 새로 추가할 fabric object */
  public fabricObjects: FabricImage[] | FabricText[] | Path[] = [];
  /** 완료 버튼 클릭 후, editor view에 적용하기 위해 사용한 기능이 무엇인지 저장 */
  public lastUsedFeature: MobileTicketEditMode = MobileTicketEditMode.NONE;

  constructor(
    private _apiExecutorService: ApiExecutorService,
    private _router: Router,
    private _sceneGraphService: ScenegraphService,
    private _buttonElementsService: ButtonElementsService
  ) {}
  public ngAfterViewInit(): void {
    this.loadDecorationInfo();
    this.getStickers();
  }
  /** 기능 실행 버튼 클릭 후, detailed editor view가 켜진 후에 설정 값들을 적용. */
  public ngAfterViewChecked(): void {
    /* background color feature */
    if (this.prompt && !this.textFocused) {
      this.prompt!.nativeElement.focus();
      this.textFocused = true;
    }
    /* background color feature */
    if (this.backgroundPath && !this._backgroundColorViewLoaded) {
      this.backgroundPath.nativeElement.setAttribute(
        'fill',
        this.backgroundColor.color
      );
      this._backgroundColorViewLoaded = true;
    }

    /* text feature */
    if (this.textEditInput && !this.textFocused) {
      if (this.fabricObjects.length === 1) {
        this.textEditInput!.nativeElement.value = this.ssalonTextAttribute.text;
      }
      this.textEditInput!.nativeElement.focus();
      this.textFocused = true;
    }

    /* draw feature */
    if (this.drawCanvas && !this._isDrawingFabricCanvasLoaded) {
      this.initDrawingCanvas();
      this._isDrawingFabricCanvasLoaded = true;
    }
  }

  /** decoration 값들을 받아와서 초기값 설정
   * 사실, 배경색만 제대로 바꿔주면 됨.
   * fabric canvas를 건드는 것은 아니기 때문.
   */
  public async loadDecorationInfo() {
    let decorationInfo = await this._apiExecutorService.getTicket(this.moimId);
    this.backgroundColor = this.ssalonColor.getSsalonColorObjectByColor(
      decorationInfo.backgroundColor
    );
    this.onBackgroundColorEditEnded.emit(this.backgroundColor.color);
  }

  public async generateImage() {
    const body: ImageGeneration = {
      prompt: this.prompt?.nativeElement.value,
      highQuality: true, // true: 0.08 달러 사용, false: 0.02 달러 사용
    };

    //실제 API 사용 (비용 O)
    this.generatingImage = true;
    let imageUrl = await this._apiExecutorService.generateImage(
      this.moimId,
      body
    );
    this.generatingImage = false;
    // 테스트용 (비용 X)
    //let imageUrl =
    //  'https://test-bukkit-240415.s3.ap-northeast-2.amazonaws.com/Images/62/1b30cc57-fa8d-4491-a609-06ccf3ba83dc.png';
    this.ssalonGenAIImageAttribute.src.push(imageUrl);

    this.generatedImage!.nativeElement.setAttribute('src', imageUrl);

    (this.generatedImage!.nativeElement as HTMLImageElement).src = imageUrl;
  }

  private uploadImage() {
    const fileInput = document.createElement('input');
    fileInput.type = 'file';
    fileInput.accept = 'image/*';
    fileInput.multiple = true;
    let fileUrls: string[] = [];

    // 파일 선택 이벤트 처리
    fileInput.onchange = async function (this: MobileTicketEditorComponent) {
      // 파일 URL 배열 초기화
      fileUrls = [];

      // 파일이 선택되었는지 확인
      if (fileInput.files && fileInput.files.length > 0) {
        const files = Array.from(fileInput.files);
        let loadedFiles = 0;

        files.forEach((file, index) => {
          const reader = new FileReader();

          reader.onload = async function (
            this: MobileTicketEditorComponent,
            e: any
          ) {
            // 파일 URL을 배열에 추가
            fileUrls.push(e.target.result);

            // 첫 번째 파일만 미리보기로 보여주기
            if (index === 0) {
              let img = document.getElementById('selected-photo-container');
              if (!img) {
                img = document.createElement('img');
                img.id = 'selected-photo-container';
                document.body.appendChild(img);
              }
              (img as HTMLImageElement).src = e.target.result;
              (img as HTMLImageElement).width = 80; // 이미지 크기 조절
            }

            // 모든 파일을 다 읽었을 때
            loadedFiles++;
            if (loadedFiles === files.length) {
              this.uploadPhotoNum = files.length;
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

  /** 기능 실행 */
  public async onClickChangeEditMode(value: MobileTicketEditMode) {
    switch (value) {
      case MobileTicketEditMode.AI_GENERATE:
        break;
      case MobileTicketEditMode.BACKGROUND_COLOR_CHANGE:
        break;
      case MobileTicketEditMode.PHOTO:
        this.uploadImage();
        break;
      case MobileTicketEditMode.STICKER:
        break;
      case MobileTicketEditMode.TEXT:
        this.textFocused = false;
        break;
      case MobileTicketEditMode.DRAW:
        break;
      case MobileTicketEditMode.NONE:
        break;
    }
    this.editMode = value;
    this.onChangeViewer.emit(value);
  }

  /** 미리보기 */
  public onClickPreviewButton(): void {
    this.onClickPreview.emit();
  }

  /** detailed edit 완료 버튼 */
  public onClickEndDetailedEditViewer(): void {
    this.lastUsedFeature = this.editMode;
    this.editMode = MobileTicketEditMode.NONE;
    this.editFeatureButtons!.setUnselectedStatus();
    this.onChangeViewer.emit(MobileTicketEditMode.NONE);

    this.onEndEditObject();
  }

  public changeBackgroundColor(value: number): void {
    this.backgroundPath?.nativeElement.setAttribute(
      'fill',
      this.ssalonColor.getSsalonColorObjectByValue(value).color
    );
    this.backgroundColor = this.ssalonColor.getSsalonColorObjectByValue(value);
  }

  public getStickers() {
    let number = 50;
    for (let i = 0; i < number; i++) {
      this.stickers.push({
        imgSrc:
          'https://dokcohci6rkid.cloudfront.net/sticker/ssalon_sticker_' +
          i +
          '.png',
        label: `${i}`,
        value: i,
        selected: false,
      });
    }
  }

  public getStickerArray(): any[][] {
    const stickerArray: any[][] = [];
    for (let i = 0; i < this.stickers.length; i += 3) {
      stickerArray.push(this.stickers.slice(i, i + 3));
    }
    return stickerArray;
  }

  public selectSticker(value: number): void {
    if (
      this.ssalonStickerAttribute.src.includes(this.stickers[value].imgSrc!)
    ) {
      this.ssalonStickerAttribute.src.splice(
        this.ssalonStickerAttribute.src.indexOf(this.stickers[value].imgSrc!),
        1
      );
      this.stickers[value].selected = false;
    } else {
      this.ssalonStickerAttribute.src.push(this.stickers[value].imgSrc!);
      this.stickers[value].selected = true;
    }
  }

  public syncTextAttributeWithSelectedText() {
    if (this.fabricObjects.length === 1) {
      this.ssalonTextAttribute = {
        text: (this.fabricObjects[0] as FabricText).text!,
        fontFamily: (this.fabricObjects[0] as FabricText).fontFamily as string,
        color: (this.fabricObjects[0] as FabricText).fill as string,
        textAlign: (this.fabricObjects[0] as FabricText).textAlign as string,
      };
    }
  }

  /**
   * @param attributeName ={
   * 'text': string,
   * 'fontFamily': string,
   * 'color': string,
   * 'textAlign': string,
   * }
   * @param value
   */
  public onChangeSsalonTextAttribute(attributeName: string, value: any): void {
    switch (attributeName) {
      case 'text':
        this.ssalonTextAttribute.text = this.textEditInput!.nativeElement.value;
        break;
      case 'fontFamily':
        this.ssalonTextAttribute.fontFamily = value;
        break;
      case 'color':
        this.ssalonTextAttribute.color =
          this.ssalonColor.getSsalonColorObjectByValue(value).color;
        break;
      case 'textAlign':
        switch (value) {
          case TextAlign.LEFT:
            this.ssalonTextAttribute.textAlign = 'left';
            break;
          case TextAlign.CENTER:
            this.ssalonTextAttribute.textAlign = 'center';
            break;
          case TextAlign.RIGHT:
            this.ssalonTextAttribute.textAlign = 'right';
            break;
        }
        break;
    }
  }

  public getSsalonTextEnumByString(value: string): TextAlign {
    switch (value) {
      case 'left':
        return TextAlign.LEFT;
      case 'center':
        return TextAlign.CENTER;
      case 'right':
        return TextAlign.RIGHT;
      default:
        return TextAlign.CENTER;
    }
  }

  public async loadImageRecursive(index: number) {
    let array;
    if (this.lastUsedFeature === MobileTicketEditMode.PHOTO) {
      array = this.ssalonPhotoAttribute;
    } else if (this.lastUsedFeature === MobileTicketEditMode.STICKER) {
      array = this.ssalonStickerAttribute;
    } else {
      array = this.ssalonGenAIImageAttribute;
    }
    if (index === array.src.length) {
      this.onObjectEditEnded.emit(this.fabricObjects);

      if (this.lastUsedFeature === MobileTicketEditMode.PHOTO) {
        this.ssalonPhotoAttribute.src = [];
      } else if (this.lastUsedFeature === MobileTicketEditMode.STICKER) {
        this.ssalonStickerAttribute.src = [];
      } else {
        this.ssalonGenAIImageAttribute.src = [];
      }

      this.lastUsedFeature = MobileTicketEditMode.NONE;
    } else {
      let tempImg = await FabricImage.fromURL(array.src[index], {
        crossOrigin: 'anonymous',
      });
      let ratio = 200 / tempImg.width;
      console.log(ratio);
      tempImg.scale(ratio);
      (this.fabricObjects as FabricImage[]).push(tempImg);
      this.loadImageRecursive(index + 1);
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
      let result =
        this.face === 'front'
          ? await this._apiExecutorService.uploadTicketImages(this.moimId, body)
          : await this._apiExecutorService.uploadDiaryImages(this.moimId, body);
      let keys = Object.keys(result.mapURI);
      for (var i = 0; i < keys.length; i++) {
        var key = keys[i];
        this.ssalonPhotoAttribute.src.push(result.mapURI[key]);
      }
      this.ssalonPhotoAttribute.src.forEach((url) => {
        let substring = url.substring(58);
        let proxyUrl = 'https://dokcohci6rkid.cloudfront.net' + substring;
        url = proxyUrl;
      });
    } catch (e) {
      console.log(e);
    }
  }

  /** 완료를 눌러야 fabric object를 생성 */
  public async onEndEditObject() {
    if (this.lastUsedFeature === MobileTicketEditMode.BACKGROUND_COLOR_CHANGE) {
      this.onBackgroundColorEditEnded.emit(this.backgroundColor.color);
      this._backgroundColorViewLoaded = false;
    } else {
      switch (this.lastUsedFeature) {
        case MobileTicketEditMode.AI_GENERATE:
          this.textFocused = false;
          this.loadImageRecursive(0);
          break;
        case MobileTicketEditMode.PHOTO:
        case MobileTicketEditMode.STICKER:
          this.loadImageRecursive(0);
          break;
        case MobileTicketEditMode.TEXT:
          if (this.fabricObjects.length === 0) {
            (this.fabricObjects as FabricText[]).push(
              new FabricText(this.ssalonTextAttribute.text, {
                top: 100,
                left: 100,
                fill: this.ssalonTextAttribute.color,
                textAlign: this.ssalonTextAttribute.textAlign,
                fontFamily: this.ssalonTextAttribute.fontFamily,
              })
            );
          } else {
            (this.fabricObjects[0] as FabricText).set(
              'text',
              this.ssalonTextAttribute.text
            );
            (this.fabricObjects[0] as FabricText).set(
              'fill',
              this.ssalonTextAttribute.color
            );
            (this.fabricObjects[0] as FabricText).set(
              'textAlign',
              this.ssalonTextAttribute.textAlign
            );
            (this.fabricObjects[0] as FabricText).set(
              'fontFamily',
              this.ssalonTextAttribute.fontFamily
            );
            this.lastUsedFeature = MobileTicketEditMode.NONE;
          }
          this.textFocused = false;
          break;
        case MobileTicketEditMode.DRAW:
          this._isDrawingFabricCanvasLoaded = true;
          break;
        case MobileTicketEditMode.NONE:
          break;
      }
      this.drawingFabricCanvas?.destroy();
      /** text의 경우, 수정하는 경우가 있는데, 이 함수에서 미리 다 바꾸기 때문에 object를 넘길 필요가 없음.
       * 그렇기 때문에, 미리 editMode를 NONE으로 바꿔버려 null을 보냄.
       * null을 받은 viewer는 canvas.renderAll()만 진행.
       * 이미지의 경우, asyncronous하게 진행되기 때문에 밑의 코드가 진행되지 않게 해놔야 의도대로 진행될 수 있음.
       */
      this.onObjectEditEnded.emit(
        this.lastUsedFeature === MobileTicketEditMode.NONE
          ? null
          : this.fabricObjects
      );
      this.lastUsedFeature = MobileTicketEditMode.NONE;
    }

    this.fabricObjects = [];
  }

  public onClickCompletedEditing(): void {}

  public initDrawingCanvas(): void {
    this.drawingFabricCanvas = new Canvas(this.drawCanvas!.nativeElement);
    this.drawingFabricCanvas.renderAll();

    this.drawingFabricCanvas!.on('mouse:down', (options) => {
      this._isDrawing = true;
      this.addPoint(options.e);
      this.drawPath();
    });

    this.drawingFabricCanvas!.on('mouse:move', (options) => {
      if (!this._isDrawing) return;
      this.addPoint(options.e);
      this.drawPath();
    });

    this.drawingFabricCanvas!.on('mouse:up', () => {
      this._isDrawing = false;
      this._drawingPoints.length = 0;
    });
  }

  private addPoint(e: MouseEvent | TouchEvent) {
    const pointer = this.drawingFabricCanvas!.getPointer(e);
    this._drawingPoints.push(
      new Vector2(pointer.x as number, pointer.y as number)
    );
  }

  private drawPath() {
    const pathData = this._drawingPoints
      .map((point) => `${point.x} ${point.y}`)
      .join(' ');
    const path = new Path(`M ${pathData}`, {
      fill: 'transparent',
      stroke: 'white',
      strokeWidth: 2,
    });
    (this.fabricObjects as Path[]).length = 0;
    (this.fabricObjects as Path[]).push(path);
    this.drawingFabricCanvas!.clear();
    this.drawingFabricCanvas!.renderAll();
    this.drawingFabricCanvas!.add(path);
  }

  public endTicketWebView(): void {
    this._router.navigate(['/web/main']);
  }
}
