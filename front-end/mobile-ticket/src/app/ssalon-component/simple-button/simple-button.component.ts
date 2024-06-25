import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ButtonElement } from '../circle-toggle-button-group/circle-toggle-button-group.component';
import { NgIf } from '@angular/common';
import { NewButtonElement } from '../simple-toggle-group/simple-toggle-group.component';

@Component({
  selector: 'app-simple-button',
  standalone: true,
  imports: [NgIf],
  templateUrl: './simple-button.component.html',
  styleUrl: './simple-button.component.scss',
})
export class SimpleButtonComponent {
  @Input() heightSize: number = 20;
  @Input() buttonElement: NewButtonElement | null = null;
  @Input() type: 'img' | 'text' = 'img';
  @Input() highContrast: boolean = false;
  @Output() public readonly onClickEvent = new EventEmitter();
}
