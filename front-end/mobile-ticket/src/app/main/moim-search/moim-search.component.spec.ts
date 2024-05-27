import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MoimSearchComponent } from './moim-search.component';

describe('MoimSearchComponent', () => {
  let component: MoimSearchComponent;
  let fixture: ComponentFixture<MoimSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MoimSearchComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MoimSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
