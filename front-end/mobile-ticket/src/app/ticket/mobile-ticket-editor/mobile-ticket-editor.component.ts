import {
  Component,
  ElementRef,
  EventEmitter,
  HostListener,
  Output,
  ViewChild,
} from '@angular/core';
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
  SsalonColorEnum,
} from '../../ssalon-component/color-board/color-board.component';
import { SimpleToggleButtonGroupComponent } from '../../ssalon-component/simple-toggle-button-group/simple-toggle-button-group.component';
import { fabric } from 'fabric';
import { Vector2 } from 'three';
import { ApiExecutorService } from '../../service/api-executor.service';

export enum MobileTicketEditMode {
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

  @Output() public readonly onChangeViewer = new EventEmitter();
  @Output() public readonly onObjectEditEnded = new EventEmitter();
  @Output() public readonly onBackgroundColorEditEnded = new EventEmitter();
  @Output() public readonly onClickPreview = new EventEmitter();
  public editMode: MobileTicketEditMode = MobileTicketEditMode.NONE;
  public ssalonColor: SsalonColor = new SsalonColor();

  public mobileTicketEditMode = MobileTicketEditMode;
  public goBackButtonElement: ButtonElement = {
    imgSrc: 'assets/icons/go-back.png',
    label: '뒤로가기',
    value: 0,
  };
  public previewButtonElement: ButtonElement = {
    imgSrc: 'assets/icons/view.png',
    label: '미리보기',
    value: 0,
  };
  public completeButtonElement: ButtonElement = {
    imgSrc: '',
    label: '완료',
    value: 0,
  };

