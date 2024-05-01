import { Canvas, FabricImage, FabricText, Path } from 'fabric';
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
  public canvas: Canvas | null = null;
  public isCursorDown: boolean = false;
  constructor(private _apiExecutorService: ApiExecutorService) {}
  public ngOnInit(): void {}
  public ngAfterViewInit(): void {
    this.initFabric();
  }
  public async initFabric() {
    this.canvas = new Canvas(this.editCanvas?.nativeElement);
    console.log(this.canvas);
    this.canvas.selection = false;
    this.canvas.on('mouse:down', (event: any) => {
      console.log(event);
      if (event.target !== null && event.target instanceof FabricText) {
        this.isCursorDown = true;
      }
    });
    this.canvas.on('mouse:up', (event: any) => {
      if (event.target !== null && event.target instanceof FabricText) {
        if (this.isCursorDown) {
          this.onClickText?.emit(event.target);
        }
        this.isCursorDown = false;
      }
    });
    this.canvas.on('object:moving', (event: any) => {
      this.isCursorDown = false;
    });
    this.canvas.on('object:scaling', (event: any) => {
      this.isCursorDown = false;
    });
    await this.loadDecorationInfo();
  }

  public addRemoveIconToControl() {}

  public async loadDecorationInfo() {
    let decorationInfo = await this._apiExecutorService.getTicket();
    await this.canvas?.loadFromJSON(decorationInfo.fabric);
    this.canvas?.renderAll();
  }

  public updateCanvas(
    fabricObjects: FabricImage[] | FabricText[] | Path[] | null
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
    this.canvas?.toDataURL({
      format: 'png',
      multiplier: 2,
      enableRetinaScaling: true,
    });
    const dataURL = this.canvas?.toDataURL({ format: 'png', multiplier: 2 });
    return dataURL;
  }

  public updateBackgroundColor(color: string): void {
    this.backgroundPath?.nativeElement.setAttribute('fill', color);
  }
}
