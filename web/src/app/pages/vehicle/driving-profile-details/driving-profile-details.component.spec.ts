import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DrivingProfileDetailsComponent } from './driving-profile-details.component';

describe('DrivingProfileDetailsComponent', () => {
  let component: DrivingProfileDetailsComponent;
  let fixture: ComponentFixture<DrivingProfileDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DrivingProfileDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DrivingProfileDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
