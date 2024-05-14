import {
  Component,
  EventEmitter,
  Input,
  Output,
  ViewChildren,
} from '@angular/core';
import { ButtonElement } from '../circle-toggle-button-group/circle-toggle-button-group.component';
import { CommonModule, NgClass, NgFor } from '@angular/common';

export interface NewButtonElement {
  imgSrc?: string;
  imgHeight?: number;
  fontFamily?: string;
  solid?: boolean;
  unselectedFontColor?: string;
  unselectedBackgroundColor?: string;
  selectedFontColor?: string;
  selectedBackgroundColor?: string;
  value: number;
  label: string;
  selected: boolean;
}
@Component({
  selector: 'app-simple-toggle',
  standalone: true,
  imports: [CommonModule, NgFor, NgClass],
  templateUrl: './simple-toggle-group.component.html',
  styleUrl: './simple-toggle-group.component.scss',
})
export class SimpleToggleGroupComponent {
  @Input() label: string = '';
  @Input() extraLabel: string = '';
  @Input() extraLabelColor: string = '';
  @Input() componentWidth: number = 350;
  @Input() buttonElements: NewButtonElement[] = [];
  @Input() buttonWidth: number = 350;
  @Input() buttonHeight: number = 48;
  @Input() fontSize: number = 16;
  @Input() columnNum: number = 3; // -1인경우 한줄로 나열
  @Input() multiSelection: boolean = false;
  @Input() align: 'left' | 'center' | 'right' = 'center';

  @Output() public readonly onClickToggleButtonEvent = new EventEmitter();
  public modifiedButtonElements: NewButtonElement[][] = [];
  constructor() {}
  public ngOnInit(): void {
    this._setButtonLayout();
  }

  private _setButtonLayout() {
    if (this.columnNum !== -1) {
      let result = [];
      for (let i = 0; i < this.buttonElements.length; i += this.columnNum) {
        result.push(this.buttonElements.slice(i, i + this.columnNum));
      }
      this.modifiedButtonElements = result;
    } else {
      this.modifiedButtonElements = [this.buttonElements];
    }
  }

  public getClass(element: NewButtonElement) {
    if (element.solid) {
      return element.selected
        ? 'selected-button-solid'
        : 'unselected-button-solid';
    } else {
      return element.selected ? 'selected-button' : 'unselected-button';
    }
  }

  public onClickToggleButton(value: number): void {
    if (!this.multiSelection) {
      this.buttonElements.forEach((element) => (element.selected = false));
      this.buttonElements.find((element) => element.value === value)!.selected =
        true;
    } else {
      this.buttonElements.find((element) => element.value === value)!.selected =
        !this.buttonElements.find((element) => element.value === value)!
          .selected;
    }
    this.onClickToggleButtonEvent.emit(value);
  }
}
