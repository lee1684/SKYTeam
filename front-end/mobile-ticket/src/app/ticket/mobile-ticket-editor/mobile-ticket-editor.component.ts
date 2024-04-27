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
  @ViewChild('textEditInput', { static: false })
  textEditInput: ElementRef | null = null;

  @Output() public readonly onChangeViewer = new EventEmitter();
  @Output() public readonly onEditSsalonObject = new EventEmitter();
  @Output() public readonly onClickPreview = new EventEmitter();
  public editMode: MobileTicketEditMode = MobileTicketEditMode.NONE;
  constructor(private _sceneGraphService: ScenegraphService) {}

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
  public stickers: number[] = [
    1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2, 3, 4, 5,
    6, 7, 8, 9, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9,
    10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2, 3,
    4, 5, 6, 7, 8, 9, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
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
    { imgSrc: '', label: '폰트1', value: 0 },
    { imgSrc: '', label: '폰트2', value: 1 },
    { imgSrc: '', label: '폰트3', value: 2 },
    { imgSrc: '', label: '폰트4', value: 3 },
    { imgSrc: '', label: '폰트5', value: 4 },
    { imgSrc: '', label: '폰트6', value: 5 },
    { imgSrc: '', label: '폰트7', value: 6 },
    { imgSrc: '', label: '폰트8', value: 7 },
    { imgSrc: '', label: '폰트1', value: 8 },
    { imgSrc: '', label: '폰트2', value: 9 },
    { imgSrc: '', label: '폰트3', value: 10 },
    { imgSrc: '', label: '폰트4', value: 11 },
    { imgSrc: '', label: '폰트5', value: 12 },
    { imgSrc: '', label: '폰트6', value: 13 },
    { imgSrc: '', label: '폰트7', value: 14 },
    { imgSrc: '', label: '폰트8', value: 15 },
  ];

  public ssalonColor: SsalonColor = new SsalonColor();
  public textColor: SsalonColorElement = this.ssalonColor.BLACK;
  public colorBoard: ButtonElement[] = [];
  public fabricObject: any = null;
  public ngAfterViewInit(): void {}
  public ngAfterViewChecked(): void {
    if (this.textEditInput !== undefined && !this.textFocused) {
      this.textEditInput!.nativeElement.focus();
      this.textFocused = true;
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

  public onClickChangeTextAlignButton(value: TextAlign): void {
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
  }

  public onChangeSsaslonTextAttribute(attributeName: string, value: any): void {
    console.log(value);
  }

  public onClickChangeTextColor(value: SsalonColorEnum): void {
    this.textColor = this.ssalonColor.getSsalonColorObjectByValue(value);
  }

  public onBlurInput(): void {
    this.onEditSsalonObject.emit(this.fabricObject);
  }
}
