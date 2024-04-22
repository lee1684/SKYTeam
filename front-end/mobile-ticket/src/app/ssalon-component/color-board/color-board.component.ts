import { Component } from '@angular/core';
import {
  ButtonElement,
  CircleToggleButtonGroupComponent,
} from '../circle-toggle-button-group/circle-toggle-button-group.component';
import { MobileTicketEditMode } from '../../ticket/mobile-ticket-editor/mobile-ticket-editor.component';

@Component({
  selector: 'app-color-board',
  standalone: true,
  imports: [CircleToggleButtonGroupComponent],
  templateUrl: './color-board.component.html',
  styleUrl: './color-board.component.scss',
})
export class ColorBoardComponent {
  public editFeatures: ButtonElement[] = [
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
}
