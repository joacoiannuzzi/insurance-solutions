import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientVehiclesComponent } from './client-vehicles.component';

describe('ClientVehiclesComponent', () => {
  let component: ClientVehiclesComponent;
  let fixture: ComponentFixture<ClientVehiclesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClientVehiclesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientVehiclesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
