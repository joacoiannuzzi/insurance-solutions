import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DrivingProfileUpdateComponent } from './driving-profile-update.component';

describe('DrivingProfileUpdateComponent', () => {
  let component: DrivingProfileUpdateComponent;
  let fixture: ComponentFixture<DrivingProfileUpdateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DrivingProfileUpdateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DrivingProfileUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
