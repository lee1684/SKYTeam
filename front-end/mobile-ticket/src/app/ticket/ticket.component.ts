import { Component, Input } from '@angular/core';
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
import { CircleToggleButtonGroupComponent } from '../ssalon-component/circle-toggle-button-group/circle-toggle-button-group.component';

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
    NgIf,
  ],
  templateUrl: './ticket.component.html',
  styleUrl: './ticket.component.scss',
})
export class TicketComponent {
  public editViewerHeight: string = '60%';
  public viewerHeight: string = '60%';

  public mobileTicketViewMode = MobileTicketViewMode;
  public mode: MobileTicketViewMode = MobileTicketViewMode.APPEDITVIEW;

  public mobileTicketEditMode = MobileTicketEditMode;
  public editMode: MobileTicketEditMode = MobileTicketEditMode.NONE;

  constructor() {}
  public changeEditMode(mode: MobileTicketEditMode) {
    this.editMode = mode;
  }

  public changeMode(mode: MobileTicketViewMode): void {
    this.mode = mode;
  }

  public addFabricObject(object: any) {
    console.log(object);
  }
}
