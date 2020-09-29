import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MonitoringSystemAssignationComponent } from './monitoring-system-assignation.component';

describe('MonitoringSystemAssignationComponent', () => {
  let component: MonitoringSystemAssignationComponent;
  let fixture: ComponentFixture<MonitoringSystemAssignationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MonitoringSystemAssignationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MonitoringSystemAssignationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
