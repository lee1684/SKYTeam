import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NewButtonElement } from '../simple-toggle-group/simple-toggle-group.component';
import { NgClass, NgFor } from '@angular/common';

@Component({
  selector: 'app-square-button',
  standalone: true,
  imports: [NgFor, NgClass],
  templateUrl: './square-button.component.html',
  styleUrl: './square-button.component.scss',
})
export class SquareButtonComponent {
  @Input() buttonElements: NewButtonElement[] = [
    { selected: true, value: 0, label: '다음' },
  ];

  @Output() public readonly onClickEvent = new EventEmitter();

  constructor() {}

  public onClickButton(value: number) {
    this.onClickEvent.emit(value);
  }
  public getClass(value: number) {
    return this.buttonElements.find((element) => {
      return element.value === value;
    })!.selected
      ? 'ready-button hover-button'
      : 'wait-button hover-button';
  }
}
