
export class MonitoringSystem {
    id: number;
  serviceName: string;
  sensor: string;
  company: string;
  isAssigned: boolean;

  constructor(
      id: number,
    serviceName: string,
    sensor: string,
    company: string,
    isAssigned: boolean
  ) {
    this.serviceName = serviceName;
    this.sensor = sensor;
    this.company = company;
    this.isAssigned = isAssigned;
  }

  static fromJsonObject(jsonObject: any): MonitoringSystem {
    return new MonitoringSystem(
      jsonObject.id,
      jsonObject.serviceName,
      jsonObject.sensor,
      jsonObject.company,
      jsonObject.isAssigned
    );
  }
}
