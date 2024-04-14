import { Component, ElementRef, ViewChild } from '@angular/core';
import { SimpleButtonComponent } from '../../ssalon-component/simple-button/simple-button.component';
import { NgFor } from '@angular/common';
import { MobileTicket } from '../../service/mobile-ticket';
import { ScenegraphService } from '../../service/scenegraph.service';

@Component({
  selector: 'app-mobile-ticket-editor',
  standalone: true,
  imports: [SimpleButtonComponent, NgFor],
  templateUrl: './mobile-ticket-editor.component.html',
  styleUrl: './mobile-ticket-editor.component.scss',
})
export class MobileTicketEditorComponent {
  @ViewChild('temp', { static: true }) temp: ElementRef | null = null;
  public editFeatureLabels: string[] = ['Text', 'Picture', 'Draw'];
  constructor(private _sceneGraphService: ScenegraphService) {}

  ngAfterViewInit(): void {}
  public onClickFocusFrontButton(): void {
    this._sceneGraphService.focusFront();
  }

  public onClickEditFeatureButton(label: string): void {
    switch (label) {
      case 'Text':
        this._sceneGraphService.mobileTicket!.frontSide!.addText();
        break;
      case 'Picture':
        this._sceneGraphService.mobileTicket!.frontSide!.addRect();
        break;
      case 'Draw':
        //this._sceneGraphService.mobileTicket!.frontSide!.addDraw();
        break;
    }
  }
  public onClickButton() {
    this.temp!.nativeElement.appendChild(
      this._sceneGraphService.mobileTicket!.frontSide!.fabricCanvas!.getElement()
    );
  }
}
