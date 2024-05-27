import { Component, Input } from '@angular/core';
import { SimpleContentComponent } from '../../../ssalon-component/simple-content/simple-content.component';
import { ApiExecutorService } from '../../../service/api-executor.service';

@Component({
  selector: 'app-moim-review-info',
  standalone: true,
  imports: [SimpleContentComponent],
  templateUrl: './moim-review-info.component.html',
  styleUrl: './moim-review-info.component.scss',
})
export class MoimReviewInfoComponent {
  @Input() moimId: string = undefined as unknown as string;
  public reviewInfo: any = {};
  constructor(private _apiExecutorService: ApiExecutorService) {}
  public ngOnInit() {
    this.reviewInfo = this._apiExecutorService.getMoimReview(this.moimId);
    console.log(this.reviewInfo);
  }
}
