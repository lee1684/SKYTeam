import { Component, Input } from '@angular/core';
import { MoimReviewCreateComponent } from './moim-review-create/moim-review-create.component';
import { MoimReviewInfoComponent } from './moim-review-info/moim-review-info.component';
import { ApiExecutorService } from '../../service/api-executor.service';
import { Router } from '@angular/router';
import { NgIf } from '@angular/common';
import { SquareButtonComponent } from '../../ssalon-component/square-button/square-button.component';
import { ButtonElementsService } from '../../service/button-elements.service';

@Component({
  selector: 'app-moim-review',
  standalone: true,
  imports: [
    NgIf,
    MoimReviewCreateComponent,
    MoimReviewInfoComponent,
    SquareButtonComponent,
  ],
  templateUrl: './moim-review.component.html',
  styleUrl: './moim-review.component.scss',
})
export class MoimReviewComponent {
  @Input() moimId: string = '';
  @Input() moimTitle: string = '';
  public isReviewCreated: boolean = false;
  constructor(
    private _apiExecutorService: ApiExecutorService,
    private _router: Router,
    public buttonElementService: ButtonElementsService
  ) {}
  public ngOnInit() {
    this.checkDiaryCreated();
  }
  public async checkDiaryCreated() {
    let result = await this._apiExecutorService.getMoimReview(this.moimId);
    if (result === false) {
      this.isReviewCreated = false;
    } else {
      this.isReviewCreated = true;
    }
  }
  public onClickCreateDiaryButton() {
    this._router.navigate([`/web/meeting-review-create`], {
      queryParams: {
        moimId: this.moimId,
        title: this.moimTitle,
      },
    });
  }
}
