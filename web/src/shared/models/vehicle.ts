import {category} from "./category";
import {DrivingProfile} from "./drivingProfile";

export class Vehicle {
  id: number;
  licensePlate: string;
  category: category;
  brand: string;
  model: string;
  drivingProfiles: DrivingProfile[];
  monitoringSystems: string;


  constructor(id: number, licensePlate: string, category: category, brand: string, model: string, drivingProfiles: DrivingProfile[], monitoringSystems: string) {
    this.id = id;
    this.licensePlate = licensePlate;
    this.category = category;
    this.brand = brand;
    this.model = model;
    this.drivingProfiles = drivingProfiles;
    this.monitoringSystems = monitoringSystems;
  }

  static fromJsonObject(jsonObject: any): Vehicle {
    return new Vehicle(jsonObject.id, jsonObject.licensePlate, jsonObject.category, jsonObject.brand, jsonObject.model, jsonObject.drivingProfiles, jsonObject.monitoringSystems);
  }
}
