import axios, { Axios, AxiosInstance } from 'axios';
import { Canvas } from 'fabric';
import { Vector2 } from 'three';
import { CSS3DObject } from 'three/examples/jsm/renderers/CSS3DRenderer.js';

export class Face extends CSS3DObject {
  public fabricCanvas: Canvas | null = null;
  public ticketSize: Vector2 | null = null;
  public side: 'front' | 'back' | null = null;
  public decoration: string | null = null;
  public axiosInstance: AxiosInstance | null = null;
  constructor(
    ticketSize: Vector2, // (width, height)
    side: 'front' | 'back',
    decoration: string
  ) {
    super(document.createElement('div'));
    this.ticketSize = ticketSize;
    this.side = side;
    this.decoration = decoration;
    this.initCanvas();
    this.loadFabric(decoration);
  }

  public initCanvas(): void {
    this.fabricCanvas = new Canvas(this.name, {
      width: this.ticketSize!.x,
      height: this.ticketSize!.y,
    });
    this.element = this.fabricCanvas.getElement();
  }

  public async loadFabric(decorationJson: any) {
    await this.fabricCanvas?.loadFromJSON(decorationJson);
    this.fabricCanvas?.renderAll();
  }
}
