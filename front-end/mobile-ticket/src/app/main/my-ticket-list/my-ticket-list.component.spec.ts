import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyTicketListComponent } from './my-ticket-list.component';

describe('MyTicketListComponent', () => {
  let component: MyTicketListComponent;
  let fixture: ComponentFixture<MyTicketListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyTicketListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MyTicketListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
