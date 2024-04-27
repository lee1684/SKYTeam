import {
  Component,
  ElementRef,
  EventEmitter,
  Input,
  Output,
  QueryList,
  ViewChildren,
} from '@angular/core';
import { ButtonElement } from '../circle-toggle-button-group/circle-toggle-button-group.component';
import { NgFor, NgIf } from '@angular/common';

export class SsalonColor {
  public BLACK: SsalonColorElement = {
    color: '#000000',
    value: SsalonColorEnum.BLACK,
  };
  public WHITE: SsalonColorElement = {
    color: '#FFFFFF',
    value: SsalonColorEnum.WHITE,
  };
  public RED: SsalonColorElement = {
    color: '#FF0000',
    value: SsalonColorEnum.RED,
  };
  public GREEN: SsalonColorElement = {
    color: '#00FF00',
    value: SsalonColorEnum.GREEN,
  };
  public BLUE: SsalonColorElement = {
    color: '#0000FF',
    value: SsalonColorEnum.BLUE,
  };
  public YELLOW: SsalonColorElement = {
    color: '#FFFF00',
    value: SsalonColorEnum.YELLOW,
  };
  public CYAN: SsalonColorElement = {
    color: '#00FFFF',
    value: SsalonColorEnum.CYAN,
  };
  public MAGENTA: SsalonColorElement = {
    color: '#FF00FF',
    value: SsalonColorEnum.MAGENTA,
  };
  public GRAY: SsalonColorElement = {
    color: '#808080',
    value: SsalonColorEnum.GRAY,
  };
  public LIGHT_GRAY: SsalonColorElement = {
    color: '#C0C0C0',
    value: SsalonColorEnum.LIGHT_GRAY,
  };
  public DARK_GRAY: SsalonColorElement = {
    color: '#A9A9A9',
    value: SsalonColorEnum.DARK_GRAY,
  };
  public ORANGE: SsalonColorElement = {
    color: '#FFA500',
    value: SsalonColorEnum.ORANGE,
  };
  public PINK: SsalonColorElement = {
    color: '#FFC0CB',
    value: SsalonColorEnum.PINK,
  };
  public PURPLE: SsalonColorElement = {
    color: '#800080',
    value: SsalonColorEnum.PURPLE,
  };
  public BROWN: SsalonColorElement = {
    color: '#A52A2A',
    value: SsalonColorEnum.BROWN,
  };
  public SSALON: SsalonColorElement = {
    color: '#006BFF',
    value: SsalonColorEnum.SSALON,
  };
  public SsalonColors: SsalonColorElement[] = [
    this.BLACK,
    this.WHITE,
    this.RED,
    this.GREEN,
    this.BLUE,
    this.YELLOW,
    this.CYAN,
    this.MAGENTA,
    this.GRAY,
    this.LIGHT_GRAY,
    this.DARK_GRAY,
    this.ORANGE,
    this.PINK,
    this.PURPLE,
    this.BROWN,
    this.SSALON,
  ];
  constructor() {}
  public getColorByValue(value: SsalonColorEnum): string {
    return this.SsalonColors.find((color) => color.value === value)!.color;
  }
  public getSsalonColorObjectByValue(
    value: SsalonColorEnum
  ): SsalonColorElement {
    return this.SsalonColors.find((color) => color.value === value)!;
  }
}
export interface SsalonColorElement {
  color: string;
  value: SsalonColorEnum;
}

export enum SsalonColorEnum {
  BLACK,
  WHITE,
  RED,
  GREEN,
  BLUE,
  YELLOW,
  CYAN,
  MAGENTA,
  GRAY,
  LIGHT_GRAY,
  DARK_GRAY,
  ORANGE,
  PINK,
  PURPLE,
  BROWN,
  SSALON,
}

@Component({
  selector: 'app-color-board',
  standalone: true,
  imports: [NgFor, NgIf],
  templateUrl: './color-board.component.html',
  styleUrl: './color-board.component.scss',
})
export class ColorBoardComponent {
  @ViewChildren('toggleButton')
  buttons: QueryList<ElementRef> | null = null;

  @Input() public selectedValue: number = 0;
  @Input() public noneStatusValue: number = 0;
  @Input() public outerSize: string = '30px';
  @Input() public innerSize: string = '20px';
  @Input() public columnNum: number = 10;
  @Output() public readonly onClickToggleButtonEvent = new EventEmitter();

  public ssalonColor: SsalonColor = new SsalonColor();
  public modifiedSsalonColors: SsalonColorElement[][] = [];

  constructor() {}
  public ngOnInit(): void {
    var result = [];

    // 8열로 채우기
    for (
      var i = 0;
      i < this.ssalonColor.SsalonColors.length;
      i += this.columnNum
    ) {
      result.push(this.ssalonColor.SsalonColors.slice(i, i + this.columnNum));
    }
    this.modifiedSsalonColors = result;
  }
  public ngAfterViewInit(): void {
    const buttonsArray = this.buttons!.toArray();
    buttonsArray[this.selectedValue].nativeElement.classList.add('selected');
  }
  /** 토글 버튼을 누를 경우 호출되는 메서드
   * @param index 인덱스
   */
  public onClickToggleButton(value: number): void {
    const buttonsArray = this.buttons!.toArray();
    if (this.selectedValue === value) return;

    for (let idx in buttonsArray) {
      if (Number(idx) !== value) {
        buttonsArray![Number(idx)].nativeElement.classList.remove('selected');
      } else {
        buttonsArray![value].nativeElement.classList.add('selected');
      }
    }
    this.selectedValue = value;

    /* 부모 컴포넌트에 이벤트 전달 */
    this.onClickToggleButtonEvent.emit(value);
  }

  public getImgSrc(originalImgSrc: string, value: number): string {
    let imgSrc = originalImgSrc;
    if (this.selectedValue === value) {
      imgSrc = imgSrc.replace('.png', '_selected.png');
    }
    return imgSrc;
  }
}
