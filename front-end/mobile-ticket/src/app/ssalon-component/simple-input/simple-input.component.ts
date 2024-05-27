import { NgIf } from '@angular/common';
import {
  ChangeDetectorRef,
  Component,
  ElementRef,
  EventEmitter,
  Input,
  Output,
  ViewChild,
} from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-simple-input',
  standalone: true,
  imports: [NgIf, FormsModule],
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
  @Input() longTextHeight: number = 120;
  @Input() type:
    | 'text'
    | 'longText'
    | 'datetime-local'
    | 'number'
    | 'category'
    | 'chat-input' = 'text';
  @Input() innerText: string | number = '';
  @Input() enableCheckbox: boolean = false;
  @Input() checkBoxLabel: string = '';

  @Output() public readonly onChangeEvent = new EventEmitter();
  @Output() public readonly onClickEvent = new EventEmitter();
  @Output() public readonly onClickCheckboxEvent = new EventEmitter();
  @Output() public readonly onClickChatSendButtonEvent = new EventEmitter();

  public isChecked: boolean = false;
  constructor(private cd: ChangeDetectorRef) {}
  public onClickInput(): void {
    this.onClickEvent.emit(this.innerText);
  }
  public onChangeInput(event: any): void {
    this.innerText = event.target.value;
    this.cd.detectChanges();
    this.onChangeEvent.emit(this.innerText);
  }
  public onClickCheckbox(event: any): void {
    this.isChecked = event.target.checked;
    this.onClickCheckboxEvent.emit(this.isChecked);
  }
  public onClickChatSendButton(): void {
    this.onClickChatSendButtonEvent.emit();
  }
}
