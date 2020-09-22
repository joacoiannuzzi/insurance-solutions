import {category} from "./category";
import {Client} from "./client";

export class Vehicle {
  id: number;
  licensePlate: string;
  category: category;
  brand: string;
  model: string;
  drivingProfiles: string;
  monitoringSystems: string;
  client: Client;


  constructor(id: number, licensePlate: string, category: category, brand: string, model: string, drivingProfiles: string, monitoringSystems: string, client: Client) {
    this.id = id;
    this.licensePlate = licensePlate;
    this.category = category;
    this.brand = brand;
    this.model = model;
    this.drivingProfiles = drivingProfiles;
    this.monitoringSystems = monitoringSystems;
    this.client = client;
  }

  static fromJsonObject(jsonObject: any): Vehicle {
    return new Vehicle(jsonObject.id, jsonObject.licensePlate, jsonObject.category, jsonObject.brand, jsonObject.model, jsonObject.drivingProfiles, jsonObject.monitoringSystems, jsonObject.client);
  }
}
