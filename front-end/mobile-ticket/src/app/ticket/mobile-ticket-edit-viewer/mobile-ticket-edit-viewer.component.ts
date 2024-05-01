import { fabric } from 'fabric';
import { ApiExecutorService } from '../../service/api-executor.service';
import {
  Component,
  ViewChild,
  ElementRef,
  Output,
  EventEmitter,
} from '@angular/core';
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
  public isCursorDown: boolean = false;
  constructor(private _apiExecutorService: ApiExecutorService) {}
  public ngOnInit(): void {}
  public ngAfterViewInit(): void {
    this.initFabric();
  }
  public async initFabric() {
    this.canvas = new fabric.Canvas(this.editCanvas?.nativeElement);
    this.canvas.selection = false;
    this.canvas.on('mouse:down', (event) => {
      if (event.target !== null && event.target instanceof fabric.Text) {
        this.isCursorDown = true;
      }
    });
    this.canvas.on('mouse:up', (event) => {
      if (event.target !== null && event.target instanceof fabric.Text) {
        if (this.isCursorDown) {
          this.onClickText?.emit(event.target);
        }
        this.isCursorDown = false;
      }
    });
    this.canvas.on('object:moving', (event) => {
      this.isCursorDown = false;
    });
    this.canvas.on('object:scaling', (event) => {
      this.isCursorDown = false;
    });
    await this.loadDecorationInfo();
    this.addRemoveIconToControl();
  }

  public addRemoveIconToControl() {
    var deleteIcon =
      "data:image/svg+xml,%3C%3Fxml version='1.0' encoding='utf-8'%3F%3E%3C!DOCTYPE svg PUBLIC '-//W3C//DTD SVG 1.1//EN' 'http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd'%3E%3Csvg version='1.1' id='Ebene_1' xmlns='http://www.w3.org/2000/svg' xmlns:xlink='http://www.w3.org/1999/xlink' x='0px' y='0px' width='595.275px' height='595.275px' viewBox='200 215 230 470' xml:space='preserve'%3E%3Ccircle style='fill:%23F44336;' cx='299.76' cy='439.067' r='218.516'/%3E%3Cg%3E%3Crect x='267.162' y='307.978' transform='matrix(0.7071 -0.7071 0.7071 0.7071 -222.6202 340.6915)' style='fill:white;' width='65.545' height='262.18'/%3E%3Crect x='266.988' y='308.153' transform='matrix(0.7071 0.7071 -0.7071 0.7071 398.3889 -83.3116)' style='fill:white;' width='65.544' height='262.179'/%3E%3C/g%3E%3C/svg%3E";
    var img = document.createElement('img');
    img.src = deleteIcon;
    function deleteObject(eventData: any, transform: any) {
      var target = transform.target;
      var canvas = target.canvas;
      canvas.remove(target);
      canvas.requestRenderAll();
      return true;
    }

    function renderIcon(
      ctx: any,
      left: any,
      top: any,
      styleOverride: any,
      fabricObject: any
    ) {
      var size = 15;
      ctx.save();
      ctx.translate(left, top);
      ctx.rotate(fabric.util.degreesToRadians(fabricObject.angle));
      ctx.drawImage(img, -size / 2, -size / 2, size, size);
      ctx.restore();
    }
    fabric.Object.prototype.controls['deleteControl'] = new fabric.Control({
      x: 0.6,
      y: -0.5,
      offsetY: 16,
      cursorStyle: 'pointer',
      mouseUpHandler: deleteObject,
      render: renderIcon,
    });
  }

  public async loadDecorationInfo() {
    let decorationInfo = await this._apiExecutorService.getTicket();
    this.canvas?.loadFromJSON(decorationInfo.fabric, () => {
      this.canvas?.renderAll();
    });
  }

  public updateCanvas(
    fabricObjects: fabric.Image[] | fabric.Text[] | fabric.Path[] | null
  ): void {
    console.log(this.canvas?.toJSON());
    if (fabricObjects !== null) {
      for (let index = 0; index < fabricObjects.length; index++) {
        this.canvas?.add(fabricObjects[index]);
      }
    }
    this.canvas?.renderAll();
  }

  public getCanvasCapture(): any {
    const dataURL = this.canvas?.toDataURL({
      format: 'png',
      quality: 1,
    });
    return dataURL;
  }

  public updateBackgroundColor(color: string): void {
    this.backgroundPath?.nativeElement.setAttribute('fill', color);
  }
}
