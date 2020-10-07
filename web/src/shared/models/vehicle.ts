import {category} from "./category";
import {DrivingProfile} from "./drivingProfile";
import {Client} from "./client";
import {MonitoringSystem} from "./monitoringSystem";

export class Vehicle {
  id: number;
  licensePlate: string;
  category: category;
  brand: string;
  model: string;
  drivingProfiles: DrivingProfile[];
  monitoringSystems: MonitoringSystem;
  client: Client;


  constructor(id: number, licensePlate: string, category: category, brand: string, model: string, drivingProfiles: DrivingProfile[], monitoringSystems: MonitoringSystem, client: Client) {
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

  static categoryToString(cat: string): string {
    switch (cat) {
      case "CAR":
        return 'Automóvil';
      case "TRUCK":
        return 'Camión';
      case "VAN":
        return 'Camioneta';
      case "MOTORCYCLE":
        return 'Moto';
      default:
        return 'Invalid option in categoryToString';
    }
  }
}
