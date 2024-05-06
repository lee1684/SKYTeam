import {
  Component,
  ViewChildren,
  QueryList,
  ElementRef,
  Input,
  Output,
  EventEmitter,
} from '@angular/core';
import { SimpleToggleButtonElement } from '../simple-toggle-button-group/simple-toggle-button-group.component';
import { NgFor, NgIf } from '@angular/common';

export interface ButtonElement {
  imgSrc: string;
  value: number;
  label: string;
}
@Component({
  selector: 'app-circle-toggle-button-group',
  standalone: true,
  imports: [NgFor, NgIf],
  templateUrl: './circle-toggle-button-group.component.html',
  styleUrl: './circle-toggle-button-group.component.scss',
})
export class CircleToggleButtonGroupComponent {
  @ViewChildren('toggleButton')
  buttons: QueryList<ElementRef> | null = null;

  @Input() public elements: ButtonElement[] = [];
  @Input() public selectedValues: number[] = [];
  @Input() public noneStatusValue: number = -1;
  @Input() public outerSize: string = '40px';
  @Input() public innerSize: number = 20;
  @Input() public enableMultipleSelection: boolean = false;
  @Output() public readonly onClickToggleButtonEvent = new EventEmitter();

  constructor() {}
  public ngOnInit(): void {}
  public ngAfterViewInit(): void {
    const buttonsArray = this.buttons!.toArray();
    console.log(this.selectedValues);
    for (let selectedValue of this.selectedValues) {
      buttonsArray[selectedValue].nativeElement.classList.add('selected');
    }
  }
  /** 토글 버튼을 누를 경우 호출되는 메서드
   * @param index 인덱스
   */
  public onClickToggleButton(value: number): void {
    const buttonsArray = this.buttons!.toArray();
    if (this.enableMultipleSelection) {
      if (this.selectedValues.includes(value)) {
        buttonsArray![value].nativeElement.classList.remove('selected');
        this.selectedValues.splice(this.selectedValues.indexOf(value), 1);
      } else {
        buttonsArray![value].nativeElement.classList.add('selected');
        this.selectedValues.push(value);
      }
    } else {
      if (this.selectedValues.includes(value)) return;

      for (let idx in buttonsArray) {
        if (Number(idx) !== value) {
          buttonsArray![Number(idx)].nativeElement.classList.remove('selected');
        } else {
          buttonsArray![value].nativeElement.classList.add('selected');
        }
      }
      this.selectedValues = [value];
    }

    /* 부모 컴포넌트에 이벤트 전달 */
    this.onClickToggleButtonEvent.emit(value);
  }

  public setUnselectedStatus(): void {
    const buttonsArray = this.buttons!.toArray();
    for (let idx in buttonsArray) {
      buttonsArray![Number(idx)].nativeElement.classList.remove('selected');
    }
    this.selectedValues = [];
    this.onClickToggleButtonEvent.emit(this.noneStatusValue);
  }

  public getImgSrc(originalImgSrc: string, value: number): string {
    let imgSrc = originalImgSrc;
    if (this.selectedValues.includes(value)) {
      imgSrc = imgSrc.replace('.png', '_selected.png');
    }
    return imgSrc;
  }
}
