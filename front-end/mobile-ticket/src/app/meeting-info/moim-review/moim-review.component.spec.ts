import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MoimReviewComponent } from './moim-review.component';

describe('MoimReviewComponent', () => {
  let component: MoimReviewComponent;
  let fixture: ComponentFixture<MoimReviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MoimReviewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MoimReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