  public editFeatures: ButtonElement[] = [
    {
      imgSrc: 'assets/icons/color-board.png',
      label: '미리보기 뷰',
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

  private _backgroundColorViewLoaded: boolean = false;

  public stickers: ButtonElement[] = [
    {
      imgSrc:
        'https://test-bukkit-240415.s3.ap-northeast-2.amazonaws.com/sticker/image+1.png',
      label: '1',
      value: 1,
    },
    {
      imgSrc:
        'https://test-bukkit-240415.s3.ap-northeast-2.amazonaws.com/sticker/image+1.png',
      label: '2',
      value: 2,
    },
    {
      imgSrc: 'assets/stickers/image_3.png',
      label: '3',
      value: 3,
    },
    {
      imgSrc: 'assets/stickers/image_4.png',
      label: '4',
      value: 4,
    },
    {
      imgSrc: 'assets/stickers/image_5.png',
      label: '5',
      value: 5,
    },
    {
      imgSrc: 'assets/stickers/image_6.png',
      label: '6',
      value: 6,
    },
    {
      imgSrc: 'assets/stickers/image_7.png',
      label: '7',
      value: 7,
    },
  ];
  public editingSticker: fabric.Image | null = null;
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
  public editingIText: fabric.IText | null = null;

  public colorBoard: ButtonElement[] = [];

  public drawingFabricCanvas: fabric.Canvas | null = null;
  private _isDrawing = false;
  private _drawingPoints: Vector2[] = [];
  private _isDrawingFabricCanvasLoaded: boolean = false;
  public drawingFabricPaths: fabric.Path[] = [];

  /** ssalon editor 설정 값 관련
   * @param backgroundColor: SsalonColorElement 배경색
   * @param sslonPhotoAttribute: SsalonImageAttribute 이미지
   * @param sslonStickerAttribute: SsalonImageAttribute 스티커
   * @param sslonTextAttribute: SsalonTextAttribute 텍스트
   * @param sslonPathAttribute: SsalonPathAttribute 그림
   */
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
  public fabricObjects: fabric.Image[] | fabric.IText[] | fabric.Path[] = [];
  /** 완료 버튼 클릭 후, editor view에 적용하기 위해 사용한 기능이 무엇인지 저장 */
  public lastUsedFeature: MobileTicketEditMode = MobileTicketEditMode.NONE;

  constructor(
    private _apiExecutorService: ApiExecutorService,
    private _sceneGraphService: ScenegraphService
  ) {}
  public ngAfterViewInit(): void {
    this.loadDecorationInfo();
  }
  /** 기능 실행 버튼 클릭 후, detailed editor view가 켜진 후에 설정 값들을 적용. */
  public ngAfterViewChecked(): void {
    /* background color feature */
    if (this.backgroundPath && !this._backgroundColorViewLoaded) {
      this.backgroundPath.nativeElement.setAttribute(
        'fill',
        this.backgroundColor.color
      );
      this._backgroundColorViewLoaded = true;
    }

    /** image feature */
    /** sticker feature */

    /* text feature */
    if (this.textEditInput && !this.textFocused) {
      /** 기존에 있던 IText를 수정하는 경우 */
      if (this.fabricObjects.length === 1) {
        this.ssalonTextAttribute = {
          text: (this.fabricObjects[0] as fabric.IText).text!,
          fontFamily: (this.fabricObjects[0] as fabric.IText)
            .fontFamily as string,
          color: (this.fabricObjects[0] as fabric.IText).fill as string,
          textAlign: (this.fabricObjects[0] as fabric.IText)
            .textAlign as string,
        };
        this.textEditInput!.nativeElement.value = this.ssalonTextAttribute.text;
        this.textEditInput!.nativeElement.focus();
        this.textFocused = true;
      }
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
    let decorationInfo = await this._apiExecutorService.getTicket();
    this.backgroundColor = this.ssalonColor.getSsalonColorObjectByColor(
      decorationInfo.backgroundColor
    );
    this.onBackgroundColorEditEnded.emit(this.backgroundColor.color);
  }

  /** 기능 실행 */
  public onClickChangeEditMode(value: MobileTicketEditMode): void {
    this.lastUsedFeature = value;
    switch (value) {
      case MobileTicketEditMode.BACKGROUND_COLOR_CHANGE:
        break;
      case MobileTicketEditMode.PHOTO:
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

  public getStickerArray(): any[][] {
    const stickerArray: any[][] = [];
    for (let i = 0; i < this.stickers.length; i += 3) {
      stickerArray.push(this.stickers.slice(i, i + 3));
    }
    return stickerArray;
  }

  public selectSticker(value: number): void {
    this.editingStickerSrc = this.stickers[value].imgSrc;
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

  /** 완료를 눌러야 fabric object를 생성 */
  public onEndEditObject(): void {
    if (this.lastUsedFeature === MobileTicketEditMode.BACKGROUND_COLOR_CHANGE) {
      this.onBackgroundColorEditEnded.emit(this.backgroundColor.color);
    } else {
      switch (this.lastUsedFeature) {
        case MobileTicketEditMode.PHOTO:
          for (
            let index = 0;
            index < this.ssalonPhotoAttribute.src.length;
            index++
          ) {
            fabric.Image.fromURL(
              this.ssalonPhotoAttribute.src[index],
              (img: fabric.Image) => {
                (this.fabricObjects as fabric.Image[]).push(img);
              }
            );
          }
          break;
        case MobileTicketEditMode.STICKER:
          for (
            let index = 0;
            index < this.ssalonStickerAttribute.src.length;
            index++
          ) {
            fabric.Image.fromURL(
              this.ssalonStickerAttribute.src[index],
              (img: fabric.Image) => {
                (this.fabricObjects as fabric.Image[]).push(img);
              }
            );
          }
          break;
        case MobileTicketEditMode.TEXT:
          if (this.fabricObjects.length === 0) {
            (this.fabricObjects as fabric.IText[]).push(
              new fabric.IText(this.ssalonTextAttribute.text, {
                top: 100,
                left: 100,
                fill: this.ssalonTextAttribute.color,
                textAlign: this.ssalonTextAttribute.textAlign,
                fontFamily: this.ssalonTextAttribute.fontFamily,
              })
            );
          } else {
            (this.fabricObjects[0] as fabric.IText).set(
              'text',
              this.ssalonTextAttribute.text
            );
            (this.fabricObjects[0] as fabric.IText).set(
              'fill',
              this.ssalonTextAttribute.color
            );
            (this.fabricObjects[0] as fabric.IText).set(
              'textAlign',
              this.ssalonTextAttribute.textAlign
            );
            (this.fabricObjects[0] as fabric.IText).set(
              'fontFamily',
              this.ssalonTextAttribute.fontFamily
            );
            this.lastUsedFeature = MobileTicketEditMode.NONE;
          }
          break;
        case MobileTicketEditMode.DRAW:
          break;
        case MobileTicketEditMode.NONE:
          break;
      }
      /** text의 경우, 수정하는 경우가 있는데, 이 함수에서 미리 다 바꾸기 때문에 object를 넘길 필요가 없음.
       * 그렇기 때문에, 미리 editMode를 NONE으로 바꿔버려 null을 보냄.
       * null을 받은 editor viewer는 canvas.renderAll()만 진행.
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
    this.drawingFabricCanvas = new fabric.Canvas(
      this.drawCanvas!.nativeElement
    );
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
    console.log(pointer);
    this._drawingPoints.push(
      new Vector2(pointer.x as number, pointer.y as number)
    );
  }

  private drawPath() {
    const pathData = this._drawingPoints
      .map((point) => `${point.x} ${point.y}`)
      .join(' ');
    const path = new fabric.Path(`M ${pathData}`, {
      fill: 'transparent',
      stroke: 'black',
      strokeWidth: 2,
    });
    this.drawingFabricCanvas!.clear().renderAll();
    this.drawingFabricCanvas!.add(path);
  }
}
