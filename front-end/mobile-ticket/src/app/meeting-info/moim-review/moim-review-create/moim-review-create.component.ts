import { Component, ViewChild } from '@angular/core';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { ApiExecutorService } from '../../../service/api-executor.service';
import { ButtonElementsService } from '../../../service/button-elements.service';
import { NgIf, Location } from '@angular/common';
import { CreateMoimReviewInfoComponent } from './create-moim-review-info/create-moim-review-info.component';
import { TopNavigatorComponent } from '../../../ssalon-component/top-navigator/top-navigator.component';
import { SquareButtonComponent } from '../../../ssalon-component/square-button/square-button.component';
import { CreateDiaryComponent } from './create-diary/create-diary.component';

export enum CreateReviewStep {
  INFO,
  DIARY,
}

export enum CreateDiaryMode {
  EMPTY,
  TEMPLATE,
  AI,
}

@Component({
  selector: 'app-moim-review-create',
  standalone: true,
  imports: [
    NgIf,
    TopNavigatorComponent,
    CreateMoimReviewInfoComponent,
    CreateDiaryComponent,
    SquareButtonComponent,
  ],
  templateUrl: './moim-review-create.component.html',
  styleUrl: './moim-review-create.component.scss',
})
export class MoimReviewCreateComponent {
  @ViewChild('createMoimReviewInfoComponent', { static: false })
  createMoimReviewInfoComponent: CreateMoimReviewInfoComponent | null = null;
  public moimId: string = '';
  public moimTitle: string = '';
  public createDiaryStep = CreateReviewStep;
  public nowStep: CreateReviewStep = CreateReviewStep.INFO;
  public reviewInfo: any = {
    description: '',
    diaryPictureUrls: [],
  };
  public resultMeetingInfo: any = {};
  public createDiaryMode: CreateDiaryMode = CreateDiaryMode.EMPTY;
  constructor(
    private _apiExecutorService: ApiExecutorService,
    public buttonElementsService: ButtonElementsService,
    private _location: Location,
    private _router: Router,
    private _route: ActivatedRoute
  ) {}
  public ngOnInit() {
    this._route.queryParams.subscribe((params) => {
      this.moimId = params['moimId'];
      this.moimTitle = params['title'];
    });
  }

  public onClickBackButton() {
    if (this.nowStep === CreateReviewStep.DIARY) {
      this.nowStep = CreateReviewStep.INFO;
    } else {
      this.reviewInfo = {
        description: '',
        diaryPictureUrls: [''],
      };
      this._location.back();
    }
  }
  public changeCreateDiaryTypeButtonState(value: boolean) {
    this.buttonElementsService.nextButtons[0].selected = value;
  }
  public changeCreateTicketButtonState(value: any) {
    if (value === 0) {
      this.createDiaryMode = CreateDiaryMode.EMPTY;
    } else if (value === 1) {
      this.createDiaryMode = CreateDiaryMode.TEMPLATE;
    }
    this.buttonElementsService.createTicketButtons[0].selected = true;
  }
  public onClickCreateTicketButton() {
    this._router.navigate([`/web/ticket`], {
      queryParams: {
        moimId: this.moimId,
        viewType: 'edit',
        createTemplate: this.createDiaryMode === 0 ? 'N' : 'A',
        face: 'back',
      },
    });
  }
  public async onClickNextButton() {
    this.reviewInfo = this.createMoimReviewInfoComponent!.reviewInfo;
    if (this.buttonElementsService.nextButtons[0].selected) {
      await this._apiExecutorService.createMoimReview(
        this.moimId,
        this.reviewInfo
      );
      this.buttonElementsService.createTicketTypeButtons.every(
        (buttonElement) => (buttonElement.selected = false)
      );
      this.nowStep = CreateReviewStep.DIARY;
    }
  }
}
