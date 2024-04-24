import {
  Component,
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
import { ColorBoardComponent } from '../../ssalon-component/color-board/color-board.component';

export enum MobileTicketEditMode {
  BACKGROUND_COLOR_CHANGE,
  PHOTO,
  STICKER,
  TEXT,
  DRAW,
  NONE,
}

@Component({
  selector: 'app-mobile-ticket-editor',
  standalone: true,
  imports: [
    SimpleButtonComponent,
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
  @Output() public readonly onChangeViewer = new EventEmitter();
  @Output() public readonly onEditSsalonObject = new EventEmitter();
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

  public colorBoard: ButtonElement[] = [];
  public fabricObject: any = null;
  ngAfterViewInit(): void {}

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

  public onClickToggleButton(value: MobileTicketEditMode): void {
    this.editMode = value;
    this.onChangeViewer.emit(value);
  }

  public onClickEndDetailedEditViewer(): void {
    this.editMode = MobileTicketEditMode.NONE;
    this.editFeatureButtons!.setUnselectedStatus();
    this.onChangeViewer.emit(MobileTicketEditMode.NONE);
  }

  public onChangeSsaslonTextAttribute(attributeName: string, value: any): void {
    console.log(value);
  }
  public onBlurInput(): void {
    this.onEditSsalonObject.emit(this.fabricObject);
  }
}
