import {
  Component,
  ElementRef,
  EventEmitter,
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
  PREVIEW,
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
  @ViewChild('temp', { static: true }) temp: ElementRef | null = null;
  @Output() public readonly onChangeViewer = new EventEmitter();
  @Output() public readonly onAddObject = new EventEmitter();
  public editMode: MobileTicketEditMode = MobileTicketEditMode.PREVIEW;
  constructor(private _sceneGraphService: ScenegraphService) {}

  public mobileTicketEditMode = MobileTicketEditMode;
  public goBackButtonElement: ButtonElement = {
    imgSrc: 'assets/icons/go-back.png',
    label: '뒤로가기',
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
      value: MobileTicketEditMode.PREVIEW,
    },
  ];

  public colors: ButtonElement[] = [
    {
      imgSrc: 'assets/icons/color-board.png',
      label: '미리보기 뷰',
      value: 0,
    },
    {
      imgSrc: 'assets/icons/color-board.png',
      label: '미리보기 뷰',
      value: 1,
    },
    {
      imgSrc: 'assets/icons/color-board.png',
      label: '미리보기 뷰',
      value: 2,
    },
    {
      imgSrc: 'assets/icons/color-board.png',
      label: '미리보기 뷰',
      value: 3,
    },
    {
      imgSrc: 'assets/icons/color-board.png',
      label: '미리보기 뷰',
      value: 4,
    },
    {
      imgSrc: 'assets/icons/color-board.png',
      label: '미리보기 뷰',
      value: 5,
    },
    {
      imgSrc: 'assets/icons/color-board.png',
      label: '미리보기 뷰',
      value: 6,
    },
    {
      imgSrc: 'assets/icons/color-board.png',
      label: '미리보기 뷰',
      value: 7,
    },
    {
      imgSrc: 'assets/icons/color-board.png',
      label: '미리보기 뷰',
      value: 8,
    },
    {
      imgSrc: 'assets/icons/color-board.png',
      label: '미리보기 뷰',
      value: 9,
    },
    {
      imgSrc: 'assets/icons/color-board.png',
      label: '미리보기 뷰',
      value: 10,
    },
    {
      imgSrc: 'assets/icons/color-board.png',
      label: '미리보기 뷰',
      value: 11,
    },
    {
      imgSrc: 'assets/icons/color-board.png',
      label: '미리보기 뷰',
      value: 12,
    },
    {
      imgSrc: 'assets/icons/color-board.png',
      label: '미리보기 뷰',
      value: 13,
    },
  ];

  public colorBoard: ButtonElement[] = [];
  public fabricObject: any = null;
  ngAfterViewInit(): void {}
  public onClickFocusFrontButton(): void {
    this._sceneGraphService.focusFront();
  }
  public onClickToggleButton(value: MobileTicketEditMode): void {
    this.editMode = value;
    this.onChangeViewer.emit(value);
  }
}
