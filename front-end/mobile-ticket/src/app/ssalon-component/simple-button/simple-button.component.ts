import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-simple-button',
  standalone: true,
  imports: [],
  templateUrl: './simple-button.component.html',
  styleUrl: './simple-button.component.scss',
})
export class SimpleButtonComponent {
  @Input() label: string = '';
}
