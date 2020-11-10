import { Vehicle } from './vehicle';
import {Sensor} from "./sensor";

export class MonitoringSystem {
  id: number;
  name: string;
  sensor: Sensor;
  monitoringCompany: string;
  assigned: boolean;
  vehicle: Vehicle

  constructor(
    id: number,
    name: string,
    sensor: Sensor,
    monitoringCompany: string,
    assigned: boolean,
    vehicle: Vehicle
  ) {
    this.id = id;
    this.name = name;
    this.sensor = sensor;
    this.monitoringCompany = monitoringCompany;
    this.assigned = assigned;
    this.vehicle = vehicle;
  }

  static fromJsonObject(jsonObject: any): MonitoringSystem {
    return new MonitoringSystem(
      jsonObject.id,
      jsonObject.name,
      jsonObject.sensor,
      jsonObject.monitoringCompany,
      jsonObject.assigned,
      jsonObject.vehicle
    );
  }
}
