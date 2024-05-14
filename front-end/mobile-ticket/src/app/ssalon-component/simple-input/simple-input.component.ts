import { NgIf } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-simple-input',
  standalone: true,
  imports: [NgIf],
  templateUrl: './simple-input.component.html',
  styleUrl: './simple-input.component.scss',
})
export class SimpleInputComponent {
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

  @Output() public readonly onChangeEvent = new EventEmitter();
  constructor() {}
  public onClickInput(): void {
    console.log('click');
  }
  public onChangeInput(event: any): void {
    console.log(event.target.value);
  }
}
