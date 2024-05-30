import { Component, Input } from '@angular/core';
import { ImageRowContainerComponent } from '../image-row-container/image-row-container.component';

@Component({
  selector: 'app-image-upload-container',
  standalone: true,
  imports: [ImageRowContainerComponent],
  templateUrl: './image-upload-container.component.html',
  styleUrl: './image-upload-container.component.scss',
})
export class ImageUploadContainerComponent {
  @Input() imageUrls: string[] = [];
  constructor() {}
  public ngOnInit() {}
}
