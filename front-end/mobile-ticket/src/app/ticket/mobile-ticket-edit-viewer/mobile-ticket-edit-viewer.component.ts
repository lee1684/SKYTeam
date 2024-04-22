import { Component } from '@angular/core';
import { fabric } from 'fabric';
@Component({
  selector: 'app-mobile-ticket-edit-viewer',
  standalone: true,
  imports: [],
  templateUrl: './mobile-ticket-edit-viewer.component.html',
  styleUrl: './mobile-ticket-edit-viewer.component.scss',
})
export class MobileTicketEditViewerComponent {
  public canvas: fabric.Canvas | null = null;
  constructor() {}
  public ngAfterViewInit(): void {
    this.initFabric();
  }
  initFabric(): void {
    this.canvas = new fabric.Canvas('edit-canvas');
    console.log(this.canvas);
    let a = new fabric.Textbox('Hello', {
      left: 100,
      top: 100,
    });
    //a.perPixelTargetFind = true;
    this.canvas.add(a);
    fabric.Image.fromURL('assets/heart.png', (img: any) => {
      this.canvas!.add(img);
      console.log(this.canvas!.toJSON());
    });
  }
}
