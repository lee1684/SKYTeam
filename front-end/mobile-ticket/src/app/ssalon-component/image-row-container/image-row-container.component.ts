import { NgFor, NgIf } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NewButtonElement } from '../simple-toggle-group/simple-toggle-group.component';

export interface Ticket {
  ticketThumb: string;
  backgroundColor: string;
  moimId: number;
  categoryName: string;
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
  @Input() tickets: Ticket[] = [];
  public backgroundColor: string = '#000000';
  @Output() public readonly onClickImageEvent = new EventEmitter();
  constructor() {}
  public onClickImage(value: number): void {
    this.onClickImageEvent.emit(value);
  }
  public getThumbSrc(moimId: number): string {
    return (
      'https://test-bukkit-240415.s3.ap-northeast-2.amazonaws.com/Thumbnails/' +
      moimId +
      '/Thumb-' +
      moimId +
      '.png'
    );
  }
}
