import {
  Component,
  ElementRef,
  EventEmitter,
  Input,
  Output,
  QueryList,
  ViewChildren,
} from '@angular/core';
import { SimpleToggleButtonElement } from '../simple-toggle-button-group/simple-toggle-button-group.component';
import { NgFor } from '@angular/common';

@Component({
  selector: 'app-circle-toggle-button-group',
  standalone: true,
  imports: [NgFor],
  templateUrl: './circle-toggle-button-group.component.html',
  styleUrl: './circle-toggle-button-group.component.scss',
})
export class CircleToggleButtonGroupComponent {
  @ViewChildren('toggleButton')
  buttons: QueryList<ElementRef> | null = null;

  @Input() public elements: SimpleToggleButtonElement[] = [];
  @Input() public selectedValue: number = 0;
  @Output() public readonly onClickToggleButtonEvent = new EventEmitter();

  public buttonWidth: string = '30%';
  constructor() {}
  public ngOnInit(): void {
    this.buttonWidth = `${100 / this.elements.length}%`;
  }
  public ngAfterViewInit(): void {
    const buttonsArray = this.buttons!.toArray();
    buttonsArray![this.selectedValue].nativeElement.classList.add('selected');
  }
  /** 토글 버튼을 누를 경우 호출되는 메서드
   * @param index 인덱스
   */
  public onClickToggleButton(value: number): void {
    if (this.selectedValue === value) return;

    const buttonsArray = this.buttons!.toArray();
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
}
