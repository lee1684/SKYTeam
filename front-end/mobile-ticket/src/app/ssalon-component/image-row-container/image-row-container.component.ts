import { NgFor, NgIf } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

export interface Ticket {
  thumbnailUrl: string;
  backgroundColor: string;
  fabric: any;
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
  @Input() tickets: Ticket[] = [
    { thumbnailUrl: 'assets/heart.png', backgroundColor: 'red', fabric: {} },
    { thumbnailUrl: 'assets/heart.png', backgroundColor: 'red', fabric: {} },
    { thumbnailUrl: 'assets/heart.png', backgroundColor: 'red', fabric: {} },
    { thumbnailUrl: 'assets/heart.png', backgroundColor: 'red', fabric: {} },
    { thumbnailUrl: 'assets/heart.png', backgroundColor: 'red', fabric: {} },
    { thumbnailUrl: 'assets/heart.png', backgroundColor: 'red', fabric: {} },
  ];

  @Output() public readonly onClickImageEvent = new EventEmitter();
  constructor() {}
  public onClickImage(value: number): void {
    this.onClickImageEvent.emit(value);
  }
}
