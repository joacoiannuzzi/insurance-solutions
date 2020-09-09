import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VehicleAssignationComponent } from './vehicle-assignation.component';

describe('VehicleAssignationComponent', () => {
  let component: VehicleAssignationComponent;
  let fixture: ComponentFixture<VehicleAssignationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VehicleAssignationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VehicleAssignationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
