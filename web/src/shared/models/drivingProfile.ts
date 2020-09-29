export class DrivingProfile {
  id: number;
  avgDailyDrivingTime: number;
  avgSpeed: number;
  finishDate: string;
  maxSpeed: number;
  minSpeed: number;
  startDate: string;
  totalDrivingTime: number;

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
    return new DrivingProfile(
      jsonObject.id,
      jsonObject.avgDailyDrivingTime,
      jsonObject.avgSpeed,
      jsonObject.finishDate,
      jsonObject.maxSpeed,
      jsonObject.minSpeed,
      jsonObject.startDate,
      jsonObject.totalDrivingTime
    );
  }
}
