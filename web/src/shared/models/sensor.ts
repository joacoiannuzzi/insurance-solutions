import {MonitoringSystem} from "./monitoringSystem";

export class Sensor {

  id: number;
  name: string;
  model: string;
  monitoringSystems?: MonitoringSystem[];


  constructor(id: number, name: string, model: string, monitoringSystems?: MonitoringSystem[]) {
    this.id = id;
    this.name = name;
    this.model = model;
    this.monitoringSystems = monitoringSystems;

  }

  static fromJsonObject(jsonObject: any): Sensor {
    return new Sensor(
      jsonObject.id,
      jsonObject.name,
      jsonObject.model,
      jsonObject.monitoringSystems.map(MonitoringSystem.fromJsonObject)
    )
  }
}
