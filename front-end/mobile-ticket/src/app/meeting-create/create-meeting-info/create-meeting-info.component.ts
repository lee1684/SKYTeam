import {
  Component,
  EventEmitter,
  Input,
  Output,
  ViewChild,
} from '@angular/core';
import { SimpleInputComponent } from '../../ssalon-component/simple-input/simple-input.component';
import { ButtonElementsService } from '../../service/button-elements.service';
import {
  BottomDialogComponent,
  BottomNavigatorType,
} from '../../ssalon-component/bottom-dialog/bottom-dialog.component';
import { SquareButtonComponent } from '../../ssalon-component/square-button/square-button.component';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-create-meeting-info',
  standalone: true,
  imports: [
    NgIf,
    SimpleInputComponent,
    SquareButtonComponent,
    BottomDialogComponent,
  ],
  templateUrl: './create-meeting-info.component.html',
  styleUrl: './create-meeting-info.component.scss',
})
export class CreateMeetingInfoComponent {
  @ViewChild('capacity', { static: true })
  capacity: SimpleInputComponent | null = null;
  @ViewChild('fee', { static: true }) fee: SimpleInputComponent | null = null;
  public bottomNavigatorType = BottomNavigatorType;
  public isPopUpBottomNavigator = false;
  @Input() public meetingInfo: any = {
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

  @Output() public readonly onCreateButtonReadyEvent = new EventEmitter();

  constructor(public buttonElementsService: ButtonElementsService) {}
  public ngAfterViewChecked() {
    if (this.isAllTyped()) {
      this.onCreateButtonReadyEvent.emit(true);
    }
  }
  public popUpBottomNavigator() {
    this.isPopUpBottomNavigator = true;
  }
  public onCategorySelectedEvent(value: any) {
    this.meetingInfo.category = this.buttonElementsService.getLabelByValue(
      this.buttonElementsService.interestSelectionButtons,
      value.value
    );
    this.isPopUpBottomNavigator = false;
  }
  public onClickNoLimitCapacity() {
    this.capacity!.innerText = Infinity;
    this.meetingInfo.maxMember = 100000;
  }
  public onClickFree() {
    this.fee!.innerText = 0;
    this.meetingInfo.payment = 0;
  }
  public onInput(type: string, value: any) {
    this.meetingInfo[type] = value;
  }
  public isAllTyped() {
    return (
      this.meetingInfo.title !== '' &&
      this.meetingInfo.description !== '' &&
      this.meetingInfo.category !== '' &&
      this.meetingInfo.capacity !== 0 &&
      this.meetingInfo.location !== '' &&
      this.meetingInfo.meetingDate !== '' &&
      this.meetingInfo.payment !== -1
    );
  }
}
