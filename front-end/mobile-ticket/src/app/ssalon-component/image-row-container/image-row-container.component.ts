import { NgFor, NgIf } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NewButtonElement } from '../simple-toggle-group/simple-toggle-group.component';

export interface Ticket {
  thumbnailUrl: string;
  backgroundColor: string;
  fabric: any;
  moimId: number;
}
@Component({
  selector: 'app-image-row-container',
  standalone: true,
  imports: [NgIf, NgFor],
  templateUrl: './image-row-container.component.html',
  styleUrl: './image-row-container.component.scss',
})
export class ImageRowContainerComponent {
  @Input() label: string = '';
  @Input() extraLabel: string = '';
  @Input() extraLabelColor: string = '';
  @Input() moreButton: boolean = false;
  @Input() imageHeight: number = 200;
  @Input() isTicketContainer: boolean = true;
  @Input() images: NewButtonElement[] = [
    { imgSrc: 'assets/heart.png', value: 0, label: 'heart', selected: false },
  ];
  @Input() tickets: Ticket[] = [
    {
      thumbnailUrl:
        'https://test-bukkit-240415.s3.ap-northeast-2.amazonaws.com/Thumbnails/2/0003e06d-16bd-48d1-b9cf-8f68d369aa7c.png',
      backgroundColor: 'red',
      fabric: {},
      moimId: 0,
    },
    {
      thumbnailUrl:
        'https://test-bukkit-240415.s3.ap-northeast-2.amazonaws.com/Thumbnails/2/0003e06d-16bd-48d1-b9cf-8f68d369aa7c.png',
      backgroundColor: 'black',
      fabric: {},
      moimId: 1,
    },
    {
      thumbnailUrl:
        'https://test-bukkit-240415.s3.ap-northeast-2.amazonaws.com/Thumbnails/2/0003e06d-16bd-48d1-b9cf-8f68d369aa7c.png',
      backgroundColor: 'gray',
      fabric: {},
      moimId: 2,
    },
    {
      thumbnailUrl:
        'https://test-bukkit-240415.s3.ap-northeast-2.amazonaws.com/Thumbnails/2/0003e06d-16bd-48d1-b9cf-8f68d369aa7c.png',
      backgroundColor: 'green',
      fabric: {},
      moimId: 3,
    },
    {
      thumbnailUrl:
        'https://test-bukkit-240415.s3.ap-northeast-2.amazonaws.com/Thumbnails/2/0003e06d-16bd-48d1-b9cf-8f68d369aa7c.png',
      backgroundColor: 'yellow',
      fabric: {},
      moimId: 4,
    },
    {
      thumbnailUrl:
        'https://test-bukkit-240415.s3.ap-northeast-2.amazonaws.com/Thumbnails/2/0003e06d-16bd-48d1-b9cf-8f68d369aa7c.png',
      backgroundColor: 'purple',
      fabric: {},
      moimId: 5,
    },
  ];

  @Output() public readonly onClickImageEvent = new EventEmitter();
  constructor() {}
  public onClickImage(value: number): void {
    this.onClickImageEvent.emit(value);
  }
}
