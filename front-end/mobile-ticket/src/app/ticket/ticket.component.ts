import { Component, Input } from '@angular/core';
import { MobileTicketViewerComponent } from './mobile-ticket-viewer/mobile-ticket-viewer.component';
import { MobileTicketEditorComponent } from './mobile-ticket-editor/mobile-ticket-editor.component';
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

export enum MobileTicketEditViewMode {
  PREVIEW,
  EDITVIEW,
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
  public editorHeight: string = '35%';

  public mobileTicketViewMode = MobileTicketViewMode;
  public mode: MobileTicketViewMode = MobileTicketViewMode.APPEDITVIEW;

  public mobileTicketEditViewMode = MobileTicketEditViewMode;
  public editViewMode: MobileTicketEditViewMode =
    MobileTicketEditViewMode.PREVIEW;

  public toggleButtonElements: SimpleToggleButtonElement[] = [
    { label: '미리보기 뷰', value: MobileTicketEditViewMode.PREVIEW },
    { label: '편집뷰', value: MobileTicketEditViewMode.EDITVIEW },
    { label: '편집뷰', value: 2 },
    { label: '편집뷰', value: 3 },
    { label: '편집뷰', value: 4 },
    { label: '편집뷰', value: 5 },
  ];
  constructor() {}

  public changeMode(mode: MobileTicketViewMode): void {
    this.mode = mode;
  }

  public changeEditViewerMode(mode: MobileTicketEditViewMode): void {
    this.editViewMode = mode;
  }
}
