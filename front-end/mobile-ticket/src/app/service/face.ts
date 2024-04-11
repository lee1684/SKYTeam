import { fabric } from 'fabric';
import { Vector2 } from 'three';
import { CSS3DObject } from 'three/examples/jsm/renderers/CSS3DRenderer';

export class Face extends CSS3DObject {
  public fabricCanvas: fabric.Canvas | null = null;
  public ticketSize: Vector2 | null = null;
  public side: 'front' | 'back' | null = null;
  public decoration: string | null = null;
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
    this.addRect();
    this.addText();
    this.addImage();
  }

  public initCanvas(): void {
    this.fabricCanvas = new fabric.Canvas(this.name, {
      width: this.ticketSize!.x,
      height: this.ticketSize!.y,
    });
    this.element = this.fabricCanvas.getElement();
  }

  public addRect(): void {
    const rect = new fabric.Rect({
      left: 0,
      top: 0,
      fill: '#FF0000',
      width: 170,
      height: 40,
    });
    rect.on('selected', function () {
      console.log('selected a rectangle');
    });
    this.fabricCanvas!.add(rect);
  }

  public addText(): void {
    const text = new fabric.Text('안녕하세요 SSALON 모바일 증표 데모', {
      left: 20,
      top: 5,
      fill: 'black',
      fontSize: 8,
      fontFamily: 'Arial',
    });
    this.fabricCanvas!.add(text);
  }

  public addImage(): void {
    const imgElement = document.createElement('img');
    imgElement.src = 'assets/image.png';
    const image = new fabric.Image(imgElement, {
      left: 0,
      top: 0,
      angle: 0,
    });
    this.fabricCanvas!.add(image);
  }
}
