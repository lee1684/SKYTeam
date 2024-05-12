import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImageRowContainerComponent } from './image-row-container.component';

describe('ImageRowContainerComponent', () => {
  let component: ImageRowContainerComponent;
  let fixture: ComponentFixture<ImageRowContainerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ImageRowContainerComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ImageRowContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
