import { Component, Input, ViewChild } from '@angular/core';
import { MobileTicketViewerComponent } from './mobile-ticket-viewer/mobile-ticket-viewer.component';
import {
  MobileTicketEditMode,
  MobileTicketEditorComponent,
} from './mobile-ticket-editor/mobile-ticket-editor.component';
import { MobileTicketEditViewerComponent } from './mobile-ticket-edit-viewer/mobile-ticket-edit-viewer.component';
import { NgIf } from '@angular/common';
import { SimpleToggleButtonGroupComponent } from '../ssalon-component/simple-toggle-button-group/simple-toggle-button-group.component';
import { SimpleButtonComponent } from '../ssalon-component/simple-button/simple-button.component';
import {
  ButtonElement,
  CircleToggleButtonGroupComponent,
} from '../ssalon-component/circle-toggle-button-group/circle-toggle-button-group.component';
import axios, { Axios } from 'axios';
import { DecorationInfo } from '../service/ssalon-config.service';
import { ApiExecutorService } from '../service/api-executor.service';

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
  constructor(private _apiExecutorService: ApiExecutorService) {}
  public changeEditMode(mode: MobileTicketEditMode) {
    this.editMode = mode;
  }

  public changeViewMode(mode: MobileTicketViewMode): void {
    if (mode === MobileTicketViewMode.APPVIEW) {
      this.updateServer();
    }
    this.mode = mode;
  }

  public addFabricObject(object: any) {
    console.log(object);
  }

  public onClickBackButton() {
    this.changeViewMode(MobileTicketViewMode.APPEDITVIEW);
  }

  public applyEdit(
    fabricObjects: fabric.Image[] | fabric.IText[] | fabric.Path[] | null
  ) {
    this.mobileTicketEditViewer?.updateCanvas(fabricObjects);
  }

  public applyBackgroundColorEdit(color: string) {
    this.mobileTicketEditViewer?.updateBackgroundColor(color);
  }

  public async updateServer() {
    if (
      this.mobileTicketEditViewer !== null &&
      this.mobileTicketEditor !== null
    ) {
      /** png로 변환해서 서버에 올려야함. */
      this.mobileTicketEditViewer.getCanvasCapture();
      let body: DecorationInfo = {
        thumbnailUrl: '서버에 올라간 url',
        backgroundColor: this.mobileTicketEditor!.backgroundColor.color,
        fabric: this.mobileTicketEditViewer!.canvas?.toJSON(),
      };
      /* 서버에 저장 API 연결해야함. */
      await this._apiExecutorService.editTicket(body);
    }
  }

  public openTextEditor(IText: fabric.IText) {
    (this.mobileTicketEditor!.fabricObjects as fabric.IText[]).push(IText);
    this.mobileTicketEditor!.syncTextAttributeWithSelectedText();
    this.mobileTicketEditor!.onClickChangeEditMode(MobileTicketEditMode.TEXT);
  }
}
