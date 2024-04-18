import {
  Component,
  ElementRef,
  EventEmitter,
  Output,
  ViewChild,
} from '@angular/core';
import { SimpleButtonComponent } from '../../ssalon-component/simple-button/simple-button.component';
import { NgFor } from '@angular/common';
import { MobileTicket } from '../../service/mobile-ticket';
import { ScenegraphService } from '../../service/scenegraph.service';
import {
  ButtonElement,
  CircleToggleButtonGroupComponent,
} from '../../ssalon-component/circle-toggle-button-group/circle-toggle-button-group.component';
import { MobileTicketViewMode } from '../ticket.component';

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
  imports: [SimpleButtonComponent, CircleToggleButtonGroupComponent, NgFor],
  templateUrl: './mobile-ticket-editor.component.html',
  styleUrl: './mobile-ticket-editor.component.scss',
})
export class MobileTicketEditorComponent {
  @ViewChild('temp', { static: true }) temp: ElementRef | null = null;
  @Output() public readonly onChangeViewer = new EventEmitter();
  @Output() public readonly onAddObject = new EventEmitter();
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
  public fabricObject: any = null;
  ngAfterViewInit(): void {}
  public onClickFocusFrontButton(): void {
    this._sceneGraphService.focusFront();
  }
  public onClickToggleButton(value: MobileTicketEditMode): void {
    this.onChangeViewer.emit(value);
  }
}
