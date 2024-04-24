import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ButtonElement } from '../circle-toggle-button-group/circle-toggle-button-group.component';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-simple-button',
  standalone: true,
  imports: [NgIf],
  templateUrl: './simple-button.component.html',
  styleUrl: './simple-button.component.scss',
})
export class SimpleButtonComponent {
  @Input() heightSize: number = 20;
  @Input() buttonElement: ButtonElement | null = null;
  @Input() type: 'img' | 'text' = 'img';
  @Output() public readonly onClickEvent = new EventEmitter();
}
