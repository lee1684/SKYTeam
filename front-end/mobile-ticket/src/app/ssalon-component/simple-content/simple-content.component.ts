import { Component, Input } from '@angular/core';
import { ImageRowContainerComponent } from '../image-row-container/image-row-container.component';
import { NewButtonElement } from '../simple-toggle-group/simple-toggle-group.component';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-simple-content',
  standalone: true,
  imports: [NgIf, ImageRowContainerComponent],
  templateUrl: './simple-content.component.html',
  styleUrl: './simple-content.component.scss',
})
export class SimpleContentComponent {
  @Input() title: string = '';
  @Input() content: string = '';
  @Input() images: NewButtonElement[] = [];
  constructor() {}
}
