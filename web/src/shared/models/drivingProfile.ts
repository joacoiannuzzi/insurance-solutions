export class DrivingProfile {
  id: number;
  avgDailyDrivingTime: number;
  avgSpeed: number;
  finishDate: string;
  maxSpeed: number;
  minSpeed: number;
  startDate: string;
  totalDrivingTime: number;
  monitoringSystemId?: number;

  constructor(
    id: number,
    avgDailyDrivingTime: number,
    avgSpeed: number,
    finishDate: string,
    maxSpeed: number,
    minSpeed: number,
    startDate: string,
    totalDrivingTime: number
  ) {
    this.id = id;
    this.avgDailyDrivingTime = avgDailyDrivingTime;
    this.avgSpeed = avgSpeed;
    this.finishDate = finishDate;
    this.maxSpeed = maxSpeed;
    this.startDate = startDate;
    this.totalDrivingTime = totalDrivingTime;
  }

  static fromJsonObject(jsonObject: any): DrivingProfile {
    let startDate: string = jsonObject.startDate.split('T')[0];
    let finishDate: string = jsonObject.finishDate.split('T')[0];
    return new DrivingProfile(
      jsonObject.id,
      jsonObject.avgDailyDrivingTime,
      jsonObject.avgSpeed,
      finishDate,
      jsonObject.maxSpeed,
      jsonObject.minSpeed,
      startDate,
      jsonObject.totalDrivingTime
    );
  }
}
