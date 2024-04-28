import {
  Component,
  EventEmitter,
  HostListener,
  Input,
  Output,
} from '@angular/core';
import { fabric } from 'fabric';
@Component({
  selector: 'app-mobile-ticket-edit-viewer',
  standalone: true,
  imports: [],
  templateUrl: './mobile-ticket-edit-viewer.component.html',
  styleUrl: './mobile-ticket-edit-viewer.component.scss',
})
export class MobileTicketEditViewerComponent {
  @Input() private _moimId: number = -1;
  @Output() public readonly onClickText = new EventEmitter();
  public canvas: fabric.Canvas | null = null;
  public isTouchDown: boolean = false;
  constructor() {}
  public ngOnInit(): void {}
  public ngAfterViewInit(): void {
    this.initFabric();
  }
  initFabric(): void {
    this.canvas = new fabric.Canvas('edit-canvas');
    let a = new fabric.IText('', {
      left: 100,
      top: 100,
      fill: '#FFFFFF',
    });
    a.set('text', 'hello\nworld');
    this.canvas.add(a);
    fabric.Image.fromURL('assets/heart.png', (img: any) => {
      this.canvas!.add(img);
      console.log(this.canvas!.toJSON());
    });

    this.canvas.on(
      'selection:created',
      function (this: MobileTicketEditViewerComponent, e: any) {
        if (e.selected.length === 1 && e.selected[0] instanceof fabric.IText) {
          console.log(this.onClickText);
          this.onClickText?.emit(e.selected[0]);
        }
      }.bind(this)
    );
    this.canvas.on(
      'selection:updated',
      function (this: MobileTicketEditViewerComponent, e: any) {}.bind(this)
    );
  }

  public updateCanvas(object: fabric.Object | null): void {
    if (object !== null) {
      this.canvas?.add(object);
    }
    this.canvas?.renderAll();
    console.log(this.canvas?.toJSON());
  }

  @HostListener('touchstart', ['$event'])
  @HostListener('mousedown', ['$event'])
  onMouseDown(event: any): void {
    this.isTouchDown = true;
  }

  @HostListener('touchend', ['$event'])
  @HostListener('mouseup', ['$event'])
  onMouseUp(event: any): void {
    this.isTouchDown = false;
  }
}
