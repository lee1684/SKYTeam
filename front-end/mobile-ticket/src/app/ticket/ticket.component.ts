import { Component, Input, ViewChild } from '@angular/core';
import { MobileTicketViewerComponent } from './mobile-ticket-viewer/mobile-ticket-viewer.component';
import {
  MobileTicketEditMode,
  MobileTicketEditorComponent,
} from './mobile-ticket-editor/mobile-ticket-editor.component';
import { MobileTicketEditViewerComponent } from './mobile-ticket-edit-viewer/mobile-ticket-edit-viewer.component';
import { NgIf } from '@angular/common';
import {
  SimpleToggleButtonElement,
  SimpleToggleButtonGroupComponent,
} from '../ssalon-component/simple-toggle-button-group/simple-toggle-button-group.component';
import { SimpleButtonComponent } from '../ssalon-component/simple-button/simple-button.component';
import {
  ButtonElement,
  CircleToggleButtonGroupComponent,
} from '../ssalon-component/circle-toggle-button-group/circle-toggle-button-group.component';

export enum MobileTicketViewMode {
  APPVIEW,
  APPEDITVIEW,
  WEBVIEW,
}

@Component({
  selector: 'app-ticket',
  standalone: true,
  imports: [
    MobileTicketViewerComponent,
    MobileTicketEditorComponent,
    MobileTicketEditViewerComponent,
    SimpleToggleButtonGroupComponent,
    CircleToggleButtonGroupComponent,
    SimpleButtonComponent,
    NgIf,
  ],
  templateUrl: './ticket.component.html',
  styleUrl: './ticket.component.scss',
})
export class TicketComponent {
  @ViewChild('mobileTicketEditViewer', { static: false })
  mobileTicketEditViewer: MobileTicketEditViewerComponent | null = null;
  @ViewChild('mobileTicketEditor', { static: false })
  mobileTicketEditor: MobileTicketEditorComponent | null = null;

  public editViewerHeight: string = '60%';
  public viewerHeight: string = '100%';

  public mobileTicketViewMode = MobileTicketViewMode;
  public mode: MobileTicketViewMode = MobileTicketViewMode.APPEDITVIEW;

  public mobileTicketEditMode = MobileTicketEditMode;
  public editMode: MobileTicketEditMode = MobileTicketEditMode.NONE;
  public goBackButtonElement: ButtonElement = {
    imgSrc: 'assets/icons/go-back.png',
    label: '뒤로가기',
    value: 0,
  };
  constructor() {}
  public changeEditMode(mode: MobileTicketEditMode) {
    this.editMode = mode;
  }

  public changeViewMode(mode: MobileTicketViewMode): void {
    this.mode = mode;
  }

  public addFabricObject(object: any) {
    console.log(object);
  }

  public onClickBackButton() {
    this.changeViewMode(MobileTicketViewMode.APPEDITVIEW);
  }

  public applyEdit(object: fabric.Object | null) {
    this.mobileTicketEditViewer?.updateCanvas(object);
  }

  public openTextEditor(IText: fabric.IText) {
    this.mobileTicketEditor!.isTextAddMode = false;
    this.mobileTicketEditor!.editingIText = IText;
    this.mobileTicketEditor!.onClickChangeEditMode(MobileTicketEditMode.TEXT);
  }
}
