import { Component, Input } from '@angular/core';
import { SimpleContentComponent } from '../../../ssalon-component/simple-content/simple-content.component';
import { ApiExecutorService } from '../../../service/api-executor.service';
import { NewButtonElement } from '../../../ssalon-component/simple-toggle-group/simple-toggle-group.component';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-moim-review-info',
  standalone: true,
  imports: [SimpleContentComponent, NgIf],
  templateUrl: './moim-review-info.component.html',
  styleUrl: './moim-review-info.component.scss',
})
export class MoimReviewInfoComponent {
  @Input() moimId: string = undefined as unknown as string;
  public reviewInfo: any = undefined as unknown as any;
  constructor(private _apiExecutorService: ApiExecutorService) {}
  public async ngOnInit() {
    this.reviewInfo = await this._apiExecutorService.getMoimReview(this.moimId);
  }
  public getImagesToNewButtonElements(imageUrls: string[]): NewButtonElement[] {
    let images: NewButtonElement[] = [];
    for (let i = 0; i < imageUrls.length; i++) {
      images.push({
        imgSrc: imageUrls[i],
        label: '사진',
        value: i,
        selected: false,
      });
    }
    return images;
  }
  public isReviewLoaded(): boolean {
    return this.reviewInfo !== undefined;
  }
}
