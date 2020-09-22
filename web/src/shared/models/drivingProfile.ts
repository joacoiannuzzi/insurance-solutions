
export class drivingProfile {
  id: number;

  constructor(id: number) {
    this.id = id;
  }

  static fromJsonObject(jsonObject: any): drivingProfile {
    return new drivingProfile(jsonObject.id);
  }
}
