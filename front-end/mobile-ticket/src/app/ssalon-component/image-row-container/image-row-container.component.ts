import { NgFor, NgIf, NgOptimizedImage } from '@angular/common';
import {
  ChangeDetectorRef,
  Component,
  EventEmitter,
  HostListener,
  Input,
  OnChanges,
  Output,
  SimpleChanges,
} from '@angular/core';
import { NewButtonElement } from '../simple-toggle-group/simple-toggle-group.component';
import { Router } from '@angular/router';
export interface TicketList {
  categoryName: string;
  meetingList: Ticket[];
}
export interface Ticket {
  ticketThumb: string;
  backgroundColor: string;
  moimId: number;
  categoryName: string;
  meetingTitle: string;
  isCreator: boolean;
  isEnd: boolean;
}
@Component({
  selector: 'app-image-row-container',
  standalone: true,
  imports: [NgIf, NgFor, NgOptimizedImage],
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
  @Input() circleImage: boolean = false;
  @Input() tickets: Ticket[] = [];
  @Input() modifiedTickets: Ticket[][] = [];
  @Input() columnType: boolean = false;
  @Input() columnNum: number = -1;
  @Input() isTicketsLoaded: Boolean = false;
  @Input() public filter: string = '';
  @Input() public ticketRowContainerWidth = 350;
  @Output() public readonly onClickImageEvent = new EventEmitter();
  constructor(private _router: Router, private _cd: ChangeDetectorRef) {}
  public ngOnInit(): void {
    if (this.isTicketContainer) {
      this._setLayout();
      if (this.columnType) {
        this.getColumnNum();
      }

      this.ticketRowContainerWidth = window.innerWidth * 0.95;
    }
  }
  public ngAfterViewInit(): void {
    this._cd.detectChanges();
  }

  public ngAfterViewChecked(): void {}
  private _setLayout() {
    if (this.columnNum !== -1) {
      let result = [];
      for (let i = 0; i < this.tickets.length; i += this.columnNum) {
        result.push(this.tickets.slice(i, i + this.columnNum));
      }
      this.modifiedTickets = result;
    } else {
      this.modifiedTickets = [this.tickets];
    }
  }
  public onClickImage(value: number): void {
    this.onClickImageEvent.emit(value);
  }

  public getThumbSrc(moimId: number): string {
    let url =
      'https://test-bukkit-240415.s3.ap-northeast-2.amazonaws.com/Thumbnails/' +
      moimId +
      '/Thumb-' +
      moimId +
      '.png';
    return url;
  }

  public onClickTicket(value: number) {
    this._router.navigate(['/web/meeting-info'], {
      queryParams: { moimId: value },
    });
  }

  public getColumnNum() {
    this.columnNum = Math.floor(window.innerWidth / 175);
    this._setLayout();
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.ticketRowContainerWidth = event.target.innerWidth * 0.95;
    if (this.columnType) {
      this.getColumnNum();
    }
  }
}
