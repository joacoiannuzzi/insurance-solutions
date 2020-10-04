export class MonitoringSystem {
  id: number;
  name: string;
  sensor: string;
  monitoringCompany: string;
  assigned: boolean;

  constructor(
    id: number,
    name: string,
    sensor: string,
    monitoringCompany: string,
    isAssigned: boolean
  ) {
    this.id = id;
    this.name = name;
    this.sensor = sensor;
    this.monitoringCompany = monitoringCompany;
    this.assigned = isAssigned;
  }

  static fromJsonObject(jsonObject: any): MonitoringSystem {
    return new MonitoringSystem(
      jsonObject.id,
      jsonObject.name,
      jsonObject.sensor,
      jsonObject.monitoringCompany,
      jsonObject.assigned
    );
  }
}
