import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SsalonLoginRedirectComponent } from './ssalon-login-redirect.component';

describe('SsalonLoginRedirectComponent', () => {
  let component: SsalonLoginRedirectComponent;
  let fixture: ComponentFixture<SsalonLoginRedirectComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SsalonLoginRedirectComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SsalonLoginRedirectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
