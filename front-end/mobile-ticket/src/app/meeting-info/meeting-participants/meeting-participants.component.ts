import { NgIf } from '@angular/common';
import {
  Component,
  EventEmitter,
  Input,
  Output,
  ViewChild,
} from '@angular/core';
import { TopNavigatorComponent } from '../../ssalon-component/top-navigator/top-navigator.component';
import { SimpleToggleButtonGroupComponent } from '../../ssalon-component/simple-toggle-button-group/simple-toggle-button-group.component';
import {
  NewButtonElement,
  SimpleToggleGroupComponent,
} from '../../ssalon-component/simple-toggle-group/simple-toggle-group.component';
import { ButtonElementsService } from '../../service/button-elements.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiExecutorService } from '../../service/api-executor.service';
import { SimpleInputComponent } from '../../ssalon-component/simple-input/simple-input.component';
import { SquareButtonComponent } from '../../ssalon-component/square-button/square-button.component';
import {
  BottomDialogComponent,
  BottomDialogType,
} from '../../ssalon-component/bottom-dialog/bottom-dialog.component';

@Component({
  selector: 'app-meeting-participants',
  standalone: true,
  imports: [
    NgIf,
    TopNavigatorComponent,
    SimpleToggleGroupComponent,
    SimpleInputComponent,
    SquareButtonComponent,
    BottomDialogComponent,
  ],
  templateUrl: './meeting-participants.component.html',
  styleUrl: './meeting-participants.component.scss',
})
export class MeetingParticipantsComponent {
  @ViewChild('reasonContainer', { static: false })
  reasonContainer: SimpleInputComponent | null = null;

  private _moimId: string = '';
  private _participants: any = undefined as unknown as any;
  public isCreator: boolean = false;
  public participantButtonElements: NewButtonElement[] = [];
  public bottomDialogType = BottomDialogType;
  public isPopUpBottomDialog: boolean = false;
  @Output() onBackButtonClickEvent = new EventEmitter<void>();
  constructor(
    private _apiExecutorService: ApiExecutorService,
    public buttonElementsService: ButtonElementsService,
    private _route: ActivatedRoute,
    private _router: Router
  ) {
    this._route.queryParams.subscribe((params) => {
      this._moimId = params['id'];
    });
  }
  public async ngOnInit() {
    this.isCreator = await this._apiExecutorService.checkCreator(this._moimId);
    this._participants = await this._apiExecutorService.getJoiningUsers(
      this._moimId
    );
    this.participantButtonElements = this.getParticipantsButtonElements();
  }

  public getParticipantsButtonElements() {
    return this._participants.map((participant: any, index: number) => {
      return {
        imgSrc: participant.profilePictureUrl,
        value: participant.userId,
        label: participant.nickname,
        selected: false,
      };
    });
  }

  public isParticipantsLoaded() {
    return this._participants !== undefined;
  }

  public checkButtonState() {
    if (
      this.participantButtonElements.filter(
        (element) => element.selected === true
      ).length === 0
    ) {
      this.buttonElementsService.manageParticipantsButtons.forEach(
        (buttonElement) => {
          buttonElement.selected = false;
        }
      );
    } else {
      this.buttonElementsService.manageParticipantsButtons.forEach(
        (buttonElement) => {
          buttonElement.selected = true;
        }
      );
    }
  }

  public onClickBackButton() {
    this._router.navigate(['/web/meeting-info'], {
      queryParams: { moimId: this._moimId },
    });
  }

  public onClickParticipantButton() {
    this.checkButtonState();
  }

  public async onClickManageButton(value: number) {
    console.log(value);
    if (
      this.buttonElementsService.manageParticipantsButtons.every(
        (buttonElement) => {
          return buttonElement.selected === true;
        }
      )
    ) {
      let reportParticipants = this.participantButtonElements
        .filter((element) => element.selected === true)
        .map((element) => element.value);
      if (value === 0) {
        // 신고
        for (let i = 0; i < reportParticipants.length; i++) {
          await this._apiExecutorService.reportParticipant(
            this._apiExecutorService.myProfile.id,
            reportParticipants[i],
            this.reasonContainer!.innerText as string
          );
        }
      } else {
        // 강퇴
        for (let i = 0; i < reportParticipants.length; i++) {
          await this._apiExecutorService.kickParticipant(
            this._moimId,
            reportParticipants[i],
            this.reasonContainer!.innerText as string
          );
        }
      }
      this.popUpSuccessBottomDialog();
    }
  }

  public popUpSuccessBottomDialog() {
    this.isPopUpBottomDialog = true;
  }

  public onClickCloseBottomDialog() {
    this.isPopUpBottomDialog = false;
    location.reload();
  }
}
