import {
  Canvas,
  Control,
  FabricImage,
  FabricObject,
  FabricText,
  Path,
} from 'fabric';
import { ApiExecutorService } from '../../service/api-executor.service';
import {
  Component,
  ViewChild,
  ElementRef,
  Output,
  EventEmitter,
  Input,
} from '@angular/core';
import { degToRad } from 'three/src/math/MathUtils.js';
import { create } from 'apisauce';
import { Ticket } from '../../ssalon-component/image-row-container/image-row-container.component';
import { DecorationInfo } from '../../service/ssalon-config.service';
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

  @Input() moimId: string = undefined as unknown as string;
  @Input() createTemplate: string = undefined as unknown as string;
  @Input() face: string = undefined as unknown as string;

  @Output() public readonly onClickText = new EventEmitter();
  public canvas: Canvas | null = null;
  public isCursorDown: boolean = false;
  public decorationInfo: DecorationInfo =
    undefined as unknown as DecorationInfo;
  public backgroundColor: string = '#ffffff';
  constructor(private _apiExecutorService: ApiExecutorService) {}
  public async ngOnInit() {
    console.log(this.moimId, this.createTemplate, this.face);
  }
  public async ngAfterViewInit() {
    await this.loadDecorationInfo();
    await this.initFabric();
  }
  public async initFabric() {
    this.canvas = new Canvas(this.editCanvas?.nativeElement);
    this.canvas.selection = false;
    this.canvas.on('mouse:down', (event: any) => {
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
    this.canvas.on('object:rotating', (event: any) => {
      this.isCursorDown = false;
    });

    await this.canvas?.loadFromJSON(this.decorationInfo.fabric);
    this.canvas?.renderAll();
  }

  public async loadDecorationInfo() {
    console.log(this.moimId);
    let decorationInfo: any = null;
    if (this.createTemplate !== undefined) {
      if (this.face === 'front') {
        if (this.createTemplate !== 'edit') {
          await this._apiExecutorService.createTicket(
            this.moimId,
            this.createTemplate!
          );
        }
        console.log(await this._apiExecutorService.getTicket(this.moimId));
        this.decorationInfo = await this._apiExecutorService.getTicket(
          this.moimId
        );
      } else if (this.face === 'back') {
        if (this.createTemplate !== 'edit') {
          await this._apiExecutorService.createDiary(
            this.moimId,
            this.createTemplate!
          );
        }
        this.decorationInfo = await this._apiExecutorService.getDiary(
          this.moimId
        );
      }
    }
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
    const dataURL = this.canvas?.toDataURL({
      format: 'png',
      multiplier: 1,
      enableRetinaScaling: true,
    });
    return dataURL;
  }

  public updateBackgroundColor(color: string): void {
    this.backgroundColor = color;
  }
}
