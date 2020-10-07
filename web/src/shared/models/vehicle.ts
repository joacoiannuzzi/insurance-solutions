import {Category} from "./category";
import {DrivingProfile} from "./drivingProfile";
import {Client} from "./client";
import {MonitoringSystem} from "./monitoringSystem";

export class Vehicle {
  id: number;
  licensePlate: string;
  category: Category | string;
  brand: string;
  model: string;
  drivingProfiles: DrivingProfile[];
  monitoringSystems: MonitoringSystem;
  client: Client;


  constructor(id: number, licensePlate: string, category: Category, brand: string, model: string, drivingProfiles: DrivingProfile[], monitoringSystems: MonitoringSystem, client: Client) {
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

  static categoryToInt(cat: Category | string): number {
    const c = typeof cat == 'string' ? cat : this.fromCategoryToString(cat)
    switch (c) {
      case "CAR":
        return 0;
      case "TRUCK":
        return 1;
      case "VAN":
        return 2;
      case "MOTORCYCLE":
        return 3;
      default:
        return -1;
    }
  }

  static fromCategoryToString(cat: Category): string {
    switch (cat) {
      case Category.CAR:
        return "CAR";
      case Category.TRUCK:
        return "TRUCK";
      case Category.VAN:
        return "VAN";
      case Category.MOTORCYCLE:
        return "MOTORCYCLE";
      default:
        return "Invalid";
    }
  }
}
