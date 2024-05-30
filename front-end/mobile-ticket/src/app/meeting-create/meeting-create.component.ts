import { Component, ViewChild } from '@angular/core';
import { TopNavigatorComponent } from '../ssalon-component/top-navigator/top-navigator.component';
import { SimpleInputComponent } from '../ssalon-component/simple-input/simple-input.component';
import { BottomDialogComponent } from '../ssalon-component/bottom-dialog/bottom-dialog.component';
import { SimpleToggleGroupComponent } from '../ssalon-component/simple-toggle-group/simple-toggle-group.component';
import { NgIf, Location } from '@angular/common';
import { ButtonElementsService } from '../service/button-elements.service';
import { SquareButtonComponent } from '../ssalon-component/square-button/square-button.component';
import { CreateMeetingInfoComponent } from './create-meeting-info/create-meeting-info.component';
import { CreateTicketComponent } from './create-ticket/create-ticket.component';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiExecutorService } from '../service/api-executor.service';

export enum CreateMeetingStep {
  INFO,
  TICKET,
}

export enum CreateTicketMode {
  EMPTY,
  TEMPLATE,
  AI,
}
@Component({
  selector: 'app-meeting-create',
  standalone: true,
  imports: [
    NgIf,
    TopNavigatorComponent,
    SimpleInputComponent,
    BottomDialogComponent,
    SimpleToggleGroupComponent,
    SquareButtonComponent,
    CreateMeetingInfoComponent,
    CreateTicketComponent,
  ],
  templateUrl: './meeting-create.component.html',
  styleUrl: './meeting-create.component.scss',
  animations: [],
})
export class MeetingCreateComponent {
  @ViewChild('createMeetingInfoComponent', { static: false })
  createMeetingInfoComponent: CreateMeetingInfoComponent | null = null;
  public createMeetingStep = CreateMeetingStep;
  public nowStep: CreateMeetingStep = CreateMeetingStep.INFO;
  public meetingId: string | null = null;
  public meetingInfo: any = {
    title: '',
    description: '',
    category: '',
    capacity: 0,
    location: '',
    meetingDate: '',
    payment: -1,
    meetingPictureUrls: [''],
    isSharable: true,
  };
  public resultMeetingInfo: any = {};
  public createTicketMode: CreateTicketMode = CreateTicketMode.EMPTY;
  constructor(
    private _apiExecutorService: ApiExecutorService,
    public buttonElementsService: ButtonElementsService,
    private _location: Location,
    private _router: Router
  ) {}
  public async ngOnInit() {}
  public onClickBackButton() {
    if (this.nowStep === CreateMeetingStep.TICKET) {
      this.nowStep = CreateMeetingStep.INFO;
    } else {
      this.meetingInfo = {
        title: '',
        description: '',
        category: '',
        capacity: 0,
        location: '',
        meetingDate: '',
        payment: -1,
        meetingPictureUrls: [''],
        isSharable: true,
      };
      this._location.back();
    }
  }
  public changeCreateTicketTypeButtonState(value: boolean) {
    this.buttonElementsService.nextButtons[0].selected = value;
  }
  public changeCreateTicketButtonState(value: any) {
    if (value === 0) {
      this.createTicketMode = CreateTicketMode.EMPTY;
    } else if (value === 1) {
      this.createTicketMode = CreateTicketMode.TEMPLATE;
    }
    this.buttonElementsService.createTicketButtons[0].selected = true;
  }
  public onClickCreateTicketButton() {
    this._router.navigate([`/web/ticket`], {
      queryParams: {
        moimId: this.resultMeetingInfo.id,
        viewType: 'edit',
        createTemplate: this.createTicketMode === 0 ? 'N' : 'A',
        face: 'front',
      },
    });
  }
  public async onClickNextButton() {
    this.meetingInfo = this.createMeetingInfoComponent!.meetingInfo;
    if (this.buttonElementsService.nextButtons[0].selected) {
      this.resultMeetingInfo = await this._apiExecutorService.createMeeting(
        this.meetingInfo
      );
      this.nowStep = CreateMeetingStep.TICKET;
    }
  }
}
