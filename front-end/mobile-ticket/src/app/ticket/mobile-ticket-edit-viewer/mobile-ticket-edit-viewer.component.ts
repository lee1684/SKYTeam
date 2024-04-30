import {
  Component,
  ElementRef,
  EventEmitter,
  HostListener,
  Input,
  Output,
  ViewChild,
} from '@angular/core';
import { fabric } from 'fabric';
import { ApiExecutorService } from '../../service/api-executor.service';
@Component({
  selector: 'app-mobile-ticket-edit-viewer',
  standalone: true,
  imports: [],
  templateUrl: './mobile-ticket-edit-viewer.component.html',
  styleUrl: './mobile-ticket-edit-viewer.component.scss',
})
export class MobileTicketEditViewerComponent {
  @ViewChild('backgroundPath', { static: false })
  backgroundPath: ElementRef | null = null;
  @ViewChild('editCanvas', { static: false })
  editCanvas: ElementRef | null = null;
  @Output() public readonly onClickText = new EventEmitter();
  public canvas: fabric.Canvas | null = null;
  public isTouchDown: boolean = false;
  constructor(private _apiExecutorService: ApiExecutorService) {}
  public ngOnInit(): void {}
  public ngAfterViewInit(): void {
    this.initFabric();
  }
  public async initFabric() {
    this.canvas = new fabric.Canvas(this.editCanvas?.nativeElement);
    this.canvas.selection = false;
    this.canvas.on(
      'selection:created',
      function (this: MobileTicketEditViewerComponent, e: any) {
        if (e.selected.length === 1 && e.selected[0] instanceof fabric.IText) {
          this.onClickText?.emit(e.selected[0]);
        }
      }.bind(this)
    );
    this.canvas.on(
      'selection:updated',
      function (this: MobileTicketEditViewerComponent, e: any) {}.bind(this)
    );
    this.loadDecorationInfo();
  }

  public async loadDecorationInfo() {
    let decorationInfo = await this._apiExecutorService.getTicket();
    this.canvas?.loadFromJSON(decorationInfo.fabric, () => {
      this.canvas?.renderAll();
    });
  }

  public updateCanvas(object: fabric.Object | null): void {
    if (object !== null) {
      this.canvas?.add(object);
    }
    this.canvas?.renderAll();
  }

  public getCanvasCapture(): any {
    console.log(this.canvas?.toJSON());
    const dataURL = this.canvas?.toDataURL({
      format: 'png',
      quality: 1,
    });
    return dataURL;
  }

  public updateBackgroundColor(color: string): void {
    this.backgroundPath?.nativeElement.setAttribute('fill', color);
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
