import { NgIf } from '@angular/common';
import {
  Component,
  ElementRef,
  EventEmitter,
  Input,
  Output,
  ViewChild,
} from '@angular/core';

@Component({
  selector: 'app-simple-input',
  standalone: true,
  imports: [NgIf],
  templateUrl: './simple-input.component.html',
  styleUrl: './simple-input.component.scss',
})
export class SimpleInputComponent {
  @ViewChild('textDiv', { static: false }) textDiv: ElementRef | null = null;
  @Input() label: string = '';
  @Input() extraLabel: string = '';
  @Input() extraLabelColor: string = '';
  @Input() placeholder: string = '';
  @Input() maxWidth: number = 300;
  @Input() type:
    | 'text'
    | 'longText'
    | 'datetime-local'
    | 'number'
    | 'category' = 'text';
  @Input() innerText: string | number = '';
  @Input() enableCheckbox: boolean = false;
  @Input() checkBoxLabel: string = '';

  @Output() public readonly onChangeEvent = new EventEmitter();
  @Output() public readonly onClickEvent = new EventEmitter();
  @Output() public readonly onClickCheckboxEvent = new EventEmitter();

  public isChecked: boolean = false;
  constructor() {}
  public onClickInput(): void {
    this.onClickEvent.emit(this.textDiv?.nativeElement.value);
  }
  public onChangeInput(event: any): void {
    this.onChangeEvent.emit(this.textDiv?.nativeElement.value);
  }
  public onClickCheckbox(event: any): void {
    this.isChecked = event.target.checked;
    this.onClickCheckboxEvent.emit(this.isChecked);
  }
}
