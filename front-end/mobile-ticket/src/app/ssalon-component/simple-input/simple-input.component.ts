import { NgIf } from '@angular/common';
import { Component, Input } from '@angular/core';

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
  constructor() {}
}
