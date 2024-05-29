import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MeetingShareComponent } from './meeting-share.component';

describe('MeetingShareComponent', () => {
  let component: MeetingShareComponent;
  let fixture: ComponentFixture<MeetingShareComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MeetingShareComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MeetingShareComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
