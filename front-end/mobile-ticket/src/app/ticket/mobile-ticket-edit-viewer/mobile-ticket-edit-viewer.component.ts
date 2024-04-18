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
    this.canvas.on({
      'touch:gesture': function () {},
      'touch:drag': function () {},
      'touch:orientation': function () {},
      'touch:shake': function () {},
      'touch:longpress': function () {},
    });
    console.log(this.canvas);
    let a = new fabric.Text('Hello', {
      left: 100,
      top: 100,
    });
    a.perPixelTargetFind = true;
    this.canvas.add(a);
  }
}
