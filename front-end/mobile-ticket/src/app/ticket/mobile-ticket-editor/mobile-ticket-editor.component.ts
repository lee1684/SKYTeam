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

export interface TextAttribute {
  text: string;
  fontFamily: string;
  color: string;
  textAlign: string;
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

  public backgroundColor: SsalonColorElement = this.ssalonColor.LIGHT_GRAY;
  private _backgroundColorViewLoaded: boolean = false;

  public stickers: number[] = [
    1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2, 3, 4, 5,
    6, 7, 8, 9, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9,
    10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2, 3,
    4, 5, 6, 7, 8, 9, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2, 3, 4, 5, 6, 7, 8,
    9, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2,
    3, 4, 5, 6, 7, 8, 9, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2, 3, 4, 5, 6, 7,
    8, 9, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1,
    2, 3, 4, 5, 6, 7, 8, 9, 10,
  ];

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
  public fabricObject: any = null;

  public drawingFabricCanvas: fabric.Canvas | null = null;
  private _isDrawing = false;
  private _drawingPoints: Vector2[] = [];
  private _isDrawingFabricCanvasLoaded: boolean = false;

  constructor(private _sceneGraphService: ScenegraphService) {}
  public ngAfterViewInit(): void {}
  public ngAfterViewChecked(): void {
    /* background color feature */
    if (this.backgroundPath && !this._backgroundColorViewLoaded) {
      this.backgroundPath.nativeElement.setAttribute(
        'fill',
        this.backgroundColor.color
      );
      this._backgroundColorViewLoaded = true;
    }

    /* text feature */
    if (this.textEditInput !== undefined && !this.textFocused) {
      if (this.editingIText === null && this.isTextAddMode) {
        this.editingIText = new fabric.IText('', {
          top: 100,
          left: 100,
          fill: '#FFFFFF',
          textAlign: 'left',
          fontFamily: 'Josefin Sans',
        });
      }
      if (this.editingIText !== null) {
        this.textColor = this.ssalonColor.getSsalonColorObjectByColor(
          this.editingIText!.fill as string
        );
        this.textAlign = this.editingIText!.textAlign as
          | 'left'
          | 'center'
          | 'right';
        this.textEditInput!.nativeElement.value = this.editingIText!.text;
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

  public getStickerArray(): any[][] {
    const stickerArray: any[][] = [];
    for (let i = 0; i < this.stickers.length; i += 3) {
      stickerArray.push(this.stickers.slice(i, i + 3));
    }
    return stickerArray;
  }
  public onClickFocusFrontButton(): void {
    this._sceneGraphService.focusFront();
  }

  public onClickChangeEditMode(value: MobileTicketEditMode): void {
    switch (value) {
      case MobileTicketEditMode.BACKGROUND_COLOR_CHANGE:
        break;
      case MobileTicketEditMode.PHOTO:
        break;
      case MobileTicketEditMode.STICKER:
        break;
      case MobileTicketEditMode.TEXT:
        this.isTextAddMode = true;
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

  public onClickPreviewButton(): void {
    this.onClickPreview.emit();
  }

  public onClickEndDetailedEditViewer(): void {
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

  /**
   * @param attributeName ={
   * 'text': string,
   * 'fontFamily': string,
   * 'color': string,
   * 'textAlign': string,
   * }
   * @param value
   */
  public onChangeSsaslonTextAttribute(attributeName: string, value: any): void {
    if (this.editingIText !== null) {
      switch (attributeName) {
        case 'text':
          let editedText = this.textEditInput!.nativeElement.value;
          this.editingIText.set('text', editedText);
          break;
        case 'fontFamily':
          this.fontFamily = value;
          this.editingIText.set('fontFamily', value);
          break;
        case 'color':
          this.textColor = this.ssalonColor.getSsalonColorObjectByValue(value);
          this.editingIText.set('fill', this.textColor.color);
          break;
        case 'textAlign':
          switch (value) {
            case TextAlign.LEFT:
              this.textAlign = 'left';
              break;
            case TextAlign.CENTER:
              this.textAlign = 'center';
              break;
            case TextAlign.RIGHT:
              this.textAlign = 'right';
              break;
          }
          this.editingIText.set('textAlign', this.textAlign);
          break;
      }
    }
  }

  public onEndEditObject(): void {
    /* 배경색 변경 모드 */
    if (this.editingIText === null) {
      this.onBackgroundColorEditEnded.emit(this.backgroundColor.color);
    } else {
      /* fabric.js 오브젝트 편집 모드 */
      if (this.isTextAddMode && this.editingIText.text !== '') {
        this.onObjectEditEnded.emit(this.editingIText);
      } else {
        this.onObjectEditEnded.emit(null);
      }
      this.editingIText = null;
    }
    this._isDrawingFabricCanvasLoaded = false;
  }

  public initDrawingCanvas(): void {
    this.drawingFabricCanvas = new fabric.Canvas(
      this.drawCanvas!.nativeElement
    );
    this.drawingFabricCanvas.add(
      new fabric.Text('hello', { left: 100, top: 100 })
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
